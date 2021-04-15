package com.webkul.mobikulmp.handlers

import android.content.Intent
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_ID
import com.webkul.mobikulmp.activities.ContactSellerActivity
import com.webkul.mobikulmp.activities.SellerProfileActivity
import com.webkul.mobikulmp.activities.StorePolicyActivity
import com.webkul.mobikulmp.fragments.SellerProfileMoreMenuFragment
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_SELLER_ID
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_STORE_POLICY
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_STORE_POLICY_TITLE

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.handlers
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 15/7/19
 */
class SellerProfileMoreMenuHandler(private val mSellerProfileMoreMenuFragment: SellerProfileMoreMenuFragment) {

    fun onClickPolicy(title: String, content: String?) {
        val intent = Intent(mSellerProfileMoreMenuFragment.context, StorePolicyActivity::class.java)
        intent.putExtra(BUNDLE_KEY_STORE_POLICY, content)
        intent.putExtra(BUNDLE_KEY_STORE_POLICY_TITLE, title)
        mSellerProfileMoreMenuFragment.startActivity(intent)
    }

    fun onClickWriteUs() {
        val intent = Intent(mSellerProfileMoreMenuFragment.context, ContactSellerActivity::class.java)
        intent.putExtra(BUNDLE_KEY_SELLER_ID, ((mSellerProfileMoreMenuFragment.activity) as SellerProfileActivity).mSellerId)
        intent.putExtra(BUNDLE_KEY_PRODUCT_ID, "0")
        mSellerProfileMoreMenuFragment.startActivity(intent)
    }
}
