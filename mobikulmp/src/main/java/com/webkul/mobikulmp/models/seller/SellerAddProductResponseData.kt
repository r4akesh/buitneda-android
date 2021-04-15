package com.webkul.mobikulmp.models.seller


/**
 *
 * Webkul Software.
 *
 * @category Mobikul
 * @package com.webkul.mobikulmp.models
 * @author Webkul
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 10/6/19
 */

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.models.BaseModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class SellerAddProductResponseData() : BaseModel(), Parcelable {

    @JsonProperty("skuType")

    var skuType: String? = null

    @JsonProperty("skuhint")

    val skuhint: String? = null

    @JsonProperty("taxHint")

    val taxHint: String? = null

    @JsonProperty("showHint")

    val isShowHint: Boolean = false

    @JsonProperty("skuPrefix")

    val skuPrefix: String? = null

    @JsonProperty("priceHint")

    val priceHint: String? = null

    @JsonProperty("imageHint")

    val imageHint: String? = null

    @JsonProperty("taxOptions")

    var taxOptions: List<OptionsData>? = null

    @JsonProperty("categories")

    var categories: CategoriesData? = null

    @JsonProperty("assignedCategories")

    val assignedCategories: List<OptionsData>? = null

    @JsonProperty("weightHint")

    val weightHint: String? = null

    @JsonProperty("weightUnit")

    val weightUnit: String? = null

    @JsonProperty("productHint")

    val productHint: String? = null

    @JsonProperty("categoryHint")

    val categoryHint: String? = null

    @JsonProperty("allowedTypes")

    var allowedTypes: List<OptionsData>? = null

    @JsonProperty("inventoryHint")

    val inventoryHint: String? = null

    @JsonProperty("currencySymbol")

    val currencySymbol: String? = null

    @JsonProperty("descriptionHint")

    val descriptionHint: String? = null

    @JsonProperty("specialPriceHint")

    val specialPriceHint: String? = null

    @JsonProperty("visibilityOptions")

    val visibilityOptions: List<OptionsData>? = null

    @JsonProperty("allowedAttributes")

    val allowedAttributes: List<AllowedAttribute>? = ArrayList()

    @JsonProperty("specialEndDateHint")

    val specialEndDateHint: String? = null

    @JsonProperty("shortdescriptionHint")

    val shortdescriptionHint: String? = null

    @JsonProperty("specialStartDateHint")

    val specialStartDateHint: String? = null

    @JsonProperty("addProductLimitStatus")

    val isAddProductLimitStatus: Boolean = false

    @JsonProperty("isCategoryTreeAllowed")

    var isIsCategoryTreeAllowed: Boolean = false

    @JsonProperty("addUpsellProductStatus")

    val isAddUpsellProductStatus: Boolean = false

    @JsonProperty("addRelatedProductStatus")

    val isAddRelatedProductStatus: Boolean = false

    @JsonProperty("addCrosssellProductStatus")

    val isAddCrosssellProductStatus: Boolean = false

    @JsonProperty("inventoryAvailabilityHint")

    val inventoryAvailabilityHint: String? = null

    @JsonProperty("inventoryAvailabilityOptions")

    val inventoryAvailabilityOptions: List<OptionsData>? = ArrayList()

    @JsonProperty("productData")

    var productData: AddProductData? = AddProductData()

    @JsonProperty("imageRole")

    var imageRole: List<ImageRole>? = ArrayList()

    val selectedAttributeSet: Int
        get() {
            for (noOfAttribute in allowedAttributes!!.indices) {
                if (allowedAttributes[noOfAttribute].value == productData!!.attributeSetId) {
                    return noOfAttribute
                }
            }
            return 0
        }

    val selectedType: Int
        get() {
            for (noOfTypes in allowedTypes!!.indices) {
                if (allowedTypes!![noOfTypes].value == productData!!.type) {
                    return noOfTypes
                }
            }
            return 0
        }

    val selectedVisibility: Int
        get() {
            for (noOfVisibilityOptions in visibilityOptions!!.indices) {
                if (visibilityOptions[noOfVisibilityOptions].value == productData!!.visibility) {
                    return noOfVisibilityOptions + 1
                }
            }
            return 0
        }

    val selectedTaxClass: Int
        get() {
            for (noOfTaxClass in taxOptions!!.indices) {
                if (taxOptions!![noOfTaxClass].value == productData!!.taxClassId) {
                    return noOfTaxClass + 1
                }
            }
            return 0
        }

    val imagesData: Map<String, String>
        get() {
            val imagesData = HashMap<String, String>()
            if (productData != null && productData!!.mediaGallery != null && imageRole != null) {
                for (roleIndex in imageRole!!.indices) {
                    if (imageRole!![roleIndex].id == null || imageRole!![roleIndex].id!!.isEmpty()) {
                        imagesData[imageRole?.get(roleIndex)?.value!!] = ""
                    } else {
                        for (imageIndex in 0 until productData!!.mediaGallery.size) {
                            if (productData!!.mediaGallery[imageIndex].id.equals(imageRole!![roleIndex].id)) {
                                imagesData[imageRole!![roleIndex].value!!] = productData!!.mediaGallery[imageIndex].file
                                        ?: ""
                                break
                            }
                        }
                    }
                }
            }
            return imagesData
        }

    constructor(parcel: Parcel) : this() {
        skuType = parcel.readString()
        taxOptions = parcel.createTypedArrayList(OptionsData)
        categories = parcel.readParcelable(CategoriesData::class.java.classLoader)
        allowedTypes = parcel.createTypedArrayList(OptionsData)
        isIsCategoryTreeAllowed = parcel.readByte() != 0.toByte()
        productData = parcel.readParcelable(AddProductData::class.java.classLoader)
        imageRole = parcel.createTypedArrayList(ImageRole)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(skuType)
        parcel.writeTypedList(taxOptions)
        parcel.writeParcelable(categories, flags)
        parcel.writeTypedList(allowedTypes)
        parcel.writeByte(if (isIsCategoryTreeAllowed) 1 else 0)
        parcel.writeParcelable(productData, flags)
        parcel.writeTypedList(imageRole)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SellerAddProductResponseData> {
        override fun createFromParcel(parcel: Parcel): SellerAddProductResponseData {
            return SellerAddProductResponseData(parcel)
        }

        override fun newArray(size: Int): Array<SellerAddProductResponseData?> {
            return arrayOfNulls(size)
        }
    }
}