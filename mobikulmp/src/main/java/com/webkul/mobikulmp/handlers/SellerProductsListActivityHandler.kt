package com.webkul.mobikulmp.handlers

import com.webkul.mobikulmp.activities.SellerProductsListActivity
import com.webkul.mobikulmp.fragments.SellerProductsFilterFragment

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
class SellerProductsListActivityHandler(private val mSellerProductsListActivity: SellerProductsListActivity) {

    fun viewProductsFilterDialog() {
        val signUpBottomSheetFragment = SellerProductsFilterFragment.newInstance()
        signUpBottomSheetFragment.setOnDetachInterface(mSellerProductsListActivity)
        signUpBottomSheetFragment.show(mSellerProductsListActivity.supportFragmentManager, signUpBottomSheetFragment.tag)
    }
}