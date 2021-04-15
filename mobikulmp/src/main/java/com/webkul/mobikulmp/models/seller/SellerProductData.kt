package com.webkul.mobikulmp.models.seller


import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikulmp.BR

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.fragments
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 16/6/19
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class SellerProductData : BaseObservable() {

    @JsonProperty("productId")

    var productId: Int = 0

    @JsonProperty("image")

    var image: String? = ""

    @JsonProperty("openable")

    var openable: Boolean? = false

    @JsonProperty("productPrice")

    var productPrice: String? = ""

    @JsonProperty("name")

    var name: String? = ""

    @JsonProperty("productType")

    var productType: String? = ""

    @JsonProperty("sku")

    var sku: String? = ""

    @JsonProperty("specialPrice")

    var specialPrice: String? = ""

    @JsonProperty("specialFromDate")

    var specialFromDate: String? = ""

    @JsonProperty("taxClassId")

    var taxClassId: String? = ""

    @JsonProperty("categories")

    var categories: List<String> = ArrayList()

    @JsonProperty("specialToDate")

    var specialToDate: String? = ""

    @JsonProperty("status")

    var status: String? = ""

    @JsonProperty("qtySold")

    var qtySold: String? = ""

    @JsonProperty("qtyPending")

    var qtyPending: String? = ""

    @JsonProperty("qtyConfirmed")

    var qtyConfirmed: String? = ""

    @JsonProperty("earnedAmount")

    var earnedAmount: String? = ""


    var position = 0

    @get:Bindable
    var isSelectionModeOn = false
        set(selectionModeOn) {
            field = selectionModeOn
            notifyPropertyChanged(BR.selectionModeOn)
        }

    @get:Bindable
    var isSelected = false
        set(selected) {
            field = selected
            notifyPropertyChanged(BR.selected)
        }

    var visible: Boolean = false
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.visible)
        }

    fun getCategoriesInString(): String {
        var categoriesString = ""
        for (index in categories.indices) {
            if (index == categories.size - 1) {
                categoriesString += categories[index]
            } else {
                categoriesString += "${categories[index]},"
            }
        }
        return categoriesString
    }
}