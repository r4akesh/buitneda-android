package com.webkul.mobikulmp.models.seller


import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class SellerOrderProductList() : Parcelable {

    @JsonProperty("qty")

    private var qty: Int = 0

    @JsonProperty("name")

    var name: String ?= ""

    @JsonProperty("productId")

    var productId: String ?= ""

    fun getQty(): String {
        return qty.toString()
    }

    fun setQty(qty: Int) {
        this.qty = qty
    }

    constructor(parcel: Parcel) : this() {
        qty = parcel.readInt()
        name = parcel.readString()
        productId = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(qty)
        parcel.writeString(name)
        parcel.writeString(productId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SellerOrderProductList> {
        override fun createFromParcel(parcel: Parcel): SellerOrderProductList {
            return SellerOrderProductList(parcel)
        }

        override fun newArray(size: Int): Array<SellerOrderProductList?> {
            return arrayOfNulls(size)
        }
    }
}
