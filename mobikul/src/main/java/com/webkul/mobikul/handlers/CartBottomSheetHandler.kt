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

package com.webkul.mobikul.handlers

import android.content.DialogInterface
import android.content.Intent
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.activities.CheckoutActivity
import com.webkul.mobikul.activities.HomeActivity
import com.webkul.mobikul.activities.MyWishListActivity
import com.webkul.mobikul.adapters.CartItemsRvAdapter
import com.webkul.mobikul.fragments.CartBottomSheetFragment
import com.webkul.mobikul.fragments.HomeFragment
import com.webkul.mobikul.fragments.ProceedCheckoutBottomSheetFragment
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_IS_CART_ITEM
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_IS_VIRTUAL_CART
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray

class CartBottomSheetHandler(val mFragmentContext: CartBottomSheetFragment) {

    fun onClickCancelBtn() {
//        Utils.hideKeyboard(mFragmentContext.mContentViewBinding.cartHeading)
//        HomeActivity.mContentViewBinding.bottomAppCl.visibility=View.VISIBLE
//        mFragmentContext.dismiss()
//       var fragment :Fragment=HomeFragment()
//        mFragmentContext.fragmentManager?.beginTransaction()?.replace(R.id.main_frame, fragment!!, fragment.javaClass.simpleName)!!.commit()
//        HomeActivity.mContentViewBinding.bottomNavView.menu.findItem(R.id.bottom_home).isChecked=true
        if (mFragmentContext.context is HomeActivity) {
            (mFragmentContext.context as HomeActivity).onBackPressed()
        } else {
            mFragmentContext.dismiss()
        }

    }

    fun onClickGoToWishList() {
        if (mFragmentContext.context is MyWishListActivity) {
            mFragmentContext.dismiss()
        } else {
            mFragmentContext.startActivity(Intent(mFragmentContext.context, MyWishListActivity::class.java))
        }
    }

    fun onClickDiscountCodeLabel() {
        if (mFragmentContext.mContentViewBinding.discountCode.visibility == View.VISIBLE) {
            mFragmentContext.mContentViewBinding.discountCode.visibility = View.GONE
            mFragmentContext.mContentViewBinding.discountCodeHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mFragmentContext.context!!, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mFragmentContext.mContentViewBinding.discountCode.visibility = View.VISIBLE
            mFragmentContext.mContentViewBinding.discountCodeHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mFragmentContext.context!!, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mFragmentContext.mContentViewBinding.scrollView.top + mFragmentContext.mContentViewBinding.discountCodeHeading.top
                mFragmentContext.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }

    fun onClickApplyOrRemoveCouponBtn(couponCode: String, isRemoveCoupon: Boolean) {
        if (!couponCode.isBlank()) {
            Utils.hideKeyboard(mFragmentContext.mContentViewBinding.discountCode)
            mFragmentContext.mContentViewBinding.loading = true
            ApiConnection.applyOrRemoveCoupon(mFragmentContext.context!!, couponCode.trim(), isRemoveCoupon)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : ApiCustomCallback<BaseModel>(mFragmentContext.context!!, true) {
                        override fun onNext(applyOrRemoveCouponResponse: BaseModel) {
                            super.onNext(applyOrRemoveCouponResponse)
                            mFragmentContext.mContentViewBinding.loading = false
                            ToastHelper.showToast(mFragmentContext.context!!, applyOrRemoveCouponResponse.message)
                            if (applyOrRemoveCouponResponse.success) {
                                mFragmentContext.callApi()
                            }
                        }

                        override fun onError(e: Throwable) {
                            super.onError(e)
                            mFragmentContext.mContentViewBinding.loading = false
                            onErrorResponse(e)
                        }
                    })
        } else {
            Utils.hideKeyboard(mFragmentContext.mContentViewBinding.discountCode)
            ToastHelper.showToast(mFragmentContext.context!!, mFragmentContext.getString(R.string.please_enter_coupon_code))
        }
    }

    private fun onErrorResponse(error: Throwable) {
        AlertDialogHelper.showNewCustomDialog(
                mFragmentContext.context as BaseActivity,
                mFragmentContext.getString(R.string.error),
                NetworkHelper.getErrorMessage(mFragmentContext.context!!, error),
                false,
                mFragmentContext.getString(R.string.ok),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                }
                , null
                , null)
    }

    fun onClickUpdateCartBtn(itemIdList: JSONArray, qtyList: JSONArray) {
        mFragmentContext.mContentViewBinding.loading = true
        ApiConnection.updateCart(mFragmentContext.context as BaseActivity, itemIdList, qtyList)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<BaseModel>(mFragmentContext.context!!, true) {
                    override fun onNext(updateCartResponse: BaseModel) {
                        super.onNext(updateCartResponse)
                        mFragmentContext.mContentViewBinding.loading = false
                        ToastHelper.showToast(mFragmentContext.context!!, updateCartResponse.message)
                        if (updateCartResponse.success) {
                            (mFragmentContext.context as BaseActivity).updateCartCount(updateCartResponse.cartCount)
                            mFragmentContext.callApi()
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mFragmentContext.mContentViewBinding.loading = false
                        onErrorResponse(e)
                    }
                })
    }

    fun onClickContinueShoppingBtn() {
        if (mFragmentContext.context is HomeActivity) {
            (mFragmentContext.context as HomeActivity).onBackPressed()
        } else {
            mFragmentContext.dismiss()
        }    }

    fun onClickEmptyCartBtn() {
        AlertDialogHelper.showNewCustomDialog(
                mFragmentContext.context as BaseActivity,
                mFragmentContext.getString(R.string.empty_shopping_cart),
                mFragmentContext.getString(R.string.empty_shopping_cart_msg),
                false,
                mFragmentContext.getString(R.string.yes_empty_it),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    mFragmentContext.mContentViewBinding.loading = true
                    ApiConnection.emptyCart(mFragmentContext.context!!)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(object : ApiCustomCallback<BaseModel>(mFragmentContext.context!!, true) {
                                override fun onNext(emptyCartResponse: BaseModel) {
                                    super.onNext(emptyCartResponse)
                                    mFragmentContext.mContentViewBinding.loading = false
                                    if (emptyCartResponse.success) {
                                        (mFragmentContext.context as BaseActivity).updateCartCount(emptyCartResponse.cartCount)
                                        mFragmentContext.callApi()
                                    } else {
                                        ToastHelper.showToast(mFragmentContext.context!!, emptyCartResponse.message)
                                    }
                                }

                                override fun onError(e: Throwable) {
                                    super.onError(e)
                                    mFragmentContext.mContentViewBinding.loading = false
                                    onErrorResponse(e)
                                }
                            })
                }
                , mFragmentContext.getString(R.string.cancel),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                })
    }

    fun onClickPriceDetailsLabel() {
        if (mFragmentContext.mContentViewBinding.priceDetails.visibility == View.VISIBLE) {
            mFragmentContext.mContentViewBinding.priceDetails.visibility = View.GONE
            mFragmentContext.mContentViewBinding.priceDetailsHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mFragmentContext.context!!, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mFragmentContext.mContentViewBinding.priceDetails.visibility = View.VISIBLE
            mFragmentContext.mContentViewBinding.priceDetailsHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mFragmentContext.context!!, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mFragmentContext.mContentViewBinding.scrollView.top + mFragmentContext.mContentViewBinding.priceDetailsHeading.top
                mFragmentContext.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }

    fun onClickProceedBtn() {
        if ((mFragmentContext.mContentViewBinding.cartItemsRv.adapter as CartItemsRvAdapter).isCartItemQtyChanged()) {
            ToastHelper.showToast(mFragmentContext.context!!, mFragmentContext.getString(R.string.error_update_cart_to_proceed))
        }else if(AppSharedPref.isLoggedIn(mFragmentContext.context!!)) {
            val intent = Intent(mFragmentContext.context!!, CheckoutActivity::class.java)
            intent.putExtra(BUNDLE_KEY_IS_VIRTUAL_CART, mFragmentContext.mContentViewBinding.data!!.isVirtual)
            intent.putExtra(BUNDLE_KEY_IS_CART_ITEM,mFragmentContext.mContentViewBinding.data!!.items)
            mFragmentContext.startActivity(intent)
        } else {
            val proceedCheckoutBottomSheetFragment =
                    ProceedCheckoutBottomSheetFragment.newInstance(
                            mFragmentContext.mContentViewBinding.data!!.isVirtual,
                            mFragmentContext.mContentViewBinding.data!!.isAllowedGuestCheckout,
                            mFragmentContext.mContentViewBinding.data!!.containsDownloadableProducts,
                            mFragmentContext.mContentViewBinding.data!!.canGuestCheckoutDownloadable)
            proceedCheckoutBottomSheetFragment.show(mFragmentContext.childFragmentManager, proceedCheckoutBottomSheetFragment.tag)
        }
    }
}