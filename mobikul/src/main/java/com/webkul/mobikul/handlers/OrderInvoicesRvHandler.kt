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
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.webkul.mobikul.R
import com.webkul.mobikul.fragments.InvoicesFragment
import com.webkul.mobikul.fragments.OrderInvoiceDetailsBottomSheetFragment
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.models.checkout.InvoiceModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class OrderInvoicesRvHandler(private val mFragmentContext: InvoicesFragment) {

    fun onClickViewInvoice(invoiceIncrementId: String,invoiceId:String) {
       // OrderInvoiceDetailsBottomSheetFragment.newInstance(invoiceIncrementId,invoiceId,mFragmentContext.mContentViewBinding.data!!.incrementId).show(mFragmentContext.childFragmentManager, OrderInvoiceDetailsBottomSheetFragment::class.java.simpleName)
        callApi()
    }

    fun onClickSaveInvoice() {

    }




    fun  callApi(){
        val incrementId: String? = mFragmentContext.arguments?.getString(BundleKeysHelper.BUNDLE_KEY_INCREMENT_ID)
        val language : String? = if(AppSharedPref.getStoreId(mFragmentContext.context!!).equals("1")) "en_US" else "pt_PT"

        ApiConnection.getGenerateInvoice(incrementId!!.toInt(),language!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiCustomCallback<InvoiceModel>(mFragmentContext.context!!, false) {
                override fun onNext(responseModel: InvoiceModel) {
                    super.onNext(responseModel)
                    downloadAndOpenPDF(responseModel.file_url)
                }

                override fun onError(e: Throwable) {
                    super.onError(e)

                }



            })
    }

    fun downloadAndOpenPDF(fileUrl:String) {
        Thread {
            val path: Uri = Uri.fromFile(downloadFile(fileUrl))
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(path, "application/pdf")
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                mFragmentContext.context!!.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                ToastHelper.showToast(mFragmentContext.context!!,"PDF Reader application is not installed in your device",Toast.LENGTH_LONG)

            }
        }.start()
    }


    fun downloadFile(dwnload_file_path: String?): File? {
        var file: File? = null
        try {
            val url = URL(dwnload_file_path)
            val urlConnection: HttpURLConnection = url
                .openConnection() as HttpURLConnection
            urlConnection.setRequestMethod("GET")
            urlConnection.setDoOutput(true)

            // connect
            urlConnection.connect()

            // set the path where we want to save the file
            val SDCardRoot: File = Environment.getExternalStorageDirectory()
            // create a new file, to save the downloaded file
            file = File(SDCardRoot,"")
            val fileOutput = FileOutputStream(file)

            // Stream used for reading the data from the internet
            val inputStream: InputStream = urlConnection.getInputStream()
            val totalsize = urlConnection.getContentLength()


            // create a buffer...
            val buffer = ByteArray(1024 * 1024)
            var bufferLength = 0
            var downloadedSize = 0.0
            while (inputStream.read(buffer).also { bufferLength = it } > 0) {
                fileOutput.write(buffer, 0, bufferLength)
                 downloadedSize += bufferLength
                val per = downloadedSize as Float / totalsize * 100

            }
            // close the output stream when complete //
            fileOutput.close()
           // setText("Download Complete. Open PDF Application installed in the device.")
        } catch (e: MalformedURLException) {

        } catch (e: IOException) {

        } catch (e: Exception) {

        }
        return file
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