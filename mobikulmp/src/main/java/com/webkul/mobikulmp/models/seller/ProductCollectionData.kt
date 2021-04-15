package com.webkul.mobikulmp.models.seller


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class ProductCollectionData {

    @JsonProperty("selected")

    var isSelected: Boolean = false

    @JsonProperty("entity_id")

    var entityId: String? = null

    @JsonProperty("thumbnail")

    var thumbnail: String? = null

    @JsonProperty("name")

    var name: String? = null

    @JsonProperty("attrinuteSet")

    var attrinuteSet: String? = null

    @JsonProperty("status")

    var status: String? = null

    @JsonProperty("type")

    var type: String? = null

    @JsonProperty("sku")

    var sku: String? = null

    @JsonProperty("price")

    var price: String? = null

}