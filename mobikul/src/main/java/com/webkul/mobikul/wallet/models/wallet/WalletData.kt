package com.webkul.mobikul.wallet.models.wallet

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class WalletData {

    @JsonProperty("flag")
    var flag: Int = 0

    @JsonProperty("type")
    var type: String = ""

    @JsonProperty("unformattedPaymentToMade")
    var unformattedPaymentToMade: Double = 0.toDouble()

    @JsonProperty("unformattedAmountInWallet")
    var unformattedAmountInWallet: Double = 0.toDouble()

    @JsonProperty("unformattedLeftAmountToPay")
    var unformattedLeftAmountToPay: Double = 0.toDouble()

    @JsonProperty("unformattedLeftInWallet")
    var unformattedLeftInWallet: Double = 0.toDouble()

    @JsonProperty("formattedPaymentToMade")
    var formattedPaymentToMade: String = ""

    @JsonProperty("formattedAmountInWallet")
    var formattedAmountInWallet: String = ""

    @JsonProperty("formattedLeftAmountToPay")
    var formattedLeftAmountToPay: String = ""

    @JsonProperty("formattedLeftInWallet")
    var formattedLeftInWallet: String = ""

    @JsonProperty("walletDiscount")
    var walletDiscount: WalletDiscount = WalletDiscount()
}