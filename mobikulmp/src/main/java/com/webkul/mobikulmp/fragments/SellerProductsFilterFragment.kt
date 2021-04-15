package com.webkul.mobikulmp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.fragments.FullScreenBottomSheetDialogFragment
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.SellerProductsListActivity
import com.webkul.mobikulmp.databinding.FragmentSellerProductFilterBinding
import com.webkul.mobikulmp.handlers.SellerProductsFilterFragHandler


/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.fragments
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 16/6/19
 */
class SellerProductsFilterFragment : FullScreenBottomSheetDialogFragment() {

    lateinit var mContentViewBinding: FragmentSellerProductFilterBinding
    internal var onDetachInterface: OnDetachInterface? = null


    fun setOnDetachInterface(onDetachInterface: OnDetachInterface) {
        this.onDetachInterface = onDetachInterface
    }

    interface OnDetachInterface {
        fun onFragmentDetached()
    }

    companion object {
        fun newInstance(): SellerProductsFilterFragment {
            val sellerProductsFilterFragment = SellerProductsFilterFragment()
            val args = Bundle()

            sellerProductsFilterFragment.arguments = args
            return sellerProductsFilterFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_seller_product_filter, container, false)
        mContentViewBinding.productName = (context as SellerProductsListActivity).mProductName
        mContentViewBinding.fromDate = (context as SellerProductsListActivity).mDateFrom
        mContentViewBinding.toDate = (context as SellerProductsListActivity).mDateTo
        mContentViewBinding.handler = SellerProductsFilterFragHandler(this)
        return mContentViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arrayData = resources.getStringArray(R.array.product_status_label)
        mContentViewBinding.productStatusSpinner.adapter = context?.let { ArrayAdapter<String>(it, R.layout.custom_spinner_item, arrayData) }
        mContentViewBinding.productStatusSpinner.setSelection((context as SellerProductsListActivity).mProductStatusPosition)

    }
}