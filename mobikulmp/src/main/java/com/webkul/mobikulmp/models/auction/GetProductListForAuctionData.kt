package com.webkul.mobikulmp.models.auction

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.webkul.mobikul.models.BaseModel
import java.util.*
import kotlin.collections.ArrayList

class GetProductListForAuctionData : BaseModel() {

    @SerializedName("totalCount")
    @Expose
    var totalCount = 0

    @SerializedName("productList")
    @Expose
    var productList: ArrayList<ProductList> = ArrayList()


}