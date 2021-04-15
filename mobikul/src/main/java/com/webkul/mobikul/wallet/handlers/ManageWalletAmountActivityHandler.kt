package com.webkul.mobikul.wallet.handlers

import android.content.DialogInterface
import com.webkul.mobikul.R
import com.webkul.mobikul.helpers.AlertDialogHelper
import com.webkul.mobikul.helpers.NetworkHelper
import com.webkul.mobikul.helpers.ToastHelper
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.models.checkout.AddToCartResponseModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikul.wallet.activities.ManageWalletAmountActivity
import com.webkul.mobikul.wallet.fragments.TransferToBankDialogFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class ManageWalletAmountActivityHandler(private val mContext: ManageWalletAmountActivity) {

    fun onClickAddToWallet(amount: String, walletProductId: String) {
        if (amount.isBlank()) {
            ToastHelper.showToast(mContext, mContext.getString(R.string.please_enter_amount))
        } else if (amount.toFloat() == 0f) {
            ToastHelper.showToast(mContext, mContext.getString(R.string.please_enter_greater_than_zero))
        } else if (amount.toDouble() < mContext.mContentViewBinding.data!!.minimumAmount.toDouble()) {
            ToastHelper.showToast(mContext, String.format(mContext.getString(R.string.please_enter_greater_than_s), mContext.mContentViewBinding.data!!.minimumAmount))
        } else if (amount.toDouble() > mContext.mContentViewBinding.data!!.maximumAmount.toDouble()) {
            ToastHelper.showToast(mContext, String.format(mContext.getString(R.string.please_enter_less_than_s), mContext.mContentViewBinding.data!!.maximumAmount))
        } else {
            Utils.hideKeyboard(mContext)
            mContext.mContentViewBinding.loading = true
            ApiConnection.addMoney(mContext, walletProductId, amount)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : ApiCustomCallback<AddToCartResponseModel>(mContext, true) {
                        override fun onNext(addToCartResponseModel: AddToCartResponseModel) {
                            super.onNext(addToCartResponseModel)
                            mContext.mContentViewBinding.loading = false
                            if (addToCartResponseModel.success) {
                                onSuccessfulResponse(addToCartResponseModel)
                            } else {
                                onFailureResponse(addToCartResponseModel)
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

    private fun onSuccessfulResponse(addToCartResponseModel: AddToCartResponseModel) {
        ToastHelper.showToast(mContext, addToCartResponseModel.message)
        if (addToCartResponseModel.success) {
            mContext.updateCartCount(addToCartResponseModel.cartCount)
            mContext.mContentViewBinding.amountTv.setText("")
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

    fun onClickTransferAmountToBank() {
        TransferToBankDialogFragment.newInstance(mContext.mContentViewBinding.data!!.accountDetails, mContext.mContentViewBinding.data!!.minimumAmount, mContext.mContentViewBinding.data!!.maximumAmount,mContext.mContentViewBinding.data!!.currencyCode).show(mContext.supportFragmentManager, TransferToBankDialogFragment::class.java.simpleName)
    }
}