package com.webkul.mobikul.wallet.handlers

import android.content.Intent
import com.webkul.mobikul.activities.OrderDetailsActivity
import com.webkul.mobikul.helpers.BundleKeysHelper
import com.webkul.mobikul.wallet.fragments.ViewTransactionDialogFragment


class ViewTransactionDialogHandler(private val mFragmentContext: ViewTransactionDialogFragment) {

    fun onClickOrderId(incrementId: String) {
        val intent = Intent(mFragmentContext.context, OrderDetailsActivity::class.java)
        intent.putExtra(BundleKeysHelper.BUNDLE_KEY_INCREMENT_ID, incrementId)
        mFragmentContext.startActivity(intent)
    }
}