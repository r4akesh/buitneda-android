package com.webkul.mobikul.models.service

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class BecomeServiceModel{
    var icon: Icon = Icon()
    var message: String = ""
    var success: Boolean = false
}
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
 class Icon{
     var banner_image: String = ""
     var call_number: String = ""
     var whatsapp_number: String = ""
 }
