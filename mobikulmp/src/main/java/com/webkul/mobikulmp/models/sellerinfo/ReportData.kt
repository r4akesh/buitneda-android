package com.webkul.mobikulmp.models.sellerinfo

import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.BR
import com.webkul.mobikul.models.homepage.BannerImage


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class ReportData() : BaseObservable(),Parcelable{
    var name: String = ""
    var email: String = ""
    @JsonProperty("showReportProduct")
    @JsonAlias("showReportSeller")
    var showReportProduct: Boolean = false

    @JsonProperty("guestCanReport")

    var guestCanReport: Boolean = false

    @JsonProperty("showReportReason")

    var showReportReason: Boolean = false

    @JsonProperty("productReportLabel")
    @JsonAlias("sellerReportLabel")
    var productReportLabel: String? = ""

    @JsonProperty("showReportOtherReason")

    var showReportOtherReason: Boolean? = false

    @JsonProperty("productOtherReasonLabel")
    @JsonAlias("sellerOtherReportLabel")
    var productOtherReasonLabel: String? = ""

    @JsonProperty("productFlagReasons")
    @JsonAlias("sellerFlagReasons")
    var productFlagReasons: ArrayList<ProductFlagReasons>? =null

    var selectedReasonMethod: String = " "

    var selectedOtherReasonMethod: Boolean = false

    var flagMessage:String= ""
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.flagMessage)
        }


    constructor(parcel: Parcel) : this() {
        showReportProduct = parcel.readByte() != 0.toByte()
        guestCanReport = parcel.readByte() != 0.toByte()
        showReportReason = parcel.readByte() != 0.toByte()
        productReportLabel = parcel.readString()
        showReportOtherReason = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        productOtherReasonLabel = parcel.readString()
        productFlagReasons = parcel.createTypedArrayList(ProductFlagReasons.CREATOR)

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (showReportProduct) 1 else 0)
        parcel.writeByte(if (guestCanReport) 1 else 0)
        parcel.writeByte(if (showReportReason) 1 else 0)
        parcel.writeString(productReportLabel)
        parcel.writeValue(showReportOtherReason)
        parcel.writeString(productOtherReasonLabel)
        parcel.writeTypedList(productFlagReasons)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ReportData> {
        override fun createFromParcel(parcel: Parcel): ReportData {
            return ReportData(parcel)
        }

        override fun newArray(size: Int): Array<ReportData?> {
            return arrayOfNulls(size)
        }
    }
}