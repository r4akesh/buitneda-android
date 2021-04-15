package com.webkul.mobikulmp.models.seller


import android.os.Parcel
import android.os.Parcelable
import com.bignerdranch.expandablerecyclerview.model.Parent
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class CategoriesData() : Parcelable, Parent<SubCategory> {
    @JsonProperty("category_id")

    var categoryId: Int = 0

    @JsonProperty("parent_id")

    var parentId: Int = 0

    @JsonProperty("name")

    var name: String ?= ""

    @JsonProperty("is_active")

    var isActive: Int = 0

    @JsonProperty("position")

    var position: Int = 0

    @JsonProperty("level")

    var level: Int = 0

    @JsonProperty("children")

    var children: List<SubCategory>? = null
    var categoriesData: MutableList<CategoriesData>? = ArrayList()


    constructor(parcel: Parcel) : this() {
        categoryId = parcel.readInt()
        parentId = parcel.readInt()
        name = parcel.readString()
        isActive = parcel.readInt()
        position = parcel.readInt()
        level = parcel.readInt()
        children = parcel.createTypedArrayList(SubCategory)
    }


    override fun getChildList(): List<SubCategory>? {
        return children
    }

    override fun isInitiallyExpanded(): Boolean {
        return false
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(categoryId)
        parcel.writeInt(parentId)
        parcel.writeString(name)
        parcel.writeInt(isActive)
        parcel.writeInt(position)
        parcel.writeInt(level)
        parcel.writeTypedList(children)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CategoriesData> {
        override fun createFromParcel(parcel: Parcel): CategoriesData {
            return CategoriesData(parcel)
        }

        override fun newArray(size: Int): Array<CategoriesData?> {
            return arrayOfNulls(size)
        }
    }

}
