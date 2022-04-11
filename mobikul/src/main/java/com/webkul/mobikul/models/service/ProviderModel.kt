package com.webkul.mobikul.models.service

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class ProviderModel {
    var business_address: String? = null
    var business_address_city: String? = null
    var business_address_country: String? = null
    var business_address_state: String? = null
    var business_address_street: String? = null
    var business_address_zip: String? = null
    var business_detail: String? = null
    var business_images: ArrayList<String>? = ArrayList()
    var company_contact_number: String? = null
    var company_logo: Any? = null
    var company_name: String? = null
    var created_at: String? = null
    var email: String? = null
    var end_date: String? = null
    var image: String? = null
    var month_value: String? = null
    var order: Any? = null
    var password: String? = null
    var service_description: String? = null
    var service_id: String? = null
//    var service_name: Any? = null
    var service_provider_order: String? = null
    var serviceprovider_id: String? = null
    var start_date: String? = null
    var status: String? = null
    var amount: Any? = null
    var top_banner_image: String? = null
    var updated_at: String? = null
    var whatsapp_number: String? = null
    var review_avg: String? = null
}