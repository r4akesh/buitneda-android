package com.webkul.mobikulmp.handlers

import android.content.Context
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_ID
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_NAME
import com.webkul.mobikul.helpers.MobikulApplication

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.models.seller
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 1/7/19
 */
class MarketplaceLandingPageProductHandler(private val mContext: Context) {

    fun onClickViewProduct(productId: String, productName: String) {
        val intent = (mContext.applicationContext as MobikulApplication).getProductDetailsActivity(mContext)
        intent.putExtra(BUNDLE_KEY_PRODUCT_ID, productId)
        intent.putExtra(BUNDLE_KEY_PRODUCT_NAME, productName)
        mContext.startActivity(intent)
    }
}
