package com.webkul.mobikul.models.homepage

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class SubCategory_ {
    @JsonProperty("id")
    var id: String? = null

    @JsonProperty("name")
    var name: String? = null

    @JsonProperty("icon")
    var icon: String? = null

    @JsonProperty("hasChildren")
    var hasChildren: Boolean? = null

}