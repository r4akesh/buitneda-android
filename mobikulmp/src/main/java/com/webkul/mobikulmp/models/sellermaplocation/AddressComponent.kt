package com.webkul.mobikulmp.models.sellermaplocation


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class AddressComponent {
    @JsonProperty("long_name")

    var longName: String? = null

    @JsonProperty("short_name")

    var shortName: String? = null

    @JsonProperty("types")

    var types: List<String>? = null
}
