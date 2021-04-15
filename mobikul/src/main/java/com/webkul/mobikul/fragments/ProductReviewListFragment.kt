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

package com.webkul.mobikul.fragments


import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.adapters.ReviewsRvAdapter
import com.webkul.mobikul.databinding.FragmentProductReviewListBinding
import com.webkul.mobikul.helpers.AlertDialogHelper
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_ID
import com.webkul.mobikul.helpers.NetworkHelper
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.product.ReviewListData
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class ProductReviewListFragment : BaseFragment() {

    companion object {
        fun newInstance(productId: String): ProductReviewListFragment {
            val productReviewListFragment = ProductReviewListFragment()
            val args = Bundle()
            args.putString(BUNDLE_KEY_PRODUCT_ID, productId)
            productReviewListFragment.arguments = args
            return productReviewListFragment
        }
    }

    lateinit var mContentViewBinding: FragmentProductReviewListBinding
    var mProductId = ""
    var mPageNumber = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_review_list, container, false)

        mProductId = arguments!!.getString(BUNDLE_KEY_PRODUCT_ID)?:""

        return mContentViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callApi()
    }

    private fun callApi() {
        mContentViewBinding.loading = true
        (context as BaseActivity).mHashIdentifier = Utils.getMd5String("getProductReviewList" + AppSharedPref.getStoreId(context!!) + AppSharedPref.getCustomerToken(context!!) + AppSharedPref.getQuoteId(context!!) + mProductId + mPageNumber)
        ApiConnection.getProductReviewList(context!!, BaseActivity.mDataBaseHandler.getETagFromDatabase((context as BaseActivity).mHashIdentifier), mProductId, mPageNumber++)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<ReviewListData>(context!!, false) {
                    override fun onNext(reviewListData: ReviewListData) {
                        super.onNext(reviewListData)
                        mContentViewBinding.loading = false
                        if (reviewListData.success) {
                            onSuccessfulResponse(reviewListData)
                        } else {
                            onFailureResponse(reviewListData)
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

    private fun onSuccessfulResponse(reviewListData: ReviewListData) {
        if (mPageNumber == 2) {
            mContentViewBinding.data = reviewListData
            mContentViewBinding.reviewRv.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
            mContentViewBinding.reviewRv.adapter = ReviewsRvAdapter(context!!, reviewListData.reviewList)
        } else {
            mContentViewBinding.data!!.reviewList.addAll(reviewListData.reviewList)
            mContentViewBinding.reviewRv.adapter?.notifyItemRangeChanged(mContentViewBinding.data!!.reviewList.size - reviewListData.reviewList.size, mContentViewBinding.data!!.reviewList.size)
        }

        mContentViewBinding.reviewRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val lastCompletelyVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (!mContentViewBinding.loading!! && mContentViewBinding.data!!.reviewList.size < mContentViewBinding.data!!.totalCount
                        && lastCompletelyVisibleItemPosition > mContentViewBinding.data!!.reviewList.size - 4) {
                    callApi()
                }
            }
        })
    }

    private fun checkAndLoadLocalData() {
        val response = BaseActivity.mDataBaseHandler.getResponseFromDatabase((context as BaseActivity).mHashIdentifier)
        if (response.isNotBlank()) {
            onSuccessfulResponse(BaseActivity.mGson.fromJson(response, ReviewListData::class.java))
        }
    }

    private fun onFailureResponse(reviewListData: ReviewListData) {
        AlertDialogHelper.showNewCustomDialog(
                context as BaseActivity,
                getString(R.string.error),
                reviewListData.message,
                false,
                getString(R.string.try_again),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    callApi()
                }
                , getString(R.string.dismiss)
                , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        })
    }

    private fun onErrorResponse(error: Throwable) {
        if ((!NetworkHelper.isNetworkAvailable(context!!) || (error is HttpException && error.code() == 304))) {
            // Do Nothing as the data is already loaded
        } else {
            AlertDialogHelper.showNewCustomDialog(
                    context as BaseActivity,
                    getString(R.string.error),
                    NetworkHelper.getErrorMessage(context!!, error),
                    false,
                    getString(R.string.try_again),
                    DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        callApi()
                    }
                    , getString(R.string.dismiss)
                    , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            })
        }
    }
}