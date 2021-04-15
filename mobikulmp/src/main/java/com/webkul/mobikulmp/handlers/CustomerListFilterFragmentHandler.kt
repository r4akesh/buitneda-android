package com.webkul.mobikulmp.handlers

import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikulmp.activities.CustomerListActivity
import com.webkul.mobikulmp.fragments.CustomerListFilterFragment


class CustomerListFilterFragmentHandler(private val mFragmentContext: CustomerListFilterFragment) {
    fun applyFilters() {
        (mFragmentContext.context as CustomerListActivity).name = mFragmentContext.mContentViewBinding.name!!
        (mFragmentContext.context as CustomerListActivity).email = mFragmentContext.mContentViewBinding.email!!
        (mFragmentContext.context as CustomerListActivity).contact = mFragmentContext.mContentViewBinding.contact!!
        (mFragmentContext.context as CustomerListActivity).gender = if (mFragmentContext.mContentViewBinding.quoteStatusSpinner.selectedItemPosition == 0) null else {
            mFragmentContext.mContentViewBinding.quoteStatusSpinner.selectedItemPosition
        }
        (mFragmentContext.context as CustomerListActivity).address = mFragmentContext.mContentViewBinding.address!!
        (mFragmentContext.context as CustomerListActivity).mPageNumber = 1
        Utils.hideKeyboard(mFragmentContext.context as CustomerListActivity)
        mFragmentContext.onDetachInterface?.onFragmentDetached()
        mFragmentContext.dismiss()
    }

    fun resetFilters() {
        (mFragmentContext.context as CustomerListActivity).name = ""
        (mFragmentContext.context as CustomerListActivity).email = ""
        (mFragmentContext.context as CustomerListActivity).contact = ""
        (mFragmentContext.context as CustomerListActivity).gender = null
        (mFragmentContext.context as CustomerListActivity).address = ""
        (mFragmentContext.context as CustomerListActivity).mPageNumber = 1
        Utils.hideKeyboard(mFragmentContext.context as CustomerListActivity)
        mFragmentContext.dismiss()
        mFragmentContext.onDetachInterface?.onFragmentDetached()
    }

    fun onClickCancelFilter() {
        Utils.hideKeyboard(mFragmentContext.context as CustomerListActivity)
        mFragmentContext.dismiss()
    }
}