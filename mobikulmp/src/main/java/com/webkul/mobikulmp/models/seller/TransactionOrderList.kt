package com.webkul.mobikulmp.models.seller


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class TransactionOrderList {

    @JsonProperty("qty")

    var qty: String = ""

    @JsonProperty("price")

    var price: String = ""

    @JsonProperty("totalTax")

    var totalTax: String = ""

    @JsonProperty("shipping")

    var shipping: String = ""

    @JsonProperty("totalPrice")

    var totalPrice: String = ""

    @JsonProperty("commission")

    var commission: String = ""

    @JsonProperty("incrementId")

    var incrementId: String = ""

    @JsonProperty("productName")

    var productName: String = ""

    @JsonProperty("subTotal")

    var subTotal: String = ""

}