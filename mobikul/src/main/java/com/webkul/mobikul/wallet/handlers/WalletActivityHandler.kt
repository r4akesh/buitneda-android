package com.webkul.mobikul.wallet.handlers

import android.content.Intent
import com.webkul.mobikul.wallet.activities.AddAccountDetailsActivity
import com.webkul.mobikul.wallet.activities.ManageWalletAmountActivity
import com.webkul.mobikul.wallet.activities.TransferWalletAmountActivity
import com.webkul.mobikul.wallet.activities.WalletActivity


class WalletActivityHandler(private val mContext: WalletActivity) {

    fun onClickManageAmount() {
        mContext.startActivity(Intent(mContext, ManageWalletAmountActivity::class.java))
    }

    fun onClickTransferAmount() {
        mContext.startActivity(Intent(mContext, TransferWalletAmountActivity::class.java))
    }

    fun onClickAddAccount() {
        mContext.startActivity(Intent(mContext, AddAccountDetailsActivity::class.java))
    }
}