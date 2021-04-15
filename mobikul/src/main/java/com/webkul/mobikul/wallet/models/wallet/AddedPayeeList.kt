package com.webkul.mobikul.wallet.models.wallet

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class AddedPayeeList() : Parcelable {
    @JsonProperty("entityId")
    var id: String = ""

    @JsonProperty("nickname")
    var name: String = ""

    @JsonProperty("email")
    var email: String = ""

    @JsonProperty("status")
    var status: String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString() ?: ""
        name = parcel.readString() ?: ""
        email = parcel.readString() ?: ""
        status = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddedPayeeList> {
        override fun createFromParcel(parcel: Parcel): AddedPayeeList {
            return AddedPayeeList(parcel)
        }

        override fun newArray(size: Int): Array<AddedPayeeList?> {
            return arrayOfNulls(size)
        }
    }


}