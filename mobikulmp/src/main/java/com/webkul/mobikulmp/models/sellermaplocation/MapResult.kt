package com.webkul.mobikulmp.models.sellermaplocation


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class MapResult {

    @JsonProperty("address_components")

    var addressComponents: List<AddressComponent>? = null

    @JsonProperty("formatted_address")

    var formattedAddress: String? = null

    @JsonProperty("geometry")

    var geometry: MapGeometry? = null

    @JsonProperty("place_id")

    var placeId: String? = null

    @JsonProperty("types")

    var types: List<String>? = null


}
