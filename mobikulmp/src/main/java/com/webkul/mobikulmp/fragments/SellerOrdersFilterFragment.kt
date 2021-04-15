package com.webkul.mobikulmp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.fragments.FullScreenBottomSheetDialogFragment
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.SellerOrdersListActivity
import com.webkul.mobikulmp.databinding.FragmentSellerOrderFilterBinding
import com.webkul.mobikulmp.handlers.SellerOrdersFilterFragHandler
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_ORDER_STATUS_LIST
import com.webkul.mobikulmp.models.seller.OrderStatus

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

class SellerOrdersFilterFragment : FullScreenBottomSheetDialogFragment() {

    internal var onDetachInterface: OnDetachInterface? = null


    fun setOnDetachInterface(onDetachInterface: OnDetachInterface) {
        this.onDetachInterface = onDetachInterface
    }

    interface OnDetachInterface {
        fun onFragmentDetached()
    }


    companion object {
        fun newInstance(orderStatus: ArrayList<OrderStatus>): SellerOrdersFilterFragment {
            val sellerProductsFilterFragment = SellerOrdersFilterFragment()
            val args = Bundle()
            args.putParcelableArrayList(BUNDLE_KEY_ORDER_STATUS_LIST, orderStatus)
            sellerProductsFilterFragment.arguments = args
            return sellerProductsFilterFragment
        }
    }

    lateinit var mContentViewBinding: FragmentSellerOrderFilterBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_seller_order_filter, container, false)
        mContentViewBinding.incrementId = (context as SellerOrdersListActivity).mIncrementId
        mContentViewBinding.fromDate = (context as SellerOrdersListActivity).mDateFrom
        mContentViewBinding.toDate = (context as SellerOrdersListActivity).mDateTo
        arguments?.getParcelableArrayList<OrderStatus>(BUNDLE_KEY_ORDER_STATUS_LIST)?.let { setupOrderStatusSp(it) }
        mContentViewBinding.handler = SellerOrdersFilterFragHandler(this)
        return mContentViewBinding.root
    }

    private fun setupOrderStatusSp(orderStatusList: ArrayList<OrderStatus>) {
        val prefixSpinnerData = java.util.ArrayList<String>()
        for (prefixIterator in 0 until orderStatusList.size) {
            orderStatusList[prefixIterator].label?.let { prefixSpinnerData.add(it) }
        }

        mContentViewBinding.orderStatusSpinner.adapter = context?.let { ArrayAdapter<String>(it, R.layout.custom_spinner_item, prefixSpinnerData) }
        mContentViewBinding.orderStatusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                (context as SellerOrdersListActivity).mStatusPosition = position
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        mContentViewBinding.orderStatusSpinner.setSelection((context as SellerOrdersListActivity).mStatusPosition)
    }
}