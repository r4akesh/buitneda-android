package com.webkul.mobikul.wallet.activities


import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.databinding.ActivityWalletBinding
import com.webkul.mobikul.wallet.handlers.WalletActivityHandler

class WalletActivity : BaseActivity() {

    lateinit var mContentViewBinding: ActivityWalletBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_wallet)
        startInitialization()
    }

    private fun startInitialization() {
        initSupportActionBar()
        mContentViewBinding.handler = WalletActivityHandler(this)
    }

    private fun initSupportActionBar() {
        supportActionBar?.title = getString(R.string.activity_title_wallet)
    }
}
