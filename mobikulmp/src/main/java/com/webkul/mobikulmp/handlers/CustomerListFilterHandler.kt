package com.webkul.mobikulmp.handlers

import com.webkul.mobikulmp.activities.CustomerListActivity
import com.webkul.mobikulmp.fragments.CustomerListFilterFragment


class CustomerListFilterHandler(val mContext: CustomerListActivity) {
    fun onClickFilterCustomerList() {
        val customerListFilterFragment = CustomerListFilterFragment()
        customerListFilterFragment.setOnDetachInterface(mContext)
        customerListFilterFragment.show(mContext.supportFragmentManager, CustomerListFilterFragment::class.java.simpleName)
    }
}