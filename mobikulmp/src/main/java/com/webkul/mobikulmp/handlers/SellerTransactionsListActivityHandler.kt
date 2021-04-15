package com.webkul.mobikulmp.handlers

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikul.helpers.ApplicationConstants.BASE_URL
import com.webkul.mobikul.helpers.AuthKeyHelper
import com.webkul.mobikul.helpers.ConstantsHelper.RC_WRITE_TO_EXTERNAL_STORAGE
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.SellerTransactionsListActivity
import com.webkul.mobikulmp.fragments.SellerTransactionsFilterFragment
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_DOWNLOAD_TRANSACTION_LIST
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.fragments
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 16/6/19
 */
class SellerTransactionsListActivityHandler(private val mContext: SellerTransactionsListActivity) {

    fun viewProductsFilterDialog() {
        val sellerTransactionsFilterFragment = SellerTransactionsFilterFragment()
        sellerTransactionsFilterFragment.setOnDetachInterface(mContext)
        sellerTransactionsFilterFragment.show(mContext.supportFragmentManager, SellerTransactionsFilterFragment::class.java.simpleName)
    }

    fun onClickDownloadCsv() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            val url = BASE_URL + MOBIKUL_DOWNLOAD_TRANSACTION_LIST +
                    "/?storeId=" + AppSharedPref.getStoreId(mContext) +
                    "&transactionId=" + mContext.mTransactionId +
                    "&dateFrom=" + mContext.mDateFrom +
                    "&dateTo=" + mContext.mDateTo +
                    "&customerToken=" + AppSharedPref.getCustomerToken(mContext)
            val fileName = "transaction_list_" + System.currentTimeMillis() + ".csv"
            downloadTransactionList(url, fileName)

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                mContext.requestPermissions(permissions, RC_WRITE_TO_EXTERNAL_STORAGE)
            }
        }
    }

    private fun downloadTransactionList(url: String, fileName: String) {
        try {
            Log.d("TAG", "downloadSlips: url: " + url)
            val request = DownloadManager.Request(Uri.parse(url))
            request.setDescription(mContext.resources.getString(R.string.download_started))
            request.setTitle(fileName)
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            request.addRequestHeader("authKey", AuthKeyHelper.getInstance().authKey)
            request.addRequestHeader("customerToken", AppSharedPref.getCustomerToken(mContext))
            request.setMimeType("text/csv")
            val manager = mContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
            manager!!.enqueue(request)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun onClickBasicDetailsBtn() {
        if (mContext.mContentViewBinding.layoutPaymentSummary.visibility == View.VISIBLE) {
            mContext.mContentViewBinding.layoutPaymentSummary.visibility = View.GONE
            mContext.mContentViewBinding.basicInformationHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mContext.mContentViewBinding.layoutPaymentSummary.visibility = View.VISIBLE
            mContext.mContentViewBinding.basicInformationHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_up_arrow_grey_wrapper), null)
        }
    }

    fun onClickMakeWithdrawal(view: View) {
        mContext.mContentViewBinding.loading = true
        MpApiConnection.makeWidthdrawal(
                mContext)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<BaseModel>(mContext, true) {
                    override fun onNext(t: BaseModel) {
                        super.onNext(t)
                        mContext.mContentViewBinding.loading = false
                        if (!t.message.isEmpty()) {
                            Toast.makeText(mContext, t.message, Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mContext.mContentViewBinding.loading = false

                    }
                })

    }
}