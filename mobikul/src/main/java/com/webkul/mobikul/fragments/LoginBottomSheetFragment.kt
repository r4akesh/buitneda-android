/*
 * Webkul Software.
 *
 * Kotlin
 *
 * @author Webkul <support@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2019 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

package com.webkul.mobikul.fragments

import android.Manifest
import android.annotation.TargetApi
import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.LoginAndSignUpActivity
import com.webkul.mobikul.databinding.FragmentLoginBottomSheetBinding
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikul.helpers.ApplicationConstants
import com.webkul.mobikul.helpers.ApplicationConstants.ENABLE_KEYBOARD_OBSERVER
import com.webkul.mobikul.helpers.MobikulApplication
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.user.LoginFormModel
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey


class LoginBottomSheetFragment : FullScreenBottomSheetDialogFragment() {

    lateinit var mContentViewBinding: FragmentLoginBottomSheetBinding

    private val KEY_NAME = "key"
    private lateinit var mCancellationSignal: CancellationSignal
    private var mCipher: Cipher? = null
    private var mKeyStore: KeyStore? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_bottom_sheet, container, false)
        return mContentViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupFormData()
        setupFingerPrintLogin()
    }

    private fun setupFormData() {
        val loginFormModel = LoginFormModel()
        loginFormModel.username = ApplicationConstants.DEMO_USERNAME
        loginFormModel.password = ApplicationConstants.DEMO_PASSWORD

        mContentViewBinding.data = loginFormModel
        mContentViewBinding.handler = (context?.applicationContext as MobikulApplication).getLoginBottomSheetHandler(this)

        if (ENABLE_KEYBOARD_OBSERVER)
            attachKeyboardListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (keyboardListenersAttached) {
            mContentViewBinding.loginBottomSheet.viewTreeObserver.removeGlobalOnLayoutListener(keyboardLayoutListener)
        }
    }

    private val keyboardLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        val heightDiff = Utils.screenHeight - (context as LoginAndSignUpActivity).mContentViewBinding.root.height
        if (heightDiff < 200) {
            onHideKeyboard()
        } else {
            onShowKeyboard(heightDiff)
        }
    }

    private var keyboardListenersAttached = false

    protected fun onShowKeyboard(keyboardHeight: Int) {
        val layoutParams = mContentViewBinding.keyboardHeightLayout.layoutParams
        layoutParams.height = keyboardHeight
        mContentViewBinding.keyboardHeightLayout.layoutParams = layoutParams
    }

    protected fun onHideKeyboard() {
        val layoutParams = mContentViewBinding.keyboardHeightLayout.layoutParams
        layoutParams.height = 0
        mContentViewBinding.keyboardHeightLayout.layoutParams = layoutParams
    }

    protected fun attachKeyboardListeners() {
        if (keyboardListenersAttached) {
            return
        }

        mContentViewBinding.loginBottomSheet.viewTreeObserver.addOnGlobalLayoutListener(keyboardLayoutListener)

        keyboardListenersAttached = true
    }


    private fun setupFingerPrintLogin() {
        if (mContentViewBinding.data!!.isFingerPrintEnable(this) && !AppSharedPref.getCustomerFingerUserName(context!!).isBlank() && !AppSharedPref.getCustomerFingerPassword(context!!).isBlank()) {
            val keyguardManager = context!!.getSystemService(AppCompatActivity.KEYGUARD_SERVICE) as KeyguardManager
            val fingerprintManager: FingerprintManager
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                fingerprintManager = context!!.getSystemService(AppCompatActivity.FINGERPRINT_SERVICE) as FingerprintManager
                if (!keyguardManager.isKeyguardSecure) {
                    mContentViewBinding.fingerPrintIv.visibility = View.GONE
                    mContentViewBinding.fingerprintErrorTv.visibility = View.VISIBLE
                    mContentViewBinding.fingerprintErrorTv.text = getString(R.string.screen_lock_not_enabled)
                    return
                }
                if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                    mContentViewBinding.fingerPrintIv.visibility = View.GONE
                    mContentViewBinding.fingerprintErrorTv.visibility = View.VISIBLE
                    mContentViewBinding.fingerprintErrorTv.text = getString(R.string.fingerprint_authentication_permission_not_enabled)
                    return
                }
                if (!fingerprintManager.hasEnrolledFingerprints()) {
                    mContentViewBinding.fingerPrintIv.visibility = View.GONE
                    mContentViewBinding.fingerprintErrorTv.visibility = View.VISIBLE
                    mContentViewBinding.fingerprintErrorTv.text = getString(R.string.register_at_least_one_fingerprint_in_settings)
                    return
                }

                mContentViewBinding.fingerprintErrorTv.visibility = View.GONE
                mContentViewBinding.fingerPrintIv.visibility = View.VISIBLE
                generateKey()
                try {
                    if (cipherInit()) {
                        val cryptoObject: FingerprintManager.CryptoObject?
                        val helper: FingerprintHandler
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            cryptoObject = mCipher?.let { FingerprintManager.CryptoObject(it) }

                            helper = FingerprintHandler(context!!)
                            cryptoObject?.let { helper.startAuth(fingerprintManager, it) }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun generateKey() {
        try {
            mKeyStore = KeyStore.getInstance("AndroidKeyStore")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val keyGenerator: KeyGenerator
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to get KeyGenerator instance", e)
        } catch (e: NoSuchProviderException) {
            throw RuntimeException("Failed to get KeyGenerator instance", e)
        }

        try {
            mKeyStore!!.load(null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                keyGenerator.init(KeyGenParameterSpec.Builder(KEY_NAME,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setUserAuthenticationRequired(true)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .build())
            }
            keyGenerator.generateKey()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun cipherInit(): Boolean {
        try {
            mCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        }

        try {
            mKeyStore!!.load(null)
            val key = mKeyStore!!.getKey(KEY_NAME, null) as SecretKey
            mCipher!!.init(Cipher.ENCRYPT_MODE, key)
            return true
        } catch (e: InvalidKeyException) {
            return false
        } catch (e: KeyStoreException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: CertificateException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: UnrecoverableKeyException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: IOException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to init Cipher", e)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private inner class FingerprintHandler internal constructor(private val mContext: Context) : FingerprintManager.AuthenticationCallback() {

        internal fun startAuth(manager: FingerprintManager, cryptoObject: FingerprintManager.CryptoObject) {
            mCancellationSignal = CancellationSignal()
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            manager.authenticate(cryptoObject, mCancellationSignal, 0, this, null)
        }

        override fun onAuthenticationError(errMsgId: Int, errString: CharSequence) {
        }

        override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence) {
            if (helpString.isNotEmpty())
                Toast.makeText(mContext, getString(R.string.authentication_help, helpString.toString()), Toast.LENGTH_LONG).show()
        }

        override fun onAuthenticationFailed() {
            Utils.showShakeError(context!!, mContentViewBinding.fingerPrintIv)
        }

        override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult) {
            Toast.makeText(mContext, mContext.getString(R.string.auth_succeeded), Toast.LENGTH_SHORT).show()
            mContentViewBinding.data!!.username = AppSharedPref.getCustomerFingerUserName(context!!)
            mContentViewBinding.data!!.password = AppSharedPref.getCustomerFingerPassword(context!!)
            mContentViewBinding.handler!!.onClickLogin(mContentViewBinding.data!!)
        }
    }

    override fun onResume() {
        super.onResume()
        setupFingerPrintLogin()
    }

    override fun onStop() {
        super.onStop()
        if (::mCancellationSignal.isInitialized) {
            mCancellationSignal.cancel()
        }
    }
}

