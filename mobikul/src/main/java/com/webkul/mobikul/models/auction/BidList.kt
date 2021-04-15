package com.webkul.mobikul.models.auction

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class BidList() : Parcelable {

    @JsonProperty("bidDate")
    private var bidDate: String? = null

    @JsonProperty("bidAmount")
    private var bidAmount: String? = null

    @JsonProperty("bidderName")
    private var bidderName: String? = null

    constructor(parcel: Parcel) : this() {
        bidDate = parcel.readString()
        bidAmount = parcel.readString()
        bidderName = parcel.readString()
    }

    protected fun BidList(`in`: Parcel) {
        bidDate = `in`.readString()
        bidAmount = `in`.readString()
        bidderName = `in`.readString()
    }



    fun getBidDate(): String? {
        return bidDate
    }

    fun setBidDate(bidDate: String?) {
        this.bidDate = bidDate
    }

    fun getBidAmount(): String? {
        return bidAmount
    }

    fun setBidAmount(bidAmount: String?) {
        this.bidAmount = bidAmount
    }

    fun getBidderName(): String? {
        return bidderName
    }

    fun setBidderName(bidderName: String?) {
        this.bidderName = bidderName
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(bidDate)
        parcel.writeString(bidAmount)
        parcel.writeString(bidderName)
    }

    companion object CREATOR : Parcelable.Creator<BidList> {
        override fun createFromParcel(parcel: Parcel): BidList {
            return BidList(parcel)
        }

        override fun newArray(size: Int): Array<BidList?> {
            return arrayOfNulls(size)
        }
    }


}