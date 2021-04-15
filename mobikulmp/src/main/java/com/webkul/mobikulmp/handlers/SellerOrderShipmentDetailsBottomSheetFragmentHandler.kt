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
import com.webkul.mobikul.fragments.TrackShipmentDialogFragment
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.models.user.TrackingData
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.fragments.SellerOrderShipmentDetailsBottomSheetFragment
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class SellerOrderShipmentDetailsBottomSheetFragmentHandler(private var mFragmentContext: SellerOrderShipmentDetailsBottomSheetFragment) {

    fun onClickCancelBtn() {
        mFragmentContext.dismiss()
    }

    fun onClickSendEmail(shipmentId:String) {
        AlertDialogHelper.showNewCustomDialog(
                mFragmentContext.context as BaseActivity,
                mFragmentContext.getString(com.webkul.mobikulmp.R.string.are_you_sure),
                mFragmentContext.getString(com.webkul.mobikulmp.R.string.shipment_confirmation),
                true,
                mFragmentContext.getString(com.webkul.mobikulmp.R.string.yes),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    mFragmentContext.mContentViewBinding.loading = true
                    val incrementId: String? = mFragmentContext.arguments?.getString(BundleKeysHelper.BUNDLE_KEY_INCREMENT_ID)
                            ?: ""
                    MpApiConnection.sendTrackingInfo(mFragmentContext.context!!, incrementId,shipmentId)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(object : ApiCustomCallback<BaseModel>(mFragmentContext.context!!, true) {
                                override fun onNext(sendOrderMailData: BaseModel) {
                                    super.onNext(sendOrderMailData)
                                    mFragmentContext.mContentViewBinding.loading = false
                                    ToastHelper.showToast(mFragmentContext.context!!, sendOrderMailData.message)
                                }

                                override fun onError(e: Throwable) {
                                    super.onError(e)
                                    mFragmentContext.mContentViewBinding.loading = false
                                    ToastHelper.showToast(mFragmentContext.context!!, mFragmentContext.getString(com.webkul.mobikulmp.R.string.something_went_wrong))
                                }
                            })
                }
                , mFragmentContext.getString(com.webkul.mobikulmp.R.string.cancel)
                , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        })
    }
    fun onClickTrackShipment(trackingData: ArrayList<TrackingData>) {
        TrackShipmentDialogFragment.newInstance(trackingData).show(mFragmentContext.childFragmentManager, TrackShipmentDialogFragment::class.java.simpleName)
    }

    fun onClickSaveShipment( ) {
        val incrementId: String? = mFragmentContext.arguments?.getString(BundleKeysHelper.BUNDLE_KEY_INCREMENT_ID)
                ?: ""
        val shipmentId: String? = mFragmentContext.arguments?.getString(BundleKeysHelper.BUNDLE_KEY_SHIPMENT_ID)
                ?: ""
        val mUrl = ApplicationConstants.BASE_URL + "/index.php/mobikulmphttp/marketplace/printshipment/storeId/" + AppSharedPref.getStoreId(mFragmentContext.context!!) + "/customerToken/" + AppSharedPref.getCustomerToken(mFragmentContext.context!!) + "/incrementId/" + incrementId + "/shipmentId/" + shipmentId
        Log.d("Tag", "printRequest: $mUrl")
        val mFileName = "Shipment_$shipmentId.pdf"
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