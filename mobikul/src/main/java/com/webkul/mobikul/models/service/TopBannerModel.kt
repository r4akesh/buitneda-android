package com.webkul.mobikul.models.service

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class TopBannerModel {
    val banner_order: String = ""
    val banner_title: String = ""
    val created_at: String = ""
    val homepagebanner_id: String = ""
    val image: String = ""
    val mobile_banner: String = ""
    val mobile_status: String = ""
    val service_id: String = ""
    val status: String = ""
    val updated_at: String = ""
}