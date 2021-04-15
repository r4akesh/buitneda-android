package com.webkul.mobikulmp.models.seller

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class OrderStatus() : Parcelable {

    @JsonProperty("status")

    var status: String ?= ""

    @JsonProperty("label")

    var label: String? = ""

    constructor(parcel: Parcel) : this() {
        status = parcel.readString()
        label = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(status)
        parcel.writeString(label)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderStatus> {
        override fun createFromParcel(parcel: Parcel): OrderStatus {
            return OrderStatus(parcel)
        }

        override fun newArray(size: Int): Array<OrderStatus?> {
            return arrayOfNulls(size)
        }
    }

}