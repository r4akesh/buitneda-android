package com.webkul.mobikul.models.service

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class HomeServiceListModel {
    val group_title: String = ""
    val service_ids: List<String> = ArrayList()
    val services: ArrayList<ServiceBannerListModel> = ArrayList()
}