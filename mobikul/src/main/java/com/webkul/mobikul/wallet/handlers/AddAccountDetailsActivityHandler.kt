package com.webkul.mobikul.wallet.handlers

import android.content.DialogInterface
import com.webkul.mobikul.R
import com.webkul.mobikul.helpers.AlertDialogHelper
import com.webkul.mobikul.helpers.NetworkHelper
import com.webkul.mobikul.helpers.ToastHelper
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikul.wallet.activities.AddAccountDetailsActivity
import com.webkul.mobikul.wallet.models.wallet.AccountDetailsResponseData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class AddAccountDetailsActivityHandler(private val mContext: AddAccountDetailsActivity) {

    fun onClickSubmitDetails(accountDetailsResponseData: AccountDetailsResponseData) {
        if (accountDetailsResponseData.isFormValidated(mContext)) {
            mContext.mContentViewBinding.loading = true
            ApiConnection.saveWalletAccountInfo(mContext, accountDetailsResponseData)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : ApiCustomCallback<BaseModel>(mContext, true) {
                        override fun onNext(saveWalletAccountInfoResponseData: BaseModel) {
                            super.onNext(saveWalletAccountInfoResponseData)
                            mContext.mContentViewBinding.loading = false
                            if (saveWalletAccountInfoResponseData.success) {
                                onSuccessfulResponse(saveWalletAccountInfoResponseData)
                            } else {
                                onFailureResponse(saveWalletAccountInfoResponseData)
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

    private fun onSuccessfulResponse(saveWalletAccountInfoResponseData: BaseModel) {
        ToastHelper.showToast(mContext, saveWalletAccountInfoResponseData.message)
        if (saveWalletAccountInfoResponseData.success) {
            mContext.mContentViewBinding.holderName.editText?.setText("")
            mContext.mContentViewBinding.bankName.editText?.setText("")
            mContext.mContentViewBinding.bankCode.editText?.setText("")
            mContext.mContentViewBinding.addInfo.editText?.setText("")
            mContext.callApi()
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
}