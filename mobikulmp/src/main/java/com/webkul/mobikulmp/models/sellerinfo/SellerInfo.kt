package com.webkul.mobikulmp.models.sellerinfo


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class SellerInfo {

    @JsonProperty("displaySellerInfo")
    var displaySellerInfo: Boolean? = false

    @JsonProperty("sellerId")
    var sellerId: String? = null

    @JsonProperty("shoptitle")
    var shoptitle: String? = null

    @JsonProperty("location")
    var location: String? = null

    @JsonProperty("sellerRating")
    var sellerRating: List<SellerRating>? = null

    @JsonProperty("reviewDescription")
    var reviewDescription: String? = ""

    @JsonProperty("sellerAverageRating")
    var sellerAverageRating: String? = ""

    @JsonProperty("sellerProductCount")
    var sellerProductCount: String? = ""

}