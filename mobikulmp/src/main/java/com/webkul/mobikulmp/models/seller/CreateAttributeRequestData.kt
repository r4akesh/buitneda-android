package com.webkul.mobikulmp.models.seller

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.webkul.mobikulmp.BR
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.models.seller
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 23/5/19
 */

class CreateAttributeRequestData : BaseObservable() {

    var attributeCode: String = ""
    var attributeLabel: String = ""
    var catalogInputType = "select"
    var isValueRequired: Boolean = false
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.valueRequired)
        }
    val attributeOptionList = ArrayList<AttributeOptionItemData>()

    val attributeOptionData: JSONArray
        get() {
            val attributeOptionsData = JSONArray()

            try {
                for (noOfOptions in attributeOptionList.indices) {
                    val attributeOptionsDataObject = JSONObject()
                    attributeOptionsDataObject.put("admin", attributeOptionList[noOfOptions].admin)
                    attributeOptionsDataObject.put("store", attributeOptionList[noOfOptions].defaultStoreView)
                    attributeOptionsDataObject.put("position", attributeOptionList[noOfOptions].position)
                    attributeOptionsDataObject.put("isdefault", if (attributeOptionList[noOfOptions].isSetToDefault) "on" else "off")
                    attributeOptionsData.put(attributeOptionsDataObject)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return attributeOptionsData
        }
}