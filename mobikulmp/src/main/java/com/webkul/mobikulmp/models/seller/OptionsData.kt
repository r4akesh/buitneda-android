package com.webkul.mobikulmp.models.seller

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


@SuppressWarnings("unused")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class OptionsData() : Parcelable {

    @JsonProperty(value = "value")
    @JsonAlias("value", "id", "category_id")
    var value: String? = null

    @JsonProperty(value = "label")
    @JsonAlias("label", "name")

    var label: String ?= ""

    constructor(parcel: Parcel) : this() {
        value = parcel.readString()
        label = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(value)
        parcel.writeString(label)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OptionsData> {
        override fun createFromParcel(parcel: Parcel): OptionsData {
            return OptionsData(parcel)
        }

        override fun newArray(size: Int): Array<OptionsData?> {
            return arrayOfNulls(size)
        }
    }

}