package com.webkul.mobikul.wallet.handlers

import com.webkul.mobikul.R
import com.webkul.mobikul.helpers.ToastHelper
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikul.wallet.activities.ManageWalletAmountActivity
import com.webkul.mobikul.wallet.fragments.TransferToBankDialogFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class TransferToBankDialogFragmentHandler(private val mFragmentContext: TransferToBankDialogFragment) {

    fun onClickSubmit() {
        if (mFragmentContext.mContentViewBinding.data!!.amount.isBlank()) {
            ToastHelper.showToast(mFragmentContext.context!!, mFragmentContext.getString(R.string.please_enter_amount))
        } else if (mFragmentContext.mContentViewBinding.data!!.amount.toFloat() == 0f) {
            ToastHelper.showToast(mFragmentContext.context!!, mFragmentContext.getString(R.string.please_enter_greater_than_zero))
        } else if (mFragmentContext.mContentViewBinding.data!!.amount.toDouble() < mFragmentContext.minimumAmount?.toDouble() ?: 0.0) {
            ToastHelper.showToast(mFragmentContext.context!!, String.format(mFragmentContext.getString(R.string.please_enter_greater_than_s), mFragmentContext.minimumAmount
                    ?: "0.0"))
        } else if (mFragmentContext.mContentViewBinding.data!!.amount.toDouble() > mFragmentContext.maximumAmount?.toDouble() ?: 0.0) {
            ToastHelper.showToast(mFragmentContext.context!!, String.format(mFragmentContext.getString(R.string.please_enter_less_than_s), mFragmentContext.maximumAmount
                    ?: "0.0"))
        } else {
            if (mFragmentContext.mContentViewBinding.data!!.isFormValidated(mFragmentContext)) {
                mFragmentContext.mContentViewBinding.loading = true
                ApiConnection.transferToBank(mFragmentContext.context!!, mFragmentContext.mContentViewBinding.data!!.amount, mFragmentContext.mContentViewBinding.data!!.accountDetails, mFragmentContext.mContentViewBinding.data!!.note)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(object : ApiCustomCallback<BaseModel>(mFragmentContext.context!!, true) {
                            override fun onNext(transferToBankResponseData: BaseModel) {
                                super.onNext(transferToBankResponseData)
                                mFragmentContext.mContentViewBinding.loading = false
                                ToastHelper.showToast(mFragmentContext.context!!, transferToBankResponseData.message)
                                if (transferToBankResponseData.success) {
                                    (mFragmentContext.context as ManageWalletAmountActivity).callApi()
                                    mFragmentContext.dismiss()
                                }
                            }

                            override fun onError(e: Throwable) {
                                super.onError(e)
                                mFragmentContext.mContentViewBinding.loading = false
                                ToastHelper.showToast(mFragmentContext.context!!, mFragmentContext.getString(R.string.something_went_wrong))
                            }
                        })
            }
        }
    }

    fun onClickReset() {
        mFragmentContext.mContentViewBinding.data?.amount = ""
       // mFragmentContext.mContentViewBinding.accountDetailsSp.setSelection(0)
        mFragmentContext.mContentViewBinding.data?.accountDetails=""
        mFragmentContext.mContentViewBinding.data?.note = ""
    }
}