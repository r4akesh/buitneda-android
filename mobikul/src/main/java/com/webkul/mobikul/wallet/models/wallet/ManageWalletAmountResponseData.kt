package com.webkul.mobikul.wallet.models.wallet

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.models.BaseModel


@SuppressWarnings("unused")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class ManageWalletAmountResponseData : BaseModel() {

    @JsonProperty("logo")
    var logo: String = ""

    @JsonProperty("subHeading")
    var subHeading: ArrayList<String> = ArrayList()

    @JsonProperty("buttonLabel")
    var buttonLabel: String = ""

    @JsonProperty("mainHeading")
    var mainHeading: String = ""

    @JsonProperty("responseCode")
    var responseCode: Int = 0

    @JsonProperty("currency")
    var currencyCode: String = ""

    @JsonProperty("walletAmount")
    var walletAmount: String = ""

    @JsonProperty("maximumAmount")
    var maximumAmount: String = ""

    @JsonProperty("minimumAmount")
    var minimumAmount: String = ""

    @JsonProperty("walletProductId")
    var walletProductId: String = ""

    @JsonProperty("transactionList")
    var transactionList: ArrayList<TransactionList> = ArrayList()

    @JsonProperty("rechargeFieldLabel")
    var rechargeFieldLabel: String = ""

    @JsonProperty("walletSummaryHeading")
    var walletSummaryHeading: String = ""

    @JsonProperty("walletSummarySubHeading")
    var walletSummarySubHeading: String = ""

    @JsonProperty("messageForAccountDetails")
    var messageForAccountDetails: String = ""

    @JsonProperty("accountDetails")
    var accountDetails: ArrayList<CustomerList> = ArrayList()

    var amount: String = ""

}