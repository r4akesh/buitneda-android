package com.webkul.mobikulmp.helpers

import android.content.Context
import android.content.Intent
import com.webkul.mobikul.fragments.AccountDetailsFragment
import com.webkul.mobikul.fragments.LoginBottomSheetFragment
import com.webkul.mobikul.fragments.NavDrawerStartFragment
import com.webkul.mobikul.fragments.SignUpBottomSheetFragment
import com.webkul.mobikul.handlers.LoginBottomSheetHandler
import com.webkul.mobikul.handlers.SignUpBottomSheetHandler
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikul.helpers.MobikulApplication
import com.webkul.mobikulmp.activities.*
import com.webkul.mobikulmp.fragment.AuctionProductListFragment
import com.webkul.mobikulmp.fragments.AccountDetailsFragmentExtend
import com.webkul.mobikulmp.fragments.NavDrawerStartFragmentExtend
import com.webkul.mobikulmp.fragments.SellerAuctionFragment
import com.webkul.mobikulmp.handlers.LoginBottomSheetExtendedHandler
import com.webkul.mobikulmp.handlers.SignUpBottomSheetExtendHandler

/**
 *
 * Webkul Software.
 *
 * @category Mobikul
 * @package com.webkul.mobikulmp.helpers
 * @author Webkul
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 15/5/19
 *
 */


class MarketplaceApplication : MobikulApplication() {

    override fun getCatalogActivity(context: Context): Intent {
        return Intent(context, CatalogProductExtendedActivity::class.java)
    }

    override fun getProductDetailsActivity(context: Context): Intent {
        return Intent(context, ProductDetailsExtendedActivity::class.java)
    }

    override fun getLoginBottomSheetHandler(loginBottomSheetFragment: LoginBottomSheetFragment): LoginBottomSheetHandler {
        return LoginBottomSheetExtendedHandler(loginBottomSheetFragment)
    }

    override fun getSignUpBottomSheetHandler(signUpBottomSheetFragment: SignUpBottomSheetFragment): SignUpBottomSheetHandler {
        return SignUpBottomSheetExtendHandler(signUpBottomSheetFragment)
    }

    override fun getNavDrawerStartFragment(): NavDrawerStartFragment {
        return NavDrawerStartFragmentExtend()
    }

    override fun gettAccounntDetailsFragment(): AccountDetailsFragment {
        return AccountDetailsFragmentExtend()
    }

    override fun getSellerDashboardActivity(context: Context): Intent? {
        return Intent(context, SellerDashboardActivity::class.java)
    }

    override fun getSellerOrdersActivity(context: Context): Intent? {
        return Intent(context, SellerOrdersListActivity::class.java)
    }

    override fun getSellerOrderDetailsActivity(context: Context): Intent? {
        return Intent(context, SellerOrderDetailsActivity::class.java)
    }

    override fun getAskAdminActivity(context: Context): Intent {
        return Intent(context, AskQuestionsToAdminActivity::class.java)
    }

    override fun getBecomeSellerActivity(context: Context): Intent? {
        return Intent(context, BecomeSellerActivity::class.java)
    }

    override fun getCreateAttributeActivity(context: Context): Intent? {
        return Intent(context, CreateAttributeActivity::class.java)
    }

    override fun getSellerProfileEditActivity(context: Context): Intent? {
        return Intent(context, SellerProfileEditActivity::class.java)
    }

    override fun getSellerAddProductActivity(context: Context): Intent? {
        return Intent(context, SellerAddProductActivity::class.java)
    }

    override fun getSellerProductsListActivity(context: Context): Intent? {
        return Intent(context, SellerProductsListActivity::class.java)
    }

    override fun getSellerTransactionsListActivity(context: Context): Intent? {
        return Intent(context, SellerTransactionsListActivity::class.java)
    }

    override fun getManagePrintPdfHeaderActivity(context: Context): Intent? {
        return Intent(context, ManagePrintPdfHeaderActivity::class.java)
    }

    override fun getSellerProfileActivity(context: Context): Intent? {
        return Intent(context, SellerProfileActivity::class.java)
    }

    override fun getChatRelatedActivity(mContext: Context): Intent? {
        val intent: Intent
        if (MpAppSharedPref.isAdmin(mContext)) {
            intent = Intent(mContext, ChatWithSellersActivity::class.java)
        } else {
            intent = Intent(mContext, ChatActivity::class.java)
            intent.putExtra("user_name", AppSharedPref.getCustomerName(mContext))
            intent.putExtra("user_id", AppSharedPref.getCustomerToken(mContext))
        }
        return intent
    }

    override fun getCustomerListActivity(mContext: Context): Intent? {
        return Intent(mContext, CustomerListActivity::class.java)
    }

    override fun getLoginAndSignUpActivity(mContext: Context): Intent? {
        return Intent(mContext, LoginAndSignUpExtendedActivity::class.java)
    }

    override fun getTempFragment(): SellerAuctionFragment? {
        return SellerAuctionFragment()
    }

}