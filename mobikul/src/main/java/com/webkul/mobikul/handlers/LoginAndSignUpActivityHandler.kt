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

import com.webkul.mobikul.activities.LoginAndSignUpActivity
import com.webkul.mobikul.fragments.LoginBottomSheetFragment
import com.webkul.mobikul.fragments.SignUpBottomSheetFragment

class LoginAndSignUpActivityHandler(var mContext: LoginAndSignUpActivity) {

    fun onClickLogin() {
        val loginBottomSheetFragment = LoginBottomSheetFragment()
        loginBottomSheetFragment.show(mContext.supportFragmentManager, loginBottomSheetFragment.tag)
    }

    fun onClickSignUp() {
        val signUpBottomSheetFragment = SignUpBottomSheetFragment()
        signUpBottomSheetFragment.show(mContext.supportFragmentManager, signUpBottomSheetFragment.tag)
    }
}