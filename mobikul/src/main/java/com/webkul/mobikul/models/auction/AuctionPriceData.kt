package com.webkul.mobikul.models.auction

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class AuctionPriceData {
    @JsonProperty("nextMinAmount")
    var nextMinAmount: String? = null

    @JsonProperty("unformattedNextMinAmount")
    var unformattedNextMinAmount: String? = null

    @JsonProperty("currentAcutionAmount")
    var currentAcutionAmount: String? = null
    var bidAmount: String = ""
    var isPlaceAsAuto = false

}