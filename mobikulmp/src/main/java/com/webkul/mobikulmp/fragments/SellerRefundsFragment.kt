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

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.fragments.BaseFragment
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_INCREMENT_ID
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_ORDER_DETAILS
import com.webkul.mobikul.helpers.ConstantsHelper
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.adapters.SellerOrderRefundsRvAdapter
import com.webkul.mobikulmp.databinding.FragmentSellerRefundsBinding
import com.webkul.mobikulmp.models.seller.SellerOrderDetailsModel

class SellerRefundsFragment : BaseFragment() {

    lateinit var mContentViewBinding: FragmentSellerRefundsBinding

    companion object {
        fun newInstance(incrementId: String, orderDetailsModel: SellerOrderDetailsModel): SellerRefundsFragment {
            val refundsFragment = SellerRefundsFragment()
            val args = Bundle()
            args.putString(BUNDLE_KEY_INCREMENT_ID, incrementId)
            args.putParcelable(BUNDLE_KEY_ORDER_DETAILS, orderDetailsModel)
            refundsFragment.arguments = args
            return refundsFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_seller_refunds, container, false)
        return mContentViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        startInitialization()
    }

    private fun startInitialization() {
        mContentViewBinding.data = arguments!!.getParcelable(BUNDLE_KEY_ORDER_DETAILS)

        setupRefundsItemsRv()
    }

    private fun setupRefundsItemsRv() {
        mContentViewBinding.orderRefundsRv.adapter = mContentViewBinding.data!!.creditMemoList?.let { SellerOrderRefundsRvAdapter(this, it) }
        mContentViewBinding.orderRefundsRv.isNestedScrollingEnabled = false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var allPermissionsGranted = true
        for (eachGrantResult in grantResults) {
            if (eachGrantResult != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false
            }
        }
        if (allPermissionsGranted) {
            if (requestCode == ConstantsHelper.RC_WRITE_TO_EXTERNAL_STORAGE) {

                val itemBinding = (mContentViewBinding.orderRefundsRv.findViewHolderForAdapterPosition((mContentViewBinding.orderRefundsRv.adapter as SellerOrderRefundsRvAdapter).adapterPosition) as SellerOrderRefundsRvAdapter.ViewHolder)
                        .mBinding
                itemBinding?.data?.let { itemBinding.handler?.onClickSaveRefund(it) }
            }

        }
    }
}