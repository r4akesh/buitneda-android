package com.webkul.mobikul.models.extra


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class AutoRelatedProduct {

    @JsonProperty("groupId")
    val groupId: String = ""

    @JsonProperty("groupMaxProducts")
    val groupMaxProducts: String = ""

    @JsonProperty("groupPostition")
    val groupPostition: String = ""

    @JsonProperty("groupPriority")
    val groupPriority: String = ""

    @JsonProperty("groupTitle")
    val groupTitle: String = ""

    @JsonProperty("page")
    val page: String = ""

    val products: ArrayList<Product> = ArrayList()
}