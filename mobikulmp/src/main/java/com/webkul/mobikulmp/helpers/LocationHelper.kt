package com.webkul.mobikulmp.helpers

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng

import com.webkul.mobikul.network.ApiClient
import com.webkul.mobikulmp.models.sellermaplocation.MapResponse
import com.webkul.mobikulmp.network.MpApiDetails
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/**
 * Webkul Software.
 *
 *
 * Java
 *
 * @author Webkul <support></support>@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

object LocationHelper {
    val GOOGLE_MAP_BASE_URL = "https://maps.googleapis.com/maps/api/geocode/"

    /*Lcoation Contant */
    private val TAG = "LocationHelper"
    private var latLng: LatLng? = null

    private val mapResponseCallback = object : Callback<MapResponse> {
        override fun onResponse(call: Call<MapResponse>, response: Response<MapResponse>) {
            if (response.body()?.status.equals("ok", ignoreCase = true)) {
                latLng = response.body()!!.results?.get(0)?.geometry?.location?.lng?.let { LatLng(response.body()!!.results?.get(0)?.geometry?.location?.lat!!, it) }
            }
        }

        override fun onFailure(call: Call<MapResponse>, t: Throwable) {
            t.printStackTrace()
        }
    }

    fun getLatLang(context: Context, strAddress: String): LatLng? {
        val coder = Geocoder(context)
        val address: List<Address>?
        try {
            address = coder.getFromLocationName(strAddress, 5)
            if (address == null) {
                return null
            }
            if (address.size == 0) {
                return null
            }
            val location = address[0]
            location.latitude
            location.longitude
            latLng = LatLng(location.latitude, location.longitude)
        } catch (ex: Exception) {
            ex.printStackTrace()
            if (ex is IOException) {
                val url = GOOGLE_MAP_BASE_URL + "json?address=$strAddress"

                val call = ApiClient.getClient()!!.create(MpApiDetails::class.java).getGoogleMapResponse(url)
                call.enqueue(mapResponseCallback)
            }
        }

        return latLng
    }
}
