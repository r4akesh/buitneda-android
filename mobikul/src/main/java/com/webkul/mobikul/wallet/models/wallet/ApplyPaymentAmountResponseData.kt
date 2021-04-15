package com.webkul.mobikul.wallet.models.wallet

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.models.BaseModel

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class ApplyPaymentAmountResponseData : BaseModel() {

    @JsonProperty("walletData")
    var walletData: WalletData = WalletData()
    /*   @JsonProperty("totals")
   var totals: ArrayList<PriceDetailsItem> = ArrayList()*/

}