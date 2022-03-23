package com.webkul.mobikul.models.service

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class ServiceBannerModel {
    @JsonProperty("banner_order")
    var banner_order: String = ""
    @JsonProperty("banner_title")
    var banner_title: String = ""
    @JsonProperty("created_at")
    var created_at: String = ""
    @JsonProperty("homepagebanner_id")
    var homepagebanner_id: String = ""
    @JsonProperty("image")
    var image: String = ""
    @JsonProperty("image_chgekcing")
    var image_chgekcing: String = ""
    @JsonProperty("is_service")
    var is_service: Any? = null
    @JsonProperty("is_service_provider")
    var is_service_provider: Int? = null
    @JsonProperty("mobile_banner")
    var mobile_banner: String = ""
    @JsonProperty("mobile_status")
    var mobile_status: String = ""
    @JsonProperty("service_id")
    var service_id: Any? = ""
    @JsonProperty("service_provider_id")
    var service_provider_id: String = ""
    @JsonProperty("status")
    var status: String = ""
    @JsonProperty("updated_at")
    var updated_at: String = ""
}