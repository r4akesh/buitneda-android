package com.webkul.mobikul.models.extra

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class Product {
    val addToCartLabel: String = ""
    val arTextureImages: List<Any> = ArrayList()
    val arType: String = ""
    val arUrl: String = ""
    val availability: String = ""

    //        val configurableData: ConfigurableData,
    val diff_timestamp: Int = 0
    val dominantColor: String = ""
    val entityId: String = ""
    val finalPrice: Double = 0.0
    val formattedFinalPrice: String = ""
    val formattedPrice: String = ""
    val formattedTierPrice: String = ""
    val hasRequiredOptions: Boolean = false
    val isAvailable: Boolean = true
    val isInRange: Boolean = true
    val isInWishlist: Boolean = true
    val isNew: Boolean = true
    val minAddToCartQty: Int = 0
    val name: String = ""
    val openBidAmount: String = ""
    val price: Double = 0.0
    val rating: Int = 0
    val reviewCount: Int = 0
    val stop_auction_time: String = ""
    val thumbNail: String = ""
    val tierPrice: String = ""
    val typeId: String = ""
    val wishlistItemId: Int = 0
}