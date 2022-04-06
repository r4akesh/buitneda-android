package com.webkul.mobikul.handlers

import android.content.Context
import android.content.Intent
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.activities.CatalogActivity
import com.webkul.mobikul.activities.ServiceProviderListActivity
import com.webkul.mobikul.customviews.MaterialSearchView
import com.webkul.mobikul.fragments.HomeFragment
import com.webkul.mobikul.fragments.ServiceProviderFragment
import com.webkul.mobikul.helpers.BundleKeysHelper
import com.webkul.mobikul.helpers.MobikulApplication

class ServiceCategoryHandler(private val mContext: Context) {


    fun onClickCategory(id: String) {
        mContext.startActivity(
            Intent(
                mContext,
                ServiceProviderListActivity::class.java
            ).putExtra("serviceId",id)
        )

    }
}