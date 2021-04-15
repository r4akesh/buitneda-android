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


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class SellerCreditMemoList() : Parcelable {

    @JsonProperty("entityId")

    var entityId: String ?= ""

    @JsonProperty("incrementId")

    var incrementId: String? = ""

    @JsonProperty("billToName")

    var billToName: String? = ""

    @JsonProperty("createdAt")

    var createdAt: String ?= ""

    @JsonProperty("status")

    var status: String ?= ""

    @JsonProperty("amount")

    var amount: String ?= ""
    var adapterPosition = 0

    constructor(parcel: Parcel) : this() {
        entityId = parcel.readString()
        incrementId = parcel.readString()
        billToName = parcel.readString()
        createdAt = parcel.readString()
        status = parcel.readString()
        amount = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(entityId)
        parcel.writeString(incrementId)
        parcel.writeString(billToName)
        parcel.writeString(createdAt)
        parcel.writeString(status)
        parcel.writeString(amount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SellerCreditMemoList> {
        override fun createFromParcel(parcel: Parcel): SellerCreditMemoList {
            return SellerCreditMemoList(parcel)
        }

        override fun newArray(size: Int): Array<SellerCreditMemoList?> {
            return arrayOfNulls(size)
        }
    }
}