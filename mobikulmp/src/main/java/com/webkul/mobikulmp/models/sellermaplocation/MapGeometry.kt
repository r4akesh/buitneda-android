package com.webkul.mobikulmp.models.sellermaplocation


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class MapGeometry {

    @JsonProperty("location")

    var location: MapLocation? = null
}
