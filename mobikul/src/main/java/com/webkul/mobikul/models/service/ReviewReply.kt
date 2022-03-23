package com.webkul.mobikul.models.service

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class ReviewReply {
    var created_at: String? = ""
    var description: String? = ""
    var reply_id: String? = ""
    var review_id: String? = ""
    var service_provider_id: String? = ""
    var status: String? = ""
    var success: Boolean? = false
    var updated_at: String? = ""
}