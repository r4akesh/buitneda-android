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
import com.webkul.mobikul.fragments.InvoicesFragment
import com.webkul.mobikul.fragments.OrderInvoiceDetailsBottomSheetFragment
import com.webkul.mobikul.helpers.*


class OrderInvoicesRvHandler(private val mFragmentContext: InvoicesFragment) {

    fun onClickViewInvoice(invoiceIncrementId: String,invoiceId:String) {
        OrderInvoiceDetailsBottomSheetFragment.newInstance(invoiceIncrementId,invoiceId,mFragmentContext.mContentViewBinding.data!!.incrementId).show(mFragmentContext.childFragmentManager, OrderInvoiceDetailsBottomSheetFragment::class.java.simpleName)
    }

    fun onClickSaveInvoice() {

    }



    fun onClickSaveInvoiceS() {

        val incrementId: String? = mFragmentContext.arguments?.getString(BundleKeysHelper.BUNDLE_KEY_INCREMENT_ID)
        val invoiceId: String? = mFragmentContext.arguments?.getString(BundleKeysHelper.BUNDLE_KEY_INVOICE_ID)
            ?: ""
        val language : String? = if(AppSharedPref.getStoreId(mFragmentContext.context!!).equals("1")) "en_US" else "pt_PT"
//        val mUrl = ApplicationConstants.BASE_URL +
//                "/mobikulmphttp/marketplace/printInvoice?storeId=" + AppSharedPref.getStoreId(mFragmentContext.context!!) +
//                "&customerToken=" + AppSharedPref.getCustomerToken(mFragmentContext.context!!) +
//                "&incrementId=" + incrementId +
//                "&invoiceId=" + invoiceId
        val mUrl = ApplicationConstants.BASE_URL +
                "/rest/V1/invoiceapi/post?increment_id=" + incrementId +"&language="+language
//        val mUrl = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"

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
}