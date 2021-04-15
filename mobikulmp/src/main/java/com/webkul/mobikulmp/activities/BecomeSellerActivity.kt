package com.webkul.mobikulmp.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.ActivityBecomeSellerBinding
import com.webkul.mobikulmp.handlers.BecomePartnerHandler
import com.webkul.mobikulmp.models.BecomePartnerData

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.models.seller
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 23/5/19
 */
class BecomeSellerActivity : BaseActivity() {
    lateinit var mContentViewBinding: ActivityBecomeSellerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_become_seller)
        startInitialization()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }


    private fun startInitialization() {
        initSupportActionBar()
        mContentViewBinding.data = BecomePartnerData()
        mContentViewBinding.handler = BecomePartnerHandler(this)

        mContentViewBinding.etShopUrl.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mContentViewBinding.handler!!.onClickMakeSeller(mContentViewBinding.data!!)
                true
            } else {
                false
            }
        }

        // Remove Space FromShop URL #556
        mContentViewBinding.etShopUrl.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun afterTextChanged(p0: Editable?) {
                val textEntered =  mContentViewBinding.etShopUrl.text.toString()

                if (textEntered.isNotEmpty() && textEntered.contains(" ")) {
                    mContentViewBinding.etShopUrl.setText( mContentViewBinding.etShopUrl.text.toString().replace(" ", ""));
                    mContentViewBinding.etShopUrl.setSelection( mContentViewBinding.etShopUrl.text!!.length);
                }
            }})
    }

    private fun initSupportActionBar() {
        supportActionBar?.title = getString(R.string.become_seller)
    }
}