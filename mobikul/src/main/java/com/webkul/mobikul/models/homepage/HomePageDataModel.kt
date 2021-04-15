package com.webkul.mobikul.models.homepage


import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.models.Brandlist
import com.webkul.mobikul.models.SortOrder


/**
 * Webkul Software.
 *
 * Kotlin
 *
 * @author Webkul <support@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2018 Webkul Software  Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class HomePageDataModel() : BaseModel(), Parcelable {

    @JsonProperty("storeId")
    var storeId: String? = ""

    @JsonProperty("websiteId")
    var websiteId: String? = ""

    @JsonProperty("totalCount")

    val totalCount: Int = 0
    @JsonProperty("allowedCurrencies")
    var allowedCurrencies: ArrayList<Currency>? = ArrayList()

    @JsonProperty("defaultCurrency")
    var defaultCurrency: String? = ""

    @JsonProperty("brandlist")
    var brandlist: List<Brandlist>? = null

    @JsonProperty("priceFormat")
    var priceFormat: PriceFormat? = PriceFormat()

    @JsonProperty("categories")
    var categories: ArrayList<Category>? = ArrayList()

    @JsonProperty("wishlistEnable")

    var wishlistEnable: Boolean = false

    @JsonProperty("featuredCategories")
    var featuredCategories: ArrayList<FeaturedCategory>? = ArrayList()

    @JsonProperty("bannerImages")

    var bannerImages: ArrayList<BannerImage>? = ArrayList()

    @JsonProperty("carousel")

    var carousel: ArrayList<Carousel>? = ArrayList()

    @JsonProperty("websiteData")

    var websiteData: ArrayList<WebsiteData>? = ArrayList()

    @JsonProperty("storeData")

    var storeData: ArrayList<StoreData>? = ArrayList()

    @JsonProperty("cmsData")

    var cmsData: ArrayList<CmsData>? = ArrayList()

    @JsonProperty("customerName")

    var customerName: String? = ""

    @JsonProperty("customerEmail")

    var customerEmail: String? = ""

    @JsonProperty("productId")

    var productId: String? = ""

    @JsonProperty("productName")

    var productName: String? = ""

    @JsonProperty("categoryId")

    var categoryId: String? = ""

    @JsonProperty("categoryName")

    var categoryName: String? = ""

    @JsonProperty("themeType")
    var themeType: Int = 0

    @JsonProperty("topSellingProducts")
    var topSellingProducts: ArrayList<HomeTopSellingProduct>? = ArrayList()

    @JsonProperty("auctionProductList")
    var auctionProductList: ArrayList<String>? = ArrayList()

    @JsonProperty("singleBannerDetails")
    var singleBannerDetails: SingleBannerDetails? = null

    @JsonProperty("bigBannerFirst")
    var bigBannerFirst: ArrayList<BigBannerFirst>? = null

    @JsonProperty("bigBannerSecond")
    var bigBannerSecond: ArrayList<BigBannerSecond>? = null


    @JsonProperty("sort_order")
    var sort_order: ArrayList<SortOrder>? = ArrayList()

    /*---#MobikulMp--*/
    @JsonProperty("isSeller")
    var isSeller: Boolean = false

    @JsonProperty("isPending")
    var isPending: Boolean = false

    @JsonProperty("isAdmin")
    var isAdmin: Boolean = false

    constructor(parcel: Parcel) : this() {
        storeId = parcel.readString()
        websiteId = parcel.readString()
        allowedCurrencies = parcel.createTypedArrayList(Currency.CREATOR)
        defaultCurrency = parcel.readString()
        priceFormat = parcel.readParcelable(PriceFormat::class.java.classLoader)
        categories = parcel.createTypedArrayList(Category.CREATOR)
        wishlistEnable = parcel.readByte() != 0.toByte()
        featuredCategories = parcel.createTypedArrayList(FeaturedCategory.CREATOR)
        bannerImages = parcel.createTypedArrayList(BannerImage.CREATOR)
        carousel = parcel.createTypedArrayList(Carousel.CREATOR)
        websiteData = parcel.createTypedArrayList(WebsiteData.CREATOR)
        storeData = parcel.createTypedArrayList(StoreData.CREATOR)
        cmsData = parcel.createTypedArrayList(CmsData.CREATOR)
        customerName = parcel.readString()
        customerEmail = parcel.readString()
        productId = parcel.readString()
        productName = parcel.readString()
        categoryId = parcel.readString()
        categoryName = parcel.readString()
        themeType = parcel.readInt()
        sort_order = parcel.createTypedArrayList(SortOrder.CREATOR)
        isSeller = parcel.readByte() != 0.toByte()
        isPending = parcel.readByte() != 0.toByte()
        isAdmin = parcel.readByte() != 0.toByte()
        brandlist = parcel.createTypedArrayList(Brandlist.CREATOR)

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(storeId)
        parcel.writeString(websiteId)
        parcel.writeTypedList(allowedCurrencies)
        parcel.writeString(defaultCurrency)
        parcel.writeParcelable(priceFormat, flags)
        parcel.writeTypedList(categories)
        parcel.writeByte(if (wishlistEnable) 1 else 0)
        parcel.writeTypedList(featuredCategories)
        parcel.writeTypedList(bannerImages)
        parcel.writeTypedList(carousel)
        parcel.writeTypedList(websiteData)
        parcel.writeTypedList(storeData)
        parcel.writeTypedList(cmsData)
        parcel.writeString(customerName)
        parcel.writeString(customerEmail)
        parcel.writeString(productId)
        parcel.writeString(productName)
        parcel.writeString(categoryId)
        parcel.writeString(categoryName)
        parcel.writeInt(themeType)
        parcel.writeTypedList(sort_order)
        parcel.writeByte(if (isSeller) 1 else 0)
        parcel.writeByte(if (isPending) 1 else 0)
        parcel.writeByte(if (isAdmin) 1 else 0)
        parcel.writeTypedList(brandlist)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HomePageDataModel> {
        override fun createFromParcel(parcel: Parcel): HomePageDataModel {
            return HomePageDataModel(parcel)
        }

        override fun newArray(size: Int): Array<HomePageDataModel?> {
            return arrayOfNulls(size)
        }
    }
}