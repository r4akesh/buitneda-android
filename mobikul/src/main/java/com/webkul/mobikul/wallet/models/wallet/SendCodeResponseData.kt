package com.webkul.mobikul.wallet.models.wallet

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.models.BaseModel

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class SendCodeResponseData : BaseModel() {

    @JsonProperty("transferValidation")
    var isTransferValidation: Boolean = false

    @JsonProperty("base_amount")
    var baseAmount: String = ""

    @JsonProperty("codeHash")
    var codeHash: String = ""

    var code = ""
}