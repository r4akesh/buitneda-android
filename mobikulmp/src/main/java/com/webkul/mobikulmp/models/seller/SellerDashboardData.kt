package com.webkul.mobikulmp.models.seller


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.models.BaseModel
import java.util.*

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.fragments
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 16/6/19
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class SellerDashboardData : BaseModel() {

    @JsonProperty("reviewList")

    var sellerReviewList: ArrayList<SellerReviewList>? = ArrayList()

    @JsonProperty("totalPayout")

    var totalPayout: String? = ""

    @JsonProperty("lifetimeSale")

    var lifetimeSale: String? = ""

    @JsonProperty("categoryChart")

    var categoryChart: String? = ""

    @JsonProperty("remainingAmount")

    var remainingAmount: String? = ""

    @JsonProperty("recentOrderList")

    var recentOrderList: ArrayList<SellerOrderData> = ArrayList()

    @JsonProperty("dailySalesStats")

    var dailySalesStats: String = ""

    @JsonProperty("yearlySalesStats")

    var yearlySalesStats: String = ""

    @JsonProperty("weeklySalesStats")

    var weeklySalesStats: String = ""

    @JsonProperty("monthlySalesStats")

    var monthlySalesStats: String = ""

    @JsonProperty("topSellingProducts")

    var topSellingProducts: ArrayList<TopSellingProduct> = ArrayList()

    @JsonProperty("topSellingCategories")

    var topSellingCategories: ArrayList<TopSellingProduct> = ArrayList()

    @JsonProperty("dailySalesLocationReport")

    var dailySalesLocationReport: String = ""

    @JsonProperty("yearlySalesLocationReport")

    var yearlySalesLocationReport: String = ""

    @JsonProperty("weeklySalesLocationReport")

    var weeklySalesLocationReport: String = ""

    @JsonProperty("monthlySalesLocationReport")

    var monthlySalesLocationReport: String = ""
}