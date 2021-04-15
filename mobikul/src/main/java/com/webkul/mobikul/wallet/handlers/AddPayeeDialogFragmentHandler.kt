package com.webkul.mobikul.wallet.handlers

import com.webkul.mobikul.R
import com.webkul.mobikul.helpers.ToastHelper
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikul.wallet.activities.TransferWalletAmountActivity
import com.webkul.mobikul.wallet.fragments.AddPayeeDialogFragment
import com.webkul.mobikul.wallet.models.wallet.AddPayeeModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class AddPayeeDialogFragmentHandler(private val mFragmentContext: AddPayeeDialogFragment) {

    fun onClickUpdate() {
        if (mFragmentContext.mContentViewBinding.data!!.isFormValidated(mFragmentContext)) {
            mFragmentContext.mContentViewBinding.loading = true
            ApiConnection.updatePayee(mFragmentContext.context!!, mFragmentContext.mContentViewBinding.data!!.id, mFragmentContext.mContentViewBinding.data!!.nickName)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : ApiCustomCallback<BaseModel>(mFragmentContext.context!!, true) {
                        override fun onNext(updatePayeeResponseData: BaseModel) {
                            super.onNext(updatePayeeResponseData)
                            mFragmentContext.mContentViewBinding.loading = false
                            ToastHelper.showToast(mFragmentContext.context!!, updatePayeeResponseData.message)
                            if (updatePayeeResponseData.success) {
                                (mFragmentContext.context as TransferWalletAmountActivity).callApi()
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

    fun onClickSubmit() {
        if (mFragmentContext.mContentViewBinding.data!!.isFormValidated(mFragmentContext)) {
            mFragmentContext.mContentViewBinding.loading = true
            ApiConnection.addPayee(mFragmentContext.context!!, mFragmentContext.mContentViewBinding.data!!.nickName, mFragmentContext.mContentViewBinding.data!!.email)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : ApiCustomCallback<BaseModel>(mFragmentContext.context!!, true) {
                        override fun onNext(addPayeeResponseData: BaseModel) {
                            super.onNext(addPayeeResponseData)
                            mFragmentContext.mContentViewBinding.loading = false
                            ToastHelper.showToast(mFragmentContext.context!!, addPayeeResponseData.message)
                            if (addPayeeResponseData.success) {
                                (mFragmentContext.context as TransferWalletAmountActivity).callApi()
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

    fun onClickReset() {
        mFragmentContext.mContentViewBinding.data = AddPayeeModel()
    }
}