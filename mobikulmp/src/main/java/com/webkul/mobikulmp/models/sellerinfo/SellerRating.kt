package com.webkul.mobikulmp.models.sellerinfo


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class SellerRating {

    @JsonProperty("label")
    var label: String? = null

    @JsonProperty("value")
    var value: Double? = null

}