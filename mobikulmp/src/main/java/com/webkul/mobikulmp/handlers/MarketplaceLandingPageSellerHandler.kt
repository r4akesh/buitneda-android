package com.webkul.mobikulmp.handlers

import android.content.Context
import android.content.Intent
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_TITLE

import com.webkul.mobikulmp.activities.SellerProfileActivity
import com.webkul.mobikulmp.helpers.MarketplaceApplication
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_SELLER_ID
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_SELLER_TITLE

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.adapters
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 1/7/19
 */

class MarketplaceLandingPageSellerHandler(private val mContext: Context) {

    fun onClickViewSellerProfile(sellerId: Int, sellerTitle: String) {
        val intent = Intent(mContext, SellerProfileActivity::class.java)
        intent.putExtra(BUNDLE_KEY_SELLER_ID, sellerId)
        intent.putExtra(BUNDLE_KEY_SELLER_TITLE, sellerTitle)
        mContext.startActivity(intent)
    }

    fun onClickViewSellerCollection(sellerId: Int, storeName: String) {
        val intent = (mContext.applicationContext as MarketplaceApplication).getCatalogActivity(mContext)
        intent.putExtra(BUNDLE_KEY_SELLER_ID, sellerId)
        intent.putExtra(BUNDLE_KEY_CATALOG_TITLE, storeName)
        mContext.startActivity(intent)
    }
}
