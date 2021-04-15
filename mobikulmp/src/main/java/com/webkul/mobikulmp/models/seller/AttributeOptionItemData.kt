package com.webkul.mobikulmp.models.seller

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.webkul.mobikulmp.BR

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

class AttributeOptionItemData : BaseObservable() {
    var admin: String = ""
    var defaultStoreView: String = ""
    var position: String = ""
    var isSetToDefault: Boolean = false
    var isValidAdmin: Boolean = true
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.validAdmin)
        }
}