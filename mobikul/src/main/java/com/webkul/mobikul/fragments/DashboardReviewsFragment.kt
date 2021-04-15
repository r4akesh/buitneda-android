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


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.activities.DashboardActivity
import com.webkul.mobikul.adapters.MyReviewsListRvAdapter
import com.webkul.mobikul.databinding.FragmentDashboardReviewsBinding
import com.webkul.mobikul.handlers.DashboardReviewsFragmentHandler
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikul.helpers.ApplicationConstants
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.user.ReviewListResponseModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class DashboardReviewsFragment : BaseFragment() {

    lateinit var mContentViewBinding: FragmentDashboardReviewsBinding
    private var mHashIdentifier: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard_reviews, container, false)
        return mContentViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callApi()
    }

    private fun callApi() {
        mContentViewBinding.loading = true
        mHashIdentifier = Utils.getMd5String("getReviewsListForDashboard" + AppSharedPref.getStoreId(context!!) + AppSharedPref.getCustomerToken(context!!))
        ApiConnection.getReviewsList(context!!, BaseActivity.mDataBaseHandler.getETagFromDatabase(mHashIdentifier), 1, true)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<ReviewListResponseModel>(context!!, false) {
                    override fun onNext(reviewListResponseModel: ReviewListResponseModel) {
                        super.onNext(reviewListResponseModel)
                        mContentViewBinding.loading = false
                        if (reviewListResponseModel.success) {
                            if (ApplicationConstants.ENABLE_OFFLINE_MODE && mHashIdentifier.isNotEmpty())
                                BaseActivity.mDataBaseHandler.addOrUpdateIntoOfflineTable(mHashIdentifier, reviewListResponseModel.eTag, BaseActivity.mGson.toJson(reviewListResponseModel))
                            onSuccessfulResponse(reviewListResponseModel)
                        } else {
                            onFailureResponse(reviewListResponseModel)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mContentViewBinding.loading = false
                        onErrorResponse(e)
                    }
                })
        checkAndLoadLocalData()
    }

    private fun checkAndLoadLocalData() {
        val response = BaseActivity.mDataBaseHandler.getResponseFromDatabase(mHashIdentifier)
        if (response.isNotBlank()) {
            onSuccessfulResponse(BaseActivity.mGson.fromJson(response, ReviewListResponseModel::class.java))
        }
    }

    private fun onSuccessfulResponse(reviewListResponseModel: ReviewListResponseModel) {
        (context as DashboardActivity).mContentViewBinding.tabs.getTabAt(2)?.text = String.format(Locale.US, getString(R.string.reviews_x), reviewListResponseModel.totalCount)
        mContentViewBinding.data = reviewListResponseModel
        mContentViewBinding.handler = DashboardReviewsFragmentHandler()
        if (mContentViewBinding.data!!.reviewList.isNotEmpty()) {
            setupReviewListRv()
        } else {
            mContentViewBinding.animationView.playAnimation()
        }
    }

    private fun setupReviewListRv() {
        mContentViewBinding.reviewsListRv.adapter = MyReviewsListRvAdapter(context!!, mContentViewBinding.data!!.reviewList)
    }

    private fun onFailureResponse(reviewListResponseModel: ReviewListResponseModel) {
        mContentViewBinding.data = reviewListResponseModel
        mContentViewBinding.animationView.playAnimation()
    }

    private fun onErrorResponse(e: Throwable) {
        if (mContentViewBinding.data == null)
            mContentViewBinding.data = ReviewListResponseModel()
        mContentViewBinding.animationView.playAnimation()
    }
}
