package com.webkul.mobikulmp.models.chat


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.models.BaseModel

/**
 *
 * Webkul Software.
 *
 * @category Mobikul
 * @package com.webkul.mobikulmp.models
 * @author Webkul
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 20/5/19
 *
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class ChatSellerListResponseData : BaseModel() {
    @JsonProperty("sellerList")

    var sellerList: List<ChatSellerData>? = null

    @JsonProperty("apiKey")

    var apiKey: String? = null
}
