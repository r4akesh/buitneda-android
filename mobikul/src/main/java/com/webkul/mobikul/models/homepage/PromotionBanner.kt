package com.webkul.mobikul.models.homepage

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.models.BaseModel

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

class PromotionBanner() : BaseModel(), Parcelable {


    @JsonProperty("category_product_id")
    var category_product_id: String? = ""


    @JsonProperty("content")
    var content: String? = ""


    @JsonProperty("id")
    var id: String? = ""


    @JsonProperty("image")
    var image: String? = ""


    @JsonProperty("title")
    var title: String? = ""


    @JsonProperty("type")
    var type: String? = ""


    constructor(parcel: Parcel) : this() {
        category_product_id = parcel.readString()
        content = parcel.readString()
        id = parcel.readString()
        image = parcel.readString()
        title = parcel.readString()
        type = parcel.readString()


    }


    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(parcel: Parcel?, flags: Int) {
        parcel?.writeString(category_product_id)
        parcel?.writeString(content)
        parcel?.writeString(id)
        parcel?.writeString(image)
        parcel?.writeString(title)
        parcel?.writeString(type)
    }

    companion object CREATOR : Parcelable.Creator<PromotionBanner> {
        override fun createFromParcel(parcel: Parcel): PromotionBanner {
            return PromotionBanner(parcel)
        }

        override fun newArray(size: Int): Array<PromotionBanner?> {
            return arrayOfNulls(size)
        }
    }
}