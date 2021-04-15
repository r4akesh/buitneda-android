package com.webkul.mobikulmp.models.seller


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.models.BaseModel
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class AddProductFieldProductCollectionData : BaseModel() {

    @JsonProperty("totalCount")

    var totalCount: Int = 0

    @JsonProperty("filterOption")

    var filterOption: ArrayList<FilterOption>? = ArrayList()

    @JsonProperty("productCollectionData")

    var productCollectionData: ArrayList<ProductCollectionData>? = ArrayList()
}