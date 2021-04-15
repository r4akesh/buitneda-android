package com.webkul.mobikulmp.models.seller


import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class CustomerDetails() : Parcelable {

    @JsonProperty("name")

    var name: String ?= ""

    @JsonProperty("date")

    var date: String ?= ""

    @JsonProperty("baseTotal")

    var baseTotal: String ?= ""

    @JsonProperty("purchaseTotal")

    var purchaseTotal: String ?= ""

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        date = parcel.readString()
        baseTotal = parcel.readString()
        purchaseTotal = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(date)
        parcel.writeString(baseTotal)
        parcel.writeString(purchaseTotal)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CustomerDetails> {
        override fun createFromParcel(parcel: Parcel): CustomerDetails {
            return CustomerDetails(parcel)
        }

        override fun newArray(size: Int): Array<CustomerDetails?> {
            return arrayOfNulls(size)
        }
    }

    fun getFormattedDate(): String {
        try {
            val originalFormat = SimpleDateFormat("MM/dd/yy", Locale.ENGLISH)
            val targetFormat = SimpleDateFormat("MMMM dd yyyy", Locale.ENGLISH)
            val date = originalFormat.parse(date)
            return targetFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date?:""
    }
}
