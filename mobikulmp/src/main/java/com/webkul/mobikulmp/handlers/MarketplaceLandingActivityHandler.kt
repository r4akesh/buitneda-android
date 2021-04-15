package com.webkul.mobikulmp.handlers

import android.content.Intent
import android.widget.Toast
import com.webkul.mobikul.helpers.*
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.BecomeSellerActivity
import com.webkul.mobikulmp.activities.MarketplaceLandingActivity
import com.webkul.mobikulmp.activities.SellerDashboardActivity
import com.webkul.mobikulmp.activities.SellerListActivity
import com.webkul.mobikulmp.helpers.MpAppSharedPref

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.adapters
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 1/7/19
 */

class MarketplaceLandingActivityHandler(private val mContext: MarketplaceLandingActivity) {

    fun onClickViewAllSeller() {
        mContext.startActivity(Intent(mContext, SellerListActivity::class.java))
    }

    fun onClickOpenYourShop() {
        if (AppSharedPref.isLoggedIn(mContext)) {
            if (MpAppSharedPref.isSellerPending(mContext)) {
                ToastHelper.showToast(mContext, mContext.getString(R.string.nav_seller_pending_status), Toast.LENGTH_LONG)
            } else if (MpAppSharedPref.isSeller(mContext)) {
                mContext.startActivity(Intent(mContext, SellerDashboardActivity::class.java))
            } else {
                mContext.startActivity(Intent(mContext, BecomeSellerActivity::class.java))
            }
        } else {
            val intent = (mContext.application as MobikulApplication).getLoginAndSignUpActivity(mContext)
            intent?.putExtra(BundleKeysHelper.BUNDLE_KEY_LOGIN_OR_REGISTER_PAGE, ConstantsHelper.OPEN_SIGN_UP_PAGE)
            mContext.startActivityForResult(intent, ConstantsHelper.RC_LOGIN)
        }
    }
}