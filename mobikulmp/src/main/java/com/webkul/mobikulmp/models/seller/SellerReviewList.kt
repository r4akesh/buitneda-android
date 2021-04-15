package com.webkul.mobikulmp.models.seller


import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


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
@SuppressWarnings("unused")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
open class SellerReviewList protected constructor(`in`: Parcel) : Parcelable {

    fun SellerReviewList() {

    }

    constructor() : this(`in` = Parcel.obtain())

    @JsonProperty(value = "name")
    @JsonAlias("name", "userName")
    var name: String? = null

    @JsonProperty("summary")

    var summary: String? = null

    @JsonProperty("date")

    var date: String? = null

    @JsonProperty(value = "comment")
    @JsonAlias("comment", "description")

    var comment: String? = null

    @JsonProperty(value = "priceRating")
    @JsonAlias("priceRating", "feedPrice")

    private var priceRating: Int = 0

    @JsonProperty(value = "valueRating")
    @JsonAlias("valueRating", "feedValue")

    private var valueRating: Int = 0

    @JsonProperty(value = "qualityRating")
    @JsonAlias("qualityRating", "feedQuality")

    private var qualityRating: Int = 0

    val priceRatingString: String
        get() = (priceRating * 0.05).toString()

    val valueRatingString: String
        get() = (valueRating * 0.05).toString()

    val qualityRatingString: String
        get() = (qualityRating * 0.05).toString()

    init {
        name = `in`.readString()
        summary = `in`.readString()
        date = `in`.readString()
        comment = `in`.readString()
        priceRating = `in`.readInt()
        valueRating = `in`.readInt()
        qualityRating = `in`.readInt()
    }

    fun getPriceRating(): Int {
        return if (priceRating > 5) {
            priceRating * 5 / 100
        } else priceRating
    }

    fun setPriceRating(priceRating: Int) {
        this.priceRating = priceRating
    }

    fun getValueRating(): Int {
        return if (valueRating > 5) {
            valueRating * 5 / 100
        } else valueRating
    }

    fun setValueRating(valueRating: Int) {
        this.valueRating = valueRating
    }

    fun getQualityRating(): Int {
        return if (qualityRating > 5) {
            qualityRating * 5 / 100
        } else qualityRating
    }

    fun setQualityRating(qualityRating: Int) {
        this.qualityRating = qualityRating
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeString(summary)
        dest.writeString(date)
        dest.writeString(comment)
        dest.writeInt(priceRating)
        dest.writeInt(valueRating)
        dest.writeInt(qualityRating)
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<SellerReviewList> = object : Parcelable.Creator<SellerReviewList> {
            override fun createFromParcel(`in`: Parcel): SellerReviewList {
                return SellerReviewList(`in`)
            }

            override fun newArray(size: Int): Array<SellerReviewList?> {
                return arrayOfNulls(size)
            }
        }
    }
}
