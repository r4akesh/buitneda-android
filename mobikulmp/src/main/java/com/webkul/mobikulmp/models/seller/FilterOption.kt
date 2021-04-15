package com.webkul.mobikulmp.models.seller


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class FilterOption {

    @JsonProperty("name")

    val name: String? = null

    @JsonProperty("id")

    val id: String? = null

    @JsonProperty("type")

    val type: String? = null

    @JsonProperty("options")

    val options: List<OptionsData>? = null

    var valueFrom: String? = null
    var valueTo: String? = null

    val selectedOptionData: Int
        get() {
            if (valueFrom != null && !valueFrom!!.isEmpty()) {
                for (noOfOptions in options!!.indices) {
                    if (options[noOfOptions].value == valueFrom)
                        return noOfOptions + 1
                }
            }
            return 0
        }
}