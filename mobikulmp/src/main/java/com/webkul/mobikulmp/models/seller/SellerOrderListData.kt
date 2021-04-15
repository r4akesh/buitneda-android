package com.webkul.mobikulmp.models.seller


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.models.BaseModel

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class SellerOrderListData : BaseModel() {

    @JsonProperty("orderList")

    var orderList: ArrayList<SellerOrderData> = ArrayList()

    @JsonProperty("totalCount")

    var totalCount: Int = 0

    @JsonProperty("orderStatus")

    var orderStatus: ArrayList<OrderStatus> = ArrayList()

}