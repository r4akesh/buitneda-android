/*
 * Webkul Software.
 *
 * Kotlin
 *
 * @author Webkul <support@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

package com.webkul.arcore.activities

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.google.ar.core.*
import com.google.ar.core.Config.FocusMode.AUTO
import com.google.ar.core.exceptions.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.PlaneRenderer
import com.google.ar.sceneform.rendering.Texture
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.webkul.arcore.helper.CameraPermissionHelper
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.databinding.ActivityArBinding
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_AR_MODEL_URL
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_NAME
import com.webkul.mobikul.helpers.ConstantsHelper.RC_AR
import com.webkul.mobikul.helpers.ToastHelper
import java.util.concurrent.CompletableFuture

private const val MIN_OPENGL_VERSION = 3.1

class ArActivity : BaseActivity() {

    private lateinit var mContentViewBinding: ActivityArBinding

    private var arFragment: ArFragment? = null
    private var objectRenderable: ModelRenderable? = null
    private var mUserRequestedInstall = false
    private var mSession: Session? = null

    private var mModelStateSnackBar: Snackbar? = null
    private lateinit var mModelCompletableFuture: CompletableFuture<Void>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = intent.getStringExtra(BUNDLE_KEY_PRODUCT_NAME)
        if (checkIsSupportedDeviceOrFinish(this)) {
            mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_ar)
            startInitialization()
        } else {
            ToastHelper.showToast(this, getString(R.string.the_ar_feature_is_not_supported_by_your_device))
            this.finish()
        }
    }

    private fun startInitialization() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                createArSession()
                arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment
                if (mSession != null) {
                    val cfg = Config(mSession)
                    cfg.updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
                    cfg.focusMode = AUTO
                    mSession?.configure(cfg)
                    arFragment?.arSceneView?.setupSession(mSession)
                }

                // Build texture sampler
                val sampler = Texture.Sampler.builder()
                        .setMinFilter(Texture.Sampler.MinFilter.LINEAR)
                        .setMagFilter(Texture.Sampler.MagFilter.LINEAR)
                        .setWrapMode(Texture.Sampler.WrapMode.REPEAT).build()

                // Build texture with sampler
                val trigrid = Texture.builder()
                        .setSource(this, R.drawable.ar_background)
                        .setSampler(sampler).build()

                // Set plane texture
                arFragment?.arSceneView
                        ?.planeRenderer
                        ?.material
                        ?.thenAcceptBoth<Texture>(trigrid) { material, texture -> material.setTexture(PlaneRenderer.MATERIAL_TEXTURE, texture) }

                // Init renderable
                loadModel()

                // Model loading listener

                // Set tap listener
                arFragment?.setOnTapArPlaneListener { hitResult: HitResult, _: Plane, _: MotionEvent ->
                    Log.d("Tag", "setOnTapArPlaneListener--objectRenderable-->" + objectRenderable)
                    if (objectRenderable != null) {
                        // Create the Anchor.
                        val anchor = hitResult.createAnchor()
                        val anchorNode = AnchorNode(anchor)
                        anchorNode.setParent(arFragment!!.arSceneView.scene)

                        // Create the transformable andy and add it to the anchor.
                        val andy = TransformableNode(arFragment!!.transformationSystem)
                        andy.setParent(anchorNode)
                        andy.renderable = objectRenderable
                        andy.select()

                        mModelStateSnackBar?.dismiss()
                    }
                }
            } else {
                val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, RC_AR)
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun loadModel() {
        try {
            mModelCompletableFuture = ModelRenderable.builder()
                    .setSource(this, Uri.parse(intent.getStringExtra(BUNDLE_KEY_AR_MODEL_URL)))
                    .build()
                    .thenAccept { renderable -> objectRenderable = renderable }
                    .exceptionally { throwable ->
                        ToastHelper.showToast(this, getString(R.string.something_went_wrong))
                        throwable.printStackTrace()
                        null
                    }

            mModelStateSnackBar = Snackbar.make(mContentViewBinding.arLayout, getString(R.string.downloading_model), Snackbar.LENGTH_INDEFINITE)
            mModelStateSnackBar?.view?.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
            mModelStateSnackBar?.view?.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)?.setTextColor(ContextCompat.getColor(this, R.color.text_color_primary))
            mModelStateSnackBar?.show()

            if (::mModelCompletableFuture.isInitialized)
                checkIfModelReady()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun checkIfModelReady() {
        if (mModelCompletableFuture.isDone && !isDestroyed) {
            if (mModelCompletableFuture.isCompletedExceptionally || mModelCompletableFuture.isCancelled) {
                mModelStateSnackBar = Snackbar.make(mContentViewBinding.arLayout, getString(R.string.model_error), Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.try_again)) {
                    loadModel()
                    mModelStateSnackBar?.dismiss()
                }
                mModelStateSnackBar?.view?.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
                mModelStateSnackBar?.view?.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)?.setTextColor(ContextCompat.getColor(this, R.color.text_color_primary))
                mModelStateSnackBar?.show()
            } else {
                mModelStateSnackBar = Snackbar.make(mContentViewBinding.arLayout, getString(R.string.model_ready), Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.dismiss)) {
                    mModelStateSnackBar?.dismiss()
                }
                mModelStateSnackBar?.view?.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
                mModelStateSnackBar?.view?.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)?.setTextColor(ContextCompat.getColor(this, R.color.text_color_primary))
                mModelStateSnackBar?.show()
            }
        } else {
            Handler().postDelayed({ this.checkIfModelReady() }, 500)
        }
    }

    private fun checkIsSupportedDeviceOrFinish(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e("error", "Sceneform requires Android N or later")
            return false
        }
        val openGlVersionString = (activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
                .deviceConfigurationInfo
                .glEsVersion
        if (java.lang.Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e("error", "Sceneform requires OpenGL ES 3.1 later")
            return false
        }
        return true
    }

    private fun createArSession() {
        var exception: Exception? = null
        var message: String? = null
        try {
            if (mSession == null) {
                when (ArCoreApk.getInstance().requestInstall(this, mUserRequestedInstall)) {
                    ArCoreApk.InstallStatus.INSTALL_REQUESTED -> mUserRequestedInstall = true
                    ArCoreApk.InstallStatus.INSTALLED -> {
                    }
                }
                if (!CameraPermissionHelper.hasCameraPermission(this)) {
                    CameraPermissionHelper.requestCameraPermission(this)
                    return
                }
                mSession = Session(this)
            }
        } catch (e: UnavailableArcoreNotInstalledException) {
            message = getString(R.string.ar_core_install_error)
            exception = e
        } catch (e: UnavailableUserDeclinedInstallationException) {
            message = getString(R.string.ar_core_install_error)
            exception = e
        } catch (e: UnavailableApkTooOldException) {
            message = getString(R.string.ar_core_update_error)
            exception = e
        } catch (e: UnavailableSdkTooOldException) {
            message = getString(R.string.ar_core_update_error)
            exception = e
        } catch (e: UnavailableDeviceNotCompatibleException) {
            message = getString(R.string.the_ar_feature_is_not_supported_by_your_device)
            exception = e
        } catch (e: Exception) {
            message = getString(R.string.ar_core_session_error)
            exception = e
        }

        if (message != null) {
            ToastHelper.showToast(this, message)
            exception?.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mModelCompletableFuture.isInitialized)
            mModelCompletableFuture.cancel(true)
    }
}