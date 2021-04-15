package com.webkul.mobikulmp.activities


import android.os.Bundle
import android.view.Menu
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.adapters.AttributeOptionRvAdapter
import com.webkul.mobikulmp.databinding.ActivityCreateAttributeBinding
import com.webkul.mobikulmp.handlers.CreateAttributeActivityHandler
import com.webkul.mobikulmp.models.seller.AttributeOptionItemData
import com.webkul.mobikulmp.models.seller.CreateAttributeRequestData

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

class CreateAttributeActivity : BaseActivity() {

    lateinit var mContentViewBinding: ActivityCreateAttributeBinding
    lateinit var mCreateAttributeRequestData: CreateAttributeRequestData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_attribute)
        startInitialization()
    }

    fun setAttributeData() {
        mCreateAttributeRequestData = CreateAttributeRequestData()
        mCreateAttributeRequestData.attributeOptionList.clear()
        mCreateAttributeRequestData.attributeOptionList.add(AttributeOptionItemData())
        mContentViewBinding.data = mCreateAttributeRequestData
        mContentViewBinding.handler = CreateAttributeActivityHandler(this)
        mContentViewBinding.attributeOptionRv.adapter = AttributeOptionRvAdapter(this, mCreateAttributeRequestData.attributeOptionList)
        mContentViewBinding.attributeOptionRv.isNestedScrollingEnabled = false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }


    private fun startInitialization() {
        initSupportActionBar()
        setAttributeData()
    }

    private fun initSupportActionBar() {
        supportActionBar?.title = getString(R.string.create_attribute)
    }
}