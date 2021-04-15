package com.webkul.mobikulmp.activities


import android.content.DialogInterface
import android.content.Intent
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.CatalogActivity
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_ID
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_TITLE
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_TYPE
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_FROM_NOTIFICATION
import com.webkul.mobikul.models.catalog.CatalogProductsResponseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_SELLER_ID
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.models.seller
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 12/7/19
 */

class CatalogProductExtendedActivity : CatalogActivity() {

    var mSellerId = 0

    override fun startInitialization(intent: Intent) {

        if (intent.hasExtra(BUNDLE_KEY_SELLER_ID)) {
            mCatalogName = intent.getStringExtra(BUNDLE_KEY_CATALOG_TITLE)!!
            mSellerId = intent.getIntExtra(BUNDLE_KEY_SELLER_ID, 0)
            mCatalogType = "sellerCollection"
        } else
            if (intent.hasExtra(BUNDLE_KEY_CATALOG_TYPE) && intent.hasExtra(BUNDLE_KEY_CATALOG_TITLE) && (intent.hasExtra(BUNDLE_KEY_CATALOG_ID))) {
                mFromNotification = intent.hasExtra(BUNDLE_KEY_FROM_NOTIFICATION)
                mCatalogType = intent.getStringExtra(BUNDLE_KEY_CATALOG_TYPE)!!
                mCatalogName = intent.getStringExtra(BUNDLE_KEY_CATALOG_TITLE)!!
                mCatalogId = intent.getStringExtra(BUNDLE_KEY_CATALOG_ID)!!
            } else {
                ToastHelper.showToast(this, getString(R.string.something_went_wrong))
            }
        initSupportActionBar()
        mPageNumber = 1
        callApi()
        checkAndLoadLocalData()
    }

    override fun callApi() {
        if (mSellerId != 0) {
            mContentViewBinding.loading = true
            mHashIdentifier = Utils.getMd5String("getSellerCatalogProductData" + AppSharedPref.getStoreId(this) + AppSharedPref.getQuoteId(this) + AppSharedPref.getCustomerToken(this) + mCatalogType + mCatalogId + mPageNumber + mSortingInputJson + mFilterInputJson)
            MpApiConnection.getSellerCatalogProductData(this
                    , mDataBaseHandler.getETagFromDatabase(mHashIdentifier)
                    , mCatalogType
                    , mCatalogId
                    , mSellerId
                    , mPageNumber++
                    , mSortingInputJson
                    , mFilterInputJson)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : ApiCustomCallback<CatalogProductsResponseModel>(this, false) {
                        override fun onNext(catalogProductsResponseModel: CatalogProductsResponseModel) {
                            super.onNext(catalogProductsResponseModel)
                            mContentViewBinding.loading = false
                            if (catalogProductsResponseModel.success) {
                                onSuccessfulResponse(catalogProductsResponseModel)
                            } else {
                                onFailureResponse(catalogProductsResponseModel)
                            }
                        }

                        override fun onError(e: Throwable) {
                            super.onError(e)
                            mContentViewBinding.loading = false
                            onErrorResponse(e)
                        }
                    })
        } else {
            super.callApi()
        }

    }


    override fun onErrorResponse(error: Throwable) {
        val response = mDataBaseHandler.getResponseFromDatabase(mHashIdentifier)
        if ((!NetworkHelper.isNetworkAvailable(this) || (error is HttpException && error.code() == 304)) && response.isNotBlank()) {
            onSuccessfulResponse(mGson.fromJson(response, CatalogProductsResponseModel::class.java))
        } else {
            AlertDialogHelper.showNewCustomDialog(
                    this,
                    getString(R.string.error),
                    NetworkHelper.getErrorMessage(this, error),
                    false,
                    getString(R.string.try_again),
                    DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        mPageNumber--
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
