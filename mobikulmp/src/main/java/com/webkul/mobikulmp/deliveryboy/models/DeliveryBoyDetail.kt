package com.webkul.mobikulmp.deliveryboy.models

import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikulmp.BR

class DeliveryBoyDetail() :Parcelable , BaseObservable() {

    @JsonProperty("deliveryBoyId")
    var deliveryBoyId: String? = null
    @JsonProperty("sellerId")
    var sellerId: String? = null
    @JsonProperty("deliveryBoyName")
    @Bindable
    var deliveryBoyName: String? = null
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.deliveryBoyName)
        }
    @JsonProperty("orderState")
    var orderState: String? = null
    @JsonProperty("isEligibleForDeliveryBoy")
    var isEligibleForDeliveryBoy: Boolean = false

    constructor(parcel: Parcel) : this() {
        deliveryBoyId = parcel.readString()
        sellerId = parcel.readString()
        deliveryBoyName = parcel.readString()
        orderState = parcel.readString()
        isEligibleForDeliveryBoy = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(deliveryBoyId)
        parcel.writeString(sellerId)
        parcel.writeString(deliveryBoyName)
        parcel.writeString(orderState)
        parcel.writeByte(if (isEligibleForDeliveryBoy) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DeliveryBoyDetail> {
        override fun createFromParcel(parcel: Parcel): DeliveryBoyDetail {
            return DeliveryBoyDetail(parcel)
        }

        override fun newArray(size: Int): Array<DeliveryBoyDetail?> {
            return arrayOfNulls(size)
        }
    }
}