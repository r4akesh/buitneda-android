package com.webkul.mobikulmp.models.seller


import android.os.Parcel
import android.os.Parcelable
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
class AllowedAttribute() : Parcelable {

    @JsonProperty("value")

    var value: String? = null

    @JsonProperty("label")

    var label: String? = null

    constructor(parcel: Parcel) : this() {
        value = parcel.readString()
        label = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(value)
        parcel.writeString(label)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AllowedAttribute> {
        override fun createFromParcel(parcel: Parcel): AllowedAttribute {
            return AllowedAttribute(parcel)
        }

        override fun newArray(size: Int): Array<AllowedAttribute?> {
            return arrayOfNulls(size)
        }
    }

}