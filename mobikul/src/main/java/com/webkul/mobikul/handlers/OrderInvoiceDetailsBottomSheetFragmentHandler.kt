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

package com.webkul.mobikul.handlers

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.fragments.OrderInvoiceDetailsBottomSheetFragment
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.models.user.InvoiceDetailsData


class OrderInvoiceDetailsBottomSheetFragmentHandler(private var mFragmentContext: OrderInvoiceDetailsBottomSheetFragment) {

    fun onClickCancelBtn() {
        mFragmentContext.dismiss()
    }


    fun onClickSaveInvoice() {

        val incrementId: String? = mFragmentContext.arguments?.getString(BundleKeysHelper.BUNDLE_KEY_INCREMENT_ID)
        val invoiceId: String? = mFragmentContext.arguments?.getString(BundleKeysHelper.BUNDLE_KEY_INVOICE_ID)
                ?: ""
        val mUrl = ApplicationConstants.BASE_URL +
                "/mobikulmphttp/marketplace/printInvoice?storeId=" + AppSharedPref.getStoreId(mFragmentContext.context!!) +
                "&customerToken=" + AppSharedPref.getCustomerToken(mFragmentContext.context!!) +
                "&incrementId=" + incrementId +
                "&invoiceId=" + invoiceId
        Log.d("Tag", "printRequest: $mUrl")
        val mFileName = "Invoice$invoiceId.pdf"
        val mMimeType = "application/pdf"
        if (ContextCompat.checkSelfPermission(mFragmentContext.context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                mFragmentContext.requestPermissions(permissions, ConstantsHelper.RC_WRITE_TO_EXTERNAL_STORAGE)
            } else {
                ToastHelper.showToast(mFragmentContext.context!!, mFragmentContext.getString(R.string.download_started))
                DownloadHelper.downloadFile(mFragmentContext.context, mUrl, mFileName, mMimeType)
            }
        } else {
            ToastHelper.showToast(mFragmentContext.context!!, mFragmentContext.getString(R.string.download_started))
            DownloadHelper.downloadFile(mFragmentContext.context, mUrl, mFileName, mMimeType)
        }
    }

//    fun onClickSaveInvoice(data: InvoiceDetailsData, invoiceId: String) {
//        if (mFragmentContext.context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE) } == PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(mFragmentContext.context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(mFragmentContext.context!!, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//            PDFHelper.instanse.generateInvoice(mFragmentContext.context!!, data, invoiceId)
//        } else {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
//                (mFragmentContext.context as BaseActivity).requestPermissions(permissions, ConstantsHelper.RC_WRITE_TO_EXTERNAL_STORAGE)
//            }
//        }
//    }
}