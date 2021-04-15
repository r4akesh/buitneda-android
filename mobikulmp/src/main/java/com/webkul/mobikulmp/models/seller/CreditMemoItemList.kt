package com.webkul.mobikulmp.models.seller


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class CreditMemoItemList {

    @JsonProperty("productName")

    var productName: String? = null

    @JsonProperty("customOption")

    var customOption: List<CustomOption>? = null

    @JsonProperty("downloadableOptionLable")

    var downloadableOptionLable: String? = null

    @JsonProperty("downloadableOptionValue")

    var downloadableOptionValue: List<Any>? = null

    @JsonProperty("sku")

    var sku: String? = null

    @JsonProperty("price")

    var price: String? = null

    @JsonProperty("qty")

    var qty: String? = null

    @JsonProperty("subTotal")

    var subTotal: String? = null

    @JsonProperty("totalTax")

    var totalTax: String? = null

    @JsonProperty("discountTotal")

    var discountTotal: String? = null

    @JsonProperty("rowTotal")

    var rowTotal: String? = null

}