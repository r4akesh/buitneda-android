package com.webkul.mobikulmp.models.seller


import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class SellerOrderData() : Parcelable {

    @JsonProperty("orderId")

    var orderId: String? = ""

    @JsonProperty("incrementId")

    var incrementId: String ?= ""

    @JsonProperty("productNames")

    var sellerOrderProductList: ArrayList<SellerOrderProductList>? = ArrayList()

    @JsonProperty("status")

    var status: String ?= ""

    @JsonProperty("customerDetails")

    var customerDetails: CustomerDetails? = CustomerDetails()

    constructor(parcel: Parcel) : this() {
        orderId = parcel.readString()
        incrementId = parcel.readString()
        sellerOrderProductList = parcel.createTypedArrayList(SellerOrderProductList.CREATOR)
        status = parcel.readString()
        customerDetails = parcel.readParcelable(CustomerDetails::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(orderId)
        parcel.writeString(incrementId)
        parcel.writeString(status)
        parcel.writeTypedList(sellerOrderProductList)
        parcel.writeParcelable(customerDetails, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SellerOrderData> {
        override fun createFromParcel(parcel: Parcel): SellerOrderData {
            return SellerOrderData(parcel)
        }

        override fun newArray(size: Int): Array<SellerOrderData?> {
            return arrayOfNulls(size)
        }
    }
}
