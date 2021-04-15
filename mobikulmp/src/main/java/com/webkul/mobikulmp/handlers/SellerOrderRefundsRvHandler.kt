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
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.webkul.mobikul.R
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_INCREMENT_ID
import com.webkul.mobikulmp.adapters.SellerOrderRefundsRvAdapter
import com.webkul.mobikulmp.fragments.SellerOrderRefundDetailsBottomSheetFragment
import com.webkul.mobikulmp.fragments.SellerRefundsFragment
import com.webkul.mobikulmp.models.seller.SellerCreditMemoList


class SellerOrderRefundsRvHandler(private val mFragmentContext: SellerRefundsFragment) {

    fun onClickViewRefund(entityId: String) {
        SellerOrderRefundDetailsBottomSheetFragment.newInstance(mFragmentContext.arguments!!.getString(BUNDLE_KEY_INCREMENT_ID), entityId).show(mFragmentContext.childFragmentManager, SellerOrderRefundDetailsBottomSheetFragment::class.java.simpleName)
    }


    fun onClickSaveRefund(data: SellerCreditMemoList) {
        var creditMemoId = data.entityId
        val incrementId: String? = mFragmentContext.arguments?.getString(BUNDLE_KEY_INCREMENT_ID)
                ?: ""
        val mUrl = ApplicationConstants.BASE_URL + "/mobikulmphttp/marketplace/printcreditmemo?storeId=" + AppSharedPref.getStoreId(mFragmentContext.context!!) + "&customerToken=" + AppSharedPref.getCustomerToken(mFragmentContext.context!!) + "&incrementId=" + incrementId + "&creditmemoId=" + creditMemoId
        Log.d("Tag", "printRequest: $mUrl")
        val mFileName = "Credit_memo_$creditMemoId.pdf"
        val mMimeType = "application/pdf"
        if (ContextCompat.checkSelfPermission(mFragmentContext.context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                mFragmentContext.requestPermissions(permissions, ConstantsHelper.RC_WRITE_TO_EXTERNAL_STORAGE)
                (mFragmentContext.mContentViewBinding.orderRefundsRv.adapter as SellerOrderRefundsRvAdapter).adapterPosition = data.adapterPosition
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