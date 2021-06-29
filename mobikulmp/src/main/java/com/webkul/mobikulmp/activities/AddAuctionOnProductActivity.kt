package com.webkul.mobikulmp.activity

import android.content.DialogInterface
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.helpers.AlertDialogHelper
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_AUCTION_ID
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_ID
import com.webkul.mobikul.helpers.NetworkHelper
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.user.OrderListResponseModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.ActivityAddAuctionOnProductBinding
import com.webkul.mobikulmp.handlers.AddAuctionOnProductActivityHandler
import com.webkul.mobikulmp.handlers.IncrementRuleRvAdapter
import com.webkul.mobikulmp.model.auction.AuctionFormData
import com.webkul.mobikulmp.models.auction.IncrementalRule
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.util.*

class AddAuctionOnProductActivity : BaseActivity() {
    var mBinding: ActivityAddAuctionOnProductBinding? =null
    var mAuctionFormData: AuctionFormData? = null
    var mProductId: String? = null
    var mAuctionId: String? = null

    override protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_auction_on_product)
//        setActionbarTitle(getResources().getString(R.string.add_auction_on_product_activity_title))
        mProductId = intent.getStringExtra(BUNDLE_KEY_PRODUCT_ID)
        mAuctionId = intent.getStringExtra(BUNDLE_KEY_AUCTION_ID)
        callApi()
    }



    private fun callApi() {
        mBinding!!.loading=true
        MpApiConnection.getAuctionFormData(this, mProductId, mAuctionId)!!
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<AuctionFormData>(this, false) {
                    override fun onNext(orderListResponseModel: AuctionFormData) {
                        super.onNext(orderListResponseModel)
                        mBinding!!.loading=false
                        if (orderListResponseModel.success) {
                            onSuccessfulResponse(orderListResponseModel)
                        } else {
                            onFailureResponse(orderListResponseModel)
                        }
                    }

                    private fun onSuccessfulResponse(orderListResponseModel: AuctionFormData) {
                        mAuctionFormData = orderListResponseModel
                        startInitialization()
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mBinding!!.loading=false
                        onErrorResponse(e)
                    }
                })
    }

    private fun onErrorResponse(error: Throwable) {
        if ((!NetworkHelper.isNetworkAvailable(this) || (error is HttpException && error.code() == 304)) && mBinding!!.data != null) {
            // Do Nothing as the data is already loaded
        } else {
            AlertDialogHelper.showNewCustomDialog(
                    this,
                    getString(com.webkul.mobikul.R.string.error),
                    NetworkHelper.getErrorMessage(this, error),
                    false,
                    getString(com.webkul.mobikul.R.string.try_again),
                    DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        callApi()
                    }
                    , getString(com.webkul.mobikul.R.string.dismiss)
                    , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                })
        }
    }


    private fun startInitialization() {
        mBinding!!.data = mAuctionFormData
        mBinding!!.handler = AddAuctionOnProductActivityHandler(this)
        setupAuctionOption()
        setupAdminIncrementRules()
        setupSellerIncrementRules()
        setStartStopTimeAdjust()
    }

    private fun setStartStopTimeAdjust() {
        mAuctionFormData!!.startTimeAdjust = mAuctionFormData!!.getStartTime()
        mAuctionFormData!!.stopTimeAdjust = mAuctionFormData!!.getStopTime()
    }

    private fun setupAuctionOption() {
        if (mAuctionFormData!!.auctionType == null || mAuctionFormData!!.auctionType!!.size == 0) {
            mBinding!!.auctionCb.isChecked = true
        } else {
            if (mAuctionFormData!!.auctionType!!.contains("1")) {
                mBinding!!.buyItNowCb.isChecked = true
            }
            if (mAuctionFormData!!.auctionType!!.contains("2")) {
                mBinding!!.auctionCb.isChecked = true
            }
        }
    }

    private fun setupAdminIncrementRules() {
        if (mAuctionFormData!!.adminIncrementalRule != null && mAuctionFormData!!.adminIncrementalRule!!.size > 0) {
            mBinding!!.adminIncrementBidRulesRv.adapter =
                IncrementRuleRvAdapter(this, mAuctionFormData!!.adminIncrementalRule, false)
            mBinding!!.adminIncrementBidRulesRv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        }
    }

    private fun setupSellerIncrementRules() {
        if (mAuctionFormData!!.getIncrementalRule() == null) {
            mAuctionFormData!!.setIncrementalRule(ArrayList<IncrementalRule>())
        }
        mBinding!!.sellerIncrementBidRulesRv.adapter =
            IncrementRuleRvAdapter(this, mAuctionFormData!!.getIncrementalRule(), true)
        mBinding!!.sellerIncrementBidRulesRv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }
}