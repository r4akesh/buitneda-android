package com.webkul.mobikul.wallet.models.wallet

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class CustomerList() : Parcelable {

    @JsonProperty("id")
    @JsonAlias("id", "payeeCustomerId", "entityId")
    var id: String = ""

    @JsonProperty("name")
    @JsonAlias("name", "bankName", "nickname")
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

    companion object CREATOR : Parcelable.Creator<CustomerList> {
        override fun createFromParcel(parcel: Parcel): CustomerList {
            return CustomerList(parcel)
        }

        override fun newArray(size: Int): Array<CustomerList?> {
            return arrayOfNulls(size)
        }
    }

}