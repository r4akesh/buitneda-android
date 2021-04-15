package com.webkul.mobikul.wallet.models.wallet

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.wallet.models.wallet.EachTransactionData

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class TransactionData {

    @JsonProperty("amount")
    val amount: EachTransactionData = EachTransactionData()

    @JsonProperty("action")
    val action: EachTransactionData = EachTransactionData()

    @JsonProperty("type")
    val type: EachTransactionData = EachTransactionData()

    @JsonProperty("order")
    val order: EachTransactionData? = null

    @JsonProperty("bankDetails")
    val bankDetails: EachTransactionData = EachTransactionData()

    @JsonProperty("sender")
    val receiver: EachTransactionData = EachTransactionData()

    @JsonProperty("date")
    val date: EachTransactionData = EachTransactionData()

    @JsonProperty("note")
    val note: EachTransactionData = EachTransactionData()

    @JsonProperty("status")
    val status: EachTransactionData? = null
}