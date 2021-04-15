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
import com.webkul.mobikul.models.user.OrderInvoiceItem

/**
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class SellerOrderInvoiceItem() : Parcelable {

    @JsonProperty("productName")

    var name: String ?= ""

    @JsonProperty("sku")

    var sku: String? = ""

    @JsonProperty("qty")

    var qty: ArrayList<OptionsData> ?= ArrayList()

    @JsonProperty("price")

    var price: String ?= ""

    @JsonProperty("subTotal")

    var subTotal: String? = ""

    @JsonProperty("totalPrice")

    var totalPrice: String ?= ""

    @JsonProperty("mpcodprice")

    var mpcodprice: String? = ""

    @JsonProperty("adminCommission")

    var adminCommission: String? = ""

    @JsonProperty("vendorTotal")

    var vendorTotal: String ?= ""

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        sku = parcel.readString()
        qty = parcel.createTypedArrayList(OptionsData)
        price = parcel.readString()
        subTotal = parcel.readString()
        totalPrice = parcel.readString()
        mpcodprice = parcel.readString()
        adminCommission = parcel.readString()
        vendorTotal = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(sku)
        parcel.writeTypedList(qty)
        parcel.writeString(price)
        parcel.writeString(subTotal)
        parcel.writeString(totalPrice)
        parcel.writeString(mpcodprice)
        parcel.writeString(adminCommission)
        parcel.writeString(vendorTotal)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderInvoiceItem> {
        override fun createFromParcel(parcel: Parcel): OrderInvoiceItem {
            return OrderInvoiceItem(parcel)
        }

        override fun newArray(size: Int): Array<OrderInvoiceItem?> {
            return arrayOfNulls(size)
        }
    }
}