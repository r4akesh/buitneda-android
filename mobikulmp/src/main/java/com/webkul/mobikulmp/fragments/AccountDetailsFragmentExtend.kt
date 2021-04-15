package com.webkul.mobikulmp.fragments

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.webkul.mobikul.fragments.AccountDetailsFragment
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.adapters.AccountMPRvAdapter
import com.webkul.mobikulmp.databinding.LayoutSellerMenusBinding
import com.webkul.mobikulmp.helpers.MpAppSharedPref
import com.webkul.mobikulmp.models.AccountMpRvModel


/**
 *
 * Webkul Software.
 *
 * @category Mobikul
 * @package com.webkul.mobikulmp.fragments
 * @author Webkul
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 15/5/19
 *
 */
class AccountDetailsFragmentExtend : AccountDetailsFragment() {

    override fun setupAccountRv() {
        if (AppSharedPref.isLoggedIn(context!!)) {
            super.setupAccountRv()

            val accountRvData: ArrayList<AccountMpRvModel> = ArrayList()

            if (MpAppSharedPref.isSeller(context!!)) {
                if (MpAppSharedPref.isSellerPending(context!!)) {
                    accountRvData.add(AccountMpRvModel(AccountMpRvModel.TYPE_SELLER_STATUS, getString(R.string.nav_seller_pending_status), R.drawable.seller_ask1))
                } else {
                    accountRvData.add(AccountMpRvModel(AccountMpRvModel.SELLER_DASHBOARD, getString(R.string.seller_dashboard), R.drawable.seller_dashboard2))
                    accountRvData.add(AccountMpRvModel(AccountMpRvModel.SELLER_PROFILE, getString(R.string.seller_profile), R.drawable.seller_profile2))
                    accountRvData.add(AccountMpRvModel(AccountMpRvModel.SELLER_ORDERS, getString(R.string.seller_orders), R.drawable.seleer_order2))
//                    accountRvData.add(AccountMpRvModel(AccountMpRvModel.ATTRIBUTES, getString(R.string.attributes), R.drawable.ic_add_attributes))
                    accountRvData.add(AccountMpRvModel(AccountMpRvModel.ADD_NEW_PRODUCT, getString(R.string.add_new_product), R.drawable.seller_add_new2))
                    accountRvData.add(AccountMpRvModel(AccountMpRvModel.SELLER_PRODUCTS, getString(R.string.seller_products), R.drawable.seller_product2))
                    accountRvData.add(AccountMpRvModel(AccountMpRvModel.TRANSACTIONS, getString(R.string.transactions), R.drawable.ic_transactions2))
//                    accountRvData.add(AccountMpRvModel(AccountMpRvModel.TYPE_CUSTOMERS, getString(R.string.customers), R.drawable.ic_vector_person))
//                    accountRvData.add(AccountMpRvModel(AccountMpRvModel.INVOICE_PRINT_TEMPLATE, getString(R.string.invoice_print_template), R.drawable.ic_invoice_print_template))
                    accountRvData.add(AccountMpRvModel(AccountMpRvModel.CHAT_WITH_ADMIN, if (MpAppSharedPref.isAdmin(context!!)) getString(R.string.chat_with_sellers) else getString(R.string.chat_with_admin), R.drawable.seller_chat2))
                }
                if (!MpAppSharedPref.isAdmin(context!!)) {
                    accountRvData.add(AccountMpRvModel(AccountMpRvModel.ASK_QUESTIONS, getString(R.string.ask_questions), R.drawable.seller_ask2))
                }
                updateSellerMenu(accountRvData)
            } else {
                accountRvData.add(AccountMpRvModel(AccountMpRvModel.TYPE_SELLER_STATUS, getString(R.string.become_seller), R.drawable.seller_ask2))
                updateSellerMenu(accountRvData)
            }
        }
        else{

            loginAccount()
        }
    }

    /*Inflate seller menus*/
    private fun updateSellerMenu(accountRvData: ArrayList<AccountMpRvModel>) {
        mContentViewBinding.navDrawerSellerAccountView.viewStub!!.setOnInflateListener { _, inflated ->
            val binding: LayoutSellerMenusBinding? = DataBindingUtil.bind(inflated)
            binding?.navDrawerSellerAccountRv?.layoutManager = GridLayoutManager(context, 4)

            binding?.navDrawerSellerAccountRv?.adapter = AccountMPRvAdapter(this@AccountDetailsFragmentExtend, accountRvData)
            binding?.navDrawerSellerAccountRv?.isNestedScrollingEnabled = false
        }
        mContentViewBinding.navDrawerSellerAccountView.viewStub!!.layoutResource = R.layout.layout_seller_menus
        if (!mContentViewBinding.navDrawerSellerAccountView.isInflated)
            mContentViewBinding.navDrawerSellerAccountView.viewStub!!.inflate()
    }

/*
    override fun setupCMSPagesRv() {
        super.setupCMSPagesRv()
        mContentViewBinding.navDrawerMpLandingPage

        mContentViewBinding.navDrawerMpLandingPage.viewStub!!.setOnInflateListener { _, inflated ->
            val binding: ItemNavDrawerMpLandingPageBinding? = DataBindingUtil.bind(inflated)
            binding?.handler = NavDrawerMpRvHandler()
        }
        mContentViewBinding.navDrawerMpLandingPage.viewStub!!.layoutResource = R.layout.item_nav_drawer_mp_landing_page
        if (!mContentViewBinding.navDrawerMpLandingPage.isInflated)
            mContentViewBinding.navDrawerMpLandingPage.viewStub!!.inflate()

    }
*/
}