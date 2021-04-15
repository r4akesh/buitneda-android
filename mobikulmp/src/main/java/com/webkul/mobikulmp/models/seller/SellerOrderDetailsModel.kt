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


import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.models.user.InvoiceItem
import com.webkul.mobikul.models.user.OrderData
import com.webkul.mobikul.models.user.ShipmentItem
import com.webkul.mobikulmp.deliveryboy.models.DeliveryBoyDetail

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class SellerOrderDetailsModel() : BaseModel(), Parcelable {

    @JsonProperty("incrementId")

    var incrementId: String ?= ""

    @JsonProperty("statusLabel")

    var statusLabel: String ? = ""

    @JsonProperty("orderDate")

    var orderDate: String? = ""

    @JsonProperty("shippingAddress")

    var shippingAddress : String ?= ""

    @JsonProperty("shippingMethod")

    var shippingMethod : String ?= ""

    @JsonProperty("billingAddress")

    var billingAddress : String ?= ""

    @JsonProperty("paymentMethod")

    var paymentMethod: String ?= ""

    @JsonProperty("orderData")

    var orderData: OrderData ?= OrderData()

    @JsonProperty("state")

    var state: String ?= ""
        get() {
            return if (field == "new") "pending" else field
        }

    @JsonProperty("buyerName")

    var buyerName: String ?= ""

    @JsonProperty("buyerEmail")

    var buyerEmail: String ?= ""

    @JsonProperty("invoiceList")

    var invoiceList: ArrayList<InvoiceItem>? = ArrayList()

    @JsonProperty("shipmentList")

    var shipmentList: ArrayList<ShipmentItem>? = ArrayList()

    @JsonProperty("creditMemoList")

    var creditMemoList: ArrayList<SellerCreditMemoList>? = ArrayList()

    @JsonProperty("manageOrder")

    var manageOrder: Boolean = false

    @JsonProperty("sendEmailButton")

    var sendEmailButton: Boolean = false

    @JsonProperty("showBuyerInformation")

    var showBuyerInformation: Boolean = false

    @JsonProperty("creditMemoButton")

    var creditMemoButton: Boolean = false

    @JsonProperty("showAddressInformation")

    var showAddressInformation: Boolean = false

    @JsonProperty("invoiceId")

    var invoiceId: String ?= ""

    @JsonProperty("shipmentId")

    var shipmentId: String ?= ""

    var carrier :String?= ""
    var trackingNumber :String?= ""

    // #Delivery boy data
    @JsonProperty("deliveryBoyDetail")
    var deliveryBoyDetail: DeliveryBoyDetail?=null

    constructor(parcel: Parcel) : this() {
        incrementId = parcel.readString()
        statusLabel = parcel.readString()
        orderDate = parcel.readString()
        shippingAddress = parcel.readString()
        shippingMethod = parcel.readString()
        billingAddress = parcel.readString()
        paymentMethod = parcel.readString()
        orderData = parcel.readParcelable(OrderData::class.java.classLoader)
        state = parcel.readString()
        buyerName = parcel.readString()
        buyerEmail = parcel.readString()
        invoiceList = parcel.createTypedArrayList(InvoiceItem.CREATOR)
        shipmentList = parcel.createTypedArrayList(ShipmentItem.CREATOR)
        creditMemoList = parcel.createTypedArrayList(SellerCreditMemoList.CREATOR)
        manageOrder = parcel.readByte() != 0.toByte()
        sendEmailButton = parcel.readByte() != 0.toByte()
        showBuyerInformation = parcel.readByte() != 0.toByte()
        creditMemoButton = parcel.readByte() != 0.toByte()
        showAddressInformation = parcel.readByte() != 0.toByte()
        invoiceId = parcel.readString()
        shipmentId = parcel.readString()
        carrier = parcel.readString()
        trackingNumber = parcel.readString()
        deliveryBoyDetail = parcel.readParcelable(DeliveryBoyDetail::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(incrementId)
        parcel.writeString(statusLabel)
        parcel.writeString(orderDate)
        parcel.writeString(shippingAddress)
        parcel.writeString(shippingMethod)
        parcel.writeString(billingAddress)
        parcel.writeString(paymentMethod)
        parcel.writeParcelable(orderData, flags)
        parcel.writeString(state)
        parcel.writeString(buyerName)
        parcel.writeString(buyerEmail)
        parcel.writeTypedList(invoiceList)
        parcel.writeTypedList(shipmentList)
        parcel.writeTypedList(creditMemoList)
        parcel.writeByte(if (manageOrder) 1 else 0)
        parcel.writeByte(if (sendEmailButton) 1 else 0)
        parcel.writeByte(if (showBuyerInformation) 1 else 0)
        parcel.writeByte(if (creditMemoButton) 1 else 0)
        parcel.writeByte(if (showAddressInformation) 1 else 0)
        parcel.writeString(invoiceId)
        parcel.writeString(shipmentId)
        parcel.writeString(carrier)
        parcel.writeString(trackingNumber)
        parcel.writeParcelable(deliveryBoyDetail, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SellerOrderDetailsModel> {
        override fun createFromParcel(parcel: Parcel): SellerOrderDetailsModel {
            return SellerOrderDetailsModel(parcel)
        }

        override fun newArray(size: Int): Array<SellerOrderDetailsModel?> {
            return arrayOfNulls(size)
        }
    }
}