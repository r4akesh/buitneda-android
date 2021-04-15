package com.webkul.mobikulmp.models.landingpage


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class Layout2 {

    @JsonProperty("buttonLabel")

    var buttonLabel: String? = null

    @JsonProperty("bannerImage")

    var bannerImage: String? = null

    @JsonProperty("displayBanner")

    var isDisplayBanner: Boolean = false

    @JsonProperty("bannerContent")

    var bannerContent: String? = null
    var buttonHeadingLal: String? = "xpz"

}
