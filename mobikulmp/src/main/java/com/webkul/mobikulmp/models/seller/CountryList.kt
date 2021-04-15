package com.webkul.mobikulmp.models.seller


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
 * @date 1/6/19
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class CountryList {

    @JsonProperty("value")

    var value: String? = ""

    @JsonProperty("label")

    var label: String? = ""

    @JsonProperty("is_region_visible")

    var isIsRegionVisible: Boolean = false

}