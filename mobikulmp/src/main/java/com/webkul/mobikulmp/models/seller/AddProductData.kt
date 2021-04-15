package com.webkul.mobikulmp.models.seller


import android.os.Parcel
import android.os.Parcelable
import android.view.View
import androidx.databinding.Bindable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikulmp.BR
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.SellerAddProductActivity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

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


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class AddProductData() : BaseModel(), Parcelable {

    @JsonProperty("type")

    var type: String? = null

    @JsonProperty("websiteIds")

    var websiteIds: List<String>? = null

    @JsonProperty("attributeSetId")

    var attributeSetId: String? = null

    @JsonProperty("categoryIds")

    var categoryIds: ArrayList<String>? = ArrayList()

    @JsonProperty("name")

    var name: String? = null

    @JsonProperty("urlKey")

    var urlKey: String? = null

    @JsonProperty("description")

    var description: String? = null

    @JsonProperty("shortDescription")

    var shortDescription: String? = null

    @JsonProperty("sku")

    var sku: String? = null

    @JsonProperty("price")

    var price: String ?= ""
        get() {
            try {
                field = (field?.toDouble()).toString()
            } catch (e: Exception) {
            }
            return field
        }

    @JsonProperty("specialPrice")

    var specialPrice: String ?= ""
        get() {
            try {
                field = (field?.toDouble()).toString()
            } catch (e: Exception) {
            }
            return field
        }

    @JsonProperty("specialFromDate")
    var specialFromDate: String? = null
        get() {
            try {
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(field)
                field = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return field
        }

    @JsonProperty("specialToDate")

    var specialToDate: String? = null
        get() {
            try {
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(field)
                field = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return field
        }

    @JsonProperty("qty")

    var qty: String? = null

    @JsonProperty("isInStock")

    var isIsInStock: Boolean = false

    @JsonProperty("visibility")

    var visibility: String? = null

    @JsonProperty("taxClassId")

    var taxClassId: String? = null

    @JsonProperty("productHasWeight")

    private var productHasWeight: Int = 0

    @JsonProperty("weight")

    var weight: String? = null

    @JsonProperty("metaTitle")

    var metaTitle: String? = null

    @JsonProperty("metaKeyword")

    var metaKeyword: String? = null

    @JsonProperty("metaDescription")

    var metaDescription: String? = null

    @JsonProperty("mpProductCartLimit")

    var mpProductCartLimit: String? = null

    @JsonProperty("baseImage")

    var baseImage: String? = null

    @JsonProperty("smallImage")

    var smallImage: String? = null

    @JsonProperty("swatchImage")

    var swatchImage: String? = null

    @JsonProperty("thumbnail")

    var thumbnail: String? = null

    @JsonProperty("mediaGallery")

    var mediaGallery: ArrayList<MediaGallery> = ArrayList()

    @JsonProperty("related")

    var related: ArrayList<String>? = null

    @JsonProperty("upsell")

    var upsell: ArrayList<String>? = null

    @JsonProperty("crossSell")

    var crossSell: ArrayList<String>? = null

    @JsonProperty("linkData")

    @get:Bindable
    var linkData: ArrayList<LinkDatum>? = ArrayList()
        set(linkData) {
            field = linkData
            notifyPropertyChanged(BR.linkData)
        }

    @JsonProperty("linksTitle")

    var linksTitle: String? = null

    @JsonProperty("sampleData")

    @get:Bindable
    var sampleData: ArrayList<SampleDatum>? = ArrayList()
        set(sampleData) {
            field = sampleData
            notifyPropertyChanged(BR.sampleData)
        }

    @JsonProperty("samplesTitle")

    var samplesTitle: String? = null

    @JsonProperty("purchasedSeparately")

    var purchasedSeparately: String? = null

    @JsonProperty("status")

    var status: Int = 1

    @JsonProperty("isFeaturedProduct")

    @get:Bindable
    var featuredProduct = 0
        set(featuredProduct) {
            field = featuredProduct
            notifyPropertyChanged(BR.featuredProduct)
        }
    var deleteImageId: String ?= ""

    @get:Bindable
    var isDownloadableProduct: Boolean = false
        set(downloadableProduct) {
            field = downloadableProduct
            if (downloadableProduct) {
                setProductHasWeight(false)
            }
            notifyPropertyChanged(BR.downloadableProduct)
        }

    val websiteJSONArray: JSONArray
        get() {
            val websiteIdsJSONArray = JSONArray()
            if (websiteIds != null) {
                for (noOfWebsites in websiteIds!!.indices) {
                    websiteIdsJSONArray.put(websiteIds!![noOfWebsites])
                }
            }
            return websiteIdsJSONArray
        }

    val categoryIdsJSONArray: JSONArray
        get() {
            val categoryIdsJSONArray = JSONArray()
            if (categoryIds != null) {
                for (noOfCategories in categoryIds!!.indices) {
                    categoryIdsJSONArray.put(categoryIds!![noOfCategories])
                }
            }
            return categoryIdsJSONArray
        }

    val specialFromDateForRequest: String?
        get() {
            try {
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(specialFromDate)
                return SimpleDateFormat("MM/dd/yyyy", Locale.US).format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return specialFromDate
        }

    val specialToDateForRequest: String?
        get() {
            try {
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(specialToDate)
                return SimpleDateFormat("MM/dd/yyyy", Locale.US).format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return specialToDate
        }
    val mediaGalleryJSONArray: JSONArray
        get() {
            val mediaGallery = JSONArray()
            try {
                for (noOfImages in this.mediaGallery!!.indices) {
                    val imagesDataObject = JSONObject()
                    imagesDataObject.put("position", noOfImages + 1)
                    imagesDataObject.put("file", this.mediaGallery!![noOfImages].file)
                    imagesDataObject.put("value_id", this.mediaGallery!![noOfImages].valueId)
                    imagesDataObject.put("label", this.mediaGallery!![noOfImages].label)
                    imagesDataObject.put("disabled", this.mediaGallery!![noOfImages].disabled)
                    imagesDataObject.put("removed", this.mediaGallery!![noOfImages].removed)
                    mediaGallery.put(imagesDataObject)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return mediaGallery
        }

    val relatedJSONArray: JSONArray
        get() {
            val relatedJSONArray = JSONArray()
            if (related != null) {
                for (noOfProducts in related!!.indices) {
                    relatedJSONArray.put(related!![noOfProducts])
                }
            }
            return relatedJSONArray
        }

    val upsellJSONArray: JSONArray
        get() {
            val upsellJSONArray = JSONArray()
            if (upsell != null) {
                for (noOfProducts in upsell!!.indices) {
                    upsellJSONArray.put(upsell!![noOfProducts])
                }
            }
            return upsellJSONArray
        }

    val crossSellJSONArray: JSONArray
        get() {
            val crossJSONArray = JSONArray()
            if (crossSell != null) {
                for (noOfProducts in crossSell!!.indices) {
                    crossJSONArray.put(crossSell!![noOfProducts])
                }
            }
            return crossJSONArray
        }

    var purchasedSeparatelyBoolean: Boolean
        get() = purchasedSeparately != null && purchasedSeparately == "1"
        set(purchasedSeparatelyBoolean) {
            purchasedSeparately = if (purchasedSeparatelyBoolean) "1" else "0"
        }

    val linksJsonData: JSONArray
        get() {
            val linksJsonData = JSONArray()
            for (linkIndex in this.linkData!!.indices) {
                try {
                    val eachLinkData = JSONObject()
                    eachLinkData.put("link_id", this.linkData!![linkIndex].linkId)
                    eachLinkData.put("sort_order", linkIndex + 1)
                    eachLinkData.put("is_delete", "")
                    eachLinkData.put("title", this.linkData!![linkIndex].title)
                    eachLinkData.put("price", this.linkData!![linkIndex].price)
                    eachLinkData.put("type", this.linkData!![linkIndex].linkType)

                    val fileJsonArray = JSONArray()
                    if (this.linkData!![linkIndex].fileSave != null) {
                        val fileData = JSONObject()
                        fileData.put("name", this.linkData!![linkIndex].fileSave?.name)
                        fileData.put("file", this.linkData!![linkIndex].fileSave?.file)
                        fileData.put("size", this.linkData!![linkIndex].fileSave?.size)
                        fileData.put("status", this.linkData!![linkIndex].fileSave?.status)
                        fileJsonArray.put(fileData)
                    }
                    eachLinkData.put("file", fileJsonArray.toString())
                    eachLinkData.put("link_url", this.linkData!![linkIndex].linkUrl)
                    val linkSampleData = JSONObject()
                    linkSampleData.put("type", this.linkData!![linkIndex].sampleType)

                    val sampleFileJsonArray = JSONArray()
                    if (this.linkData!![linkIndex].sampleFileSave != null) {
                        val sampleFileData = JSONObject()
                        sampleFileData.put("name", this.linkData!![linkIndex].sampleFileSave?.name)
                        sampleFileData.put("file", this.linkData!![linkIndex].sampleFileSave?.file)
                        sampleFileData.put("size", this.linkData!![linkIndex].sampleFileSave?.size)
                        sampleFileData.put("status", this.linkData!![linkIndex].sampleFileSave?.status)
                        sampleFileJsonArray.put(sampleFileData)
                    }
                    linkSampleData.put("file", sampleFileJsonArray.toString())
                    linkSampleData.put("link_url", this.linkData!![linkIndex].sampleUrl)
                    eachLinkData.put("sample", linkSampleData)
                    eachLinkData.put("is_shareable", this.linkData!![linkIndex].shareable)
                    eachLinkData.put("number_of_downloads", this.linkData!![linkIndex].numberOfDownloads)
                    eachLinkData.put("is_unlimited", if (this.linkData!![linkIndex].unlimited) "1" else "0")
                    linksJsonData.put(eachLinkData)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            return linksJsonData
        }

    val samplesJsonData: JSONArray
        get() {
            val samplesJsonData = JSONArray()
            for (sampleIndex in this.sampleData!!.indices) {
                try {
                    val eachSampleData = JSONObject()
                    eachSampleData.put("sample_id", this.sampleData!![sampleIndex].sampleId)
                    eachSampleData.put("sort_order", sampleIndex + 1)
                    eachSampleData.put("is_delete", "")
                    eachSampleData.put("title", this.sampleData!![sampleIndex].title)
                    eachSampleData.put("type", this.sampleData!![sampleIndex].sampleType)

                    val sampleFileJsonArray = JSONArray()
                    val sampleFileData = JSONObject()
                    if (this.sampleData!![sampleIndex].fileSave != null) {
                        sampleFileData.put("name", this.sampleData!![sampleIndex].fileSave?.name)
                        sampleFileData.put("file", this.sampleData!![sampleIndex].fileSave?.file)
                        sampleFileData.put("size", this.sampleData!![sampleIndex].fileSave?.size)
                        sampleFileData.put("status", this.sampleData!![sampleIndex].fileSave?.status)
                        sampleFileJsonArray.put(sampleFileData)
                    }
                    eachSampleData.put("file", sampleFileJsonArray.toString())
                    eachSampleData.put("sample_url", this.sampleData!![sampleIndex].sampleUrl)
                    samplesJsonData.put(eachSampleData)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            return samplesJsonData
        }

    constructor(parcel: Parcel) : this() {
        type = parcel.readString()
        websiteIds = parcel.createStringArrayList()
        attributeSetId = parcel.readString()
        name = parcel.readString()
        urlKey = parcel.readString()
        description = parcel.readString()
        shortDescription = parcel.readString()
        sku = parcel.readString()
        price = parcel.readString()
        specialPrice = parcel.readString()
        specialFromDate = parcel.readString()
        specialToDate = parcel.readString()
        qty = parcel.readString()
        isIsInStock = parcel.readByte() != 0.toByte()
        visibility = parcel.readString()
        taxClassId = parcel.readString()
        productHasWeight = parcel.readInt()
        weight = parcel.readString()
        metaTitle = parcel.readString()
        metaKeyword = parcel.readString()
        metaDescription = parcel.readString()
        mpProductCartLimit = parcel.readString()
        baseImage = parcel.readString()
        smallImage = parcel.readString()
        swatchImage = parcel.readString()
        thumbnail = parcel.readString()
        linksTitle = parcel.readString()
        samplesTitle = parcel.readString()
        purchasedSeparately = parcel.readString()
        status = parcel.readInt()
        deleteImageId = parcel.readString()
    }

    @Bindable
    fun getProductHasWeight(): Boolean {
        return productHasWeight == 1
    }

    fun setProductHasWeight(productWeight: Boolean) {
        this.productHasWeight = if (productWeight) 1 else 0
        if (productWeight) {
            isDownloadableProduct = false
        }
        notifyPropertyChanged(BR.productHasWeight)
    }

    fun isFormValidated(context: SellerAddProductActivity): Boolean {
        var isFormValidated = true

        if (this.isDownloadableProduct)
            isFormValidated = validateDownloadableData(context)

        if (productHasWeight == 1 && (weight == null || weight!!.trim { it <= ' ' }.isEmpty() || java.lang.Double.parseDouble(weight?:"") <= 0)) {
            context.mContentViewBinding.weightTil.error = context.getString(R.string.this_is_a_required_field)
            Utils.showShakeError(context, context.mContentViewBinding.weightTil)
            context.mContentViewBinding.weightEt.requestFocus()
            isFormValidated = false
        } else {
            context.mContentViewBinding.weightTil.isErrorEnabled = false
            context.mContentViewBinding.weightTil.error = null
        }

        if (visibility == null || visibility!!.trim { it <= ' ' }.isEmpty()) {
            Utils.showShakeError(context, context.mContentViewBinding.visibilitySp)
            context.mContentViewBinding.scrollView.smoothScrollBy(0, context.mContentViewBinding.visibilitySp.getBottom());
            Utils.setSpinnerError(context.mContentViewBinding.visibilitySp, context.resources.getString(R.string._please_select_))
            isFormValidated = false
        }

        if (qty == null || qty!!.trim { it <= ' ' }.isEmpty()) {
            context.mContentViewBinding.stockCountTil.error = context.getString(R.string.this_is_a_required_field)
            Utils.showShakeError(context, context.mContentViewBinding.stockCountTil)
            context.mContentViewBinding.stockCountEt.requestFocus()
            isFormValidated = false
        } else {
            context.mContentViewBinding.stockCountTil.isErrorEnabled = false
            context.mContentViewBinding.stockCountTil.error = null
        }

        if (price == null || price!!.trim { it <= ' ' }.isEmpty()) {
            context.mContentViewBinding.priceTil.error = context.getString(R.string.this_is_a_required_field)
            Utils.showShakeError(context, context.mContentViewBinding.priceTil)
            context.mContentViewBinding.priceEt.requestFocus()
            isFormValidated = false
        } else {
            context.mContentViewBinding.priceTil.isErrorEnabled = false
            context.mContentViewBinding.priceTil.error = null
        }

        if (context.mContentViewBinding.skuTil.visibility == View.VISIBLE && (sku == null || sku!!.trim { it <= ' ' }.isEmpty())) {
            context.mContentViewBinding.skuTil.error = context.getString(R.string.this_is_a_required_field)
            Utils.showShakeError(context, context.mContentViewBinding.skuTil)
            context.mContentViewBinding.skuEt.requestFocus()
            isFormValidated = false
        } else {
            context.mContentViewBinding.skuTil.isErrorEnabled = false
            context.mContentViewBinding.skuTil.error = null
        }

        if (description == null || description!!.trim { it <= ' ' }.isEmpty()) {
            context.mContentViewBinding.descriptionError.error = context.getString(R.string.this_is_a_required_field)
            Utils.showShakeError(context, context.mContentViewBinding.descriptionLayout)
            context.mContentViewBinding.descriptionError.visibility =View.VISIBLE
            context.mContentViewBinding.descriptionLayout.requestFocus()
            isFormValidated = false
        } else {
            context.mContentViewBinding.descriptionError.visibility =View.GONE
            context.mContentViewBinding.descriptionError.error = null
        }

        if (name == null || name!!.trim { it <= ' ' }.isEmpty()) {
            context.mContentViewBinding.productNameTil.error = context.getString(R.string.this_is_a_required_field)
            Utils.showShakeError(context, context.mContentViewBinding.productNameTil)
            context.mContentViewBinding.productNameEt.requestFocus()
            isFormValidated = false
        } else {
            context.mContentViewBinding.productNameTil.isErrorEnabled = false
            context.mContentViewBinding.productNameTil.error = null
        }
        return isFormValidated
    }

    private fun validateDownloadableData(context: SellerAddProductActivity): Boolean {
        var isDownloadableFormValidated = true
        if (this.linkData != null && this.sampleData != null) {
            for (sampleIndex in this.sampleData!!.indices) {
                if (this.sampleData!![sampleIndex].title == null || this.sampleData!![sampleIndex].title!!.trim { it <= ' ' }.isEmpty()) {
                    isDownloadableFormValidated = false
                    this.sampleData!![sampleIndex].isShowError = true
                    this.sampleData!![sampleIndex].isExpended = true
                }
            }
            for (linkIndex in this.linkData!!.indices) {
                if (this.linkData!![linkIndex].title == null || this.linkData!![linkIndex].title!!.trim { it <= ' ' }.isEmpty()) {
                    isDownloadableFormValidated = false
                    this.linkData!![linkIndex].isShowError = true
                    this.linkData!![linkIndex].isExpended = true
                }
            }
        }
        return isDownloadableFormValidated
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeStringList(websiteIds)
        parcel.writeString(attributeSetId)
        parcel.writeString(name)
        parcel.writeString(urlKey)
        parcel.writeString(description)
        parcel.writeString(shortDescription)
        parcel.writeString(sku)
        parcel.writeString(price)
        parcel.writeString(specialPrice)
        parcel.writeString(specialFromDate)
        parcel.writeString(specialToDate)
        parcel.writeString(qty)
        parcel.writeByte(if (isIsInStock) 1 else 0)
        parcel.writeString(visibility)
        parcel.writeString(taxClassId)
        parcel.writeInt(productHasWeight)
        parcel.writeString(weight)
        parcel.writeString(metaTitle)
        parcel.writeString(metaKeyword)
        parcel.writeString(metaDescription)
        parcel.writeString(mpProductCartLimit)
        parcel.writeString(baseImage)
        parcel.writeString(smallImage)
        parcel.writeString(swatchImage)
        parcel.writeString(thumbnail)
        parcel.writeString(linksTitle)
        parcel.writeString(samplesTitle)
        parcel.writeString(purchasedSeparately)
        parcel.writeInt(status)
        parcel.writeString(deleteImageId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddProductData> {
        override fun createFromParcel(parcel: Parcel): AddProductData {
            return AddProductData(parcel)
        }

        override fun newArray(size: Int): Array<AddProductData?> {
            return arrayOfNulls(size)
        }
    }
}