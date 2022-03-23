package com.webkul.mobikul.models.service

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class ServiceCommonModel(
    val banners: List<Banner>,
    val message: String,
    val success: Boolean
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class Banner(
    val banner_order: String,
    val banner_title: String,
    val created_at: String,
    val homepagebanner_id: String,
    val image: String,
    val image_chgekcing: String,
    val is_service: Any,
    val is_service_provider: Int,
    val mobile_banner: String,
    val mobile_status: String,
    val service_id: Any,
    val service_provider_id: String,
    val status: String,
    val updated_at: String
)