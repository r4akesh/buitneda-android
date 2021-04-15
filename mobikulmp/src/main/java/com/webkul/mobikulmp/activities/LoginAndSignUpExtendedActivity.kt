package com.webkul.mobikulmp.activities

import com.webkul.mobikul.activities.LoginAndSignUpActivity
import com.webkul.mobikul.models.user.SignUpResponseModel
import com.webkul.mobikulmp.helpers.MpAppSharedPref
import org.json.JSONObject

class LoginAndSignUpExtendedActivity : LoginAndSignUpActivity() {


    override fun socialLoginResponse(socialLoginData: JSONObject?) {
        super.socialLoginResponse(socialLoginData)
    }

    override fun updateSharedPref(signUpResponseModel: SignUpResponseModel) {
        super.updateSharedPref(signUpResponseModel)
        try {
            MpAppSharedPref.setIsSeller(this, signUpResponseModel.isSeller)
            MpAppSharedPref.setIsSellerPending(this, signUpResponseModel.isPending)
            MpAppSharedPref.setIsAdmin(this, signUpResponseModel.isAdmin)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}