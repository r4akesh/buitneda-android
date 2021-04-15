package com.webkul.mobikulmp.models.sellermaplocation


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class MapLocation {
    @JsonProperty("lat")

    var lat: Double = 0.toDouble()

    @JsonProperty("lng")

    var lng: Double = 0.toDouble()
}
