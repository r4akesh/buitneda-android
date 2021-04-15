package com.webkul.mobikulmp.models.auction

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.models.auction.BidList

/**
 * Created by vedesh.kumar on 20/4/18. @Webkul Software Private limited
 */
class AuctionBidDetailsData : BaseModel() {
    @SerializedName("autoBidList")
    @Expose
    var autoBidList: ArrayList<BidList>? = null

    @SerializedName("normalBidList")
    @Expose
    var normalBidList: ArrayList<BidList>? = null

}