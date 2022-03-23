package com.webkul.mobikulmp.models


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.models.BaseModel

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class CustomerListResponse : BaseModel() {


    @JsonProperty("totalCount")
    var totalCount: Int = 0
    @JsonProperty("customerList")

    var customerList: ArrayList<CustomerList>? = ArrayList()

}