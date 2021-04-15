package com.webkul.mobikulmp.models


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
 * @package com.webkul.mobikulmp.models.seller
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 10/7/19
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class SellerListResponseData : BaseModel() {
    @JsonProperty("displayBanner")

    var isShowBanner = false

    @JsonProperty("bannerImage")

    var banner: String? = null

    @JsonProperty("bannerContent")

    val bannerContent = ""

    @JsonProperty("buttonNHeadingLabel")

    val buttonLabel = ""

    @JsonProperty("topLabel")

    val topLabel = ""

    @JsonProperty("bottomLabel")

    val bottomLabel = ""

    @JsonProperty("sellersData")

    val sellerList: List<SellerListData> = ArrayList()
}
