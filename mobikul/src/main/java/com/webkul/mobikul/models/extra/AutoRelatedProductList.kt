package com.webkul.mobikul.models.extra

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.models.BaseModel

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class AutoRelatedProductList : BaseModel() {
    @JsonProperty("autoRelatedProducts")
    val autoRelatedProducts: ArrayList<AutoRelatedProduct>  = ArrayList()

}
