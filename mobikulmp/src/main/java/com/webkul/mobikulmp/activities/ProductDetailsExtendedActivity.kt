package com.webkul.mobikulmp.activities

import android.view.View
import com.google.gson.Gson
import com.webkul.mobikul.activities.ProductDetailsActivity
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikul.helpers.NetworkHelper
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.product.ProductDetailsPageModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.databinding.SellerInfoLayoutBinding
import com.webkul.mobikulmp.handlers.SellerInfoHandler
import com.webkul.mobikulmp.models.sellerinfo.ProductDetailsPageModelExtended
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.activities
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 7/9/19
 */
class ProductDetailsExtendedActivity : ProductDetailsActivity() {

    override fun callApi() {
        mContentViewBinding.loading = true
        mHashIdentifier = Utils.getMd5String("getProductPageData" + AppSharedPref.getStoreId(this) + AppSharedPref.getCustomerToken(this) + AppSharedPref.getQuoteId(this) + AppSharedPref.getCurrencyCode(this) + mProductId)
        MpApiConnection.getProductPageData(this, mDataBaseHandler.getETagFromDatabase(mHashIdentifier), mProductId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<ProductDetailsPageModelExtended>(this, false) {
                    override fun onNext(productDetailsPageModel: ProductDetailsPageModelExtended) {
                        super.onNext(productDetailsPageModel)
                        mContentViewBinding.loading = false
                        if (productDetailsPageModel.success) {
                            if (AppSharedPref.getRecentlyViewedProductsEnabled(this@ProductDetailsExtendedActivity) && NetworkHelper.isNetworkAvailable(this@ProductDetailsExtendedActivity)) {
                                mDataBaseHandler.addRecentlyViewed(AppSharedPref.getStoreId(this@ProductDetailsExtendedActivity), AppSharedPref.getCurrencyCode(this@ProductDetailsExtendedActivity), productDetailsPageModel.id, Gson().toJson(productDetailsPageModel))
                            }
                            sellerInfo(productDetailsPageModel)
                            onSuccessfulResponse(productDetailsPageModel)
                        } else {
                            onFailureResponse(productDetailsPageModel)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mContentViewBinding.loading = false
                        onErrorResponse(e)
                    }
                })
        // checkAndLoadLocalData()
    }

    override fun checkAndLoadLocalData() {
        mDataBaseHandler.getResponseFromDatabaseOnThread(mHashIdentifier)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Observer<String> {
                    override fun onNext(response: String) {
                        if (response.isNotBlank()) {
                            val productDetailsPageModelExtended = mGson.fromJson(response, ProductDetailsPageModelExtended::class.java)
                            onSuccessfulResponse(productDetailsPageModelExtended as ProductDetailsPageModel)
                            sellerInfo(productDetailsPageModelExtended)
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
    }

    var binding: SellerInfoLayoutBinding? = null

    fun sellerInfo(productDetailsPageModel: ProductDetailsPageModelExtended?) {
        if (productDetailsPageModel?.sellerInfo?.displaySellerInfo == true && productDetailsPageModel.sellerInfo?.sellerId != "0" && binding == null) {
            mContentViewBinding.sellerInfo.visibility = View.VISIBLE
            binding = SellerInfoLayoutBinding.inflate(layoutInflater, mContentViewBinding.sellerInfo, true)
            binding?.data = productDetailsPageModel
            binding?.handler = SellerInfoHandler(this)
        } else if (productDetailsPageModel?.sellerInfo != null && productDetailsPageModel.sellerInfo?.displaySellerInfo == false) {
            mContentViewBinding.sellerInfo.visibility = View.GONE
        } else {
            if (binding != null) {
                binding?.data = productDetailsPageModel
                binding?.executePendingBindings()
            }
        }
        mContentViewBinding.invalidateAll()
    }
}
