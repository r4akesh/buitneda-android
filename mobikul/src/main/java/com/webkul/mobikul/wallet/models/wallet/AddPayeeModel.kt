package com.webkul.mobikul.wallet.models.wallet

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.webkul.mobikul.BR
import com.webkul.mobikul.R
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.wallet.fragments.AddPayeeDialogFragment


class AddPayeeModel : BaseObservable() {

    var id: String = ""
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }

    var nickName: String = ""
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.nickName)
        }
    var email: String = ""
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.email)
        }
    var confirmEmail: String = ""
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.confirmEmail)
        }

    fun isFormValidated(mFragmentContext: AddPayeeDialogFragment): Boolean {
        var isFormValidated = true

        /* Checking bankCode */
        if (confirmEmail.isBlank()) {
            isFormValidated = false
            mFragmentContext.mContentViewBinding.confirmEmail.error = mFragmentContext.getString(R.string.confirm_email) + " " + mFragmentContext.getString(R.string.is_required)
            Utils.showShakeError(mFragmentContext.context!!, mFragmentContext.mContentViewBinding.confirmEmail)
            mFragmentContext.mContentViewBinding.confirmEmail.requestFocus()
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(confirmEmail).matches()) {
            isFormValidated = false
            mFragmentContext.mContentViewBinding.confirmEmail.error = mFragmentContext.getString(R.string.enter_a_valid) + " " + mFragmentContext.getString(R.string.confirm_email)
            Utils.showShakeError(mFragmentContext.context!!, mFragmentContext.mContentViewBinding.confirmEmail)
            mFragmentContext.mContentViewBinding.confirmEmail.requestFocus()
        } else if (email.isNotEmpty() && confirmEmail.isNotEmpty() && email != confirmEmail) {
            isFormValidated = false
            mFragmentContext.mContentViewBinding.confirmEmail.error = mFragmentContext.getString(R.string.email_match_issue)
            Utils.showShakeError(mFragmentContext.context!!, mFragmentContext.mContentViewBinding.confirmEmail)
            mFragmentContext.mContentViewBinding.confirmEmail.requestFocus()
        } else {
            mFragmentContext.mContentViewBinding.confirmEmail.isErrorEnabled = false
            mFragmentContext.mContentViewBinding.confirmEmail.error = null
        }

        /* Checking bankCode */
        if (email.isBlank()) {
            isFormValidated = false
            mFragmentContext.mContentViewBinding.email.error = mFragmentContext.getString(R.string.email_address) + " " + mFragmentContext.getString(R.string.is_required)
            Utils.showShakeError(mFragmentContext.context!!, mFragmentContext.mContentViewBinding.email)
            mFragmentContext.mContentViewBinding.email.requestFocus()
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isFormValidated = false
            mFragmentContext.mContentViewBinding.email.error = mFragmentContext.getString(R.string.enter_a_valid) + " " + mFragmentContext.getString(R.string.email_address)
            Utils.showShakeError(mFragmentContext.context!!, mFragmentContext.mContentViewBinding.email)
            mFragmentContext.mContentViewBinding.email.requestFocus()
        } else {
            mFragmentContext.mContentViewBinding.email.isErrorEnabled = false
            mFragmentContext.mContentViewBinding.email.error = null
        }

        /* Checking bankCode */
        if (nickName.isBlank()) {
            isFormValidated = false
            mFragmentContext.mContentViewBinding.nickName.error = mFragmentContext.getString(R.string.nick_name) + " " + mFragmentContext.getString(R.string.is_required)
            Utils.showShakeError(mFragmentContext.context!!, mFragmentContext.mContentViewBinding.nickName)
            mFragmentContext.mContentViewBinding.nickName.requestFocus()
        } else {
            mFragmentContext.mContentViewBinding.nickName.isErrorEnabled = false
            mFragmentContext.mContentViewBinding.nickName.error = null
        }

        return isFormValidated
    }
}