package com.webkul.mobikulmp.handlers

import android.content.DialogInterface
import android.widget.Toast
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_NAME
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.fragments.ReportSellerBottomSheetFragment
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class ReportSellerBottomSheetFragmentHandler(val mFragmentContext: ReportSellerBottomSheetFragment) {

    fun onClickCancelBtn() {
        mFragmentContext.dismiss()
    }

    fun onClickSubmitReport() {
        if (validatedForm()) {
            mFragmentContext.mContentViewBinding.loading = true
            if (mFragmentContext.mContentViewBinding.fromPage == 1) {
                MpApiConnection.saveSellerReport(
                        mFragmentContext.context!!,
                        mFragmentContext.mContentViewBinding.data!!,
                        mFragmentContext.arguments?.getString(MpBundleKeysHelper.BUNDLE_PRODUCT_SELLER_ID, "")
                                ?: ""
                ).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(object : ApiCustomCallback<BaseModel>(mFragmentContext.context!!, true) {
                            override fun onNext(responseModel: BaseModel) {
                                super.onNext(responseModel)
                                mFragmentContext.mContentViewBinding.loading = false
                                if (responseModel.success) {
                                    AlertDialogHelper.run {
                                        showNewCustomDialog(
                                                mFragmentContext.activity as BaseActivity,
                                                context.getString(R.string.thank_you),
                                                mFragmentContext.getString(R.string.report_seller_message,mFragmentContext.arguments?.getString(BUNDLE_KEY_PRODUCT_NAME)),
                                                false,
                                                mFragmentContext.getString(R.string.close),
                                                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                                                    dialogInterface.dismiss()
                                                    mFragmentContext.dismiss()
                                                }
                                                , ""
                                                , null)
                                    }

                                } else {
                                    onFailureResponse(responseModel)
                                }
                            }

                            override fun onError(e: Throwable) {
                                super.onError(e)
                                mFragmentContext.mContentViewBinding.loading = false
                                onErrorResponse(e)
                            }
                        })
            } else {
                MpApiConnection.saveProductReport(
                        mFragmentContext.context!!,
                        mFragmentContext.mContentViewBinding.data!!,
                        mFragmentContext.arguments?.getString(MpBundleKeysHelper.BUNDLE_PRODUCT_SELLER_ID, "")
                                ?: ""
                )
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(object : ApiCustomCallback<BaseModel>(mFragmentContext.context!!, true) {
                            override fun onNext(responseModel: BaseModel) {
                                super.onNext(responseModel)
                                mFragmentContext.mContentViewBinding.loading = false
                                if (responseModel.success) {
                                    AlertDialogHelper.run {
                                        showNewCustomDialog(
                                                mFragmentContext.activity as BaseActivity,
                                                context.getString(R.string.thank_you),
                                                mFragmentContext.getString(R.string.report_product_message,mFragmentContext.arguments?.getString(BUNDLE_KEY_PRODUCT_NAME)),
                                                false,
                                                mFragmentContext.getString(R.string.close),
                                                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                                                    dialogInterface.dismiss()
                                                    mFragmentContext.dismiss()
                                                }
                                                , ""
                                                , null)
                                    }
                                } else {
                                    onFailureResponse(responseModel)
                                }
                            }

                            override fun onError(e: Throwable) {
                                super.onError(e)
                                mFragmentContext.mContentViewBinding.loading = false
                                onErrorResponse(e)
                            }
                        })
            }
        }
    }

    private fun validatedForm(): Boolean {
        var isFormValidated = true
        if (mFragmentContext.mContentViewBinding.data!!.name.trim().isEmpty()) {
            isFormValidated = false
            mFragmentContext.mContentViewBinding.nameTil.error = mFragmentContext.getString(R.string.name) + " " + mFragmentContext.getString(R.string.is_required)
            mFragmentContext.mContentViewBinding.nameTil.requestFocus()
        } else {
            mFragmentContext.mContentViewBinding.nameTil.error = null
        }

        if (mFragmentContext.mContentViewBinding.data!!.email.trim().isEmpty()) {
            isFormValidated = false
            mFragmentContext.mContentViewBinding.emailTil.error = mFragmentContext.getString(R.string.email) + " " + mFragmentContext.getString(R.string.is_required)
            mFragmentContext.mContentViewBinding.emailTil.requestFocus()
        } else if (mFragmentContext.mContentViewBinding.data!!.email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mFragmentContext.mContentViewBinding.data!!.email).matches()) {
            mFragmentContext.mContentViewBinding.emailTil.error = mFragmentContext.getString(R.string.enter_a_valid) + " " + mFragmentContext.getString(R.string.email)
            mFragmentContext.mContentViewBinding.emailTil.requestFocus()
            isFormValidated = false
        } else {
            mFragmentContext.mContentViewBinding.emailTil.error = null
        }

        if (mFragmentContext.mContentViewBinding.data!!.showReportOtherReason == true && mFragmentContext.mContentViewBinding.data?.selectedOtherReasonMethod == true && mFragmentContext.mContentViewBinding.data?.flagMessage.isNullOrEmpty()) {
            mFragmentContext.mContentViewBinding.flagReasonTil.error = mFragmentContext.getString(R.string.reason) + " " + mFragmentContext.getString(R.string.is_required)
            mFragmentContext.mContentViewBinding.flagReasonTil.requestFocus()
            isFormValidated = false
        }
        return isFormValidated
    }

    fun onFailureResponse(response: Any) {
        AlertDialogHelper.showNewCustomDialog(
                mFragmentContext.activity as BaseActivity,
                mFragmentContext.getString(R.string.error),
                (response as BaseModel).message,
                false,
                mFragmentContext.getString(R.string.ok),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    mFragmentContext.dismiss()
                }
                , ""
                , null)
    }

    private fun onErrorResponse(error: Throwable) {
        if ((!NetworkHelper.isNetworkAvailable(mFragmentContext.context!!) || (error is HttpException && error.code() == 304))) {
        } else {
            AlertDialogHelper.showNewCustomDialog(
                    mFragmentContext.activity as BaseActivity,
                    mFragmentContext.getString(R.string.oops),
                    NetworkHelper.getErrorMessage(mFragmentContext.context!!, error),
                    false,
                    mFragmentContext.getString(R.string.try_again),
                    DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        onClickSubmitReport()
                    }
                    , mFragmentContext.getString(com.webkul.mobikul.R.string.close)
                    , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            })
        }
    }


    fun clearMessage() {
        mFragmentContext.mContentViewBinding.flagReasonEt.setText("")
        mFragmentContext.otherMethodRb.isChecked = true
    }
}