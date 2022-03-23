package com.webkul.mobikul.models.service

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class ServiceBannerListModel {
    val created_at: String = ""
    val image: String = ""
    val order: Any = ""
    val service_id: String = ""
    val service_order: String = ""
    val service_title: String = ""
    val status: String = ""
    val updated_at: String = ""
}