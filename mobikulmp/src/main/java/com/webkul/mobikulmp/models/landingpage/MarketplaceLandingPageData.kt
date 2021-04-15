package com.webkul.mobikulmp.models.landingpage


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.models.BaseModel

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class MarketplaceLandingPageData : BaseModel() {

    @JsonProperty("pageLayout")

    var pageLayout: Int? = 0

    @JsonProperty("layout1")

    var layout1: Layout? = Layout()

    @JsonProperty("layout2")

    var layout2: Layout2? = Layout2()

    @JsonProperty("layout3")

    var layout3: Layout3? = Layout3()


}
