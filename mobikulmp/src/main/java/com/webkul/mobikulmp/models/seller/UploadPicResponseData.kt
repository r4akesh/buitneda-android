package com.webkul.mobikulmp.models.seller


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.webkul.mobikul.models.BaseModel

/**
 * Webkul Software.
 *
 * Java
 *
 * @author Webkul <support></support>@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class UploadPicResponseData : BaseModel() {

    @JsonProperty("name")

    val name: String? = null

    @JsonProperty("type")

    val type: String? = null

    @JsonProperty("error")

    val error: Int = 0

    @JsonProperty("size")

    val size: Int = 0

    @JsonProperty("file")

    val file: String? = null

    @JsonProperty("url")

    val url: String? = null
}