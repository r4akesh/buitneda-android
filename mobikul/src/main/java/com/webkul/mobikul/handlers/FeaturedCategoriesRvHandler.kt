package com.webkul.mobikul.handlers

import android.content.Intent
import com.webkul.mobikul.activities.BrandCatalogActivity
import com.webkul.mobikul.activities.CatalogActivity
import com.webkul.mobikul.activities.HomeActivity
import com.webkul.mobikul.fragments.HomeFragment
import com.webkul.mobikul.helpers.BundleKeysHelper
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_ID
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_TITLE
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_TYPE
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_TYPE_CATEGORY
import com.webkul.mobikul.helpers.MobikulApplication

/**
 * Webkul Software.
 *
 * Kotlin
 *
 * @author Webkul <support@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

class FeaturedCategoriesRvHandler(private val mContext: HomeFragment) {

    fun onClickCategory(name: String, id: String) {
        val intent = Intent(mContext.context, BrandCatalogActivity::class.java)
        intent.putExtra(BUNDLE_KEY_CATALOG_TYPE, BUNDLE_KEY_CATALOG_TYPE_CATEGORY)
        intent.putExtra(BUNDLE_KEY_CATALOG_TITLE, name)
        intent.putExtra(BUNDLE_KEY_CATALOG_ID, id)
        mContext.startActivity(intent)
    }

    fun onClickFeatureCategory(name: String, id: String) {
        val intent = Intent(mContext.context, CatalogActivity::class.java)
        intent.putExtra(BUNDLE_KEY_CATALOG_TYPE, BUNDLE_KEY_CATALOG_TYPE_CATEGORY)
        intent.putExtra(BUNDLE_KEY_CATALOG_TITLE, name)
        intent.putExtra(BUNDLE_KEY_CATALOG_ID, id)
        mContext.startActivity(intent)
    }

    fun onClickProduct(name: String, id: String , dominantColor:String){
        val intent = (mContext?.activity?.applicationContext as MobikulApplication).getProductDetailsActivity(mContext.context!!)
        intent.putExtra(BundleKeysHelper.BUNDLE_KEY_PRODUCT_DOMINANT_COLOR, dominantColor)
        intent.putExtra(BundleKeysHelper.BUNDLE_KEY_PRODUCT_NAME, name)
        intent.putExtra(BundleKeysHelper.BUNDLE_KEY_PRODUCT_ID, id)
        mContext.startActivity(intent)
    }
}