package com.webkul.mobikul.wallet.models.wallet

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


@SuppressWarnings("unused")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class TransactionList {

    @JsonProperty("description")
    var description: String = ""

    @JsonProperty("viewId")
    var viewId: String = ""

    @JsonProperty("debit")
    var debit: String = ""

    @JsonProperty("credit")
    var credit: String = ""

    @JsonProperty("status")
    var status: String = ""
}