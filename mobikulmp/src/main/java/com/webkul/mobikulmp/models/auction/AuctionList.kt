package com.webkul.mobikulmp.models.auction


import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.webkul.mobikulmp.BR
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AuctionList : BaseObservable() {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("minQty")
    @Expose
    var minQty: String? = null

    @SerializedName("maxQty")
    @Expose
    var maxQty: String? = null

    @SerializedName("stopTime")
    @Expose
    var stopTime: String? = null
        get() {
            val currentoffset = Date().timezoneOffset * -1 * 60000
            try {
                val df = SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US)
                val date = df.parse(field)
                val toDate = Date(date.time + currentoffset + -1000 * stopTimeOffset)
                return df.format(toDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return field
        }

    @SerializedName("thumbNail")
    @Expose
    var thumbNail: String? = null

    @SerializedName("auctionId")
    @Expose
    var auctionId: String? = null

    @SerializedName("startTime")
    @Expose
    var startTime: String? = null
        get() {
            val currentoffset = Date().timezoneOffset * -1 * 60000
            try {
                val df = SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US)
                val date = df.parse(field)
                val toDate = Date(date.time + currentoffset + -1000 * startTimeOffset)
                return df.format(toDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return field
        }

    @SerializedName("isLinkable")
    @Expose
    var isIsLinkable = false
        private set

    @SerializedName("reservePrice")
    @Expose
    var reservePrice: String? = null

    @SerializedName("autoBidStatus")
    @Expose
    var autoBidStatus: String? = null

    @SerializedName("auctionStatus")
    @Expose
    var auctionStatus: String? = null

    @SerializedName("isProductSold")
    @Expose
    var isProductSold: String? = null

    @SerializedName("startingPrice")
    @Expose
    var startingPrice: String? = null

    @SerializedName("incrementalStatus")
    @Expose
    var incrementalStatus: String? = null
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

    @SerializedName("startTimeOffset")
    @Expose
    private val startTimeOffset = 0

    @SerializedName("stopTimeOffset")
    @Expose
    private val stopTimeOffset = 0

    fun setIsLinkable(isLinkable: Boolean) {
        isIsLinkable = isLinkable
    }

}