package com.webkul.mobikulmp.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.helpers.AlertDialogHelper
import com.webkul.mobikul.helpers.ConstantsHelper
import com.webkul.mobikul.helpers.NetworkHelper
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.adapters.MarketplaceLandingPageBestSellerListRvAdapter
import com.webkul.mobikulmp.databinding.ActivityMarketplaceLandingBinding
import com.webkul.mobikulmp.handlers.MarketplaceLandingActivityHandler
import com.webkul.mobikulmp.models.landingpage.MarketplaceLandingPageData
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class MarketplaceLandingActivity : BaseActivity() {

    lateinit var mContentViewBinding: ActivityMarketplaceLandingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_marketplace_landing)
        supportActionBar?.title = getString(R.string.marketplace)
        callApi()
    }

    private fun callApi() {
        mContentViewBinding.loading = true
        MpApiConnection.getLandingPageData(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<MarketplaceLandingPageData>(this, true) {
                    override fun onNext(marketplaceLandingPageData: MarketplaceLandingPageData) {
                        super.onNext(marketplaceLandingPageData)
                        mContentViewBinding.loading = false
                        if (marketplaceLandingPageData.success) {
                            initViews(marketplaceLandingPageData)
                        } else {
                            onFailureResponse(marketplaceLandingPageData)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        onErrorResponse(e)
                    }
                })
    }

    private fun initViews(landingPageData: MarketplaceLandingPageData) {
        mContentViewBinding.data = landingPageData
        mContentViewBinding.handler = MarketplaceLandingActivityHandler(this@MarketplaceLandingActivity)
        if (landingPageData.pageLayout == 1) {
            Log.d("Tag", "MarketplaceLandingActivity-  product adapter->" + landingPageData.layout1?.sellersData?.size)
            Log.d("Tag", "MarketplaceLandingActivity-  product layout1->" + landingPageData.layout1)
            Log.d("Tag", "MarketplaceLandingActivity-  product layout1->" + landingPageData.layout1?.buttonHeadingLal)
            Log.d("Tag", "MarketplaceLandingActivity-  product pageLayout->" + landingPageData.pageLayout)
            mContentViewBinding.bestSellerListRv.layoutManager = LinearLayoutManager(this@MarketplaceLandingActivity)
            mContentViewBinding.bestSellerListRv.adapter = landingPageData.layout1?.sellersData?.let { MarketplaceLandingPageBestSellerListRvAdapter(this@MarketplaceLandingActivity, it) }
        }
    }

    override fun onFailureResponse(response: Any) {
        AlertDialogHelper.showNewCustomDialog(
                this,
                getString(com.webkul.mobikul.R.string.error),
                (response as BaseModel).message,
                false,
                getString(com.webkul.mobikul.R.string.ok),
            { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
                , ""
                , null)
    }


    private fun onErrorResponse(error: Throwable) {
        mContentViewBinding.loading = false
        if ((!NetworkHelper.isNetworkAvailable(this) || (error is HttpException && error.code() == 304))) {

        } else {
            AlertDialogHelper.showNewCustomDialog(
                    this,
                    getString(com.webkul.mobikul.R.string.oops),
                    NetworkHelper.getErrorMessage(this, error),
                    false,
                    getString(com.webkul.mobikul.R.string.try_again),
                    DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        mContentViewBinding.loading = true
                        callApi()
                    }
                    , getString(com.webkul.mobikul.R.string.close)
                    , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                ConstantsHelper.RC_LOGIN -> {
                    mContentViewBinding.handler?.onClickOpenYourShop()
                }
            }
        }
    }
}
