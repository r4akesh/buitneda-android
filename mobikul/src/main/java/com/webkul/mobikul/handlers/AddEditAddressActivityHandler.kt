package com.webkul.mobikul.handlers

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.*
import com.mobikul.locationpicker.AddressPickerActivity
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.AddEditAddressActivity
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_ADDRESS_DATA
import com.webkul.mobikul.helpers.ConstantsHelper.RC_PLACE_PICKER
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.models.user.AddressFormResponseModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Webkul Software.
 *
 * Kotlin
 *
 * @author Webkul <support@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

class AddEditAddressActivityHandler(val mContext: AddEditAddressActivity) {

    fun onClickSaveAddress(addressFormResponseModel: AddressFormResponseModel) {
        setStreetData(mContext)
        if (addressFormResponseModel.isFormValidated(mContext)) {
            Utils.hideKeyboard(mContext)
            if (mContext.mNewAddressForCheckout == null) {
                mContext.mContentViewBinding.loading = true
                ApiConnection.saveAddress(mContext, mContext.mAddressId, addressFormResponseModel.getTheAddressData())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(object : ApiCustomCallback<BaseModel>(mContext, true) {
                            override fun onNext(saveAddressResponseModel: BaseModel) {
                                super.onNext(saveAddressResponseModel)
                                mContext.mContentViewBinding.loading = false
                                if (saveAddressResponseModel.success) {
                                    onSuccessfulResponse(saveAddressResponseModel)
                                } else {
                                    onFailureResponse(saveAddressResponseModel)
                                }
                            }

                            override fun onError(e: Throwable) {
                                super.onError(e)
                                mContext.mContentViewBinding.loading = false
                                onErrorResponse(e, addressFormResponseModel)
                            }
                        })
            } else {
                setBackResultAndFinish()
            }
        }
    }

    private fun setStreetData(addEditAddressActivity: AddEditAddressActivity) {
        addEditAddressActivity.mContentViewBinding.data?.addressData?.street = ArrayList()
        addEditAddressActivity.mContentViewBinding.data?.addressData?.street?.add(addEditAddressActivity.mContentViewBinding.streetAddress0Et.text!!.toString().trim())
        addEditAddressActivity.mContentViewBinding.data?.addressData?.street?.add(addEditAddressActivity.mContentViewBinding.streetAddress1Et.text!!.toString().trim())
        addEditAddressActivity.mContentViewBinding.data?.addressData?.street?.add(addEditAddressActivity.mContentViewBinding.streetAddress2Et.text!!.toString().trim())
        addEditAddressActivity.mContentViewBinding.data?.addressData?.street?.add(addEditAddressActivity.mContentViewBinding.streetAddress3Et.text!!.toString().trim())
    }

    private fun onSuccessfulResponse(saveAddressResponseModel: BaseModel) {
        ToastHelper.showToast(mContext, saveAddressResponseModel.message)
        setBackResultAndFinish()
    }

    private fun setBackResultAndFinish() {
        val backResult = Intent()
        backResult.putExtra(BUNDLE_KEY_ADDRESS_DATA, mContext.mContentViewBinding.data!!.addressData)
        mContext.setResult(AppCompatActivity.RESULT_OK, backResult)
        mContext.finish()
    }

    private fun onFailureResponse(saveAddressResponseModel: BaseModel) {
        AlertDialogHelper.showNewCustomDialog(
                mContext,
                mContext.getString(R.string.error),
                saveAddressResponseModel.message,
                false,
                mContext.getString(R.string.dismiss),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                }
                , null
                , null)
    }

    private fun onErrorResponse(error: Throwable, addressFormResponseModel: AddressFormResponseModel) {
        AlertDialogHelper.showNewCustomDialog(
                mContext,
                mContext.getString(R.string.error),
                NetworkHelper.getErrorMessage(mContext, error),
                false,
                mContext.getString(R.string.try_again),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    onClickSaveAddress(addressFormResponseModel)
                }
                , mContext.getString(R.string.dismiss)
                , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
            mContext.finish()
        })
    }

    fun onClickGetCurrentLocation() {

        mContext.mLocationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mContext, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ToastHelper.showToast(mContext, mContext.getString(R.string.access_needed_to_fill_address))
                ActivityCompat.requestPermissions(mContext, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ConstantsHelper.RC_LOCATION_PERMISSION)
            } else {
                ActivityCompat.requestPermissions(mContext, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ConstantsHelper.RC_LOCATION_PERMISSION)
            }
            return
        }
        checkIsGpsEnabled()

    }


    private fun checkIsGpsEnabled() {
        val googleApiClient = GoogleApiClient.Builder(mContext).addApi(LocationServices.API).build()
        googleApiClient.connect()

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 10000 / 2

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)

        val result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        result.setResultCallback(ResultCallback<LocationSettingsResult> { result ->
            val status = result.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                    mContext.mContentViewBinding.loading = true
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return@ResultCallback
                    }

                    try {

                        if (mContext.getString(R.string.maps_api_key).isNullOrEmpty()) {
                            mContext.mLocationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, mContext)
                            Handler().postDelayed({
                                if (mContext.mContentViewBinding.data!!.addressData.city.isNullOrEmpty()) {
                                    try {
                                        mContext.mContentViewBinding.loading = false
                                        ToastHelper.showToast(mContext, mContext.getString(R.string.unable_to_get_exact_address))
                                        mContext.mLocationManager?.removeUpdates(mContext)
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            }, 15000)
                        } else {
                            val intent = Intent(mContext, AddressPickerActivity::class.java)
                            intent.putExtra(AddressPickerActivity.ARG_ZOOM_LEVEL, 16.0f)
                            mContext.startActivityForResult(intent, RC_PLACE_PICKER)
                        }

                    } catch (ex: SecurityException) {
                        ex.printStackTrace()
                    } catch (ex: IllegalArgumentException) {
                        ex.printStackTrace()
                    }
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                    status.startResolutionForResult(mContext, ConstantsHelper.RC_CHECK_LOCATION_SETTINGS)
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }

                else -> ToastHelper.showToast(mContext, mContext.getString(R.string.something_went_wrong))
            }
        })
    }
}