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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class SavedAccountsRvHandler(private val mContext: AddAccountDetailsActivity) {

    fun onClickRequestDelete(id: String) {
        mContext.mContentViewBinding.loading = true
        ApiConnection.deleteWalletAccountInfo(mContext, id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<BaseModel>(mContext, true) {
                    override fun onNext(deleteWalletAccountInfoResponseData: BaseModel) {
                        super.onNext(deleteWalletAccountInfoResponseData)
                        mContext.mContentViewBinding.loading = false
                        if (deleteWalletAccountInfoResponseData.success) {
                            onSuccessfulResponse(deleteWalletAccountInfoResponseData)
                        } else {
                            onFailureResponse(deleteWalletAccountInfoResponseData)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mContext.mContentViewBinding.loading = false
                        onErrorResponse(e)
                    }
                })
    }

    private fun onSuccessfulResponse(deleteWalletAccountInfoResponseData: BaseModel) {
        ToastHelper.showToast(mContext, deleteWalletAccountInfoResponseData.message)
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