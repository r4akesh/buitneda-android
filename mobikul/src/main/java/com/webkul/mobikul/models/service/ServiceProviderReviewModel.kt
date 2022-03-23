package com.webkul.mobikul.models.service

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class ServiceProviderReviewModel {
    var created_at: String = ""
    var description: Any = ""
    var photo: Any? = ""
    var rating: String = ""
    var review_id: String = ""
    var review_reply: ReviewReply = ReviewReply()
    var service_provider_id: String = ""
    var status: String = ""
    var title: Any = ""
    var updated_at: String = ""
}