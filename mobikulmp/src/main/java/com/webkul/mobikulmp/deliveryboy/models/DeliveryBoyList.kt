package com.webkul.mobikulmp.deliveryboy.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@SuppressWarnings("unused")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class DeliveryBoyList {
    @JsonProperty("id")
    @JsonIgnore
    var id: String? = null

    @JsonProperty("name")
    @JsonIgnore
    var name: String? = null

    @JsonProperty("status")
    @JsonIgnore
    var status = 0

    @JsonProperty("availabilityStatus")
    @JsonIgnore
    var availabilityStatus: Boolean? = false

    @JsonProperty("orderCount")
    @JsonIgnore
    var orderCount: Int? = null
        get() = field ?: 0

    @JsonProperty("avatar")
    @JsonIgnore
    var avatar: String? = null

}