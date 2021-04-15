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

package com.webkul.mobikulmp.handlers

import com.webkul.mobikul.R
import com.webkul.mobikulmp.activities.SellerOrderDetailsActivity
import com.webkul.mobikulmp.fragments.ItemSellerOrderedFragment
import com.webkul.mobikulmp.fragments.SellerOrderInvoiceDetailsBottomSheetFragment
import com.webkul.mobikulmp.fragments.SellerOrderShipmentDetailsBottomSheetFragment
import com.webkul.mobikulmp.fragments.SellerRefundsFragment

class SellerOrderDetailsActivityHandler(private val mContext: SellerOrderDetailsActivity) {

    fun onClickItemOrdered() {
        mContext.mNavigationIconClickListener.onMenuItemClick(mContext.mMenuItemMoreOptions)
        mContext.supportActionBar?.title = mContext.getString(R.string.item_ordered)
        val fragmentTransaction = mContext.supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_frame, ItemSellerOrderedFragment.newInstance(mContext.mIncrementId, mContext.mContentViewBinding.data!!), ItemSellerOrderedFragment::class.java.simpleName)
        fragmentTransaction.commit()
    }

    fun onClickInvoices() {
        mContext.mNavigationIconClickListener.onMenuItemClick(mContext.mMenuItemMoreOptions)
        mContext.mContentViewBinding.data!!.invoiceId?.let { SellerOrderInvoiceDetailsBottomSheetFragment.newInstance(mContext.mIncrementId, it).show(mContext.supportFragmentManager, SellerOrderInvoiceDetailsBottomSheetFragment::class.java.simpleName) }
    }

    fun onClickShipments() {
        mContext.mNavigationIconClickListener.onMenuItemClick(mContext.mMenuItemMoreOptions)
        mContext.mContentViewBinding.data!!.shipmentId?.let { SellerOrderShipmentDetailsBottomSheetFragment.newInstance(mContext.mIncrementId, it).show(mContext.supportFragmentManager, SellerOrderShipmentDetailsBottomSheetFragment::class.java.simpleName) }
    }

    fun onClickRefunds() {
        mContext.mNavigationIconClickListener.onMenuItemClick(mContext.mMenuItemMoreOptions)
        mContext.supportActionBar?.title = mContext.getString(R.string.refunds)
        val fragmentTransaction = mContext.supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_frame, SellerRefundsFragment.newInstance(mContext.mIncrementId, mContext.mContentViewBinding.data!!), SellerRefundsFragment::class.java.simpleName)
        fragmentTransaction.commit()
    }
}