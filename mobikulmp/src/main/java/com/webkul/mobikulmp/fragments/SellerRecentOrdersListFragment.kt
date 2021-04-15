package com.webkul.mobikulmp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.fragments.BaseFragment
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.adapters.SellerLatestOrderListFragRvAdapter
import com.webkul.mobikulmp.databinding.FragmentSellerRecentOrdersListBinding
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_SELLER_ORDER_DATA
import com.webkul.mobikulmp.models.seller.SellerOrderData
import java.util.*


/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.fragments
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 10/7/19
 */

class SellerRecentOrdersListFragment : BaseFragment() {

    private lateinit var mContentViewBinding: FragmentSellerRecentOrdersListBinding
    private var mRecentOrdersList: ArrayList<SellerOrderData>? = null


    private var onDetachInterface: OnDetachInterface? = null


    fun setOnDetachInterface(onDetachInterface: OnDetachInterface) {
        this.onDetachInterface = onDetachInterface
    }

    interface OnDetachInterface {
        fun onFragmentDetached()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_seller_recent_orders_list, container, false)
        mRecentOrdersList = arguments!!.getParcelableArrayList(BUNDLE_KEY_SELLER_ORDER_DATA)
        return mContentViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mContentViewBinding.displayOrder = false
        if (mRecentOrdersList!!.size > 0) {
            mContentViewBinding.displayOrder = true
            mContentViewBinding.sellerOrdersRv.adapter = SellerLatestOrderListFragRvAdapter(context!!, mRecentOrdersList!!)
        }
    }

    companion object {

        fun newInstance(recentOrderList: ArrayList<SellerOrderData>): SellerRecentOrdersListFragment {
            val sellerRecentOrdersListFragment = SellerRecentOrdersListFragment()
            val args = Bundle()
            args.putParcelableArrayList(BUNDLE_KEY_SELLER_ORDER_DATA, recentOrderList)
            sellerRecentOrdersListFragment.arguments = args
            return sellerRecentOrdersListFragment
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (onDetachInterface != null) {
            onDetachInterface!!.onFragmentDetached()
        }
    }

}