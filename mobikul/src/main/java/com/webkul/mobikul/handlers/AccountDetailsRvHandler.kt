package com.webkul.mobikul.handlers

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.*
import com.webkul.mobikul.fragments.AccountDetailsFragment
import com.webkul.mobikul.helpers.ConstantsHelper
import com.webkul.mobikul.helpers.ConstantsHelper.RC_QR_LOGIN
import com.webkul.mobikul.helpers.MobikulApplication
import com.webkul.mobikul.helpers.ToastHelper
import com.webkul.mobikul.models.user.AccountRvModel.Companion.ACCOUNT_INFORMATION
import com.webkul.mobikul.models.user.AccountRvModel.Companion.ADDRESS_BOOK
import com.webkul.mobikul.models.user.AccountRvModel.Companion.AUCTION_PRODUCT
import com.webkul.mobikul.models.user.AccountRvModel.Companion.DASHBOARD
import com.webkul.mobikul.models.user.AccountRvModel.Companion.DOWNLOADABLE_PRODUCTS
import com.webkul.mobikul.models.user.AccountRvModel.Companion.LOG_IN
import com.webkul.mobikul.models.user.AccountRvModel.Companion.ORDERS
import com.webkul.mobikul.models.user.AccountRvModel.Companion.PRODUCT_REVIEWS
import com.webkul.mobikul.models.user.AccountRvModel.Companion.QR_CODE_LOGIN
import com.webkul.mobikul.models.user.AccountRvModel.Companion.SUGGESTION
import com.webkul.mobikul.models.user.AccountRvModel.Companion.WALLET
import com.webkul.mobikul.models.user.AccountRvModel.Companion.WHATS_APP
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

class AccountDetailsRvHandler(val mFragmentContext: AccountDetailsFragment) {

    fun onClickRvItem(type: Int) {
        when (type) {
            DASHBOARD -> {
                mFragmentContext.startActivity(Intent(mFragmentContext.context, DashboardActivity::class.java))

            }
            LOG_IN -> {
//                mFragmentContext.startActivity(Intent(mFragmentContext.context, DashboardActivity::class.java))
//
                mFragmentContext.activity?.startActivityForResult((mFragmentContext.activity!!.application as MobikulApplication).getLoginAndSignUpActivity(mFragmentContext.context!!), ConstantsHelper.RC_LOGIN)

            }
            WISH_LIST -> {
                mFragmentContext.startActivity(Intent(mFragmentContext.context, MyWishListActivity::class.java))
            }
            ORDERS -> {
                mFragmentContext.startActivity(Intent(mFragmentContext.context, MyOrdersActivity::class.java))
            }
            SUGGESTION -> {
                mFragmentContext.startActivity(Intent(mFragmentContext.context, ProductNotFound::class.java))
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

            AUCTION_PRODUCT -> {
                mFragmentContext.startActivity(Intent(mFragmentContext.context, AuctionFragmentActivity::class.java))
            }
            WHATS_APP -> {

                val intent = Intent(Intent.ACTION_VIEW)
                intent.setPackage("com.whatsapp")
                intent.data = Uri.parse("https://api.whatsapp.com/send?phone=${mFragmentContext.context?.getString(R.string.whats_app_number)}")
                if (mFragmentContext.context!!.packageManager.resolveActivity(intent, 0) != null) {
                    mFragmentContext.startActivity(intent)
                } else {
                    ToastHelper.showToast(mFragmentContext.context!!, mFragmentContext.context!!.getString(R.string.please_install_whatsapp))
                    val url = "https://play.google.com/store/search?q=whatsapp&c=apps"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    mFragmentContext.startActivity(intent)
                }

             /*
                              val contact = "+244923474222" // use country code with your phone number

              val url = "https://api.whatsapp.com/send?phone=$contact"
                try {
                    val pm: PackageManager = mFragmentContext.context!!.getPackageManager()
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    mFragmentContext.startActivity(i)
                } catch (e: PackageManager.NameNotFoundException) {
                    Toast.makeText(mFragmentContext.context!!,e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }*/

            }

        }
//        (mFragmentContext.context as HomeActivity).mContentViewBinding.drawerLayout.closeDrawers()
    }
}