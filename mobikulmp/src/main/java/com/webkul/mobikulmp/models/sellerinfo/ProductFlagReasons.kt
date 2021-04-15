package com.webkul.mobikulmp.models.sellerinfo

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class ProductFlagReasons() :Parcelable{

    @JsonProperty("entity_id")

    var entityId: String? = ""

    @JsonProperty("reason")

    var reason: String? = ""

    @JsonProperty("created_at")

    var createdAt: String? = ""

    @JsonProperty("updated_at")

    var updatedAt: String? = ""

    @JsonProperty("status")

    var status: String? = ""

    constructor(parcel: Parcel) : this() {
        entityId = parcel.readString()
        reason = parcel.readString()
        createdAt = parcel.readString()
        updatedAt = parcel.readString()
        status = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(entityId)
        parcel.writeString(reason)
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductFlagReasons> {
        override fun createFromParcel(parcel: Parcel): ProductFlagReasons {
            return ProductFlagReasons(parcel)
        }

        override fun newArray(size: Int): Array<ProductFlagReasons?> {
            return arrayOfNulls(size)
        }
    }


}