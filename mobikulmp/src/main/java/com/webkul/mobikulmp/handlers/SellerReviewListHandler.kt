package com.webkul.mobikulmp.handlers

import android.widget.Toast
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.SellerReviewActivity
import com.webkul.mobikulmp.fragments.SellerMakeReviewFragment

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

class SellerReviewListHandler(private val mContext: SellerReviewActivity, private val mSellerId: Int, private val mShopUrl: String) {

    fun onClickMakeSellerReview() {
        if (!AppSharedPref.isLoggedIn(mContext)) {
            Toast.makeText(mContext, R.string.please_login_to_write_reviews, Toast.LENGTH_SHORT).show()
        } else {
            val sellerMakeReviewFragment = SellerMakeReviewFragment.newInstance(mSellerId, mShopUrl)
            sellerMakeReviewFragment.show(mContext.supportFragmentManager, SellerMakeReviewFragment::class.java.simpleName)
        }
    }
}

