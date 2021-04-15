package com.webkul.mobikul.handlers

import android.content.Context
import android.content.Intent
import android.view.View
import com.webkul.mobikul.activities.ProductDetailsActivity
import com.webkul.mobikul.helpers.BundleKeysHelper
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_ID
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_IMAGE
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_NAME
import com.webkul.mobikul.helpers.MobikulApplication

/**
 * Created by vedesh.kumar on 10/4/18. @Webkul Software Private limited
 */
class AuctionBidListRvHandler(val mContext: Context) {
    fun onClickOpenProduct( productId: String?, productName: String?) {
        val intent = (mContext?.applicationContext as MobikulApplication).getProductDetailsActivity(mContext!!)
        intent.putExtra(BUNDLE_KEY_PRODUCT_ID, productId)
        intent.putExtra(BUNDLE_KEY_PRODUCT_NAME, productName)
//        intent.putExtra(BUNDLE_KEY_PRODUCT_IMAGE, thumbNail)
        intent.putExtra(BUNDLE_KEY_PRODUCT_IMAGE, "")
//        intent.putExtra(BundleKeysHelper.BUNDLE_KEY_PRODUCT_DOMINANT_COLOR, dominantColor)
        mContext.startActivity(intent)
           }



}