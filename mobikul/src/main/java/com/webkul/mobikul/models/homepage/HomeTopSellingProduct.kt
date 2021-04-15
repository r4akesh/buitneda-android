package com.webkul.mobikul.models.homepage

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.models.BaseModel


/**
 * Webkul Software.
 *
 * Kotlin
 *
 * @author Webkul <support@webkul.com>
 *
 *
 *
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class HomeTopSellingProduct() : Parcelable ,BaseModel(){



    @JsonProperty("reviewCount")
    var reviewCount: Int? = null

    @JsonProperty("configurableData")
    val configurableData: ConfigurableData? = null

    @JsonProperty("isInWishlist")
    val isInWishlist: Boolean? = null

    @JsonProperty("wishlistItemId")
    val wishlistItemId: Int? = null

    @JsonProperty("typeId")
    val typeId: String? = null

    @JsonProperty("entityId")
    val entityId: String? = null

    @JsonProperty("rating")
    val rating: Int? = null

    @JsonProperty("isAvailable")
    val isAvailable: Boolean? = null

    @JsonProperty("price")
    val price: Double? = null

    @JsonProperty("finalPrice")
    val finalPrice: Double? = null

    @JsonProperty("formattedPrice")
    val formattedPrice: String? = null

    @JsonProperty("formattedFinalPrice")
    val formattedFinalPrice: String? = null

    @JsonProperty("name")
    val name: String? = null

    @JsonProperty("hasRequiredOptions")
    val hasRequiredOptions: Boolean? = null

    @JsonProperty("isNew")
    val isNew: Boolean? = null

    @JsonProperty("isInRange")
    val isInRange: Boolean? = null

    @JsonProperty("thumbNail")
    val thumbNail: String? = null

    @JsonProperty("dominantColor")
    val dominantColor: String? = null

    @JsonProperty("tierPrice")
    val tierPrice: String? = null

    @JsonProperty("formattedTierPrice")
    val formattedTierPrice: String? = null

    @JsonProperty("minAddToCartQty")
    val minAddToCartQty: Int? = null

    @JsonProperty("availability")
    val availability: String? = null

    @JsonProperty("arUrl")
    val arUrl: String? = null

    @JsonProperty("arType")
    val arType: String? = null

    @JsonProperty("arTextureImages")
    val arTextureImages: List<Any>? = null

    @JsonProperty("stop_auction_time")
    val stopAuctionTime: String? = null

    @JsonProperty("diff_timestamp")
    val diffTimestamp: Int? = null

    @JsonProperty("highest-bid-amount")
    val highestBidAmount: String? = null

    @JsonProperty("addToCartLabel")
    val addToCartLabel: String? = null

    @JsonProperty("openBidAmount")
    var openBidAmount: String? = null

    constructor(parcel: Parcel) : this() {
        reviewCount = parcel.readValue(Int::class.java.classLoader) as? Int
        openBidAmount = parcel.readString()
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeValue(reviewCount)
        dest?.writeString(openBidAmount)
    }


    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TopProducts> {
        override fun createFromParcel(parcel: Parcel): TopProducts {
            return TopProducts(parcel)
        }

        override fun newArray(size: Int): Array<TopProducts?> {
            return arrayOfNulls(size)
        }
    }


}