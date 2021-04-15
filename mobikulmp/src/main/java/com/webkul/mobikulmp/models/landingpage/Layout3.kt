package com.webkul.mobikulmp.models.landingpage


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class Layout3 {

    @JsonProperty("headingOne")

    var headingOne: String? = null

    @JsonProperty("headingTwo")

    var headingTwo: String? = null

    @JsonProperty("headingThree")

    var headingThree: String? = null

    @JsonProperty("iconOne")

    var iconOne: String? = null

    @JsonProperty("iconTwo")

    var iconTwo: String? = null

    @JsonProperty("labelOne")

    var labelOne: String? = null

    @JsonProperty("labelTwo")

    var labelTwo: String? = null

    @JsonProperty("iconFour")

    var iconFour: String? = null

    @JsonProperty("iconFive")

    var iconFive: String? = null

    @JsonProperty("iconThree")

    var iconThree: String? = null

    @JsonProperty("labelFour")

    var labelFour: String? = null

    @JsonProperty("labelFive")

    var labelFive: String? = null

    @JsonProperty("labelThree")

    var labelThree: String? = null

    @JsonProperty("bannerImage")

    var bannerImage: String? = null

    @JsonProperty("displayIcon")

    var isDisplayIcon: Boolean = false

    @JsonProperty("displayBanner")

    var isDisplayBanner: Boolean = false

    @JsonProperty("bannerContent")

    var bannerContent: String? = null

    var buttonHeadingLal: String? = "xpz"

}
