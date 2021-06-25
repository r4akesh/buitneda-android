package com.webkul.mobikul.activities

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.google.android.gms.security.ProviderInstaller
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.ActivitySplashScreenBinding
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.helpers.AppSharedPref.Companion.KEY_PRICE_PATTERN
import com.webkul.mobikul.helpers.AppSharedPref.Companion.KEY_PRICE_PRECISION
import com.webkul.mobikul.helpers.AppSharedPref.Companion.PRICE_FORMAT_PREF
import com.webkul.mobikul.helpers.ApplicationConstants.BASE_URL
import com.webkul.mobikul.helpers.ApplicationConstants.DEFAULT_WEBSITE_ID
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_ID
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_TITLE
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_TYPE
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_TYPE_CATEGORY
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_FROM_NOTIFICATION
//import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_HOME_PAGE_DATA
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_IS_FRESH_HOME_PAGE_DATA
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_ID
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_NAME
import com.webkul.mobikul.helpers.ConstantsHelper.CUSTOMER_NOT_EXIST
import com.webkul.mobikul.helpers.NetworkHelper.Companion.isNetworkAvailable
import com.webkul.mobikul.helpers.Utils.Companion.getMd5String
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.models.homepage.*
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

/**
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

class SplashScreenActivity : BaseActivity() {

    private lateinit var mContentViewBinding: ActivitySplashScreenBinding
    private var mUrl = ""
    private var mIsAnimationFinished: Boolean = true
    private var mHomePageDataModel: HomePageDataModel? = null
    private var mIsFreshHomePageData: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)

        try {
            ProviderInstaller.installIfNeeded(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //FirebaseAnalyticsHelper.logAppScreenView("Launch", "SplashScreenActivity.class")
        FirebaseAnalyticsHelper.logAppOpenEvent()
//        addAnimationListener()
        checkAction()



    }

    /*private fun addAnimationListener() {
        if (ENABLE_SPLASH_ANIMATION) {
            mContentViewBinding.splashScreenAnimation.addAnimatorListener(object :
                Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                }

                override fun onAnimationEnd(animation: Animator) {
                    mIsAnimationFinished = true
                    if (mHomePageDataModel == null)
                        mContentViewBinding.loading = true
                    startHomeActivity()
                }

                override fun onAnimationCancel(animation: Animator) {
                }

                override fun onAnimationRepeat(animation: Animator) {
                }
            })
        } else {
            mContentViewBinding.loading = true
            mIsAnimationFinished = true
        }
    }*/

    private fun checkAction() {
        val action = intent?.action
        val data = intent?.dataString
        if (action == Intent.ACTION_VIEW && data != null && data != BASE_URL) {
            mUrl = data
        }

        checkLocalData()
//        callApi()
    }

    private var hashIdentifier = ""
    private val TAG = "SplashScreenActivity"

    @SuppressLint("CheckResult")
    private fun checkLocalData() {

        if (AppSharedPref.getWebsiteId(this) == DEFAULT_WEBSITE_ID && AppSharedPref.getStoreId(this) == "0" && mUrl.isBlank()) {
            callApi()
        } else {
            hashIdentifier = getMd5String(
                "homePageData" + AppSharedPref.getWebsiteId(this) + AppSharedPref.getStoreId(this) + AppSharedPref.getCustomerToken(
                    this
                ) + AppSharedPref.getQuoteId(this) + AppSharedPref.getCurrencyCode(this) + mUrl
            )
            Log.d(
                TAG, "cacheData: " + "homePageData" + ":" + AppSharedPref.getWebsiteId(this) + ":" +
                        AppSharedPref.getStoreId(this) + ":" +
                        AppSharedPref.getCustomerToken(
                            this
                        ) + ":" + AppSharedPref.getQuoteId(this) + ":" + AppSharedPref.getCurrencyCode(
                    this
                ) + mUrl
            )
            Log.d(TAG, "checkLocalData: $hashIdentifier")
            mDataBaseHandler.getResponseFromDatabaseOnThread(hashIdentifier)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Observer<String> {
                    override fun onNext(data: String) {
                        Log.d(TAG, "checkLocalData: $data")
                        if (data.isBlank()) {
                            callApi(hashIdentifier)
                        } else {
                            updateAnimationCheckAndProceed(data)
                        }
                    }

                    override fun onError(e: Throwable) {
                        callApi(hashIdentifier)
                    }

                    override fun onSubscribe(disposable: Disposable) {
                        mCompositeDisposable.add(disposable)
                    }

                    override fun onComplete() {

                    }
                })
        }
    }

    private fun updateAnimationCheckAndProceed(response: String) {
        /*if (!AppSharedPref.showSplash(this)) {
            mIsAnimationFinished = true
        }*/
        mIsAnimationFinished = true
        onSuccessfulResponse(mGson.fromJson(response, HomePageDataModel::class.java))
        /* Handler(Looper.myLooper()!!).postDelayed({
             onSuccessfulResponse(mGson.fromJson(response, HomePageDataModel::class.java))
         }, if (ENABLE_SPLASH_ANIMATION) 0.toLong() else 2000.toLong())*/
    }


    private fun callApi() {
        callApi(hashIdentifier)
    }

    private fun callApi(hashIdentifier: String) {
        ApiConnection.getHomePageData(
            this,
            mDataBaseHandler.getETagFromDatabase(hashIdentifier),
            mUrl.isNotBlank(),
            mUrl
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiCustomCallback<HomePageDataModel>(this, true) {
                override fun onNext(responseModel: HomePageDataModel) {
                    super.onNext(responseModel, hashIdentifier)
                    if (responseModel.success) {
                        mIsFreshHomePageData = true
                        responseModel.websiteId = "1"
                        onSuccessfulResponse(responseModel)
                    } else {
                        onFailureResponse(responseModel)
                    }
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    onErrorResponse(e)
                }
            })
    }

    private fun onSuccessfulResponse(homePageDataModel: HomePageDataModel) {
        mHomePageDataModel = homePageDataModel
        setAppSharedPrefConfigDetails()
        mHomePageDataModel!!.priceFormat?.let { updatePriceFormatPref(it) }
        checkDeepLinkData()
    }

    private fun setAppSharedPrefConfigDetails() {
        if(mHomePageDataModel!!.websiteId!=null){
            if (!mHomePageDataModel!!.websiteId.isNullOrEmpty()) {
                mHomePageDataModel!!.websiteId?.let { AppSharedPref.setWebsiteId(this, it) }
            }
        }

        mHomePageDataModel!!.websiteData?.let { setWebsiteData(it) }
        if (!mHomePageDataModel!!.websiteData.isNullOrEmpty()) {
            mHomePageDataModel!!.websiteData?.size?.let {
                AppSharedPref.setWebsiteDataCount(
                    this,
                    it
                )
            }

        }

        if (!mHomePageDataModel!!.storeId.isNullOrEmpty()) {
            mHomePageDataModel!!.storeId?.let { AppSharedPref.setStoreId(this, it) }
            mHomePageDataModel!!.storeData?.size?.let { AppSharedPref.setStoreDataSize(this, it) }
            mHomePageDataModel!!.storeData?.let { setStoreData(it) }
        }
        if (AppSharedPref.getCurrencyCode(this).isEmpty()) {
            mHomePageDataModel!!.defaultCurrency?.let { AppSharedPref.setCurrencyCode(this, it) }
            mHomePageDataModel!!.allowedCurrencies?.size?.let {
                AppSharedPref.setCurrencyCodeSize(
                    this,
                    it
                )
            }
            mHomePageDataModel!!.allowedCurrencies?.let { setCurrencyData(it) }
        }

        AppSharedPref.setIsWishlistEnabled(this, mHomePageDataModel!!.wishlistEnable)
        mHomePageDataModel!!.categories?.let { AppSharedPref.setCategoryData(this, it) }
        AppSharedPref.setShowSplash(this, true)

        updateCartCount(mHomePageDataModel!!.cartCount)

        val customerDataSharedPref =
            AppSharedPref.getSharedPreferenceEditor(this, AppSharedPref.CUSTOMER_PREF)
        customerDataSharedPref.putString(
            AppSharedPref.KEY_CUSTOMER_NAME,
            mHomePageDataModel!!.customerName
        )
        customerDataSharedPref.putString(
            AppSharedPref.KEY_CUSTOMER_EMAIL,
            mHomePageDataModel!!.customerEmail
        )
        customerDataSharedPref.apply()
    }

    private fun setWebsiteData(websiteDataArray: ArrayList<WebsiteData>) {
        websiteDataArray.forEach { websiteData ->
            if (AppSharedPref.getWebsiteId(this) == websiteData.id) {
                websiteData.name?.let { AppSharedPref.setWebsiteLabel(this, it) }
            }
        }
    }

    private fun setStoreData(storeDataArray: ArrayList<StoreData>) {
        storeDataArray.forEach { storeData ->
            storeData.stores?.forEach { languageData ->
                if (AppSharedPref.getStoreId(this) == languageData.id) {
                    languageData.name?.let { AppSharedPref.setStoreLabel(this, it) }
                    languageData.code?.let { AppSharedPref.setStoreCode(this, it) }
                    LocaleUtils.updateConfig(this)
                    return
                }
            }
        }
    }

    private fun setCurrencyData(allowedCurrencies: ArrayList<Currency>) {
        allowedCurrencies.forEach { currencyData ->
            if (AppSharedPref.getCurrencyCode(this) == currencyData.code) {
                currencyData.label?.let { AppSharedPref.setCurrencyLabel(this, it) }
                return
            }
        }
    }

    private fun updatePriceFormatPref(priceFormat: PriceFormat) {
        val customerDataSharedPref =
            AppSharedPref.getSharedPreferenceEditor(this, PRICE_FORMAT_PREF)
        customerDataSharedPref.putString(KEY_PRICE_PATTERN, priceFormat.pattern)
        customerDataSharedPref.putInt(KEY_PRICE_PRECISION, priceFormat.precision)
        customerDataSharedPref.apply()
    }

    private fun checkDeepLinkData() {
        if (!mHomePageDataModel!!.productId.isNullOrEmpty()) {
            val intent = (applicationContext as MobikulApplication).getProductDetailsActivity(this)
            intent.putExtra(BUNDLE_KEY_PRODUCT_ID, mHomePageDataModel!!.productId)
            intent.putExtra(BUNDLE_KEY_PRODUCT_NAME, mHomePageDataModel!!.productName)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra(BUNDLE_KEY_FROM_NOTIFICATION, true)
            startActivity(intent)
            finish()
        } else if (!mHomePageDataModel!!.categoryId.isNullOrEmpty()) {
            val intent = Intent(this, CatalogActivity::class.java)
            intent.putExtra(BUNDLE_KEY_CATALOG_TYPE, BUNDLE_KEY_CATALOG_TYPE_CATEGORY)
            intent.putExtra(BUNDLE_KEY_CATALOG_TITLE, mHomePageDataModel!!.categoryName)
            intent.putExtra(BUNDLE_KEY_CATALOG_ID, mHomePageDataModel!!.categoryId)
            intent.putExtra(BUNDLE_KEY_FROM_NOTIFICATION, true)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } else {
            startHomeActivity(mHomePageDataModel!!)
        }
    }

    override fun setToolbarUpView() {
        //Nothing
    }

    private fun startHomeActivity(mHomePageDataModel: HomePageDataModel) {
        if (mIsAnimationFinished && mHomePageDataModel != null) {
            HomeDataSingleton.getInstance().mHomePageDataModel = mHomePageDataModel
            val intent = Intent(this, HomeActivity::class.java)
//            HomeActivity.data = mHomePageDataModel
           // intent.putExtra(BUNDLE_KEY_HOME_PAGE_DATA, mHomePageDataModel)
            intent.putExtra(BUNDLE_KEY_IS_FRESH_HOME_PAGE_DATA, mIsFreshHomePageData)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK


            val bundle: Bundle? = this.intent.extras
            bundle?.let {
//                val notificationType = this.intent.getStringExtra("notificationType")
//                val productId = this.intent.getStringExtra("productId")
//                val productName = this.intent.getStringExtra("productName")
//                val categoryName = this.intent.getStringExtra("categoryName")
//                val incrementId = this.intent.getStringExtra("incrementId")
//                val categoryId = this.intent.getStringExtra("categoryId")
//
//
//                Log.d(TAG, "startHomeActivity: $notificationType")
//
//
//                intent.putExtra("notificationType", notificationType)
//                intent.putExtra("productId", productId)
//                intent.putExtra("productName", productName)
//                intent.putExtra("categoryName", categoryName)
//                intent.putExtra("categoryId", categoryId)
//                intent.putExtra("incrementId", incrementId)



                val bundleKeySet = bundle.keySet() // string key set

                for (key in bundleKeySet) { // traverse and print pairs
                    Log.i(TAG, key + " : " + bundle.getString(key))
                    intent.putExtra(key!!, bundle.getString(key))
                }
            }


            startActivity(intent)
            finish()
        }
    }

    override fun onFailureResponse(response: Any) {
        super.onFailureResponse(response)
        mContentViewBinding.loading = false
        when ((response as BaseModel).otherError) {
            CUSTOMER_NOT_EXIST -> {
                // Do nothing as it will be handled from the super.
            }
            else -> {
                AlertDialogHelper.showNewCustomDialog(
                    this,
                    getString(R.string.error),
                    response.message,
                    false,
                    getString(R.string.ok),
                    { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        finish()
                    }, "", null
                )
            }
        }
    }

    private fun onErrorResponse(error: Throwable) {
        mContentViewBinding.loading = false
        if ((!isNetworkAvailable(this) || (error is HttpException && error.code() == 304))) {

            mDataBaseHandler.getResponseFromDatabaseOnThread(hashIdentifier)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Observer<String> {
                    override fun onNext(response: String) {
                        if (response.isBlank()) {
                            AlertDialogHelper.showNewCustomDialog(
                                this@SplashScreenActivity,
                                getString(R.string.oops),
                                NetworkHelper.getErrorMessage(this@SplashScreenActivity, error),
                                false,
                                getString(R.string.try_again),
                                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                                    dialogInterface.dismiss()
                                    mContentViewBinding.loading = true
                                    callApi()
                                }, getString(R.string.close),
                                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                                    dialogInterface.dismiss()
                                    finish()
                                })
                        } else {
                            mHomePageDataModel =
                                mGson.fromJson(response, HomePageDataModel::class.java)
                            mIsFreshHomePageData = false
                            startHomeActivity(mHomePageDataModel!!)
                        }
                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onSubscribe(disposable: Disposable) {
                        mCompositeDisposable.add(disposable)
                    }

                    override fun onComplete() {

                    }
                })
        } else {
            AlertDialogHelper.showNewCustomDialog(
                this,
                getString(R.string.oops),
                NetworkHelper.getErrorMessage(this, error),
                false,
                getString(R.string.try_again),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    mContentViewBinding.loading = true
                    callApi()
                }, getString(R.string.close),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    finish()
                })
        }
    }
}