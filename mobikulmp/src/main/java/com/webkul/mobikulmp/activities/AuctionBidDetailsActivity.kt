package com.webkul.mobikulmp.activities

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.helpers.ApplicationConstants
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_AUCTION_ID
import com.webkul.mobikul.helpers.NetworkHelper
import com.webkul.mobikul.helpers.ToastHelper
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.adapters.AuctionBidDetailsListRvAdapter
import com.webkul.mobikulmp.databinding.ActivityAuctionBidDetailsBinding
import com.webkul.mobikulmp.models.auction.AuctionBidDetailsData
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AuctionBidDetailsActivity : BaseActivity() {
    private var mBinding: ActivityAuctionBidDetailsBinding? = null
    private var mAuctionId: String? = null
    private var mAuctionBidDetailsData: AuctionBidDetailsData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_auction_bid_details)
        actionBar?.setTitle(R.string.aution_bid_details_activity_title)
        mAuctionId = intent.getStringExtra(BUNDLE_KEY_AUCTION_ID)
        callApi()
    }

    private fun callApi() {
        mBinding!!.loading = true
        MpApiConnection.getAuctionBitDetails(this, mAuctionId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<AuctionBidDetailsData>(this, false) {
                    override fun onNext(orderListResponseModel: AuctionBidDetailsData) {
                        super.onNext(orderListResponseModel)
                        mBinding!!.loading=false
                        if (orderListResponseModel != null && orderListResponseModel.success) {
                            mAuctionBidDetailsData=orderListResponseModel
                            startInitialization()

                        }
                    }


                    override fun onError(e: Throwable) {
                        super.onError(e)
                        NetworkHelper.getErrorMessage( this@AuctionBidDetailsActivity,e)
                        mBinding!!.loading=false
//                                onErrorResponse(e)
                    }
                })

    }


    private fun startInitialization() {
        mBinding!!.data = mAuctionBidDetailsData
        mBinding!!.normalBitListRv.adapter = AuctionBidDetailsListRvAdapter(this, mAuctionBidDetailsData!!.normalBidList!!)
        mBinding!!.normalBitListRv.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        mBinding!!.normalBitListRv.isNestedScrollingEnabled = false
        mBinding!!.autoBitListRv.adapter = AuctionBidDetailsListRvAdapter(this, mAuctionBidDetailsData!!.autoBidList!!)
        mBinding!!.autoBitListRv.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        mBinding!!.autoBitListRv.isNestedScrollingEnabled = false
    }
}