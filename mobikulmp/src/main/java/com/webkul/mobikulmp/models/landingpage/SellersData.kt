package com.webkul.mobikulmp.models.landingpage


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.models.product.ProductTileData

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class SellersData {

    @JsonProperty("logo")

    var logo: String? = ""

    @JsonProperty("products")

    var products: List<ProductTileData>? = null

    @JsonProperty("sellerId")

    var sellerId: Int = 0

    @JsonProperty("shopTitle")

    var shopTitle: String? = ""

    @JsonProperty("productCount")

    var productCount: String? = ""


}
