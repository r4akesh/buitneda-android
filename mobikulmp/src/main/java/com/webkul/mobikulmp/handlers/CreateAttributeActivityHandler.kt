package com.webkul.mobikulmp.handlers

import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.webkul.mobikul.helpers.ToastHelper
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.helpers.Utils.Companion.showShakeError
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.CreateAttributeActivity
import com.webkul.mobikulmp.models.seller.AttributeOptionItemData
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.models.seller
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 23/5/19
 */


class CreateAttributeActivityHandler(private val mContext: CreateAttributeActivity) {

    private fun isFormDataValid(): Boolean {
        var isFormValidated = true

        if (mContext.mCreateAttributeRequestData.attributeCode.trim().isBlank()) {
            isFormValidated = false
            mContext.mContentViewBinding.attributeCodeTil.error = mContext.getString(R.string.attribute_code) + " " + mContext.getString(R.string.is_required)
            showShakeError(mContext, mContext.mContentViewBinding.attributeCodeTil)
        } else {
            mContext.mContentViewBinding.attributeCodeTil.isErrorEnabled = false
            mContext.mContentViewBinding.attributeCodeTil.error = null
        }

        if (mContext.mCreateAttributeRequestData.attributeCode.trim().isBlank()) {
            isFormValidated = false
            mContext.mContentViewBinding.attributeLabelTil.error = mContext.getString(R.string.attribute_label) + " " + mContext.getString(R.string.is_required)
            showShakeError(mContext, mContext.mContentViewBinding.attributeLabelTil)
        } else {
            mContext.mContentViewBinding.attributeLabelTil.isErrorEnabled = false
            mContext.mContentViewBinding.attributeLabelTil.error = null
        }

        for (noOfOptions in 0 until mContext.mCreateAttributeRequestData.attributeOptionList.size) {
            if (mContext.mCreateAttributeRequestData.attributeOptionList.get(noOfOptions).admin.trim().isBlank()) {
                mContext.mCreateAttributeRequestData.attributeOptionList.get(noOfOptions).isValidAdmin = false
                val holder: RecyclerView.ViewHolder? = mContext.mContentViewBinding.attributeOptionRv.findViewHolderForAdapterPosition(noOfOptions)
                val view: TextInputLayout? = holder?.itemView?.findViewById<TextInputLayout>(R.id.admin_til)
                if (view != null) {
                    view.requestFocus()
                    showShakeError(mContext, view)
                }
                isFormValidated = false
                break
            } else {
                mContext.mCreateAttributeRequestData.attributeOptionList.get(noOfOptions).isValidAdmin = true
            }
        }
        return isFormValidated
    }

    fun onClickSubmitBtn() {
        if (isFormDataValid()) {

            mContext.mContentViewBinding.loading = true
            Utils.hideKeyboard(mContext)
            MpApiConnection.saveAttribute(mContext, mContext.mCreateAttributeRequestData)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : ApiCustomCallback<BaseModel>(mContext, true) {
                        override fun onNext(baseModel: BaseModel) {
                            super.onNext(baseModel)
                            mContext.mContentViewBinding.loading = false
                            ToastHelper.showToast(context, baseModel.message)
                            if (baseModel.success) {
                                mContext.setAttributeData()
                            }
                        }

                        override fun onError(e: Throwable) {
                            super.onError(e)
                            mContext.mContentViewBinding.loading = false
                            ToastHelper.showToast(context, mContext.getString(R.string.something_went_wrong))
                        }
                    })
        }
    }

    fun onClickAddAnotherBtn() {
        mContext.mCreateAttributeRequestData.attributeOptionList.add(AttributeOptionItemData())
        mContext.mContentViewBinding.attributeOptionRv.getAdapter()?.notifyDataSetChanged()
        Handler().postDelayed({ mContext.mContentViewBinding.scrollView.fullScroll(View.FOCUS_DOWN); }, 300)
    }

    fun onClickAttributeValuesBtn() {
        if (mContext.mContentViewBinding.attributeValues.visibility == View.VISIBLE) {
            mContext.mContentViewBinding.attributeValues.visibility = View.GONE
            mContext.mContentViewBinding.attributeValuesHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mContext.mContentViewBinding.attributeValues.visibility = View.VISIBLE
            mContext.mContentViewBinding.attributeValuesHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mContext.mContentViewBinding.scrollView.top + mContext.mContentViewBinding.attributeValuesHeading.top
                mContext.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }

    fun onClickAddValuesBtn() {
        if (mContext.mContentViewBinding.attributeOptionRv.visibility == View.VISIBLE) {
            mContext.mContentViewBinding.attributeOptionRv.visibility = View.GONE
            mContext.mContentViewBinding.addAnotherValue.visibility = View.GONE

            mContext.mContentViewBinding.addValuesHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mContext.mContentViewBinding.attributeOptionRv.visibility = View.VISIBLE
            mContext.mContentViewBinding.addAnotherValue.visibility = View.VISIBLE
            mContext.mContentViewBinding.basicInformationHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mContext.mContentViewBinding.scrollView.top + mContext.mContentViewBinding.addValuesHeading.top
                mContext.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)


            }, 200)
        }
    }

    fun onClickBasicDetailsBtn() {
        if (mContext.mContentViewBinding.basicInformation.visibility == View.VISIBLE) {
            mContext.mContentViewBinding.basicInformation.visibility = View.GONE
            mContext.mContentViewBinding.basicInformationHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mContext.mContentViewBinding.basicInformation.visibility = View.VISIBLE
            mContext.mContentViewBinding.basicInformationHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mContext.mContentViewBinding.scrollView.top + mContext.mContentViewBinding.basicInformationHeading.top
                mContext.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }
}
