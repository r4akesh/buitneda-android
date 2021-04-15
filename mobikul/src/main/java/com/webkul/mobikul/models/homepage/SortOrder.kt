package com.webkul.mobikul.models.homepage

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class SortOrder {
    @JsonProperty("id")
    var id: String? = null

    @JsonProperty("layout_id")
    var layoutId: String? = null

    @JsonProperty("label")
    var label: String? = null

    @JsonProperty("position")
    var position: String? = null

    @JsonProperty("type")
    var type: String? = null

}