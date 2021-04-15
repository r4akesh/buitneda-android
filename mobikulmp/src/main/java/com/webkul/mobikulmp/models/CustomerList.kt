package com.webkul.mobikulmp.models


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class CustomerList {

    @JsonProperty("customerName")

    var customerName: String? = null

    @JsonProperty("customerEmail")

    var customerEmail: String? = null

    @JsonProperty("customerAddress")

    var customerAddress: String? = null

    @JsonProperty("customerGender")

    var customerGender: String? = null

    @JsonProperty("customerTelephone")

    var customerTelephone: String? = null

    @JsonProperty("customerBaseTotal")

    var customerBaseTotal: String? = null

    @JsonProperty("customerOrderCount")

    var customerOrderCount: String? = null
}