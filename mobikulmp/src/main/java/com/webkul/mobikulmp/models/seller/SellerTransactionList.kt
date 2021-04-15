package com.webkul.mobikulmp.models.seller


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class SellerTransactionList {

    @JsonProperty("id")

    var id: String = ""

    @JsonProperty("date")

    var date: String = ""

    @JsonProperty("amount")

    var amount: String = ""

    @JsonProperty("comment")

    var comment: String = ""

    @JsonProperty("transactionId")

    var transactionId: String = ""

}