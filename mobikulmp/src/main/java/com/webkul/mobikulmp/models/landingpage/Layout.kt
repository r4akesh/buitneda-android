package com.webkul.mobikulmp.models.landingpage


import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


@SuppressWarnings("unused")

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class Layout {

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

    @JsonProperty("iconThree")

    var iconThree: String? = null

    @JsonProperty("labelFour")

    var labelFour: String? = null

    @JsonProperty("firstLabel")

    var firstLabel: String? = null

    @JsonProperty("labelThree")

    var labelThree: String? = null

    @JsonProperty("thirdLabel")

    var thirdLabel: String? = null

    @JsonProperty("fourthLabel")

    var fourthLabel: String? = null

    @JsonProperty("bannerImage")

    var bannerImage: String? = null

    @JsonProperty("displayIcon")

    var isDisplayIcon: Boolean = false

    @JsonProperty("showSellers")

    var isShowSellers: Boolean = false

    @JsonProperty("secondLabel")

    var secondLabel: String? = null

    @JsonProperty("sellersData")

    var sellersData: List<SellersData>? = null

    @JsonProperty("aboutContent")

    var aboutContent: String? = null

    @JsonProperty("displayBanner")

    var isDisplayBanner: Boolean = false

    @JsonProperty("bannerContent")

    var bannerContent: String? = null

    @JsonProperty(value = "buttonnHeadingLabel")
    @JsonAlias("buttonnHeadingLabel", "buttonHeadingLabel")
    var buttonHeadingLabel: String? = null

    var buttonHeadingLal: String? = "xpz"

}
