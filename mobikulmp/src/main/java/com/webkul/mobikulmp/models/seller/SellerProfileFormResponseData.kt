package com.webkul.mobikulmp.models.seller


import androidx.databinding.Bindable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikulmp.BR
import java.util.*

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.models.seller
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 1/6/19
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class SellerProfileFormResponseData : BaseModel() {

    @JsonProperty("taxvat")

    var taxvat: String? = ""

    @JsonProperty("vimeoId")

    var vimeoId: String? = ""

    @JsonProperty("country")

    var country: String? = ""

    @JsonProperty("twitterId")

    var twitterId: String? = ""

    @JsonProperty("youtubeId")

    var youtubeId: String? = ""

    @JsonProperty("shopTitle")

    var shopTitle: String? = ""

    @JsonProperty("facebookId")

    var facebookId: String? = ""

    @JsonProperty("taxvatHint")

    var taxvatHint: String? = ""

    @JsonProperty("bannerHint")

    var bannerHint: String? = ""

    @JsonProperty("instagramId")

    var instagramId: String? = ""

    @JsonProperty("countryList")

    var countryList: ArrayList<CountryList>? = ArrayList()

    @JsonProperty("pinterestId")

    var pinterestId: String? = ""

    @JsonProperty("twitterHint")

    var twitterHint: String? = ""

    @JsonProperty("metaKeyword")

    var metaKeyword: String? = ""

    @JsonProperty("bannerImage")

    @get:Bindable
    var bannerImage: String? = ""
        set(bannerImage) {
            field = bannerImage
            notifyPropertyChanged(BR.bannerImage)
        }

    @JsonProperty("countryHint")

    var countryHint: String? = ""

    @JsonProperty("flagImageUrl")

    var flagImageUrl: String? = ""

    @JsonProperty("facebookHint")

    var facebookHint: String? = ""

    @JsonProperty("googleplusId")

    var googleplusId: String? = ""

    @JsonProperty("profileImage")

    @get:Bindable
    var profileImage: String? = ""
        set(profileImage) {
            field = profileImage
            notifyPropertyChanged(BR.profileImage)
        }

    @JsonProperty("contactNumber")

    var contactNumber: String? = ""

    @JsonProperty("shopTitleHint")

    var shopTitleHint: String? = ""

    @JsonProperty("paymentDetails")

    var paymentDetails: String? = ""

    @JsonProperty("shippingPolicy")

    var shippingPolicy: String? = ""

    @JsonProperty("returnPolicy")

    var returnPolicy: String? = ""

    @JsonProperty("privacyPolicy")

    var privacyPolicy: String? = ""

    @JsonProperty("companyLocality")

    var companyLocality: String? = ""

    @JsonProperty("showProfileHint")

    var isShowProfileHint: Boolean = false

    @JsonProperty("backgroundColor")

    @get:Bindable
    var backgroundColor: String? = "#FFFFFF"
        set(backgroundColor) {
            field = backgroundColor
            notifyPropertyChanged(BR.backgroundColor)
        }

    @JsonProperty("metaDescription")

    var metaDescription: String? = ""

    @JsonProperty("metaKeywordHint")

    var metaKeywordHint: String? = ""

    @JsonProperty("returnPolicyHint")

    var returnPolicyHint: String? = ""

    @JsonProperty("isYoutubeActive")

    var isYoutubeActive: Boolean = false
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.youtubeActive)
        }

    @JsonProperty("isTwitterActive")

    var isTwitterActive: Boolean = false
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.twitterActive)
        }

    @JsonProperty("isFacebookActive")

    var isFacebookActive: Boolean = false
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.facebookActive)
        }


    @JsonProperty("isInstagramActive")

    var isInstagramActive: Boolean = false
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.instagramActive)
        }

    @JsonProperty("isPinterestActive")

    var isPinterestActive: Boolean = false
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.pinterestActive)
        }

    @JsonProperty("isVimeoActive")

    var isVimeoActive: Boolean = false
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.vimeoActive)
        }

    @JsonProperty("isgoogleplusActive")

    var isGoogleplusActive: Boolean = false
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.googleplusActive)
        }

    @JsonProperty("profileImageHint")

    var profileImageHint: String? = ""

    @JsonProperty("contactNumberHint")

    var contactNumberHint: String? = ""

    @JsonProperty("companyDescription")

    var companyDescription: String? = ""

    @JsonProperty("shippingPolicyHint")

    var shippingPolicyHint: String? = ""

    @JsonProperty("paymentDetailsHint")

    var paymentDetailsHint: String? = ""

    @JsonProperty("backgroundColorHint")

    var backgroundColorHint: String? = ""

    @JsonProperty("metaDescriptionHint")

    var metaDescriptionHint: String? = ""

    @JsonProperty("companyLocalityHint")

    var companyLocalityHint: String? = ""

    @JsonProperty("companyDescriptionHint")

    var companyDescriptionHint: String? = ""

    val selectedCountryPositionFromAddressData: Int
        get() {
            for (countryPosition in countryList!!.indices) {
                if (countryList!![countryPosition].value == country) {
                    return countryPosition
                }
            }
            return 0
        }

}