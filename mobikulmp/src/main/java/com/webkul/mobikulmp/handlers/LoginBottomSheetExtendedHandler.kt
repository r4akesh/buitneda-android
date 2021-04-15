package com.webkul.mobikulmp.handlers

import com.webkul.mobikul.fragments.LoginBottomSheetFragment
import com.webkul.mobikul.handlers.LoginBottomSheetHandler
import com.webkul.mobikul.models.user.LoginResponseModel
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
class LoginBottomSheetExtendedHandler(mFragmentContext: LoginBottomSheetFragment) : LoginBottomSheetHandler(mFragmentContext) {
    override fun updateSharedPref(loginResponseModel: LoginResponseModel) {
        super.updateSharedPref(loginResponseModel)
        try {
            MpAppSharedPref.setIsSeller(mFragmentContext.context!!, loginResponseModel.isSeller)
            MpAppSharedPref.setIsSellerPending(mFragmentContext.context!!, loginResponseModel.isPending)
            MpAppSharedPref.setIsAdmin(mFragmentContext.context!!, loginResponseModel.isAdmin)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
