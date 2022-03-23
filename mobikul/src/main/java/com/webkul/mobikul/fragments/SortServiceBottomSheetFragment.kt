package com.webkul.mobikul.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.SortBottomSheetLayoutBinding
import com.webkul.mobikul.interfaces.OnSelectSortBy


class SortServiceBottomSheetFragment(private val onSelectSortBy: OnSelectSortBy) : BottomSheetDialogFragment() {

    companion object;

    lateinit var mContentViewBinding: SortBottomSheetLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.sort_bottom_sheet_layout, container, false)
        init()
        return mContentViewBinding.root
    }


    private fun init(){
        mContentViewBinding.ascendingOrder.setOnClickListener{
            onSelectSortBy.onSortBy("ascending")
            dismiss()
        }

        mContentViewBinding.descendingOrder.setOnClickListener{
            onSelectSortBy.onSortBy("descending")
            dismiss()
        }
    }




}