package com.webkul.mobikul.models.service

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class ServiceProviderModel {
    var business_address: Any? = ""
    var business_address_city: String?= ""
    var business_address_country: String?= ""
    var business_address_state: String?= ""
    var business_address_street: String? = ""
    var business_address_zip: String? = ""
    var business_detail: String? = ""
    var business_images: Any? = ""
    var company_contact_number: String? = ""
    var company_logo: Any? = ""
    var company_name: String? = ""
    var created_at: String? = ""
    var email: String? = ""
    var end_date: String? = ""
    var image: String? = ""
    var month_varue: String? = ""
    var order: String? = ""
    var password: Any? = ""
    var review_avg: String? = ""
    var service_description: String? = ""
    var service_id: String? = ""
    var service_name: String? = ""
    var service_provider_order: Any? = ""
    var serviceprovider_id: String? = ""
    var start_date: String? = ""
    var status: String? = ""
    var top_banner_image: Any? = ""
    var total_review: Any? = ""
    var updated_at: String? = ""
    var whatsapp_number: String? = ""
    var amount: Any? = ""



}