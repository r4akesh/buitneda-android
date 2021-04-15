/*
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

package com.webkul.mobikul.handlers

import android.content.Context
import android.content.Intent
import com.webkul.mobikul.activities.CatalogActivity
import com.webkul.mobikul.activities.SubCategoryActivity
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_ID
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_TITLE
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_TYPE
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_TYPE_CATEGORY

class SubCategoriesRvHandler(private val mContext: Context) {

    fun onClickItem(hasChildren: Boolean, name: String, id: String) {
        val intent: Intent
        if (hasChildren) {
            intent = Intent(mContext, SubCategoryActivity::class.java)
        } else {
            intent = Intent(mContext, CatalogActivity::class.java)
        }
        intent.putExtra(BUNDLE_KEY_CATALOG_TYPE, BUNDLE_KEY_CATALOG_TYPE_CATEGORY)
        intent.putExtra(BUNDLE_KEY_CATALOG_TITLE, name)
        intent.putExtra(BUNDLE_KEY_CATALOG_ID, id)
        mContext.startActivity(intent)
    }
}