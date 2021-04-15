package com.webkul.mobikulmp.handlers

import android.content.Intent
import android.view.View

import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_INCREMENT_ID
import com.webkul.mobikulmp.activities.SellerOrderDetailsActivity


/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.fragments
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 28/6/19
 */

class SellerOrderItemRecyclerHandler {

    fun viewOrderDetails(view: View, incrementId: String) {
        val intent = Intent(view.context, SellerOrderDetailsActivity::class.java)
        intent.putExtra(BUNDLE_KEY_INCREMENT_ID, incrementId)
        view.context.startActivity(intent)
    }
}