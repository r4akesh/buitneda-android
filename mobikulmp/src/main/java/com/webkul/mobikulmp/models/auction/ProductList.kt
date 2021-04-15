package com.webkul.mobikulmp.models.auction

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProductList {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("price")
    @Expose
    var price: String? = null

    @SerializedName("thumbNail")
    @Expose
    var thumbNail: String? = null

    @SerializedName("isLinkable")
    @Expose
    var isIsLinkable = false
        private set

    fun setIsLinkable(isLinkable: Boolean) {
        isIsLinkable = isLinkable
    }
}