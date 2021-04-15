package com.webkul.mobikulmp.handlers

import android.content.Context
import android.content.Intent
import com.webkul.mobikul.activities.ProductDetailsActivity
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_AUCTION_ID
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_ID
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_IMAGE
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_NAME
import com.webkul.mobikulmp.activities.AuctionBidDetailsActivity
import com.webkul.mobikulmp.activity.AddAuctionOnProductActivity

/**
 * Created by vedesh.kumar on 13/4/18. @Webkul Software Private limited
 */
class AuctionListRvHandler(private val mContext: Context) {
    fun onClickAuction(auctionId: String?, selectionModeOn: Boolean) {
        if (!selectionModeOn) {
            val intent = Intent(mContext, AuctionBidDetailsActivity::class.java)
            intent.putExtra(BUNDLE_KEY_AUCTION_ID, auctionId)
            mContext.startActivity(intent)
        }
    }

    fun onClickProduct(id: String, name: String?) {
        val intent = Intent(mContext, ProductDetailsActivity::class.java)
        intent.putExtra(BUNDLE_KEY_PRODUCT_ID, id.toInt())
        intent.putExtra(BUNDLE_KEY_PRODUCT_NAME, name)
        intent.putExtra(BUNDLE_KEY_PRODUCT_IMAGE, "")
        mContext.startActivity(intent)
    }

    fun onClickEditAuction(productId: String?, auctionId: String?) {
        val intent = Intent(mContext, AddAuctionOnProductActivity::class.java)
        intent.putExtra(BUNDLE_KEY_PRODUCT_ID, productId)
        intent.putExtra(BUNDLE_KEY_AUCTION_ID, auctionId)
        mContext.startActivity(intent)
    }

}