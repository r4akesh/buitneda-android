package com.webkul.mobikulmp.models.seller


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class CreditMemoFormItemList {

    @JsonProperty("itemId")

    var itemId: String = ""

    @JsonProperty("productName")

    var productName: String = ""

    @JsonProperty("customOption")

    var customOption: List<Any> = ArrayList()

    @JsonProperty("downloadableOptionLable")

    var downloadableOptionLable: String = ""

    @JsonProperty("downloadableOptionValue")

    var downloadableOptionValue: List<Any> = ArrayList()

    @JsonProperty("sku")

    var sku: String = ""

    @JsonProperty("price")

    var price: String = ""

    @JsonProperty("qty")

    var qty: List<OptionsData> = ArrayList()

    @JsonProperty("totalPrice")

    var totalPrice: String = ""

    @JsonProperty("mpcodprice")

    var mpcodprice: String = ""

    @JsonProperty("adminCommission")

    var adminCommission: String = ""

    @JsonProperty("vendorTotal")

    var vendorTotal: String = ""

    @JsonProperty("subTotal")

    var subTotal: String = ""

    @JsonProperty("totalTax")

    var totalTax: String = ""

    @JsonProperty("rowTotal")

    var rowTotal: String = ""

    @JsonProperty("discount")

    var discount: String = ""

    var isReturnToStock: Boolean = false
    var qtyToRefund = "1"
}