package com.webkul.mobikulmp.handlers

import com.webkul.mobikul.fragments.SignUpBottomSheetFragment
import com.webkul.mobikul.handlers.SignUpBottomSheetHandler
import com.webkul.mobikul.models.user.SignUpResponseModel
import com.webkul.mobikulmp.helpers.MpAppSharedPref

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.handlers
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 22/5/19
 */
class SignUpBottomSheetExtendHandler(mFragmentContext: SignUpBottomSheetFragment) : SignUpBottomSheetHandler(mFragmentContext) {

    override fun updateSharedPref(signUpResponseModel: SignUpResponseModel) {
        super.updateSharedPref(signUpResponseModel)
        try {
            MpAppSharedPref.setIsSeller(mFragmentContext.context!!, signUpResponseModel.isSeller)
            MpAppSharedPref.setIsSellerPending(mFragmentContext.context!!, signUpResponseModel.isPending)
            MpAppSharedPref.setIsAdmin(mFragmentContext.context!!, signUpResponseModel.isAdmin)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
