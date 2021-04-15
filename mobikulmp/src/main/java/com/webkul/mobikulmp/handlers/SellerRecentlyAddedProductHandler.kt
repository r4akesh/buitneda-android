package com.webkul.mobikulmp.handlers

import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_TITLE
import com.webkul.mobikulmp.fragments.SellerRecentlyAddedProductFragment
import com.webkul.mobikulmp.helpers.MarketplaceApplication
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_SELLER_ID
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_SELLER_TITLE

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.handlers
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 29/7/19
 */
class SellerRecentlyAddedProductHandler(val sellerRecentlyAddedProductFragment: SellerRecentlyAddedProductFragment) {

    fun onClickViewAllProduct() {
        val intent = (sellerRecentlyAddedProductFragment.context!!.applicationContext as MarketplaceApplication).getCatalogActivity(sellerRecentlyAddedProductFragment.context!!)
        intent.putExtra(BUNDLE_KEY_SELLER_ID, sellerRecentlyAddedProductFragment.arguments?.getInt(BUNDLE_KEY_SELLER_ID))
        intent.putExtra(BUNDLE_KEY_CATALOG_TITLE, sellerRecentlyAddedProductFragment.arguments?.getString(BUNDLE_KEY_SELLER_TITLE))
        sellerRecentlyAddedProductFragment.startActivity(intent)
    }
}
