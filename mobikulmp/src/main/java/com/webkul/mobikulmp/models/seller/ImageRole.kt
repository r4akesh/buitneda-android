package com.webkul.mobikulmp.models.seller

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class ImageRole() : Parcelable {

    @JsonProperty("id")

    var id: String? = null

    @JsonProperty("value")

    var value: String ?= ""
get() = field?:""
    @JsonProperty("label")

    var label: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        value = parcel.readString()
        label = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(value)
        parcel.writeString(label)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageRole> {
        override fun createFromParcel(parcel: Parcel): ImageRole {
            return ImageRole(parcel)
        }

        override fun newArray(size: Int): Array<ImageRole?> {
            return arrayOfNulls(size)
        }
    }

}