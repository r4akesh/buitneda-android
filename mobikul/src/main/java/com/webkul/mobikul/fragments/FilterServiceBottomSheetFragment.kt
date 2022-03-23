package com.webkul.mobikul.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.webkul.mobikul.R
import com.webkul.mobikul.adapters.ServiceFilterAdapter
import com.webkul.mobikul.databinding.FilterBottomSheetLayoutBinding
import com.webkul.mobikul.interfaces.OnFilterItemSelectListener
import com.webkul.mobikul.models.service.ServiceBannerListModel


class FilterServiceBottomSheetFragment(
    private val onFilterItemSelectListener: OnFilterItemSelectListener,
    private val list: ArrayList<ServiceBannerListModel>
) : BottomSheetDialogFragment(),
    OnFilterItemSelectListener {
    lateinit var mContentViewBinding: FilterBottomSheetLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mContentViewBinding =
            DataBindingUtil.inflate(inflater, R.layout.filter_bottom_sheet_layout, container, false)
        init()
        return mContentViewBinding.root
    }


    private fun init() {
        setUpFilterList()
    }

    private fun setUpFilterList() {
        mContentViewBinding.filterListLayout.layoutManager = LinearLayoutManager(context)
        val serviceFilterAdapter = ServiceFilterAdapter(requireContext(), list, this)
        mContentViewBinding.filterListLayout.adapter = serviceFilterAdapter
    }

    override fun onFilterItemSelect(serviceProviderId: String) {
        dismiss()
        onFilterItemSelectListener.onFilterItemSelect(serviceProviderId)
    }


}