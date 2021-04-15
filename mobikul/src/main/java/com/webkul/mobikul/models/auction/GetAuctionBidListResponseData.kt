package com.webkul.mobikul.models.auction

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.models.BaseModel

/**
 * Created by vedesh.kumar on 10/4/18. @Webkul Software Private limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class GetAuctionBidListResponseData : BaseModel() {
    @JsonProperty("totalCount")
    var totalCount = 0

    @JsonProperty("auctonBidList")
    var auctonBidList: List<AuctonBidList> = ArrayList()

}