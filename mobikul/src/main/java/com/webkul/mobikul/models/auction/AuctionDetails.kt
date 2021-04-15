package com.webkul.mobikul.models.auction

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class AuctionDetails {
    @JsonProperty("minQty")
    var minQty: String? = null

    @JsonProperty("maxQty")
    var maxQty: String? = null

    @JsonProperty("bidCount")
    var bidCount: String? = null

    @JsonProperty("openAmount")
    var openAmount: String? = null

}