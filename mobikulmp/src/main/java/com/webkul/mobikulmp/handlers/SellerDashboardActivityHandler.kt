package com.webkul.mobikulmp.handlers

import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.SellerDashboardActivity
import com.webkul.mobikulmp.fragments.SellerRecentOrdersListFragment
import com.webkul.mobikulmp.models.seller.SellerDashboardData

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.fragments
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 16/6/19
 */

class SellerDashboardActivityHandler(private val mContext: SellerDashboardActivity, private val mData: SellerDashboardData) {

    fun viewSellerOrders() {
        mContext.mItemTimeRange?.isVisible = false
        mContext.supportActionBar?.title = mContext.getString(R.string.seller_orders)

        val fragmentManager = mContext.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        val sellerRecentOrdersListFragment = SellerRecentOrdersListFragment.newInstance(mData.recentOrderList)
        sellerRecentOrdersListFragment.setOnDetachInterface(mContext)
        fragmentTransaction.add(R.id.main_container, sellerRecentOrdersListFragment)
        fragmentTransaction.addToBackStack(SellerRecentOrdersListFragment::class.java.simpleName)
        fragmentTransaction.commit()
    }

    fun viewSellerReviews() {
        /* FragmentManager fragmentManager = ((SellerDashboardActivity) mContext).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.add(R.id.main_container, SellerRecentReviewListFragment.newInstance(mData.getSellerReviewList()));
        fragmentTransaction.addToBackStack(SellerRecentReviewListFragment.class.getSimpleName() + BACKSTACK_SUFFIX);
        fragmentTransaction.commit();*/
    }

    fun onClickTop5ProductBtn() {
        if (mContext.mContentViewBinding.top5ProductInformation.visibility == View.VISIBLE) {
            mContext.mContentViewBinding.top5ProductInformation.visibility = View.GONE
            mContext.mContentViewBinding.top5ProductHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mContext.mContentViewBinding.top5ProductInformation.visibility = View.VISIBLE
            mContext.mContentViewBinding.top5ProductHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mContext.mContentViewBinding.scrollView.top + mContext.mContentViewBinding.top5ProductHeading.top
                mContext.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }

    fun onClickTop5CategoriesBtn() {
        if (mContext.mContentViewBinding.top5CategoryInformation.visibility == View.VISIBLE) {
            mContext.mContentViewBinding.top5CategoryInformation.visibility = View.GONE
            mContext.mContentViewBinding.top5CategoryHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mContext.mContentViewBinding.top5CategoryInformation.visibility = View.VISIBLE
            mContext.mContentViewBinding.top5CategoryHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mContext.mContentViewBinding.scrollView.top + mContext.mContentViewBinding.top5CategoryHeading.top
                mContext.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }

    fun onClickLatestOrdersBtn() {
        if (mContext.mContentViewBinding.latestOrdersInformation.visibility == View.VISIBLE) {
            mContext.mContentViewBinding.latestOrdersInformation.visibility = View.GONE
            mContext.mContentViewBinding.latestOrdersHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mContext.mContentViewBinding.latestOrdersInformation.visibility = View.VISIBLE
            mContext.mContentViewBinding.latestOrdersHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mContext.mContentViewBinding.scrollView.top + mContext.mContentViewBinding.latestOrdersHeading.top
                mContext.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }

    fun onClickRecentReviewsBtn() {
        if (mContext.mContentViewBinding.recentReviewsInformation.visibility == View.VISIBLE) {
            mContext.mContentViewBinding.recentReviewsInformation.visibility = View.GONE
            mContext.mContentViewBinding.recentReviewsHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mContext.mContentViewBinding.recentReviewsInformation.visibility = View.VISIBLE
            mContext.mContentViewBinding.recentReviewsHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mContext.mContentViewBinding.scrollView.top + mContext.mContentViewBinding.recentReviewsHeading.top
                mContext.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }
}