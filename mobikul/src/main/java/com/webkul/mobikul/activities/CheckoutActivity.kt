/*
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

package com.webkul.mobikul.activities

import android.os.Bundle
import android.view.Menu
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.ActivityCheckoutBinding
import com.webkul.mobikul.fragments.PaymentInfoFragment
import com.webkul.mobikul.fragments.ShippingInfoFragment
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_IS_CART_ITEM
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_IS_VIRTUAL_CART
import com.webkul.mobikul.helpers.FirebaseAnalyticsHelper
import com.webkul.mobikul.models.catalog.CartItem
import com.webkul.mobikul.models.checkout.CheckoutAddressInfoResponseModel

class CheckoutActivity : BaseActivity(), PaymentInfoFragment.OnDetachInterface {
    lateinit var mContentViewBinding: ActivityCheckoutBinding
    var mIsVirtual = false
    var cartItem : ArrayList<CartItem> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_checkout)
        startInitialization()


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    private fun startInitialization() {
        if(intent!=null && intent.hasExtra(BUNDLE_KEY_IS_VIRTUAL_CART)){
            mIsVirtual = intent.getBooleanExtra(BUNDLE_KEY_IS_VIRTUAL_CART, false)
        }


        if(intent!=null && intent.hasExtra(BUNDLE_KEY_IS_CART_ITEM)){
            cartItem = intent.getSerializableExtra(BUNDLE_KEY_IS_CART_ITEM) as ArrayList<CartItem>
        }
        if (mIsVirtual) {
            setupPaymentInfoFragment()
        } else {
            setupShippingInfoFragment()
        }

        FirebaseAnalyticsHelper.logCheckoutBeginEvent(cartItem)
    }

    private fun setupShippingInfoFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.container, ShippingInfoFragment(), ShippingInfoFragment::class.java.simpleName)
        fragmentTransaction.commit()
    }

    fun setupPaymentInfoFragment(shippingMethod: String = "", checkoutAddressData: CheckoutAddressInfoResponseModel? = null) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val paymentInfoFragment = PaymentInfoFragment.newInstance(shippingMethod, checkoutAddressData)

        paymentInfoFragment.setOnDetachInterface(this)
        fragmentTransaction.add(R.id.container, paymentInfoFragment, PaymentInfoFragment::class.java.simpleName)

        if (checkoutAddressData != null)
            fragmentTransaction.addToBackStack(PaymentInfoFragment::class.java.simpleName)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onFragmentDetached() {
        val shippingInfoFragment: ShippingInfoFragment? = supportFragmentManager.findFragmentByTag(ShippingInfoFragment::class.java.simpleName) as? ShippingInfoFragment
        shippingInfoFragment?.let {
            it.callApi()
        }
    }

}