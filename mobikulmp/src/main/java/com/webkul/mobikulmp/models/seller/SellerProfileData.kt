package com.webkul.mobikulmp.models.seller


import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.models.homepage.PriceFormat
import com.webkul.mobikul.models.product.ProductTileData
import com.webkul.mobikulmp.models.sellerinfo.ReportData
import java.util.*

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.models.seller
 * @copyright Copyright (c) 2010-2018 Webkul Software  Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 12/7/19
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class SellerProfileData() : BaseModel(), Parcelable {

    @JsonProperty("shopUrl")

    var shopUrl: String? = null

    @JsonProperty("vimeoId")

    var vimeoId: String? = null

    @JsonProperty("sellerId")

    var sellerId: Int = 0

    @JsonProperty("location")

    var location: String? = null

    @JsonProperty("shopTitle")

    var shopTitle: String? = ""

    @JsonProperty("twitterId")

    var twitterId: String? = null

    @JsonProperty("youtubeId")

    var youtubeId: String? = null

    @JsonProperty("facebookId")

    var facebookId: String? = null

    @JsonProperty("orderCount")

    var orderCount: Int = 0

    @JsonProperty("price5Star")

    var price5Star: Float = 0f

    @JsonProperty("price4Star")

    var price4Star: Float = 0f

    @JsonProperty("price3Star")

    var price3Star: Float = 0f

    @JsonProperty("price2Star")

    var price2Star: Float = 0f

    @JsonProperty("price1Star")

    var price1Star: Float = 0f

    @JsonProperty("value5Star")

    var value5Star: Float = 0f

    @JsonProperty("value4Star")

    var value4Star: Float = 0f

    @JsonProperty("value3Star")

    var value3Star: Float = 0f

    @JsonProperty("value2Star")

    var value2Star: Float = 0f

    @JsonProperty("value1Star")

    var value1Star: Float = 0f

    @JsonProperty("reviewList")

    var reviewList: List<SellerReviewList>? = null

    @JsonProperty("pinterestId")

    var pinterestId: String? = null

    @JsonProperty("bannerImage")

    var bannerImage: String? = null

    @JsonProperty("sellerBannerDomiinantColor")

    var sellerBannerDomiinantColor: String? = null

    @JsonProperty("instagramId")
    var instagramId: String? = null

    @JsonProperty("description")
    var description: String? = ""
        get() = field ?: ""

    @JsonProperty("productCount")

    var productCount: Int = 0

    @JsonProperty("googleplusId")

    var googleplusId: String? = null

    @JsonProperty("profileImage")

    var profileImage: String? = null

    @JsonProperty("sellerProfileDominantColor")

    var sellerProfileDominantColor: String? = ""

    @JsonProperty("returnPolicy")

    var returnPolicy: String? = ""

    @JsonProperty("shippingPolicy")

    var shippingPolicy: String? = ""
    var privacyPolicy: String? = ""

    @JsonProperty("quality5Star")

    var quality5Star: Float = 0f

    @JsonProperty("quality4Star")

    var quality4Star: Float = 0f

    @JsonProperty("quality3Star")

    var quality3Star: Float = 0f

    @JsonProperty("quality2Star")

    var quality2Star: Float = 0f

    @JsonProperty("quality1Star")

    var quality1Star: Float = 0f

    @JsonProperty("isVimeoActive")

    var isIsVimeoActive: Boolean = false

    @JsonProperty("averageRating")

    var averageRating: Double = 0.toDouble()

    @JsonProperty("feedbackCount")

    var feedbackCount: Int = 0

    @JsonProperty("isYoutubeActive")

    var isIsYoutubeActive: Boolean = false

    @JsonProperty("backgroundColor")

    var backgroundColor: String? = null
        get() = field ?: "#FFFFFF"

    @JsonProperty("isTwitterActive")

    var isIsTwitterActive: Boolean = false

    @JsonProperty("isFacebookActive")

    var isIsFacebookActive: Boolean = false

    @JsonProperty("isInstagramActive")

    var isIsInstagramActive: Boolean = false

    @JsonProperty("isPinterestActive")

    var isIsPinterestActive: Boolean = false

    @JsonProperty("averagePriceRating")

    var averagePriceRating: Double = 0.toDouble()

    @JsonProperty("isgoogleplusActive")

    var isIsgoogleplusActive: Boolean = false

    @JsonProperty("averageValueRating")

    var averageValueRating: Double = 0.toDouble()

    @JsonProperty("averageQualityRating")

    var averageQualityRating: Double = 0.toDouble()

    @JsonProperty("recentProductList")

    var recentProductList: ArrayList<ProductTileData> = ArrayList()

    @JsonProperty("reportData")

    var reportData: ReportData? = ReportData()


    val averageRatingValue: String
        get() = averageRating.toString()

    val averageRatingPercent: String
        get() = (averageRating / 0.05).toInt().toString()

    constructor(parcel: Parcel) : this() {
        shopUrl = parcel.readString()
        vimeoId = parcel.readString()
        sellerId = parcel.readInt()
        location = parcel.readString()
        shopTitle = parcel.readString()
        twitterId = parcel.readString()
        youtubeId = parcel.readString()
        facebookId = parcel.readString()
        orderCount = parcel.readInt()
        price5Star = parcel.readFloat()
        price4Star = parcel.readFloat()
        price3Star = parcel.readFloat()
        price2Star = parcel.readFloat()
        price1Star = parcel.readFloat()
        value5Star = parcel.readFloat()
        value4Star = parcel.readFloat()
        value3Star = parcel.readFloat()
        value2Star = parcel.readFloat()
        value1Star = parcel.readFloat()
        reviewList = parcel.createTypedArrayList(SellerReviewList.CREATOR)
        pinterestId = parcel.readString()
        bannerImage = parcel.readString()
        instagramId = parcel.readString()
        description = parcel.readString()
        productCount = parcel.readInt()
        googleplusId = parcel.readString()
        profileImage = parcel.readString()
        returnPolicy = parcel.readString()
        shippingPolicy = parcel.readString()
        privacyPolicy = parcel.readString()
        quality5Star = parcel.readFloat()
        quality4Star = parcel.readFloat()
        quality3Star = parcel.readFloat()
        quality2Star = parcel.readFloat()
        quality1Star = parcel.readFloat()
        isIsVimeoActive = parcel.readByte() != 0.toByte()
        feedbackCount = parcel.readInt()
        isIsYoutubeActive = parcel.readByte() != 0.toByte()
        backgroundColor = parcel.readString()
        isIsTwitterActive = parcel.readByte() != 0.toByte()
        isIsFacebookActive = parcel.readByte() != 0.toByte()
        isIsInstagramActive = parcel.readByte() != 0.toByte()
        isIsPinterestActive = parcel.readByte() != 0.toByte()
        averagePriceRating = parcel.readDouble()
        isIsgoogleplusActive = parcel.readByte() != 0.toByte()
        averageValueRating = parcel.readDouble()
        averageQualityRating = parcel.readDouble()
        reportData = parcel.readParcelable(ReportData::class.java.classLoader)

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(shopUrl)
        parcel.writeString(vimeoId)
        parcel.writeInt(sellerId)
        parcel.writeString(location)
        parcel.writeString(shopTitle)
        parcel.writeString(twitterId)
        parcel.writeString(youtubeId)
        parcel.writeString(facebookId)
        parcel.writeInt(orderCount)
        parcel.writeFloat(price5Star)
        parcel.writeFloat(price4Star)
        parcel.writeFloat(price3Star)
        parcel.writeFloat(price2Star)
        parcel.writeFloat(price1Star)
        parcel.writeFloat(value5Star)
        parcel.writeFloat(value4Star)
        parcel.writeFloat(value3Star)
        parcel.writeFloat(value2Star)
        parcel.writeFloat(value1Star)
        parcel.writeTypedList(reviewList)
        parcel.writeString(pinterestId)
        parcel.writeString(bannerImage)
        parcel.writeString(instagramId)
        parcel.writeString(description)
        parcel.writeInt(productCount)
        parcel.writeString(googleplusId)
        parcel.writeString(profileImage)
        parcel.writeString(returnPolicy)
        parcel.writeString(shippingPolicy)
        parcel.writeString(privacyPolicy)
        parcel.writeFloat(quality5Star)
        parcel.writeFloat(quality4Star)
        parcel.writeFloat(quality3Star)
        parcel.writeFloat(quality2Star)
        parcel.writeFloat(quality1Star)
        parcel.writeByte(if (isIsVimeoActive) 1 else 0)
        parcel.writeInt(feedbackCount)
        parcel.writeByte(if (isIsYoutubeActive) 1 else 0)
        parcel.writeString(backgroundColor)
        parcel.writeByte(if (isIsTwitterActive) 1 else 0)
        parcel.writeByte(if (isIsFacebookActive) 1 else 0)
        parcel.writeByte(if (isIsInstagramActive) 1 else 0)
        parcel.writeByte(if (isIsPinterestActive) 1 else 0)
        parcel.writeDouble(averagePriceRating)
        parcel.writeByte(if (isIsgoogleplusActive) 1 else 0)
        parcel.writeDouble(averageValueRating)
        parcel.writeDouble(averageQualityRating)
        parcel.writeParcelable(reportData, flags)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SellerProfileData> {
        override fun createFromParcel(parcel: Parcel): SellerProfileData {
            return SellerProfileData(parcel)
        }

        override fun newArray(size: Int): Array<SellerProfileData?> {
            return arrayOfNulls(size)
        }
    }
}