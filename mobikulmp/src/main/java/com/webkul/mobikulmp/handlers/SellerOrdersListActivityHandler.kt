package com.webkul.mobikulmp.handlers

import com.webkul.mobikulmp.activities.SellerOrdersListActivity
import com.webkul.mobikulmp.fragments.SellerOrdersFilterFragment

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
class SellerOrdersListActivityHandler(private val mContext: SellerOrdersListActivity) {

    fun showOrdersFilterDialog() {
        val sellerOrdersFilterFragment = SellerOrdersFilterFragment.newInstance(mContext.mContentViewBinding.data!!.orderStatus)
        sellerOrdersFilterFragment.setOnDetachInterface(mContext)
        sellerOrdersFilterFragment.show(mContext.supportFragmentManager, SellerOrdersFilterFragment::class.java.simpleName)
    }
}