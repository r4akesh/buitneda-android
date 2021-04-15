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

package com.webkul.mobikul.handlers

import android.content.Intent
import com.webkul.mobikul.activities.CheckoutActivity
import com.webkul.mobikul.fragments.ProceedCheckoutBottomSheetFragment
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_IS_VIRTUAL_CART
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_LOGIN_OR_REGISTER_PAGE
import com.webkul.mobikul.helpers.ConstantsHelper.OPEN_LOGIN_PAGE
import com.webkul.mobikul.helpers.ConstantsHelper.OPEN_SIGN_UP_PAGE
import com.webkul.mobikul.helpers.ConstantsHelper.RC_LOGIN
import com.webkul.mobikul.helpers.MobikulApplication

class ProceedCheckoutBottomSheetFragmentHandler(val mFragmentContext: ProceedCheckoutBottomSheetFragment) {

    fun onClickLoginBtn() {
        val intent = (mFragmentContext.activity!!.application as MobikulApplication).getLoginAndSignUpActivity(mFragmentContext.context!!)
        intent?.putExtra(BUNDLE_KEY_LOGIN_OR_REGISTER_PAGE, OPEN_LOGIN_PAGE)
        mFragmentContext.activity!!.startActivityForResult(intent, RC_LOGIN)
        mFragmentContext.dismiss()
    }

    fun onClickCheckoutAsGuestBtn() {
        val intent = Intent(mFragmentContext.context!!, CheckoutActivity::class.java)
        intent.putExtra(BUNDLE_KEY_IS_VIRTUAL_CART, mFragmentContext.mIsVirtual)
        mFragmentContext.activity!!.startActivity(intent)
        mFragmentContext.dismiss()
    }

    fun onClickRegisterAndCheckoutBtn() {
        val intent = (mFragmentContext.activity!!.application as MobikulApplication).getLoginAndSignUpActivity(mFragmentContext.context!!)
        intent?.putExtra(BUNDLE_KEY_LOGIN_OR_REGISTER_PAGE, OPEN_SIGN_UP_PAGE)
        mFragmentContext.activity!!.startActivityForResult(intent, RC_LOGIN)
        mFragmentContext.dismiss()
    }
}