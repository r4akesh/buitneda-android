/*
 * Webkul Software.
 *
 * Kotlin
 *
 * @author Webkul <support@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2019 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

package com.webkul.mobikulmp.models.seller


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.models.user.OrderShipmentItem
import com.webkul.mobikul.models.user.TrackingData


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class SellerShipmentDetailsData : BaseModel() {

    @JsonProperty("orderHeading")

    var orderHeading: String = ""

    @JsonProperty("orderDate")

    var orderDate: String = ""

    @JsonProperty("orderStatus")

    var orderStatus: String = ""

    @JsonProperty("state")

    var state: String = ""

    @JsonProperty("showBuyerInformation")

    var showBuyerInformation: Boolean = false

    @JsonProperty("buyerInfoHeading")

    var buyerInfoHeading: String = ""

    @JsonProperty("customerNameHeading")

    var customerNameHeading: String = ""

    @JsonProperty("customerName")

    var customerName: String = ""

    @JsonProperty("customerEmailHeading")

    var customerEmailHeading: String = ""

    @JsonProperty("customerEmail")

    var customerEmail: String = ""

    @JsonProperty("itemList")

    var items: ArrayList<OrderShipmentItem> = ArrayList()

    @JsonProperty("trackingData")

    var trackingData: ArrayList<TrackingData> = ArrayList()

    @JsonProperty("showAddressInformation")

    var showAddressInformation: Boolean = false

    @JsonProperty("addressinfoHeading")

    var addressinfoHeading: String = ""

    @JsonProperty("shippingAddress")

    var shippingAddress: String = ""

    @JsonProperty("billingAddress")

    var billingAddress: String = ""

    @JsonProperty("shippingMethod")

    var shippingMethod: String = ""

    @JsonProperty("paymentMethod")

    var paymentMethod: String = ""
}