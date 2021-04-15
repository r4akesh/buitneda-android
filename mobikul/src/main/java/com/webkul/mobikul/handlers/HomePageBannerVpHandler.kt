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

import android.content.Intent
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.activities.CatalogActivity
import com.webkul.mobikul.customviews.MaterialSearchView
import com.webkul.mobikul.fragments.HomeFragment
import com.webkul.mobikul.helpers.BundleKeysHelper
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_ID
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_TITLE
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_TYPE
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_TYPE_CATEGORY
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_DOMINANT_COLOR
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_ID
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_NAME
import com.webkul.mobikul.helpers.MobikulApplication

class HomePageBannerVpHandler(private val mContext: HomeFragment) {

   lateinit var mMaterialSearchView: MaterialSearchView
    fun onClickBanner(type: String, name: String, id: String, dominantColor: String) {
        when (type) {
            "category" -> {
                val intent = Intent(mContext.context, CatalogActivity::class.java)
                intent.putExtra(BUNDLE_KEY_CATALOG_TYPE, BUNDLE_KEY_CATALOG_TYPE_CATEGORY)
                intent.putExtra(BUNDLE_KEY_CATALOG_TITLE, name)
                intent.putExtra(BUNDLE_KEY_CATALOG_ID, id)
                mContext.startActivity(intent)
            }
            "product" -> {
                val intent = (mContext?.activity?.applicationContext as MobikulApplication).getProductDetailsActivity(mContext.context!!)
                intent.putExtra(BUNDLE_KEY_PRODUCT_DOMINANT_COLOR, dominantColor)
                intent.putExtra(BUNDLE_KEY_PRODUCT_NAME, name)
                intent.putExtra(BUNDLE_KEY_PRODUCT_ID, id)
                mContext.startActivity(intent)
            }
        }
    }
     fun onClickSearch() {
        // mMaterialSearchView=MaterialSearchView(mContext.context!!)
      /*  //val contentLayout = findViewById<FrameLayout>(android.R.id.content)
        if (this::mMaterialSearchView.isInitialized && mMaterialSearchView.parent != null)
            (mMaterialSearchView.parent as ViewGroup).removeView(mMaterialSearchView)

        if (this::mMaterialSearchView.isInitialized) {
            contentLayout.addView(mMaterialSearchView)
            mMaterialSearchView.openSearch()
        }*/
         (mContext.activity as BaseActivity).openMaterialSearchView()
    }



  }