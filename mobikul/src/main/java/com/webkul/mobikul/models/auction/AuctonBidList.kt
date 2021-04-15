package com.webkul.mobikul.models.auction

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class AuctonBidList {
    @JsonProperty("productId")
    var productId: String? = null

    @JsonProperty("productName")
    var productName: String? = null

    @JsonProperty("biddingPrice")
    var biddingPrice: String? = null

    @JsonProperty("biddingStatus")
    var biddingStatus: String? = null

    @JsonProperty("winningPrice")
    val winningPrice: String? = null

    @JsonProperty("winningStatus")
    var winningStatus: String? = null

}