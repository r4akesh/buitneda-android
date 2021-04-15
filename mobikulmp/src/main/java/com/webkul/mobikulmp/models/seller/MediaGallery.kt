package com.webkul.mobikulmp.models.seller


import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class MediaGallery : Parcelable {
    @JsonProperty("value_id")

    var valueId: String? = null

    @JsonProperty("file")

    var file: String? = null

    @JsonProperty("media_type")

    var mediaType: String? = null

    @JsonProperty("entity_id")

    var entityId: String? = null

    @JsonProperty("label")

    var label: String? = null

    @JsonProperty("position")

    var position: Int = 0

    @JsonProperty("disabled")

    var disabled: String? = "0"

    @JsonProperty("label_default")

    var labelDefault: String? = null

    @JsonProperty("position_default")

    var positionDefault: String? = null

    @JsonProperty("disabled_default")

    var disabledDefault: String? = null

    @JsonProperty("video_provider")

    var videoProvider: String? = null

    @JsonProperty("video_url")

    var videoUrl: String? = null

    @JsonProperty("video_title")

    var videoTitle: String? = null

    @JsonProperty("video_description")

    var videoDescription: String? = null

    @JsonProperty("video_metadata")

    var videoMetadata: String? = null

    @JsonProperty("video_provider_default")

    var videoProviderDefault: String? = null

    @JsonProperty("video_url_default")

    var videoUrlDefault: String? = null

    @JsonProperty("video_title_default")

    var videoTitleDefault: String? = null

    @JsonProperty("video_description_default")

    var videoDescriptionDefault: String? = null

    @JsonProperty("video_metadata_default")

    var videoMetadataDefault: String? = null

    @JsonProperty("url")

    var url: String? = null

    @JsonProperty("id")

    var id: String? = null

    @JsonProperty("path")

    var path: String? = null
    var baseImagePosition = 0
    var removed = 0


    var adapterPosition = 0

    var disabledBool: Boolean
        get() = disabled != null && disabled == "1"
        set(disabled) = if (disabled) {
            this.disabled = "1"
        } else {
            this.disabled = "0"
        }

    constructor() {

    }

    protected constructor(`in`: Parcel) {
        valueId = `in`.readString()
        file = `in`.readString()
        mediaType = `in`.readString()
        entityId = `in`.readString()
        label = `in`.readString()
        position = `in`.readInt()
        disabled = `in`.readString()
        labelDefault = `in`.readString()
        positionDefault = `in`.readString()
        disabledDefault = `in`.readString()
        videoProvider = `in`.readString()
        videoUrl = `in`.readString()
        videoTitle = `in`.readString()
        videoDescription = `in`.readString()
        videoMetadata = `in`.readString()
        videoProviderDefault = `in`.readString()
        videoUrlDefault = `in`.readString()
        videoTitleDefault = `in`.readString()
        videoDescriptionDefault = `in`.readString()
        videoMetadataDefault = `in`.readString()
        url = `in`.readString()
        id = `in`.readString()
        path = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(valueId)
        dest.writeString(file)
        dest.writeString(mediaType)
        dest.writeString(entityId)
        dest.writeString(label)
        dest.writeInt(position)
        dest.writeString(disabled)
        dest.writeString(labelDefault)
        dest.writeString(positionDefault)
        dest.writeString(disabledDefault)
        dest.writeString(videoProvider)
        dest.writeString(videoUrl)
        dest.writeString(videoTitle)
        dest.writeString(videoDescription)
        dest.writeString(videoMetadata)
        dest.writeString(videoProviderDefault)
        dest.writeString(videoUrlDefault)
        dest.writeString(videoTitleDefault)
        dest.writeString(videoDescriptionDefault)
        dest.writeString(videoMetadataDefault)
        dest.writeString(url)
        dest.writeString(id)
        dest.writeString(path)
    }

    companion object {
        val CREATOR: Parcelable.Creator<MediaGallery> = object : Parcelable.Creator<MediaGallery> {
            override fun createFromParcel(`in`: Parcel): MediaGallery {
                return MediaGallery(`in`)
            }

            override fun newArray(size: Int): Array<MediaGallery?> {
                return arrayOfNulls(size)
            }
        }
    }
}