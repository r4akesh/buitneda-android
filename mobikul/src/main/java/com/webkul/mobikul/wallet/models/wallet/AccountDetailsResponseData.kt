package com.webkul.mobikul.wallet.models.wallet

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.webkul.mobikul.R
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.wallet.activities.AddAccountDetailsActivity


class AccountDetailsResponseData : BaseModel() {

    @JsonProperty("response_data")
    var responseData: String = ""

    var holderName: String = ""
    var bankName: String = ""
    var bankCode: String = ""
    var additionalInformation: String = ""

    fun getSavedAccountList(): ArrayList<SavedAccountsList> {
        return Gson().fromJson(responseData, object : TypeToken<ArrayList<SavedAccountsList>>() {}.type)
    }

    fun isFormValidated(context: AddAccountDetailsActivity): Boolean {
        var isFormValidated = true

        /* Checking bankCode */
        if (bankCode.isBlank()) {
            isFormValidated = false
            context.mContentViewBinding.bankCode.error = context.getString(R.string.bank_code) + " " + context.getString(R.string.is_required)
            Utils.showShakeError(context, context.mContentViewBinding.bankCode)
            context.mContentViewBinding.bankCode.requestFocus()
        } else {
            context.mContentViewBinding.bankCode.isErrorEnabled = false
            context.mContentViewBinding.bankCode.error = null
        }

        /* Checking bankName */
        if (bankName.isBlank()) {
            isFormValidated = false
            context.mContentViewBinding.bankName.error = context.getString(R.string.bank_name) + " " + context.getString(R.string.is_required)
            Utils.showShakeError(context, context.mContentViewBinding.bankName)
            context.mContentViewBinding.bankName.requestFocus()
        } else {
            context.mContentViewBinding.bankName.isErrorEnabled = false
            context.mContentViewBinding.bankName.error = null
        }

        /* Checking bankCode */
        if (holderName.isBlank()) {
            isFormValidated = false
            context.mContentViewBinding.holderName.error = context.getString(R.string.a_c_holder_name) + " " + context.getString(R.string.is_required)
            Utils.showShakeError(context, context.mContentViewBinding.holderName)
            context.mContentViewBinding.holderName.requestFocus()
        } else {
            context.mContentViewBinding.holderName.isErrorEnabled = false
            context.mContentViewBinding.holderName.error = null
        }

        return isFormValidated
    }
}