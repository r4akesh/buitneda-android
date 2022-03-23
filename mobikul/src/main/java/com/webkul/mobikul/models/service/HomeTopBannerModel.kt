package com.webkul.mobikul.models.service

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.webkul.mobikul.models.BaseModel
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class HomeTopBannerModel : BaseModel() {
    val top_banner: ArrayList<TopBannerModel> = ArrayList()
}

