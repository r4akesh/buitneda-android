package com.webkul.mobikul.models.service

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
 class ServiceListModel{
     var message: String = ""
     var service_provider: ArrayList<ServiceProviderModel> = ArrayList()
     var success: Boolean = false
 }

