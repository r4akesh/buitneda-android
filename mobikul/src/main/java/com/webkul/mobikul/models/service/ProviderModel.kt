package com.webkul.mobikul.models.service

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class ProviderModel {
    val business_address: String = ""
    val business_address_city: String = ""
    val business_address_country: String = ""
    val business_address_state: String = ""
    val business_address_street: String = ""
    val business_address_zip: String = ""
    var business_detail: String? = ""
    val business_images: ArrayList<String> = ArrayList()
    val company_contact_number: String = ""
    val company_logo: Any = ""
    val company_name: String = ""
    val created_at: String = ""
    val email: String = ""
    val end_date: String = ""
    val image: String = ""
    val month_value: String = ""
    val order: Any = ""
    val password: String = ""
    val service_description: String? = ""
    val service_id: String = ""
    val service_name: Any = ""
    val service_provider_order: String = ""
    val serviceprovider_id: String = ""
    val start_date: String = ""
    val status: String = ""
    val amount: Any? = ""
    val top_banner_image: String = ""
    val updated_at: String = ""
    val whatsapp_number: String = ""
    val review_avg: String? = ""
}