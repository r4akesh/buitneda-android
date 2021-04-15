/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.models.seller
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 16/7/19
 */


package com.webkul.mobikulmp.activities

import android.os.Bundle
import android.view.Menu
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.ActivityContactSellerBinding
import com.webkul.mobikulmp.handlers.ContactSellerActivityHandler
import com.webkul.mobikulmp.models.seller.ContactSellerRequestData

class ContactSellerActivity : BaseActivity() {

    lateinit var mContentViewBinding: ActivityContactSellerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_contact_seller)
        startInitialization()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    private fun startInitialization() {
        initSupportActionBar()
        mContentViewBinding.data = ContactSellerRequestData()
        mContentViewBinding.handler = ContactSellerActivityHandler(this)
        setupAutoFillData()
    }

    private fun initSupportActionBar() {
        supportActionBar?.title = getString(R.string.activity_title_contact_us)
    }

    private fun setupAutoFillData() {
        if (AppSharedPref.isLoggedIn(this)) {
            mContentViewBinding.data!!.name = AppSharedPref.getCustomerName(this)
            mContentViewBinding.data!!.email = AppSharedPref.getCustomerEmail(this)
            mContentViewBinding.executePendingBindings()
        }
    }
}