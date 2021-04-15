package com.webkul.mobikulmp.models.seller


import androidx.databinding.Bindable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikulmp.BR
import org.json.JSONException
import org.json.JSONObject
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class CreditMemoFormResponseData : BaseModel() {

    @JsonProperty("itemList")

    var itemList: ArrayList<CreditMemoFormItemList> = ArrayList()

    @JsonProperty("status")

    var status: String = ""

    @JsonProperty("totalTax")

    var totalTax: String = ""

    @JsonProperty("discount")

    var discount: String = ""

    @JsonProperty("subTotal")

    var subTotal: String = ""

    @JsonProperty("orderDate")

    var orderDate: String = ""

    @JsonProperty("invoiceId")

    var invoiceId: String = ""

    @JsonProperty("paidAmount")

    var paidAmount: String = ""

    @JsonProperty("subHeading")

    var subHeading: String = ""

    @JsonProperty("grandTotal")

    var grandTotal: String = ""

    @JsonProperty("mainHeading")

    var mainHeading: String = ""

    @JsonProperty("refundAmount")

    var refundAmount: String = ""

    @JsonProperty("customerName")

    var customerName: String = ""

    @JsonProperty("customerEmail")

    var customerEmail: String = ""

    @JsonProperty("paymentMethod")

    var paymentMethod: String = ""

    @JsonProperty("shippingAmount")

    var shippingAmount: String = ""

    @JsonProperty("shippingRefund")

    var shippingRefund: String = ""

    @JsonProperty("billingAddress")

    var billingAddress: String = ""

    @JsonProperty("shippingMethod")

    var shippingMethod: String = ""

    @JsonProperty("shippingAddress")

    var shippingAddress: String = ""

    @JsonProperty("orderGrandTotal")

    var orderGrandTotal: String = ""

    @JsonProperty("subtotalHeading")

    var subtotalHeading: String = ""

    @JsonProperty("discountHeading")

    var discountHeading: String = ""

    @JsonProperty("totalTaxHeading")

    var totalTaxHeading: String = ""

    @JsonProperty("itemsQtyHeading")

    var itemsQtyHeading: String = ""

    @JsonProperty("itemsCodHeading")

    var itemsCodHeading: String = ""

    @JsonProperty("orderInfoHeading")

    var orderInfoHeading: String = ""

    @JsonProperty("emailCopyHeading")

    var emailCopyHeading: String = ""

    @JsonProperty("buyerInfoHeading")

    var buyerInfoHeading: String = ""

    @JsonProperty("itemsPiceHeading")

    var itemsPiceHeading: String = ""

    @JsonProperty("grandTotalHeading")

    var grandTotalHeading: String = ""

    @JsonProperty("paidAmountHeading")

    var paidAmountHeading: String = ""

    @JsonProperty("refundTotalHeading")

    var refundTotalHeading: String = ""

    @JsonProperty("itemsTaxAmtHeading")

    var itemsTaxAmtHeading: String = ""

    @JsonProperty("itemsRefundHeading")

    var itemsRefundHeading: String = ""

    @JsonProperty("refundAmountHeading")

    var refundAmountHeading: String = ""

    @JsonProperty("customerNameHeading")

    var customerNameHeading: String = ""

    @JsonProperty("adjustmentFeeHeading")

    var adjustmentFeeHeading: String = ""

    @JsonProperty("itemsSubtotalHeading")

    var itemsSubtotalHeading: String = ""

    @JsonProperty("itemsRowTotalHeading")

    var itemsRowTotalHeading: String = ""

    @JsonProperty("customerEmailHeading")

    var customerEmailHeading: String = ""

    @JsonProperty("paymentMethodHeading")

    var paymentMethodHeading: String = ""

    @JsonProperty("refundShippingHeading")

    var refundShippingHeading: String = ""

    @JsonProperty("appendCommentsHeading")

    var appendCommentsHeading: String = ""

    @JsonProperty("shippingAmountHeading")

    var shippingAmountHeading: String = ""

    @JsonProperty("shippingRefundHeading")

    var shippingRefundHeading: String = ""

    @JsonProperty("billingAddressHeading")

    var billingAddressHeading: String = ""

    @JsonProperty("shippingMethodHeading")

    var shippingMethodHeading: String = ""

    @JsonProperty("shippingAddressHeading")

    var shippingAddressHeading: String = ""

    @JsonProperty("orderGrandTotalHeading")

    var orderGrandTotalHeading: String = ""

    @JsonProperty("refundOnlineEnableFlag")

    var isRefundOnlineEnableFlag: Boolean = false

    @JsonProperty("adjustmentRefundHeading")

    var adjustmentRefundHeading: String = ""

    @JsonProperty("itemsDiscountAmtHeading")

    var itemsDiscountAmtHeading: String = ""

    @JsonProperty("itemsQtyToRefundHeading")

    var itemsQtyToRefundHeading: String = ""

    @JsonProperty("creditMemoCommentHeading")

    var creditMemoCommentHeading: String = ""

    @JsonProperty("visibleOnFrontendHeading")

    var visibleOnFrontendHeading: String = ""

    @JsonProperty("itemsReturnToStockHeading")

    var itemsReturnToStockHeading: String = ""

    @JsonProperty("refundOnlineButtonHeading")

    var refundOnlineButtonHeading: String = ""

    @JsonProperty("refundOfflineButtonHeading")

    var refundOfflineButtonHeading: String = ""

    @JsonProperty("itemsProductNameHeading")

    var itemsProductNameHeading: String = ""

    var creditMemoComment = ""
    var refundShipping = "0"
    var adjustmentRefund = "0"
    var adjustmentFee = "0"
    var isAppendComment = false

    @get:Bindable
    var isVisibleInFrontend = false
        set(visibleInFrontend) {
            field = visibleInFrontend
            notifyPropertyChanged(BR.visibleInFrontend)
        }

    @get:Bindable
    var isEmailCopyOfCreditMemo = false
        set(emailCopyOfCreditMemo) {
            field = emailCopyOfCreditMemo
            notifyPropertyChanged(BR.emailCopyOfCreditMemo)
        }

    val itemsListObject: JSONObject
        get() {
            val items = JSONObject()
            for (noOfItems in itemList!!.indices) {
                try {
                    val eachItem = JSONObject()
                    eachItem.put("qty", itemList!![noOfItems].qtyToRefund)
                    eachItem.put("back_to_stock", if (itemList!![noOfItems].isReturnToStock) 1 else 0)
                    items.put(itemList!![noOfItems].itemId, eachItem)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            return items
        }
}