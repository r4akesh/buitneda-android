/*
 * Webkul Software.
 *
 * Kotlin
 *
 * @author Webkul <support@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2019 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

package com.webkul.mobikulmp.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.adapters.OrderItemsRvAdapter
import com.webkul.mobikul.adapters.OrderTotalsRvAdapter
import com.webkul.mobikul.fragments.BaseFragment
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_INCREMENT_ID
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_ORDER_DETAILS
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.FragmentItemSellerOrderedBinding
import com.webkul.mobikulmp.handlers.ItemSellerOrderedFragmentHandler
import com.webkul.mobikulmp.models.seller.SellerOrderDetailsModel

class ItemSellerOrderedFragment : BaseFragment() {

    lateinit var mContentViewBinding: FragmentItemSellerOrderedBinding

    companion object {
        fun newInstance(incrementId: String, orderDetailsModel: SellerOrderDetailsModel): ItemSellerOrderedFragment {
            val itemOrderedFragment = ItemSellerOrderedFragment()
            val args = Bundle()
            args.putString(BUNDLE_KEY_INCREMENT_ID, incrementId)
            args.putParcelable(BUNDLE_KEY_ORDER_DETAILS, orderDetailsModel)
            itemOrderedFragment.arguments = args
            return itemOrderedFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_item_seller_ordered, container, false)
        return mContentViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        startInitialization()
    }

    private fun startInitialization() {
        mContentViewBinding.data = arguments!!.getParcelable(BUNDLE_KEY_ORDER_DETAILS)

        setupOrderItemsRv()
        setupOrderTotalsRv()

        mContentViewBinding.handler = arguments?.getString(BUNDLE_KEY_INCREMENT_ID)?.let { ItemSellerOrderedFragmentHandler(this, it) }
    }

    private fun setupOrderItemsRv() {
        mContentViewBinding.orderItemsRv.adapter = mContentViewBinding.data!!.orderData?.items?.let { OrderItemsRvAdapter(context!!, it) }
        mContentViewBinding.orderItemsRv.isNestedScrollingEnabled = false
    }

    private fun setupOrderTotalsRv() {
        mContentViewBinding.orderTotalsRv.adapter = mContentViewBinding.data!!.orderData?.totals?.let { OrderTotalsRvAdapter(context!!, it) }
        mContentViewBinding.orderTotalsRv.isNestedScrollingEnabled = false
    }
}