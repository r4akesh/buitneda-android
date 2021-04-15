package com.webkul.mobikul.models.homepage


import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.models.Brandlist
import com.webkul.mobikul.models.product.ProductTileData


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
class Carousel() : Parcelable {

    @JsonProperty("id")
    var id: String? = ""

    @JsonProperty("type")
    var type: String? = ""

  @JsonProperty("cmsType")
    var cmsType: String? = ""

    @JsonProperty("label")
    var label: String? = ""

    @JsonProperty("categoryId")
    var categoryId: String? = null

    @JsonProperty("timer")
    var timer: String? = null

    @JsonProperty("timer_time_stamp")
    var timerTimeStamp: Int = 0

    @JsonProperty("color")
    var color: String? = ""

    @JsonProperty("image")
    var image: String? = ""

    @JsonProperty("imagePath")
    var imagePath: String? = null

    @JsonProperty("productList")
    var productList: ArrayList<ProductTileData>? = ArrayList()

    @JsonProperty("banners")
    var banners: ArrayList<BannerImage>? = ArrayList()

    @JsonProperty("featuredCategories")
    var featuredCategories: ArrayList<FeaturedCategory>? = ArrayList()

    @JsonProperty("singlebanner")
    var singlebanner: ArrayList<FeaturedCategory>? = ArrayList()

    @JsonProperty("auctionProductList")
    var auctionproductlist: ArrayList<String>? = ArrayList()

    @JsonProperty("bigBannerFirst")
    var bigBannerFirst: ArrayList<BigBannerFirst>? = null

    @JsonProperty("bigBannerSecond")
    var bigBannerSecond: ArrayList<BigBannerSecond>? = null

    @JsonProperty("brandlist")
    var brandlist: List<Brandlist>? = ArrayList()

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        categoryId = parcel.readString()
        timerTimeStamp = parcel.readInt()
        type = parcel.readString()
        label = parcel.readString()
        cmsType = parcel.readString()
        color = parcel.readString()
        image = parcel.readString()
        imagePath = parcel.readString()
        productList = parcel.createTypedArrayList(ProductTileData.CREATOR)
        banners = parcel.createTypedArrayList(BannerImage.CREATOR)
        featuredCategories = parcel.createTypedArrayList(FeaturedCategory.CREATOR)
        brandlist = parcel.createTypedArrayList(Brandlist.CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(categoryId)
        parcel.writeInt(timerTimeStamp)
        parcel.writeString(type)
        parcel.writeString(label)
        parcel.writeString(cmsType)
        parcel.writeString(color)
        parcel.writeString(image)
        parcel.writeString(imagePath)
        parcel.writeTypedList(productList)
        parcel.writeTypedList(banners)
        parcel.writeTypedList(featuredCategories)
        parcel.writeTypedList(brandlist)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Carousel> {
        override fun createFromParcel(parcel: Parcel): Carousel {
            return Carousel(parcel)
        }

        override fun newArray(size: Int): Array<Carousel?> {
            return arrayOfNulls(size)
        }
    }
}