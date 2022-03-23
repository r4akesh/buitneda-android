package com.webkul.mobikul.models.service

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
 class AllServiceModel{
     var message: String = ""
     var services: ArrayList<ServiceBannerListModel> = ArrayList()
     var success: Boolean = false
 }

