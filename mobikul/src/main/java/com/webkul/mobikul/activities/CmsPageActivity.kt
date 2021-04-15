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

package com.webkul.mobikul.activities

import android.annotation.TargetApi
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.ActivityCmsPageBinding
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CMS_PAGE_ID
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CMS_PAGE_TITLE
import com.webkul.mobikul.models.extra.CMSPageDataModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException


class CmsPageActivity : BaseActivity() {

    lateinit var mContentViewBinding: ActivityCmsPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_cms_page)
        startInitialization()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    private fun startInitialization() {
        initSupportActionBar()
        callApi()
    }

    private fun initSupportActionBar() {
        supportActionBar?.title = intent.getStringExtra(BUNDLE_KEY_CMS_PAGE_TITLE)
    }

    private fun callApi() {
        mHashIdentifier = Utils.getMd5String("getSearchTermsList" + AppSharedPref.getStoreId(this))
        ApiConnection.getCMSPageData(this, mDataBaseHandler.getETagFromDatabase(mHashIdentifier), intent.getStringExtra(BUNDLE_KEY_CMS_PAGE_ID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<CMSPageDataModel>(this, true) {
                    override fun onNext(cmsPageDataModel: CMSPageDataModel) {
                        super.onNext(cmsPageDataModel)
                        if (cmsPageDataModel.success) {
                            onSuccessfulResponse(cmsPageDataModel)
                        } else {
                            onFailureResponse(cmsPageDataModel)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        onErrorResponse(e)
                    }
                })
    }

    private fun onSuccessfulResponse(cmsPageDataModel: CMSPageDataModel) {
        mContentViewBinding.cmsPageInfoWebView.visibility = View.GONE
        mContentViewBinding.data = cmsPageDataModel
        mContentViewBinding.cmsPageInfoWebView.settings.defaultFontSize = 14
        mContentViewBinding.cmsPageInfoWebView.settings.javaScriptEnabled = true
        mContentViewBinding.cmsPageInfoWebView.isFocusableInTouchMode = false
        mContentViewBinding.cmsPageInfoWebView.isFocusable = false
        mContentViewBinding.cmsPageInfoWebView.isClickable = false
        mContentViewBinding.cmsPageInfoWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url.contains("goo.gl")) {
                    val gmmIntentUri = Uri.parse(url)
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    if (mapIntent.resolveActivity(packageManager) != null) {
                        startActivity(mapIntent)
                    }
                    return true
                } else {
                    CustomTabsHelper.openTab(this@CmsPageActivity, url)
                    return true
                }

            }

            @TargetApi(Build.VERSION_CODES.N)
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                if (request.url.toString().contains("goo.gl")) {
                    val gmmIntentUri = Uri.parse(request.url.toString())
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    if (mapIntent.resolveActivity(packageManager) != null) {
                        startActivity(mapIntent)
                    }
                    return true
                } else {
                    CustomTabsHelper.openTab(this@CmsPageActivity, request.url.toString())
                    return true
                }

            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                mContentViewBinding.cmsPageInfoWebView.visibility = View.VISIBLE
            }

        }
    }

    private fun onFailureResponse(cmsPageDataModel: CMSPageDataModel) {
        AlertDialogHelper.showNewCustomDialog(
                this,
                getString(R.string.error),
                cmsPageDataModel.message,
                false,
                getString(R.string.try_again),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    callApi()
                }
                , getString(R.string.dismiss)
                , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
            finish()
        })
    }

    private fun onErrorResponse(error: Throwable) {
        val response = mDataBaseHandler.getResponseFromDatabase(mHashIdentifier)
        if ((!NetworkHelper.isNetworkAvailable(this) || (error is HttpException && error.code() == 304)) && response.isNotBlank()) {
            onSuccessfulResponse(mGson.fromJson(response, CMSPageDataModel::class.java))
        } else {
            AlertDialogHelper.showNewCustomDialog(
                    this,
                    getString(R.string.error),
                    NetworkHelper.getErrorMessage(this, error),
                    false,
                    getString(R.string.try_again),
                    DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        callApi()
                    }
                    , getString(R.string.dismiss)
                    , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                finish()
            })
        }
    }
}
