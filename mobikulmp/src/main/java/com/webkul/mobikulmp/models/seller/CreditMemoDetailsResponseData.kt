package com.webkul.mobikulmp.models.seller


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.models.user.TotalItem
import java.util.*

/**
 * Webkul Software.
 *
 *
 * Java
 *
 * @author Webkul <support></support>@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class CreditMemoDetailsResponseData : BaseModel() {

    @JsonProperty("orderId")

    var orderId: String? = ""

    @JsonProperty("buttons")

    var buttons: List<CreditMemoButton>? = ArrayList()

    @JsonProperty("itemList")

    var itemList: ArrayList<CreditMemoItemList> = ArrayList()

    @JsonProperty("totals")

    var totals: ArrayList<TotalItem> = ArrayList()

    @JsonProperty("subTotal")

    var subTotal: String? = ""

    @JsonProperty("discount")

    var discount: String? = ""

    @JsonProperty("totalTax")

    var totalTax: String? = ""

    @JsonProperty("orderDate")

    var orderDate: String? = ""

    @JsonProperty("totalShipping")

    var totalShipping: String? = ""

    @JsonProperty("codAmount")

    var codAmount: String? = ""

    @JsonProperty("qtyHeading")

    var qtyHeading: String? = ""

    @JsonProperty("subHeading")

    var subHeading: String? = ""

    @JsonProperty("codHeading")

    var codHeading: String? = ""

    @JsonProperty("mainHeading")

    var mainHeading: String? = ""

    @JsonProperty("orderStatus")

    var orderStatus: String? = ""

    @JsonProperty("orderHeading")

    var orderHeading: String? = ""

    @JsonProperty("customerName")

    var customerName: String? = ""

    @JsonProperty("priceHeading")

    var priceHeading: String? = ""

    @JsonProperty("grandTotalAmt")

    var grandTotalAmt: String? = ""

    @JsonProperty("adjustmentFee")

    var adjustmentFee: String? = ""

    @JsonProperty("adjustmentAmt")

    var adjustmentAmt: String? = ""

    @JsonProperty("customerEmail")

    var customerEmail: String? = ""

    @JsonProperty("paymentMethod")

    var paymentMethod: String? = ""

    @JsonProperty("taxAmtHeading")

    var taxAmtHeading: String? = ""

    @JsonProperty("shippingMethod")

    var shippingMethod: String? = ""

    @JsonProperty("creditmemoDate")

    var creditmemoDate: String? = ""

    @JsonProperty("billingAddressHeading")

    var billingAddressHeading: String? = ""

    @JsonProperty("billingAddress")

    var billingAddress: String? = ""

    @JsonProperty("totalTaxHeading")

    var totalTaxHeading: String? = ""

    @JsonProperty("discountHeading")

    var discountHeading: String? = ""

    @JsonProperty("rowTotalHeading")

    var rowTotalHeading: String? = ""

    @JsonProperty("subtotalHeading")

    var subtotalHeading: String? = ""

    @JsonProperty("shippingAddress")

    var shippingAddress: String? = ""

    @JsonProperty("subTotalHeading")

    var subTotalHeading: String? = ""

    @JsonProperty("creditmemoStatus")

    var creditmemoStatus: String? = ""

    @JsonProperty("orderInfoHeading")

    var orderInfoHeading: String? = ""

    @JsonProperty("orderDateHeading")

    var orderDateHeading: String? = ""

    @JsonProperty("buyerInfoHeading")

    var buyerInfoHeading: String? = ""

    @JsonProperty("codChargeHeading")

    var codChargeHeading: String? = ""

    @JsonProperty("grandTotalHeading")

    var grandTotalHeading: String? = ""

    @JsonProperty("productNameHeading")

    var productNameHeading: String? = ""

    @JsonProperty("AddressInfoHeading")

    var addressInfoHeading: String? = ""

    @JsonProperty("orderStatusHeading")

    var orderStatusHeading: String? = ""

    @JsonProperty("shippingAmtHeading")

    var shippingAmtHeading: String? = ""

    @JsonProperty("discountAmtHeading")

    var discountAmtHeading: String? = ""

    @JsonProperty("customerNameHeading")

    var customerNameHeading: String? = ""

    @JsonProperty("customerEmailHeading")

    var customerEmailHeading: String? = ""

    @JsonProperty("adjustmentFeeHeading")

    var adjustmentFeeHeading: String? = ""

    @JsonProperty("itemsRefundedHeading")

    var itemsRefundedHeading: String? = ""

    @JsonProperty("paymentMethodHeading")

    var paymentMethodHeading: String? = ""

    @JsonProperty("shippingAddressHeading")

    var shippingAddressHeading: String? = ""

    @JsonProperty("shipAndTrackInfoHeading")

    var shipAndTrackInfoHeading: String? = ""

    @JsonProperty("paymentandshippingHeading")

    var paymentandshippingHeading: String? = ""

    @JsonProperty("adjustmentHeading")

    var adjustmentHeading: String? = ""
}
