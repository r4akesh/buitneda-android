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
class SampleDatum() : BaseObservable(), Parcelable {

    @JsonProperty("sample_id")

    var sampleId: String? = null
        get() = if (field == null) {
            "0"
        } else field

    @JsonProperty("title")

    @get:Bindable
    var title: String? = ""
        set(title) {
            field = title
            notifyPropertyChanged(BR.title)
        }

    @JsonProperty("sample_url")

    var sampleUrl: String? = null

    @JsonProperty("sample_type")

    var sampleType: String? = "url"
        @Bindable get() = field ?: "url"
        set(value) {
            field = value
            notifyPropertyChanged(BR.sampleType)
        }

    @JsonProperty("sort_order")

    var sortOrder: String? = null

    @JsonProperty("store_title")

    var storeTitle: String? = null

    @JsonProperty("file_save")

    var fileSave: FileData? = null

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

    constructor(parcel: Parcel) : this() {
        sampleUrl = parcel.readString()
        sortOrder = parcel.readString()
        storeTitle = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(sampleUrl)
        parcel.writeString(sortOrder)
        parcel.writeString(storeTitle)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SampleDatum> {
        override fun createFromParcel(parcel: Parcel): SampleDatum {
            return SampleDatum(parcel)
        }

        override fun newArray(size: Int): Array<SampleDatum?> {
            return arrayOfNulls(size)
        }
    }

}