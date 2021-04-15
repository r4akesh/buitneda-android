package com.webkul.mobikulmp.models.seller


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.models.BaseModel

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class SellerTransactionListResponseData : BaseModel() {

    @JsonProperty("totalCount")

    var totalCount: Int = 0

    @JsonProperty("transactionList")

    var sellerTransactionList: ArrayList<SellerTransactionList> = ArrayList()

    @JsonProperty("remainingTransactionAmount")

    var remainingTransactionAmount: String = ""

    @JsonProperty("unformattedRemainingPayout")

    var unformattedRemainingPayout: Float = 0.toFloat()

    @JsonProperty("totalPayout")

    var totalPayout: String = ""

    @JsonProperty("unformattedTotalPayout")

    var unformattedTotalPayout: Float = 0.toFloat()

    @JsonProperty("totalSellerEarning")

    var totalSellerEarning: String = ""

    @JsonProperty("unformattedTotalSellerEarning")

    var unformattedTotalSellerEarning: Float = 0.toFloat()

    @JsonProperty("totalSale")

    var totalSale: String = ""

    @JsonProperty("unformattedTotalSale")

    var unformattedTotalSale: Float = 0.toFloat()

    @JsonProperty("totalTax")

    var totalTax: String = ""

    @JsonProperty("unformattedTotalTax")

    var unformattedTotalTax: Float = 0.toFloat()

    @JsonProperty("totalCommission")

    var totalCommission: String = ""

    @JsonProperty("unformattedTotalCommission")

    var unformattedTotalCommission: Float = 0.toFloat()

}