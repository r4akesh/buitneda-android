package com.webkul.mobikulmp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.fragments.FullScreenBottomSheetDialogFragment
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.CustomerListActivity
import com.webkul.mobikulmp.databinding.FragmentCustomerListFilterBinding
import com.webkul.mobikulmp.handlers.CustomerListFilterFragmentHandler


class CustomerListFilterFragment : FullScreenBottomSheetDialogFragment() {
    lateinit var mContentViewBinding: FragmentCustomerListFilterBinding
    internal var onDetachInterface: OnDetachInterface? = null


    fun setOnDetachInterface(onDetachInterface: OnDetachInterface) {
        this.onDetachInterface = onDetachInterface
    }

    interface OnDetachInterface {
        fun onFragmentDetached()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_customer_list_filter, container, false)
        mContentViewBinding.name = (context as CustomerListActivity).name
        mContentViewBinding.email = (context as CustomerListActivity).email
        mContentViewBinding.gender = if ((context as CustomerListActivity).gender == null) 0 else {
            (context as CustomerListActivity).gender!!
        }
        mContentViewBinding.contact = (context as CustomerListActivity).contact
        mContentViewBinding.address = (context as CustomerListActivity).address
        mContentViewBinding.handler = CustomerListFilterFragmentHandler(this)
        return mContentViewBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arrayData = resources.getStringArray(R.array.gender)
        mContentViewBinding.quoteStatusSpinner.adapter = context?.let { ArrayAdapter<String>(it, R.layout.custom_spinner_item, arrayData) }
        mContentViewBinding.quoteStatusSpinner.setSelection(if ((context as CustomerListActivity).gender == null) 0 else {
            (context as CustomerListActivity).gender!!
        })

    }


}