package com.webkul.mobikul.wallet.models.wallet

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.wallet.models.wallet.AddedPayeeList
import com.webkul.mobikul.wallet.models.wallet.CustomerList

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class TransferWalletAmountResponseData : BaseModel() {

    @JsonProperty("logo")
    var logo: String = ""

    @JsonProperty("mainHeading")
    var mainHeading: String = ""

    @JsonProperty("currency")
    var currencyCode: String = ""

    @JsonProperty("walletAmount")
    var walletAmount: String = ""

    @JsonProperty("payeeList")
    var payeeList: ArrayList<CustomerList> = ArrayList()

    @JsonProperty("addedPayee")
    var addedPayee: ArrayList<AddedPayeeList> = ArrayList()

    @JsonProperty("walletSummaryHeading")
    var walletSummaryHeading: String = ""

    @JsonProperty("walletSummarySubHeading")
    var walletSummarySubHeading: String = ""

    var amount = ""
    var note = ""
}