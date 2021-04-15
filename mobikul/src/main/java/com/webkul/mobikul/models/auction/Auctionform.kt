package com.webkul.mobikul.models.auction

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class Auctionform {
    @JsonProperty("pro_name")
    var proName: String? = null

    @JsonProperty("entity_id")
    var entityId: String? = null

    @JsonProperty("product_id")
    var productId: String? = null

    @JsonProperty("auto_auction_opt")
    var autoAuctionOpt: String? = null

    @JsonProperty("auto_bid_allowed")
    var autoBidAllowed: String? = null

    @JsonProperty("stop_auction_time_stamp")
    var stopAuctionTimeStamp: String? = null

}