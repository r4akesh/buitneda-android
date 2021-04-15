package com.webkul.mobikul.wallet.handlers

import android.content.DialogInterface
import android.view.View
import com.webkul.mobikul.R
import com.webkul.mobikul.helpers.AlertDialogHelper
import com.webkul.mobikul.helpers.NetworkHelper
import com.webkul.mobikul.helpers.ToastHelper
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikul.wallet.activities.TransferWalletAmountActivity
import com.webkul.mobikul.wallet.fragments.AddPayeeDialogFragment
import com.webkul.mobikul.wallet.models.wallet.SendCodeResponseData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class TransferWalletAmountHandler(private val mContext: TransferWalletAmountActivity) {

    fun onClickTransferAmount(receiverId: String, amount: String, note: String) {
        if (receiverId == "0") {
            ToastHelper.showToast(mContext, mContext.getString(R.string.please_select_payee))
        } else if (amount.isBlank()) {
            ToastHelper.showToast(mContext, mContext.getString(R.string.please_enter_amount))
        } else if (amount.toFloat() == 0f) {
            ToastHelper.showToast(mContext, mContext.getString(R.string.please_enter_greater_than_zero))
        } else {
            mContext.mContentViewBinding.loading = true
            Utils.hideKeyboard(mContext)
            ApiConnection.sendCode(mContext, receiverId, amount, note)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : ApiCustomCallback<SendCodeResponseData>(mContext, true) {
                        override fun onNext(sendCodeResponseData: SendCodeResponseData) {
                            super.onNext(sendCodeResponseData)
                            mContext.mContentViewBinding.loading = false
                            if (sendCodeResponseData.success) {
                                mContext.sendCodeResponseData = sendCodeResponseData
                                onSuccessfulResponse(sendCodeResponseData)
                            } else {
                                onFailureResponse(sendCodeResponseData)
                            }
                        }

                        override fun onError(e: Throwable) {
                            super.onError(e)
                            mContext.mContentViewBinding.loading = false
                            onErrorResponse(e)
                        }
                    })
        }
    }

    private fun onSuccessfulResponse(sendCodeResponseData: SendCodeResponseData) {
        ToastHelper.showToast(mContext, sendCodeResponseData.message)
        if (sendCodeResponseData.success) {
            if (sendCodeResponseData.isTransferValidation) {
                mContext.mContentViewBinding.transferForm.visibility = View.GONE
                mContext.mContentViewBinding.validationForm.visibility = View.VISIBLE
                mContext.mContentViewBinding.codeData = sendCodeResponseData
            } else {
                mContext.mContentViewBinding.customerListSpinner.setSelection(0)
                mContext.mContentViewBinding.amountEt.setText("")
                mContext.mContentViewBinding.noteEt.setText("")
                mContext.mContentViewBinding.amountEt.requestFocus()
                mContext.callApi()
            }
        }
    }

    fun onFailureResponse(response: Any) {
        AlertDialogHelper.showNewCustomDialog(
                mContext,
                mContext.getString(R.string.error),
                (response as BaseModel).message,
                false,
                mContext.getString(R.string.ok),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                }
                , ""
                , null)
    }

    private fun onErrorResponse(error: Throwable) {
        AlertDialogHelper.showNewCustomDialog(
                mContext,
                mContext.getString(R.string.error),
                NetworkHelper.getErrorMessage(mContext, error),
                false,
                mContext.getString(R.string.ok),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                }
                , ""
                , null)
    }

    fun onClickSendAmount(receiverId: String, amount: String, code: String, note: String) {
        if (code.isBlank()) {
            ToastHelper.showToast(mContext, mContext.getString(R.string.please_enter_code))
        } else {
            mContext.mContentViewBinding.loading = true
            Utils.hideKeyboard(mContext)
            ApiConnection.sendAmount(mContext, receiverId, amount, code, note, mContext.sendCodeResponseData?.codeHash)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : ApiCustomCallback<BaseModel>(mContext, true) {
                        override fun onNext(sendAmountResponseData: BaseModel) {
                            super.onNext(sendAmountResponseData)
                            mContext.mContentViewBinding.loading = false
                            if (sendAmountResponseData.success) {
                                mContext.mContentViewBinding.customerListSpinner.setSelection(0)
                                mContext.mContentViewBinding.amountEt.setText("")
                                mContext.mContentViewBinding.noteEt.setText("")
                                mContext.mContentViewBinding.codeEt.setText("")
                                mContext.mContentViewBinding.validationForm.setVisibility(View.GONE)
                                mContext.mContentViewBinding.transferForm.setVisibility(View.VISIBLE)
                                mContext.mContentViewBinding.amountEt.requestFocus()
                                mContext.callApi()
                            } else {
                                onFailureResponse(sendAmountResponseData)
                            }
                        }

                        override fun onError(e: Throwable) {
                            super.onError(e)
                            mContext.mContentViewBinding.loading = false
                            onErrorResponse(e)
                        }
                    })
        }
    }

    fun onClickAddPayee() {
        AddPayeeDialogFragment().show(mContext.supportFragmentManager, AddPayeeDialogFragment::class.java.simpleName)
    }
}