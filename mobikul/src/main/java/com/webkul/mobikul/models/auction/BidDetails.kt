package com.webkul.mobikul.models.auction

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class BidDetails {
    @JsonProperty("minQty")
    var minQty: String? = null

    @JsonProperty("maxQty")
    var maxQty: String? = null

    @JsonProperty("stopTime")
    var stopTime: String? = null

    @JsonProperty("bidRules")
    var bidRules: String? = null

    @JsonProperty("startTime")
    var startTime: String? = null

    @JsonProperty("availTime")
    var availTime: String? = null

    @JsonProperty("currentAmount")
    var currentAmount: String? = null

}