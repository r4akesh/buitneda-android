package com.webkul.mobikul.models.auction

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class Winning {
    @JsonProperty("title")
    var title: String? = null

    @JsonProperty("minQty")
    var minQty: String? = null

    @JsonProperty("amount")
    var amount: String? = null

    @JsonProperty("maxQty")
    var maxQty: String? = null

    @JsonProperty("message")
    var message: String? = null

    @JsonProperty("bidCount")
    var bidCount: String? = null

    @JsonProperty("openAmount")
    var openAmount: String? = null

    @JsonProperty("remainingTime")
    var remainingTime = 0

    @JsonProperty("currentBidAmount")
    var currentBidAmount: String? = null

    @JsonProperty("showAuctionDetails")
    var isShowAuctionDetails = false

}