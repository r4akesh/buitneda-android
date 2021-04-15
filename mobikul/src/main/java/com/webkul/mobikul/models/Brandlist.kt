package com.webkul.mobikul.models

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.*
import java.util.*


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id", "label", "productCount", "image")
class Brandlist() :Parcelable {
    @get:JsonProperty("id")
    @set:JsonProperty("id")
    @JsonProperty("id")
    var id: String? = null

    @get:JsonProperty("label")
    @set:JsonProperty("label")
    @JsonProperty("label")
    var label: String? = null

    @get:JsonProperty("productCount")
    @set:JsonProperty("productCount")
    @JsonProperty("productCount")
    var productCount: Int? = null

    @get:JsonProperty("image")
    @set:JsonProperty("image")
    @JsonProperty("image")
    var image: String? = null

    @JsonIgnore
    private val additionalProperties: MutableMap<String, Any> = HashMap()

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        label = parcel.readString()
        productCount = parcel.readValue(Int::class.java.classLoader) as? Int
        image = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(label)
        parcel.writeValue(productCount)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Brandlist> {
        override fun createFromParcel(parcel: Parcel): Brandlist {
            return Brandlist(parcel)
        }

        override fun newArray(size: Int): Array<Brandlist?> {
            return arrayOfNulls(size)
        }
    }
}