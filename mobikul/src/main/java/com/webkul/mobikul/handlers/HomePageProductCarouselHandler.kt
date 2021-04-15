package com.webkul.mobikul.handlers

import android.content.Intent
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.activities.CatalogActivity
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_ID
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_TITLE
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_TYPE
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_TYPE_CUSTOM_CAROUSEL

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

class HomePageProductCarouselHandler(private val mContext: BaseActivity) {

    fun onClickViewAllBtn(name: String, id: String) {
        val intent = Intent(mContext, CatalogActivity::class.java)
        intent.putExtra(BUNDLE_KEY_CATALOG_TYPE, BUNDLE_KEY_CATALOG_TYPE_CUSTOM_CAROUSEL)
        intent.putExtra(BUNDLE_KEY_CATALOG_TITLE, name)
        intent.putExtra(BUNDLE_KEY_CATALOG_ID, id)
        mContext.startActivity(intent)
    }
}