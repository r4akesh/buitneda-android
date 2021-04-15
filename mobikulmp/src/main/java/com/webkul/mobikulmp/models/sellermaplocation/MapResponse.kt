package com.webkul.mobikulmp.models.sellermaplocation


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.models.BaseModel


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class MapResponse : BaseModel() {

    @JsonProperty("results")

    var results: List<MapResult>? = null

    @JsonProperty("status")

    var status: String? = null
}
