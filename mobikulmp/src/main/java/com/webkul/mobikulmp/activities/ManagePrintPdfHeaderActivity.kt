package com.webkul.mobikulmp.activities

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.helpers.ToastHelper
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.ActivityManagePrintPdfHeaderBinding
import com.webkul.mobikulmp.models.pdfheader.PdfHeaderInfoDataResponse
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ManagePrintPdfHeaderActivity : BaseActivity() {

    lateinit var mContentViewBinding: ActivityManagePrintPdfHeaderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_manage_print_pdf_header)
        initialize()
    }

    private fun initialize() {
        title = getString(R.string.edit_pdf_header)
        mContentViewBinding.loading = true

        MpApiConnection.getPdfHeaderFormData(this@ManagePrintPdfHeaderActivity)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<PdfHeaderInfoDataResponse>(this@ManagePrintPdfHeaderActivity, true) {
                    override fun onNext(pdfHeaderInfoDataResponse: PdfHeaderInfoDataResponse) {
                        super.onNext(pdfHeaderInfoDataResponse)
                        mContentViewBinding.loading = false
                        if (pdfHeaderInfoDataResponse.success) {
                            mContentViewBinding.data = pdfHeaderInfoDataResponse.headerInfo
                            mContentViewBinding.executePendingBindings()
                        } else {
                            ToastHelper.showToast(this@ManagePrintPdfHeaderActivity, pdfHeaderInfoDataResponse.message, Toast.LENGTH_SHORT)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mContentViewBinding.loading = false
                        ToastHelper.showToast(context, getString(R.string.something_went_wrong))
                    }
                })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    fun onClickSaveInfoBtn(view: View) {
        Utils.hideKeyboard(this@ManagePrintPdfHeaderActivity)
        if (mContentViewBinding.data == null || mContentViewBinding.data!!.isEmpty()) {
            ToastHelper.showToast(this, getString(R.string.please_fill_the_information), Toast.LENGTH_SHORT)
        } else {
            mContentViewBinding.loading = true

            MpApiConnection.savePdfHeader(this@ManagePrintPdfHeaderActivity, mContentViewBinding.data!!)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : ApiCustomCallback<BaseModel>(this@ManagePrintPdfHeaderActivity, true) {
                        override fun onNext(baseModel: BaseModel) {
                            super.onNext(baseModel)
                            mContentViewBinding.loading = false
                            ToastHelper.showToast(this@ManagePrintPdfHeaderActivity, baseModel.message, Toast.LENGTH_SHORT)
                        }

                        override fun onError(e: Throwable) {
                            super.onError(e)
                            mContentViewBinding.loading = false
                            ToastHelper.showToast(context, getString(R.string.something_went_wrong))
                        }
                    })
        }
    }
}