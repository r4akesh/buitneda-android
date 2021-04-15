package com.webkul.mobikul.models.homepage

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class BigBannerSecond {
    @JsonProperty("id")
    var id: String? = null

    @JsonProperty("name")
    var name: String? = null

    @JsonProperty("url")
    var url: String? = null

    @JsonProperty("dominantColor")
    var dominantColor: String? = null

    @JsonProperty("imagePath")
    var imagePath: String? = null

    @JsonProperty("productList")
    var productList: List<Any>? = null

}