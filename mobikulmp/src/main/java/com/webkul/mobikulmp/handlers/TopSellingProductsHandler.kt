package com.webkul.mobikulmp.handlers

import android.view.View
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_DOMINANT_COLOR
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_ID
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_IMAGE
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_NAME
import com.webkul.mobikul.helpers.FirebaseAnalyticsHelper
import com.webkul.mobikul.helpers.MobikulApplication


/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.fragments
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 16/6/19
 */

class TopSellingProductsHandler {

    fun onClickItem(view: View, productId: String, productName: String) {
        val intent = (view.context.applicationContext as MobikulApplication).getProductDetailsActivity(view.context)
        intent.putExtra(BUNDLE_KEY_PRODUCT_ID, productId)
        intent.putExtra(BUNDLE_KEY_PRODUCT_NAME, productName)
        intent.putExtra(BUNDLE_KEY_PRODUCT_IMAGE, "")
        intent.putExtra(BUNDLE_KEY_PRODUCT_DOMINANT_COLOR, "")

        view.context.startActivity(intent)
    }
}
