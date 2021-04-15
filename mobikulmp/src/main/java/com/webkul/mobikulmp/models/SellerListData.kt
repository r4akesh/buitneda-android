package com.webkul.mobikulmp.models


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.models.seller
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 10/7/19
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class SellerListData {
    @JsonProperty("logo")

    val sellerIcon: String? = ""

    @JsonProperty("bannerImage")

    val bannerImage: String? = ""

    @JsonProperty("shoptitle")

    val shopTitle: String? = ""

    @JsonProperty("companyLocality")

    val companyLocality: String? = ""

    @JsonProperty("productCount")

    val sellerProductCount: String? = ""

    @JsonProperty("sellerId")

    var sellerId: Int = 0
}
