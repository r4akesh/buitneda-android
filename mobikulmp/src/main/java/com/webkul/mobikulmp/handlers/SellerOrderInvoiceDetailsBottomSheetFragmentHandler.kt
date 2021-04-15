/*
 * Webkul Software.
 *
 * Kotlin
 *
 * @author Webkul <support@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2019 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

package com.webkul.mobikulmp.handlers

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.fragments.SellerOrderInvoiceDetailsBottomSheetFragment
import com.webkul.mobikulmp.models.seller.SellerInvoiceDetailsData
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class SellerOrderInvoiceDetailsBottomSheetFragmentHandler(private var mFragmentContext: SellerOrderInvoiceDetailsBottomSheetFragment) {

    fun onClickCancelBtn() {
        mFragmentContext.dismiss()
    }

    fun onClickSendEmail(invoiceId: String) {
        AlertDialogHelper.showNewCustomDialog(
                mFragmentContext.context as BaseActivity,
                mFragmentContext.getString(R.string.are_you_sure),
                mFragmentContext.getString(R.string.confirm_invoice_email_send),
                true,
                mFragmentContext.getString(R.string.yes),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    mFragmentContext.mContentViewBinding.loading = true
                    MpApiConnection.sendSellerInvoiceMail(mFragmentContext.context!!, invoiceId, mFragmentContext.mIncrementId)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(object : ApiCustomCallback<BaseModel>(mFragmentContext.context!!, true) {
                                override fun onNext(t: BaseModel) {
                                    super.onNext(t)
                                    mFragmentContext.mContentViewBinding.loading = false
                                    ToastHelper.showToast(mFragmentContext.context!!, t.message)
                                }

                                override fun onError(e: Throwable) {
                                    super.onError(e)
                                    mFragmentContext.mContentViewBinding.loading = false
                                    ToastHelper.showToast(mFragmentContext.context!!, mFragmentContext.getString(R.string.something_went_wrong))
                                }
                            })
                }
                , mFragmentContext.getString(R.string.cancel)
                , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        })
    }

    /* fun onClickSaveInvoice(data: SellerInvoiceDetailsData, invoiceId: String) {
         if (mFragmentContext.context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE) } == PackageManager.PERMISSION_GRANTED &&
                 ContextCompat.checkSelfPermission(mFragmentContext.context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                 ContextCompat.checkSelfPermission(mFragmentContext.context!!, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
             PDFHelperMp.instance.generateInvoice(mFragmentContext.context!!, data, invoiceId)
         } else {
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                 val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                 (mFragmentContext.context as BaseActivity).requestPermissions(permissions, ConstantsHelper.RC_WRITE_TO_EXTERNAL_STORAGE)
             }
         }
     }*/


    fun onClickSaveInvoice(data: SellerInvoiceDetailsData, invoiceId: String) {

        val incrementId: String? = mFragmentContext.arguments?.getString(BundleKeysHelper.BUNDLE_KEY_INCREMENT_ID)
                ?: ""
        val mUrl = ApplicationConstants.BASE_URL + "/mobikulmphttp/marketplace/printInvoice?storeId=" + AppSharedPref.getStoreId(mFragmentContext.context!!) + "&customerToken=" + AppSharedPref.getCustomerToken(mFragmentContext.context!!) + "&incrementId=" + incrementId + "&invoiceId=" + invoiceId
        Log.d("Tag", "printRequest: $mUrl")
        val mFileName = "Invoice$invoiceId.pdf"
        val mMimeType = "application/pdf"
        if (ContextCompat.checkSelfPermission(mFragmentContext.context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                mFragmentContext.requestPermissions(permissions, ConstantsHelper.RC_WRITE_TO_EXTERNAL_STORAGE)
            } else {
                ToastHelper.showToast(mFragmentContext.context!!, mFragmentContext.getString(com.webkul.mobikul.R.string.download_started))
                DownloadHelper.downloadFile(mFragmentContext.context, mUrl, mFileName, mMimeType)
            }
        } else {
            ToastHelper.showToast(mFragmentContext.context!!, mFragmentContext.getString(com.webkul.mobikul.R.string.download_started))
            DownloadHelper.downloadFile(mFragmentContext.context, mUrl, mFileName, mMimeType)
        }
    }
}