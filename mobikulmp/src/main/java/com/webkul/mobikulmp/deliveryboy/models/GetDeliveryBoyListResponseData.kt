package com.webkul.mobikulmp.deliveryboy.models

import com.fasterxml.jackson.annotation.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.webkul.mobikul.models.BaseModel
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetDeliveryBoyListResponseData : BaseModel() {
    @JsonProperty("totalCount")
    @JsonIgnore
    var totalCount = 0
    @JsonProperty("deliveryboyList")
    @JsonAlias("deliveryboyList")
    @JsonIgnore
    var deliveryboyList: ArrayList<DeliveryBoyList>? = null

}