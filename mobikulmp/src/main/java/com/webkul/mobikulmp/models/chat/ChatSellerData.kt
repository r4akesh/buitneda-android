package com.webkul.mobikulmp.models.chat


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


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
class ChatSellerData {
    @JsonProperty("customerToken")

    var customerToken: String? = null

    @JsonProperty("token")

    var token: String? = null

    @JsonProperty("name")

    var name: String = ""

    @JsonProperty("profileImage")

    var profileImage: String? = null

    @JsonProperty("email")

    var sellerEmail: String? = null
}
