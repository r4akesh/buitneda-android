package com.webkul.mobikulmp.handlers

import android.content.Intent
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.helpers.BundleKeysHelper
import com.webkul.mobikul.helpers.ConstantsHelper
import com.webkul.mobikul.helpers.MobikulApplication
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.SellerReviewActivity
import com.webkul.mobikulmp.fragments.SellerMakeReviewFragment
import com.webkul.mobikulmp.fragments.SellerStoreReviewFragment
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_SELLER_ID
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_SHOP_URL

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.helpers
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 25/7/19
 */
class SellerStoreReviewHandler(private val mSellerStoreReviewFragment: SellerStoreReviewFragment) {


    fun onClickMakeSellerReview(sellerId: Int, shopUrl: String) {
        val sellerMakeReviewFragment = SellerMakeReviewFragment.newInstance(sellerId, shopUrl)
        sellerMakeReviewFragment.show((mSellerStoreReviewFragment.activity as BaseActivity).supportFragmentManager, SellerMakeReviewFragment::class.java.simpleName)
    }


    fun onClickLogin() {
        val intent = (mSellerStoreReviewFragment.activity!!.application as MobikulApplication).getLoginAndSignUpActivity(mSellerStoreReviewFragment.context!!)
        intent?.putExtra(BundleKeysHelper.BUNDLE_KEY_LOGIN_OR_REGISTER_PAGE, ConstantsHelper.OPEN_LOGIN_PAGE)
        mSellerStoreReviewFragment.startActivityForResult(intent, ConstantsHelper.RC_LOGIN)
    }


    fun onClickStoreRating() {
        if (mSellerStoreReviewFragment.mContentViewBinding.storeRatingInformation.visibility == View.VISIBLE) {
            mSellerStoreReviewFragment.mContentViewBinding.storeRatingInformation.visibility = View.GONE
            mSellerStoreReviewFragment.mContentViewBinding.storeRatingHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mSellerStoreReviewFragment.context!!, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mSellerStoreReviewFragment.mContentViewBinding.storeRatingInformation.visibility = View.VISIBLE
            mSellerStoreReviewFragment.mContentViewBinding.storeRatingHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mSellerStoreReviewFragment.context!!, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mSellerStoreReviewFragment.mContentViewBinding.scrollView.top + mSellerStoreReviewFragment.mContentViewBinding.storeRatingHeading.top
                mSellerStoreReviewFragment.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }

    fun onClickSellerRating() {
        if (mSellerStoreReviewFragment.mContentViewBinding.sellerReviewsInformation.visibility == View.VISIBLE) {
            mSellerStoreReviewFragment.mContentViewBinding.sellerReviewsInformation.visibility = View.GONE
            mSellerStoreReviewFragment.mContentViewBinding.sellerReviewsHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mSellerStoreReviewFragment.context!!, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mSellerStoreReviewFragment.mContentViewBinding.sellerReviewsInformation.visibility = View.VISIBLE
            mSellerStoreReviewFragment.mContentViewBinding.sellerReviewsHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mSellerStoreReviewFragment.context!!, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mSellerStoreReviewFragment.mContentViewBinding.scrollView.top + mSellerStoreReviewFragment.mContentViewBinding.sellerReviewsHeading.top
                mSellerStoreReviewFragment.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }

    fun onClickViewSellerReview(sellerId: Int, recentSellerReviewCount: Int, shopUrl: String) {
        if (recentSellerReviewCount == 0) {
            Toast.makeText(mSellerStoreReviewFragment.context, mSellerStoreReviewFragment.getString(R.string.no_review_found_be_the_first_one), Toast.LENGTH_LONG).show()
        } else {
            val intent = Intent(mSellerStoreReviewFragment.context, SellerReviewActivity::class.java)
            intent.putExtra(BUNDLE_KEY_SELLER_ID, sellerId)
            intent.putExtra(BUNDLE_KEY_SHOP_URL, shopUrl)
            mSellerStoreReviewFragment.startActivity(intent)
        }
    }
}
