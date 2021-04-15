package com.webkul.mobikulmp.handlers

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.fragments
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 28/6/19
 */

import android.content.Context
import com.webkul.mobikul.helpers.BundleKeysHelper
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_NAME
import com.webkul.mobikul.helpers.MobikulApplication
import com.webkul.mobikulmp.models.seller.SellerOrderProductList


/**
 * Handler class used to handler click events coming from seller order item from seller order list especially
 * click actions from product name
 */
class SellerOrderProductHandler(private val mContext: Context, private val mData: SellerOrderProductList) {

    fun viewProduct() {
        if (mData.productId != "0") {
            val intent = (mContext.applicationContext as MobikulApplication).getProductDetailsActivity(mContext)
            intent.putExtra(BundleKeysHelper.BUNDLE_KEY_PRODUCT_ID, mData.productId)
            intent.putExtra(BUNDLE_KEY_PRODUCT_NAME, mData.name)
            mContext.startActivity(intent)
        }
    }
}