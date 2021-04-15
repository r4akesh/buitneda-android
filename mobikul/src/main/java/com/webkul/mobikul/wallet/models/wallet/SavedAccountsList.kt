package com.webkul.mobikul.wallet.models.wallet

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class SavedAccountsList {

    @JsonProperty("id")
    var id: String = ""

    @JsonProperty("holdername")
    var holdername: String = ""

    @JsonProperty("bankname")
    var bankname: String = ""

    @JsonProperty("bankcode")
    var bankcode: String = ""

    @JsonProperty("additional")
    var additional: String = ""
}