package com.webkul.mobikulmp.fragments

import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.fragments.NavDrawerStartFragment
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.adapters.NavDrawerAccountMpRvAdapter
import com.webkul.mobikulmp.databinding.ItemNavDrawerMpLandingPageBinding
import com.webkul.mobikulmp.databinding.LayoutSellerMenusBinding
import com.webkul.mobikulmp.handlers.NavDrawerMpRvHandler
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
class NavDrawerStartFragmentExtend : NavDrawerStartFragment() {

    override fun setupAccountRv() {
        if (AppSharedPref.isLoggedIn(context!!)) {
            super.setupAccountRv()

            val accountRvData: ArrayList<AccountMpRvModel> = ArrayList()

            if (MpAppSharedPref.isSeller(context!!)) {
                if (MpAppSharedPref.isSellerPending(context!!)) {
                    accountRvData.add(AccountMpRvModel(AccountMpRvModel.TYPE_SELLER_STATUS, getString(R.string.nav_seller_pending_status), R.drawable.ic_ask_questions))
                } else {
                    accountRvData.add(AccountMpRvModel(AccountMpRvModel.SELLER_DASHBOARD, getString(R.string.seller_dashboard), R.drawable.ic_seller_dashboard))
                    accountRvData.add(AccountMpRvModel(AccountMpRvModel.SELLER_PROFILE, getString(R.string.seller_profile), R.drawable.ic_seller_profile))
                    accountRvData.add(AccountMpRvModel(AccountMpRvModel.SELLER_ORDERS, getString(R.string.seller_orders), R.drawable.ic_seller_orders))
                    accountRvData.add(AccountMpRvModel(AccountMpRvModel.ATTRIBUTES, getString(R.string.attributes), R.drawable.ic_add_attributes))
                    accountRvData.add(AccountMpRvModel(AccountMpRvModel.ADD_NEW_PRODUCT, getString(R.string.add_new_product), R.drawable.ic_add_new_product))
                    accountRvData.add(AccountMpRvModel(AccountMpRvModel.SELLER_PRODUCTS, getString(R.string.seller_products), R.drawable.ic_seller_products))
                    accountRvData.add(AccountMpRvModel(AccountMpRvModel.TRANSACTIONS, getString(R.string.transactions), R.drawable.ic_transactions))
                    accountRvData.add(AccountMpRvModel(AccountMpRvModel.TYPE_CUSTOMERS, getString(R.string.customers), R.drawable.ic_vector_person))
                    accountRvData.add(AccountMpRvModel(AccountMpRvModel.INVOICE_PRINT_TEMPLATE, getString(R.string.invoice_print_template), R.drawable.ic_invoice_print_template))
                    accountRvData.add(AccountMpRvModel(AccountMpRvModel.CHAT_WITH_ADMIN, if (MpAppSharedPref.isAdmin(context!!)) getString(R.string.chat_with_sellers) else getString(R.string.chat_with_admin), R.drawable.ic_seller_chat_with_admin))
                }
                if (!MpAppSharedPref.isAdmin(context!!)) {
                    accountRvData.add(AccountMpRvModel(AccountMpRvModel.ASK_QUESTIONS, getString(R.string.ask_questions), R.drawable.ic_ask_questions))
                }
                updateSellerMenu(accountRvData)
            } else {
                accountRvData.add(AccountMpRvModel(AccountMpRvModel.TYPE_SELLER_STATUS, getString(R.string.become_seller), R.drawable.ic_ask_questions))
                updateSellerMenu(accountRvData)
            }
        }
    }

    /*Inflate seller menus*/
    private fun updateSellerMenu(accountRvData: ArrayList<AccountMpRvModel>) {
        mContentViewBinding.navDrawerSellerAccountView.viewStub!!.setOnInflateListener { _, inflated ->
            val binding: LayoutSellerMenusBinding? = DataBindingUtil.bind(inflated)
            binding?.navDrawerSellerAccountRv?.adapter = NavDrawerAccountMpRvAdapter(this@NavDrawerStartFragmentExtend, accountRvData)
            binding?.navDrawerSellerAccountRv?.isNestedScrollingEnabled = false
        }
        mContentViewBinding.navDrawerSellerAccountView.viewStub!!.layoutResource = R.layout.layout_seller_menus
        if (!mContentViewBinding.navDrawerSellerAccountView.isInflated)
            mContentViewBinding.navDrawerSellerAccountView.viewStub!!.inflate()
    }

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
}