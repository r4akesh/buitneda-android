package com.webkul.mobikulmp.models.sellerinfo


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.models.product.ProductDetailsPageModel

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class ProductDetailsPageModelExtended : ProductDetailsPageModel() {

    @JsonProperty("sellerInfo")

    var sellerInfo: SellerInfo? = SellerInfo()

    @JsonProperty("reportData")

    var reportData: ReportData? = ReportData()


}