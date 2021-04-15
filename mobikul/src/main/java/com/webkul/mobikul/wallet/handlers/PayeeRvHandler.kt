package com.webkul.mobikul.wallet.handlers

import android.content.DialogInterface
import com.webkul.mobikul.R
import com.webkul.mobikul.helpers.AlertDialogHelper
import com.webkul.mobikul.helpers.NetworkHelper
import com.webkul.mobikul.helpers.ToastHelper
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikul.wallet.activities.TransferWalletAmountActivity
import com.webkul.mobikul.wallet.fragments.AddPayeeDialogFragment
import com.webkul.mobikul.wallet.models.wallet.AddedPayeeList
import com.webkul.mobikul.wallet.models.wallet.CustomerList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class PayeeRvHandler(private val mContext: TransferWalletAmountActivity) {

    fun onClickEditBtn(customerList: CustomerList) {
        AddPayeeDialogFragment.newInstance(customerList).show(mContext.supportFragmentManager, AddPayeeDialogFragment::class.java.simpleName)
    }

    fun onClickDeleteBtn(id: String) {
        mContext.mContentViewBinding.loading = true
        ApiConnection.deletePayee(mContext, id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<BaseModel>(mContext, true) {
                    override fun onNext(deletePayeeResponseData: BaseModel) {
                        super.onNext(deletePayeeResponseData)
                        mContext.mContentViewBinding.loading = false
                        if (deletePayeeResponseData.success) {
                            onSuccessfulResponse(deletePayeeResponseData)
                        } else {
                            onFailureResponse(deletePayeeResponseData)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mContext.mContentViewBinding.loading = false
                        onErrorResponse(e)
                    }
                })
    }

    private fun onSuccessfulResponse(deletePayeeResponseData: BaseModel) {
        ToastHelper.showToast(mContext, deletePayeeResponseData.message)
        mContext.callApi()
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