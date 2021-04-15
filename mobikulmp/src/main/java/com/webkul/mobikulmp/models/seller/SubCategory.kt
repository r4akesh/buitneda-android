package com.webkul.mobikulmp.models.seller


import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class SubCategory() : Parcelable {
    @JsonProperty("category_id")

    var categoryId: Int = 0

    @JsonProperty("parent_id")

    var parentId: String? = null

    @JsonProperty("name")

    var name: String? = null

    @JsonProperty("is_active")

    var isActive: String? = null

    @JsonProperty("position")

    var position: String? = null

    @JsonProperty("level")

    var level: String? = null

    @JsonProperty("children")

    var children: List<SubCategory>? = null

    @JsonProperty("is_private_category")

    var isPrivateCategory: Int = 0

    @JsonProperty("category_private_group")

    var categoryPrivateGroup: String? = null

    constructor(parcel: Parcel) : this() {
        categoryId = parcel.readInt()
        parentId = parcel.readString()
        name = parcel.readString()
        isActive = parcel.readString()
        position = parcel.readString()
        level = parcel.readString()
        children = parcel.createTypedArrayList(CREATOR)
        isPrivateCategory = parcel.readInt()
        categoryPrivateGroup = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(categoryId)
        parcel.writeString(parentId)
        parcel.writeString(name)
        parcel.writeString(isActive)
        parcel.writeString(position)
        parcel.writeString(level)
        parcel.writeTypedList(children)
        parcel.writeInt(isPrivateCategory)
        parcel.writeString(categoryPrivateGroup)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SubCategory> {
        override fun createFromParcel(parcel: Parcel): SubCategory {
            return SubCategory(parcel)
        }

        override fun newArray(size: Int): Array<SubCategory?> {
            return arrayOfNulls(size)
        }
    }


}
