package com.webkul.mobikulmp.handlers

import com.webkul.mobikul.fragments.AccountDetailsFragment
import com.webkul.mobikul.helpers.MobikulApplication
import com.webkul.mobikulmp.models.AccountMpRvModel.Companion.ADD_NEW_PRODUCT
import com.webkul.mobikulmp.models.AccountMpRvModel.Companion.ASK_QUESTIONS
import com.webkul.mobikulmp.models.AccountMpRvModel.Companion.ATTRIBUTES
import com.webkul.mobikulmp.models.AccountMpRvModel.Companion.CHAT_WITH_ADMIN
import com.webkul.mobikulmp.models.AccountMpRvModel.Companion.INVOICE_PRINT_TEMPLATE
import com.webkul.mobikulmp.models.AccountMpRvModel.Companion.SELLER_DASHBOARD
import com.webkul.mobikulmp.models.AccountMpRvModel.Companion.SELLER_ORDERS
import com.webkul.mobikulmp.models.AccountMpRvModel.Companion.SELLER_PRODUCTS
import com.webkul.mobikulmp.models.AccountMpRvModel.Companion.SELLER_PROFILE
import com.webkul.mobikulmp.models.AccountMpRvModel.Companion.TRANSACTIONS
import com.webkul.mobikulmp.models.AccountMpRvModel.Companion.TYPE_CUSTOMERS
import com.webkul.mobikulmp.models.AccountMpRvModel.Companion.TYPE_SELLER_STATUS

/**
 * Webkul Software.
 *
 * Kotlin
 *
 * @author Webkul <support@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

class AccountMpExtendRvHandler(val mFragmentContext: AccountDetailsFragment) {

    fun onClickRvItem(type: Int) {
        when (type) {
            ASK_QUESTIONS -> {
                mFragmentContext.startActivity((mFragmentContext.context!!.applicationContext as MobikulApplication).getAskAdminActivity(mFragmentContext.context!!))
            }
            SELLER_DASHBOARD -> {
                mFragmentContext.startActivity((mFragmentContext.context!!.applicationContext as MobikulApplication).getSellerDashboardActivity(mFragmentContext.context!!))
            }
            SELLER_ORDERS -> {
                mFragmentContext.startActivity((mFragmentContext.context!!.applicationContext as MobikulApplication).getSellerOrdersActivity(mFragmentContext.context!!))
            }
            TYPE_SELLER_STATUS -> {
                mFragmentContext.startActivity((mFragmentContext.context!!.applicationContext as MobikulApplication).getBecomeSellerActivity(mFragmentContext.context!!))
            }
            ATTRIBUTES -> {
                mFragmentContext.startActivity((mFragmentContext.context!!.applicationContext as MobikulApplication).getCreateAttributeActivity(mFragmentContext.context!!))
            }
            SELLER_PROFILE -> {
                mFragmentContext.startActivity((mFragmentContext.context!!.applicationContext as MobikulApplication).getSellerProfileEditActivity(mFragmentContext.context!!))
            }
            ADD_NEW_PRODUCT -> {
                mFragmentContext.startActivity((mFragmentContext.context!!.applicationContext as MobikulApplication).getSellerAddProductActivity(mFragmentContext.context!!))
            }
            SELLER_PRODUCTS -> {
                mFragmentContext.startActivity((mFragmentContext.context!!.applicationContext as MobikulApplication).getSellerProductsListActivity(mFragmentContext.context!!))
            }
            TRANSACTIONS -> {
                mFragmentContext.startActivity((mFragmentContext.context!!.applicationContext as MobikulApplication).getSellerTransactionsListActivity(mFragmentContext.context!!))
            }
            INVOICE_PRINT_TEMPLATE -> {
                mFragmentContext.startActivity((mFragmentContext.context!!.applicationContext as MobikulApplication).getManagePrintPdfHeaderActivity(mFragmentContext.context!!))
            }
            CHAT_WITH_ADMIN -> {
                mFragmentContext.startActivity((mFragmentContext.context!!.applicationContext as MobikulApplication).getChatRelatedActivity(mFragmentContext.context!!))
            }
            TYPE_CUSTOMERS -> {
                mFragmentContext.startActivity((mFragmentContext.context!!.applicationContext as MobikulApplication).getCustomerListActivity(mFragmentContext.context!!))
            }

        }
//        (mFragmentContext.context as HomeActivity).mContentViewBinding.drawerLayout.closeDrawers()
    }
}