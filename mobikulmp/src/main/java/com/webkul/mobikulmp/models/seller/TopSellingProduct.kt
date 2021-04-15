package com.webkul.mobikulmp.models.seller


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


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
class TopSellingProduct {

    @JsonProperty("id")

    var id: String = ""

    @JsonProperty("name")

    var name: String? = ""

    @JsonProperty("productUrl")

    var productUrl: String? = ""

    @JsonProperty("salesQty")

    var salesQty: String? = ""

    @JsonProperty("openable")

    var isOpenable: Boolean = false

    @JsonProperty("rating")

    var rating: String = ""

    @JsonProperty("thumbNail")

    var thumbNail: String = ""
}
