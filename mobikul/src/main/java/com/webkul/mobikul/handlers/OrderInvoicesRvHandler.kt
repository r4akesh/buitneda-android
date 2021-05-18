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
import android.app.DownloadManager
import android.content.*
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.webkul.mobikul.R
import com.webkul.mobikul.fragments.InvoicesFragment
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.models.checkout.InvoiceModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File


class OrderInvoicesRvHandler(private val mFragmentContext: InvoicesFragment) {
    private val permissionRequestCode = 123
    fun onClickViewInvoice(invoiceIncrementId: String,invoiceId:String) {
       // OrderInvoiceDetailsBottomSheetFragment.newInstance(invoiceIncrementId,invoiceId,mFragmentContext.mContentViewBinding.data!!.incrementId).show(mFragmentContext.childFragmentManager, OrderInvoiceDetailsBottomSheetFragment::class.java.simpleName)
        callApi()
    }

    fun onClickSaveInvoice() {

    }




    fun  callApi(){
        val incrementId: String? = mFragmentContext.arguments?.getString(BundleKeysHelper.BUNDLE_KEY_INCREMENT_ID)
        val language : String? = if(AppSharedPref.getStoreId(mFragmentContext.context!!).equals("1")) "en_US" else "pt_PT"

        ApiConnection.getGenerateInvoice(incrementId!!,language!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiCustomCallback<InvoiceModel>(mFragmentContext.context!!, false) {
                override fun onNext(responseModel: InvoiceModel) {
                    super.onNext(responseModel)
                    downloadFile(mFragmentContext.context!!,responseModel.file_url,incrementId+".pdf")
                }

                override fun onError(e: Throwable) {
                    super.onError(e)

                }



            })
    }


    fun downloadFile(context: Context, url: String?, fileName: String?) {
       if(checkPermission()){
           try {
               if (url != null && !url.isEmpty()) {
                   val uri = Uri.parse(url)
                   context.registerReceiver(
                       attachmentDownloadCompleteReceive, IntentFilter(
                           DownloadManager.ACTION_DOWNLOAD_COMPLETE
                       )
                   )
                   val request = DownloadManager.Request(Uri.parse(url))
                       .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                       .setTitle(fileName)
                       .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                       .setAllowedOverMetered(true)
                       .setAllowedOverRoaming(false)
                       .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName)
                   val downloadManager= context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                   val downloadID = downloadManager.enqueue(request)
               }
           } catch (e: IllegalStateException) {
               Toast.makeText(
                   context,
                   "Please insert an SD card to download file",
                   Toast.LENGTH_SHORT
               ).show()
           }
       }else{
           makeRequestPermissions()
       }
    }


    private fun getMimeType(url: String): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            val mime = MimeTypeMap.getSingleton()
            type = mime.getMimeTypeFromExtension(extension)
        }
        return type
    }


    var attachmentDownloadCompleteReceive: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
                val downloadId = intent.getLongExtra(
                    DownloadManager.EXTRA_DOWNLOAD_ID, 0
                )
                openDownloadedAttachment(context, downloadId,intent)
            }
        }
    }


    private fun openDownloadedAttachment(context: Context, downloadId: Long,intent: Intent) {
        val query = DownloadManager.Query()
        query.setFilterById(intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0))
        val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val cursor = manager.query(query)
        val fname: String = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE))
        val pdfFile = File(
            Environment.getExternalStorageDirectory().toString() + "/Downloads/" + fname
        ) //File path

        if (pdfFile.isFile) //Checking if the file exists or not
        {
            val path = Uri.fromFile(pdfFile)
            val objIntent = Intent(Intent.ACTION_VIEW)
            objIntent.setDataAndType(path, "application/pdf")
            objIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(objIntent) //Starting the pdf viewer
        } else {
            Log.d(
                "OO",
                Environment.getExternalStorageDirectory().toString() + "/Downloads/" + fname
            )
            Log.d("OO", fname)
            // Toast.makeText(getApplicationContext(),"Test",Toast.LENGTH_LONG).show();
        }
    }



    private fun openDownloadedAttachment(context:Context,attachmentUri:Uri,attachmentMimeType:String) {
        var attach = attachmentUri
        if (attach != null) {
            // Get Content Uri.
            if (ContentResolver.SCHEME_FILE.equals(attachmentUri.getScheme())) {
                // FileUri - Convert it to contentUri.
                val file = File(attach.getPath());
                attach = FileProvider.getUriForFile(context, "com.webkul.mobikul.provider", file)
            }

            val openAttachmentIntent = Intent(Intent.ACTION_VIEW);
            openAttachmentIntent.setDataAndType(attachmentUri, attachmentMimeType);
            openAttachmentIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                context.startActivity(openAttachmentIntent);
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    context,
                    context.getString(R.string.unable_to_open_file),
                    Toast.LENGTH_LONG
                ).show();
            }
        }
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





    private fun checkPermission(): Boolean {

        val firstPermission: Int =
            ContextCompat.checkSelfPermission(
                mFragmentContext.context!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        val secondPermission: Int = ContextCompat.checkSelfPermission(
            mFragmentContext.context!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return firstPermission == PackageManager.PERMISSION_GRANTED &&
                secondPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun makeRequestPermissions() {
        ActivityCompat.requestPermissions(
            mFragmentContext.activity!!,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            permissionRequestCode
        )

    }
}