package com.webkul.mobikulmp.models.seller

//import com.bignerdranch.expandablerecyclerview.model.Parent

import android.os.Parcel
import android.os.Parcelable
import com.bignerdranch.expandablerecyclerview.model.Parent
import java.util.*

/**
 *
 * Webkul Software.
 *
 * @category Mobikul
 * @package com.webkul.mobikulmp.models
 * @author Webkul
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 10/6/19
 *
 */
class CustomCategoryData() : Parent<CustomCategoryData>, Parcelable {

    var id: Int = 0
    var name: String ?= ""
    get() = field?:""
    var childrenData: ArrayList<CustomCategoryData>? = ArrayList()
    var level: Int = 0

    var isChecked = false

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        name = parcel.readString()
        level = parcel.readInt()
        isChecked = parcel.readByte() != 0.toByte()
    }

    override fun getChildList(): List<CustomCategoryData>? {
        return childrenData
    }

    override fun isInitiallyExpanded(): Boolean {
        return false
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeInt(level)
        parcel.writeByte(if (isChecked) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CustomCategoryData> {
        override fun createFromParcel(parcel: Parcel): CustomCategoryData {
            return CustomCategoryData(parcel)
        }

        override fun newArray(size: Int): Array<CustomCategoryData?> {
            return arrayOfNulls(size)
        }
    }
}
