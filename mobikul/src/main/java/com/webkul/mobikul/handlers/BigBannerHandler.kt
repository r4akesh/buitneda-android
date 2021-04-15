package com.webkul.mobikul.handlers

import android.content.Intent
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.activities.CatalogActivity
import com.webkul.mobikul.customviews.MaterialSearchView
import com.webkul.mobikul.fragments.HomeFragment
import com.webkul.mobikul.helpers.BundleKeysHelper
import com.webkul.mobikul.helpers.MobikulApplication

class BigBannerHandler(private val mContext: HomeFragment) {
    lateinit var mMaterialSearchView: MaterialSearchView

    fun onClickBannerCategory( name: String, id: String) {
                val intent = Intent(mContext.context, CatalogActivity::class.java)
                intent.putExtra(BundleKeysHelper.BUNDLE_KEY_CATALOG_TYPE, BundleKeysHelper.BUNDLE_KEY_CATALOG_TYPE_CATEGORY)
                intent.putExtra(BundleKeysHelper.BUNDLE_KEY_CATALOG_TITLE, "")
                intent.putExtra(BundleKeysHelper.BUNDLE_KEY_CATALOG_ID, id)
                mContext.startActivity(intent)

    }

    fun onClickBanner(type: String, name: String, id: String, dominantColor: String) {
        when (type) {
            "category" -> {
                val intent = Intent(mContext.context, CatalogActivity::class.java)
                intent.putExtra(BundleKeysHelper.BUNDLE_KEY_CATALOG_TYPE, BundleKeysHelper.BUNDLE_KEY_CATALOG_TYPE_CATEGORY)
                intent.putExtra(BundleKeysHelper.BUNDLE_KEY_CATALOG_TITLE, name)
                intent.putExtra(BundleKeysHelper.BUNDLE_KEY_CATALOG_ID, id)
                mContext.startActivity(intent)
            }
            "product" -> {
                val intent = (mContext?.activity?.applicationContext as MobikulApplication).getProductDetailsActivity(mContext.context!!)
                intent.putExtra(BundleKeysHelper.BUNDLE_KEY_PRODUCT_DOMINANT_COLOR, dominantColor)
                intent.putExtra(BundleKeysHelper.BUNDLE_KEY_PRODUCT_NAME, name)
                intent.putExtra(BundleKeysHelper.BUNDLE_KEY_PRODUCT_ID, id)
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