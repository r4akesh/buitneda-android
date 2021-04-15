package com.webkul.mobikulmp.activities

import android.os.Bundle
import android.view.Menu
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.ActivityStorePolicyBinding
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_STORE_POLICY
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_STORE_POLICY_TITLE

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

class StorePolicyActivity : BaseActivity() {
    private lateinit var mContentViewBinding: ActivityStorePolicyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_store_policy)
        var description = intent.getStringExtra(BUNDLE_KEY_STORE_POLICY)
        if (description.isNullOrBlank()) {
            description = getString(R.string.policy_not_found)
        }
        mContentViewBinding.description = description
        supportActionBar?.title = intent.getStringExtra(BUNDLE_KEY_STORE_POLICY_TITLE)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }
}
