package com.webkul.mobikul.wallet.models.wallet

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.webkul.mobikul.BR
import com.webkul.mobikul.R
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.wallet.fragments.TransferToBankDialogFragment


class TransferToBankModel : BaseObservable() {

    var amount: String = ""
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.amount)
        }
    var accountDetails: String = ""
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.accountDetails)
        }
    var note: String = ""
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.note)
        }

    fun isFormValidated(mFragmentContext: TransferToBankDialogFragment): Boolean {
        var isFormValidated = true

        /* Checking bankCode */
        if (amount.isBlank()) {
            isFormValidated = false
            mFragmentContext.mContentViewBinding.amount.error = mFragmentContext.getString(R.string.amount) + " " + mFragmentContext.getString(R.string.is_required)
            Utils.showShakeError(mFragmentContext.context!!, mFragmentContext.mContentViewBinding.amount)
            mFragmentContext.mContentViewBinding.amount.requestFocus()
        } else if (amount.toFloat() == 0f) {
            isFormValidated = false
            mFragmentContext.mContentViewBinding.amount.error = mFragmentContext.getString(R.string.please_enter_greater_than_zero)
            Utils.showShakeError(mFragmentContext.context!!, mFragmentContext.mContentViewBinding.amount)
            mFragmentContext.mContentViewBinding.amount.requestFocus()
        }else if(accountDetails.isBlank()){
            isFormValidated = false
            mFragmentContext.mContentViewBinding.accountDetails.error = mFragmentContext.getString(R.string.account_details) + " " + mFragmentContext.getString(R.string.is_required)
            Utils.showShakeError(mFragmentContext.context!!, mFragmentContext.mContentViewBinding.accountDetails)
            mFragmentContext.mContentViewBinding.accountDetails.requestFocus()

        } else {
            mFragmentContext.mContentViewBinding.amount.isErrorEnabled = false
            mFragmentContext.mContentViewBinding.amount.error = null
        }

        return isFormValidated
    }
}