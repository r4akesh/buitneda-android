package com.webkul.mobikul.handlers

import android.content.Intent
import com.webkul.mobikul.activities.CatalogActivity
import com.webkul.mobikul.activities.HomeActivity
import com.webkul.mobikul.activities.SubCategoryActivity
import com.webkul.mobikul.fragments.NavDrawerStartFragment
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_ID
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_TITLE
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_TYPE
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_TYPE_CATEGORY
import com.webkul.mobikul.models.homepage.Category

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

class NavDrawerCategoriesRvHandler(private val mFragmentContext: NavDrawerStartFragment) {

    fun onClickItem(categoryData: Category) {
        val intent: Intent
        if (categoryData.hasChildren) {
            intent = Intent(mFragmentContext.context, SubCategoryActivity::class.java)
        } else {
            intent = Intent(mFragmentContext.context, CatalogActivity::class.java)
        }
        intent.putExtra(BUNDLE_KEY_CATALOG_TYPE, BUNDLE_KEY_CATALOG_TYPE_CATEGORY)
        intent.putExtra(BUNDLE_KEY_CATALOG_TITLE, categoryData.name)
        intent.putExtra(BUNDLE_KEY_CATALOG_ID, categoryData.id)
        mFragmentContext.startActivity(intent)
//        (mFragmentContext.context as HomeActivity).mContentViewBinding.drawerLayout.closeDrawers()
    }
}