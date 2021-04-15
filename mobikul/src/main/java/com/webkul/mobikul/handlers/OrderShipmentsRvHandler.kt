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

import android.content.DialogInterface
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.fragments.OrderShipmentDetailsBottomSheetFragment
import com.webkul.mobikul.fragments.ShipmentsFragment
import com.webkul.mobikul.fragments.TrackShipmentDialogFragment
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.models.user.ShipmentDetailsData
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class OrderShipmentsRvHandler(private val mFragmentContext: ShipmentsFragment) {


    fun onClickViewShipment(incrementId: String,id:String) {
        OrderShipmentDetailsBottomSheetFragment.newInstance(incrementId,id).show(mFragmentContext.childFragmentManager, OrderShipmentDetailsBottomSheetFragment::class.java.simpleName)
    }
    fun onClickTrackShipment(incrementId: String) {
        getShipmentData(incrementId)
    }

    private fun getShipmentData(incrementId: String) {
        mFragmentContext.mContentViewBinding.loading = true
        (mFragmentContext.context as BaseActivity).mHashIdentifier = Utils.getMd5String("getShipmentDetailsData" + AppSharedPref.getStoreId(mFragmentContext.context!!) + AppSharedPref.getCustomerToken(mFragmentContext.context!!) + incrementId)
        ApiConnection.getShipmentDetailsData(mFragmentContext.context!!, BaseActivity.mDataBaseHandler.getETagFromDatabase((mFragmentContext.context as BaseActivity).mHashIdentifier), incrementId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<ShipmentDetailsData>(mFragmentContext.context!!, true) {
                    override fun onNext(shipmentDetailsData: ShipmentDetailsData) {
                        super.onNext(shipmentDetailsData)
                        mFragmentContext.mContentViewBinding.loading = false
                        if (shipmentDetailsData.success) {
                            onSuccessfulResponse(shipmentDetailsData)
                        } else {
                            onFailureResponse(shipmentDetailsData)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mFragmentContext.mContentViewBinding.loading = false
                        onErrorResponse(e)
                    }
                })
    }

    private fun onSuccessfulResponse(shipmentDetailsData: ShipmentDetailsData) {
        if (shipmentDetailsData.trackingData.isEmpty()) {
            ToastHelper.showToast(mFragmentContext.context!!, mFragmentContext.context!!.getString(R.string.tracking_info_not_found))
        } else {
            TrackShipmentDialogFragment.newInstance(shipmentDetailsData.trackingData).show(mFragmentContext.childFragmentManager, TrackShipmentDialogFragment::class.java.simpleName)
        }
    }

    private fun onFailureResponse(shipmentDetailsData: ShipmentDetailsData) {
        AlertDialogHelper.showNewCustomDialog(
                mFragmentContext.context as BaseActivity,
                mFragmentContext.getString(R.string.error),
                shipmentDetailsData.message,
                false,
                mFragmentContext.getString(R.string.ok),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                }
                , ""
                , null)
    }

    private fun onErrorResponse(error: Throwable) {
        if ((!NetworkHelper.isNetworkAvailable(mFragmentContext.context!!) || (error is HttpException && error.code() == 304))) {
            val response = BaseActivity.mDataBaseHandler.getResponseFromDatabase((mFragmentContext.context as BaseActivity).mHashIdentifier)
            if (response.isNotBlank()) {
                onSuccessfulResponse(BaseActivity.mGson.fromJson(response, ShipmentDetailsData::class.java))
            }
        } else {
            AlertDialogHelper.showNewCustomDialog(
                    mFragmentContext.context as BaseActivity,
                    mFragmentContext.getString(R.string.error),
                    NetworkHelper.getErrorMessage(mFragmentContext.context!!, error),
                    false,
                    mFragmentContext.getString(R.string.ok),
                    DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                    }
                    , ""
                    , null)
        }
    }
}