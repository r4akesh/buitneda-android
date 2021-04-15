package com.webkul.mobikul.handlers

import android.content.Intent
import com.webkul.mobikul.activities.*
import com.webkul.mobikul.fragments.NavDrawerStartFragment
import com.webkul.mobikul.helpers.ConstantsHelper.RC_QR_LOGIN
import com.webkul.mobikul.models.user.AccountRvModel
import com.webkul.mobikul.models.user.AccountRvModel.Companion.ACCOUNT_INFORMATION
import com.webkul.mobikul.models.user.AccountRvModel.Companion.ADDRESS_BOOK
import com.webkul.mobikul.models.user.AccountRvModel.Companion.DASHBOARD
import com.webkul.mobikul.models.user.AccountRvModel.Companion.DOWNLOADABLE_PRODUCTS
import com.webkul.mobikul.models.user.AccountRvModel.Companion.ORDERS
import com.webkul.mobikul.models.user.AccountRvModel.Companion.PRODUCT_REVIEWS
import com.webkul.mobikul.models.user.AccountRvModel.Companion.QR_CODE_LOGIN
import com.webkul.mobikul.models.user.AccountRvModel.Companion.WALLET
import com.webkul.mobikul.models.user.AccountRvModel.Companion.WISH_LIST
import com.webkul.mobikul.wallet.activities.WalletActivity

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

class AccountRvHandler(val mFragmentContext: NavDrawerStartFragment) {

    fun onClickRvItem(type: Int) {
        when (type) {
            DASHBOARD -> {
                mFragmentContext.startActivity(Intent(mFragmentContext.context, DashboardActivity::class.java))
            }
            WISH_LIST -> {
                mFragmentContext.startActivity(Intent(mFragmentContext.context, MyWishListActivity::class.java))
            }
            ORDERS -> {
                mFragmentContext.startActivity(Intent(mFragmentContext.context, MyOrdersActivity::class.java))
            }
            DOWNLOADABLE_PRODUCTS -> {
                mFragmentContext.startActivity(Intent(mFragmentContext.context, MyDownloadableProductsActivity::class.java))
            }
            PRODUCT_REVIEWS -> {
                mFragmentContext.startActivity(Intent(mFragmentContext.context, MyReviewListActivity::class.java))
            }
            ADDRESS_BOOK -> {
                mFragmentContext.startActivity(Intent(mFragmentContext.context, AddressBookActivity::class.java))
            }
            ACCOUNT_INFORMATION -> {
                mFragmentContext.startActivity(Intent(mFragmentContext.context, AccountInfoActivity::class.java))
            }
            QR_CODE_LOGIN -> {
                (mFragmentContext.activity as BaseActivity).openScanner(RC_QR_LOGIN)
            }
            WALLET -> {
                mFragmentContext.startActivity(Intent(mFragmentContext.context, WalletActivity::class.java))
            }

        }
//        (mFragmentContext.context as HomeActivity).mContentViewBinding.drawerLayout.closeDrawers()
    }
}