package com.webkul.mobikul.models.service

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class ServiceReviewListModel {
    val message: String = ""
    val review: List<ServiceProviderReviewModel> = ArrayList()
    val success: Boolean = false
}
