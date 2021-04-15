package com.webkul.mobikulmp.models.seller


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.models.BaseModel
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class ViewTransactionResponseData : BaseModel() {

    @JsonProperty("date")

    var date: String = ""

    @JsonProperty("type")

    var type: String = ""

    @JsonProperty("method")

    var method: String = ""

    @JsonProperty("amount")

    var amount: String = ""

    @JsonProperty("comment")

    var comment: String = ""

    @JsonProperty("orderList")

    var transactionOrderList: ArrayList<TransactionOrderList> = ArrayList()

    @JsonProperty("transactionId")

    var transactionId: String = ""

}