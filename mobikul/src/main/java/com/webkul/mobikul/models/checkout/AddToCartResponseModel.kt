/*
 * Webkul Software.
 *
 * Kotlin
 *
 * @author Webkul <support@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

package com.webkul.mobikul.models.checkout


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.models.BaseModel


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class AddToCartResponseModel : BaseModel() {

    @JsonProperty("quoteId")
    var quoteId: Int = 0

    @JsonProperty("cart_quote_id")
    var cartQuoteId: Int = 0

    @JsonProperty("cartItemId")
    var cartItemId: String = ""

    @JsonProperty("isVirtual")

    var isVirtual: Boolean = false
}