package com.webkul.mobikul.wallet.handlers

import android.view.View
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.wallet.fragments.ViewTransactionDialogFragment

/**
 * Created by vedesh.kumar on 4/10/17. @Webkul Software Private limited
 */

class TransactionListHandler {

    fun onClickOrder(view: View, viewId: String) {
        val supportFragmentManager = (view.context as BaseActivity).supportFragmentManager
        val viewTransactionDialogFragment = ViewTransactionDialogFragment.newInstance(viewId)
        viewTransactionDialogFragment.show(supportFragmentManager, ViewTransactionDialogFragment::class.java!!.getSimpleName())
    }
}