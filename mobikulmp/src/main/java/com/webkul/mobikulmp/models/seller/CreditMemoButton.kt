package com.webkul.mobikulmp.models.seller


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class CreditMemoButton {

    @JsonProperty("label")

    var label: String? = null

    @JsonProperty("title")

    var title: String? = null

    @JsonProperty("confirm")

    var confirm: String? = null

}