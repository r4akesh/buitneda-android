package com.webkul.mobikulmp.models.seller


import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.BR

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class LinkDatum() : BaseObservable(), Parcelable {

    @JsonProperty("link_id")

    var linkId: String? = null
        get() = if (field == null) {
            "0"
        } else field

    @JsonProperty("title")

    @get:Bindable
    var title: String? = null
        set(title) {
            field = title
            notifyPropertyChanged(BR.title)
        }

    @JsonProperty("price")

    var price: String? = null

    @JsonProperty("link_url")

    var linkUrl: String? = null


    @JsonProperty("is_shareable")

    var shareable: Int = 0
        @Bindable get() = field
        set(shareable) {
            field = shareable
            notifyPropertyChanged(BR.shareable)
        }


    @JsonProperty("sample_file")

    var sampleFile: String? = null

    @JsonProperty("sample_url")

    var sampleUrl: String? = null


    @JsonProperty("link_type")

    var linkType: String? = "url"
        @Bindable get() = field ?: "url"
        set(value) {
            field = value
            notifyPropertyChanged(BR.linkType)
        }

    var fileTypeSelected: Int = 0
        @Bindable get() {
            if (linkType == "url") {
                field = 0
            } else {
                field = 1
            }
            return field
        }
        set(value) {
            field = value

            if (value == 0) {
                linkType = "url"
            } else {
                linkType = "file"
            }
            notifyPropertyChanged(BR.fileTypeSelected)
        }

    @JsonProperty("sample_type")

    var sampleType: String? = "url"
        @Bindable get() = field ?: "url"
        set(value) {
            field = value
            notifyPropertyChanged(BR.sampleType)
        }
    var sampleTypeSelected: Int = 0
        @Bindable get() {
            if (sampleType == "url") {
                field = 0
            } else {
                field = 1
            }
            return field
        }
        set(value) {
            field = value

            if (value == 0) {
                sampleType = "url"
            } else {
                sampleType = "file"
            }
            notifyPropertyChanged(BR.sampleTypeSelected)
        }


    @JsonProperty("sort_order")

    var sortOrder: String? = null

    @JsonProperty("number_of_downloads")

    var numberOfDownloads: String? = null

    @JsonProperty("store_title")

    var storeTitle: String? = null

    @JsonProperty("is_unlimited")

    @get:Bindable
    var unlimited: Boolean = false
        set(unlimited) {
            field = unlimited
            notifyPropertyChanged(BR.unlimited)
        }

    @JsonProperty("file_save")

    var fileSave: FileData? = null

    @JsonProperty("sample_file_save")

    var sampleFileSave: FileData? = null

    @get:Bindable
    var isExpended: Boolean = false
        set(expended) {
            field = expended
            notifyPropertyChanged(BR.expended)
        }

    @get:Bindable
    var isShowError: Boolean = false
        set(showError) {
            field = showError
            notifyPropertyChanged(BR.showError)
        }

    constructor(parcel: Parcel) : this() {
        price = parcel.readString()
        linkUrl = parcel.readString()
        sampleFile = parcel.readString()
        sampleUrl = parcel.readString()
        sortOrder = parcel.readString()
        numberOfDownloads = parcel.readString()
        storeTitle = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(price)
        parcel.writeString(linkUrl)
        parcel.writeString(sampleFile)
        parcel.writeString(sampleUrl)
        parcel.writeString(sortOrder)
        parcel.writeString(numberOfDownloads)
        parcel.writeString(storeTitle)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LinkDatum> {
        override fun createFromParcel(parcel: Parcel): LinkDatum {
            return LinkDatum(parcel)
        }

        override fun newArray(size: Int): Array<LinkDatum?> {
            return arrayOfNulls(size)
        }
    }

}