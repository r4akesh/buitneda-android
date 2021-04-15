package com.webkul.mobikulmp.handlers;

import android.content.Context;
import android.content.Intent;

import com.webkul.mobikul.activities.ProductDetailsActivity;
import com.webkul.mobikulmp.activity.AddAuctionOnProductActivity;

import static com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_AUCTION_ID;
import static com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_ID;
import static com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_IMAGE;
import static com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_NAME;

/**
 * Created by vedesh.kumar on 13/4/18. @Webkul Software Private limited
 */

public class AuctionProductListRvHandler {

    private Context mContext;

    public AuctionProductListRvHandler(Context context) {

        mContext = context;
    }

    public void onClickProduct(String id, String name) {
        Intent intent = new Intent(mContext, ProductDetailsActivity.class);
        intent.putExtra(BUNDLE_KEY_PRODUCT_ID, Integer.parseInt(id));
        intent.putExtra(BUNDLE_KEY_PRODUCT_NAME, name);
        intent.putExtra(BUNDLE_KEY_PRODUCT_IMAGE, "");
        mContext.startActivity(intent);
    }

    public void onClickAddAuction(String productId) {
        Intent intent = new Intent(mContext, AddAuctionOnProductActivity.class);
        intent.putExtra(BUNDLE_KEY_PRODUCT_ID, productId);
        intent.putExtra(BUNDLE_KEY_AUCTION_ID, "");
        mContext.startActivity(intent);
    }
}