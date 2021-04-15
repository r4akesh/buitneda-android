package com.webkul.mobikul.wallet.models.wallet

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class WalletDiscount {
    @JsonProperty("flag")
    var flag:Int?=null

    @JsonProperty("amount")
    var amount:Int?=null

    @JsonProperty("type")
    var type:String?=null

    @JsonProperty("grand_total")
    var grandTotal:Int?=null

    @JsonProperty("leftinWallet")
    var leftinWallet:String?=null


}

/*"": 1,
      "": 62,
      "": "set",
      "": 62,
      "": "$343.00"*/