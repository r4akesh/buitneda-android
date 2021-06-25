package com.webkul.mobikulmp.activities

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.helpers.AlertDialogHelper
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikul.helpers.NetworkHelper
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.adapters.SellerFeedbackRvAdapter
import com.webkul.mobikulmp.databinding.ActivitySellerReviewBinding
import com.webkul.mobikulmp.handlers.SellerReviewListHandler
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_SELLER_ID
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_SHOP_URL
import com.webkul.mobikulmp.models.seller.SellerReviewListData
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.fragments
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 26/07/19
 */
class SellerReviewActivity : BaseActivity() {

    private lateinit var mContentViewBinding: ActivitySellerReviewBinding
    private var mPageNumber = 1
    private var mSellerId = 0
    private var isFirstCall = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_seller_review)
        startInitialization()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    private fun startInitialization() {
        mSellerId = intent.getIntExtra(BUNDLE_KEY_SELLER_ID, 0)
        mContentViewBinding.handler = SellerReviewListHandler(this, mSellerId, intent.getStringExtra(BUNDLE_KEY_SHOP_URL)?:"")
        initSupportActionBar()
        callApi()
        checkAndLoadLocalData()
    }

    private fun initSupportActionBar() {
        supportActionBar?.title = getString(R.string.seller_reviews)
    }

    fun callApi() {
        mContentViewBinding.loading = true
        mHashIdentifier = Utils.getMd5String("getSellerReviews" + AppSharedPref.getStoreId(this) + mSellerId + mPageNumber)
        MpApiConnection.getSellerReviews(
                this,
                mDataBaseHandler.getETagFromDatabase(mHashIdentifier),
                mSellerId,
                mPageNumber++)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<SellerReviewListData>(this, true) {
                    override fun onNext(t: SellerReviewListData) {
                        super.onNext(t)
                        mContentViewBinding.loading = false
                        if (t.success) {
                            onSuccessfulResponse(t)
                        } else {
                            onFailureResponse(t)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mContentViewBinding.loading = false
                        onErrorResponse(e)
                    }
                })

    }

    override fun onFailureResponse(response: Any) {
        AlertDialogHelper.showNewCustomDialog(
                this,
                getString(R.string.error),
                (response as BaseModel).message,
                false,
                getString(R.string.ok),
            { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                finish()
            }
                , ""
                , null)
    }

    private fun onSuccessfulResponse(response: SellerReviewListData) {
        if (isFirstCall) {
            isFirstCall = false
            mContentViewBinding.data = response
            mContentViewBinding.sellerFeedbackRv.layoutManager = LinearLayoutManager(this)
            mContentViewBinding.sellerFeedbackRv.adapter = SellerFeedbackRvAdapter(this, response.reviewList!!)

            mContentViewBinding.sellerFeedbackRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val lastCompletelyVisibleItemPosition: Int = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    callLazyConnection(lastCompletelyVisibleItemPosition)
                }
            })
        } else {
            mContentViewBinding.data?.reviewList!!.addAll(response.reviewList)
            mContentViewBinding.sellerFeedbackRv.adapter!!.notifyDataSetChanged()
        }
    }

    private fun onErrorResponse(error: Throwable) {
        val response = mDataBaseHandler.getResponseFromDatabase(mHashIdentifier)
        if ((!NetworkHelper.isNetworkAvailable(this) || (error is HttpException && error.code() == 304))) {
            onSuccessfulResponse(mGson.fromJson(response, SellerReviewListData::class.java))
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
                        mPageNumber--
                        callApi()
                    }
                    , getString(com.webkul.mobikul.R.string.close)
                    , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            })
        }
    }

    private fun checkAndLoadLocalData() {
        val response = mDataBaseHandler.getResponseFromDatabase(mHashIdentifier)
        if (response.isNotBlank()) {
            onSuccessfulResponse(mGson.fromJson(response, SellerReviewListData::class.java))
        }
    }


    private fun callLazyConnection(lastCompletelyVisibleItemPosition: Int) {
        if (!(mContentViewBinding.loading)!! && mContentViewBinding.data!!.reviewList.size < mContentViewBinding.data!!.totalCount
                && lastCompletelyVisibleItemPosition == mContentViewBinding.data!!.reviewList.size - 1) {
            callApi()
        }
    }

}
