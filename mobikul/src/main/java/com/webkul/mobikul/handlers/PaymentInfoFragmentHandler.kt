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
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.AddEditAddressActivity
import com.webkul.mobikul.activities.CheckoutActivity
import com.webkul.mobikul.activities.OrderPlacedActivity
import com.webkul.mobikul.activities.WebPaymentActivity
import com.webkul.mobikul.fragments.AddressListFragment
import com.webkul.mobikul.fragments.PaymentInfoFragment
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CANCEL_URL
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_FAILURE_URL
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_SAVE_ORDER_RESPONSE
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_START_PAYMENT_URL
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_SUCCESS_URL
import com.webkul.mobikul.helpers.ConstantsHelper.RC_PAYMENT
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.models.checkout.SaveOrderResponseModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject


class PaymentInfoFragmentHandler(val mFragmentContext: PaymentInfoFragment) {

    private var mSaveOrderResponseModel: SaveOrderResponseModel = SaveOrderResponseModel()

    fun onClickChangeAddress() {
        val fragmentManager = mFragmentContext.activity!!.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        fragmentTransaction.add(android.R.id.content, AddressListFragment.newInstance(mFragmentContext.mContentViewBinding.checkoutAddressData!!))
        fragmentTransaction.addToBackStack(AddressListFragment::class.java.simpleName)
        fragmentTransaction.commit()
    }

    fun onClickAddEditNewAddress() {
        val intent = Intent(mFragmentContext.context, AddEditAddressActivity::class.java)
        intent.putExtra(BundleKeysHelper.BUNDLE_KEY_ADDRESS_DATA, mFragmentContext.mContentViewBinding.checkoutAddressData!!.newAddressData)
        mFragmentContext.startActivityForResult(intent, ConstantsHelper.RC_ADD_EDIT_ADDRESS)
    }

    fun onClickPlaceOrderBtn() {
        if (mFragmentContext.mContentViewBinding.payWithWalletCb.isChecked && mFragmentContext.mContentViewBinding.walletData!!.walletData.unformattedLeftAmountToPay == 0.toDouble()) {
            mFragmentContext.mContentViewBinding.data!!.selectedPaymentMethod = ApplicationConstants.PAYMENT_MP_WALLET
        }
        if (!mFragmentContext.mContentViewBinding.checkoutAddressData!!.sameAsShipping && mFragmentContext.mContentViewBinding.checkoutAddressData!!.selectedAddressData?.id == "0") {
            ToastHelper.showToast(mFragmentContext.context!!, mFragmentContext.getString(R.string.payment_address_select_error))
            Utils.showShakeError(mFragmentContext.context!!, mFragmentContext.mContentViewBinding.addEditAddBtn)
            mFragmentContext.mContentViewBinding.mainScroller.smoothScrollTo(0, 0)
        } else if (mFragmentContext.mContentViewBinding.data!!.selectedPaymentMethod.isBlank()) {
            ToastHelper.showToast(mFragmentContext.context!!, mFragmentContext.getString(R.string.payment_method_select_error))
            Utils.showShakeError(mFragmentContext.context!!, mFragmentContext.mContentViewBinding.paymentMethodRg)
            val scrollTo = mFragmentContext.mContentViewBinding.mainScroller.top + mFragmentContext.mContentViewBinding.paymentMethodRg.top - 100
            mFragmentContext.mContentViewBinding.mainScroller.smoothScrollTo(0, scrollTo)
        } else {
            (mFragmentContext.context as CheckoutActivity).mContentViewBinding.loading = true
            var discountObject = JSONObject()
            if(mFragmentContext.mContentViewBinding.walletData!=null){
                discountObject.put("flag", mFragmentContext.mContentViewBinding.walletData!!.walletData.walletDiscount.flag)
                discountObject.put("amount", mFragmentContext.mContentViewBinding.walletData!!.walletData.walletDiscount.amount)
                discountObject.put("type", mFragmentContext.mContentViewBinding.walletData!!.walletData.walletDiscount.type)
                discountObject.put("grand_total", mFragmentContext.mContentViewBinding.walletData!!.walletData.walletDiscount.grandTotal)
                discountObject.put("leftinWallet", mFragmentContext.mContentViewBinding.walletData!!.walletData.walletDiscount.leftinWallet)
            }
            ApiConnection.placeOrder(mFragmentContext.context as CheckoutActivity,
                    mFragmentContext.mContentViewBinding.data!!.selectedPaymentMethod,
                    mFragmentContext.mContentViewBinding.checkoutAddressData!!.getNewAddressData(),
                    discountObject)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : ApiCustomCallback<SaveOrderResponseModel>(mFragmentContext.context as CheckoutActivity, true) {
                        override fun onNext(saveOrderResponseModel: SaveOrderResponseModel) {
                            super.onNext(saveOrderResponseModel)
                            (context as CheckoutActivity).mContentViewBinding.loading = false
                            if (saveOrderResponseModel.success) {
                                onSuccessfulResponse(saveOrderResponseModel)
                            } else {
                                onFailureResponse(saveOrderResponseModel)
                            }
                        }

                        override fun onError(e: Throwable) {
                            super.onError(e)
                            (context as CheckoutActivity).mContentViewBinding.loading = false
                            onErrorResponse(e)
                        }
                    })
        }
    }

    private fun onSuccessfulResponse(saveOrderResponseModel: SaveOrderResponseModel) {
        mSaveOrderResponseModel = saveOrderResponseModel
        FirebaseAnalyticsHelper.logECommercePurchaseEvent(saveOrderResponseModel.incrementId!!,saveOrderResponseModel.orderId!!)
        AppSharedPref.setQuoteId(mFragmentContext.context!!, 0)
        AppSharedPref.setCartCount(mFragmentContext.context!!, 0)
        if (mSaveOrderResponseModel.webview) {
            val intent = Intent(mFragmentContext.context, WebPaymentActivity::class.java)
            intent.putExtra(BUNDLE_KEY_START_PAYMENT_URL, mSaveOrderResponseModel.redirectUrl)
            intent.putExtra(BUNDLE_KEY_SUCCESS_URL, mSaveOrderResponseModel.successUrl)
            intent.putExtra(BUNDLE_KEY_CANCEL_URL, mSaveOrderResponseModel.cancelUrl)
            intent.putExtra(BUNDLE_KEY_FAILURE_URL, mSaveOrderResponseModel.failureUrl)
            mFragmentContext.startActivityForResult(intent, RC_PAYMENT)
        } else {
            when (mFragmentContext.mContentViewBinding.data!!.selectedPaymentMethod) {

                else -> onPaymentResponse()
            }
        }
    }

    fun onPaymentResponse() {
        val intent = Intent(mFragmentContext.context, OrderPlacedActivity::class.java)
        intent.putExtra(BUNDLE_KEY_SAVE_ORDER_RESPONSE, mSaveOrderResponseModel)
        mFragmentContext.startActivity(intent)
    }

    private fun onFailureResponse(saveOrderResponseModel: SaveOrderResponseModel) {
        if (saveOrderResponseModel.cartCount == 0) {
            AlertDialogHelper.showNewCustomDialog(
                    (mFragmentContext.context as CheckoutActivity),
                    mFragmentContext.getString(R.string.error),
                    saveOrderResponseModel.message,
                    false,
                    mFragmentContext.getString(R.string.ok),
                    DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        (mFragmentContext.context as CheckoutActivity).finish()
                    })
        } else {
            AlertDialogHelper.showNewCustomDialog(
                    (mFragmentContext.context as CheckoutActivity),
                    mFragmentContext.getString(R.string.error),
                    saveOrderResponseModel.message,
                    false,
                    mFragmentContext.getString(R.string.try_again),
                    DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        onClickPlaceOrderBtn()
                    }
                    , mFragmentContext.getString(R.string.dismiss)
                    , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            })
        }
    }

    private fun onErrorResponse(error: Throwable) {
        AlertDialogHelper.showNewCustomDialog(
                (mFragmentContext.context as CheckoutActivity),
                mFragmentContext.getString(R.string.error),
                NetworkHelper.getErrorMessage((mFragmentContext.context as CheckoutActivity), error),
                false,
                mFragmentContext.getString(R.string.try_again),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    onClickPlaceOrderBtn()
                }
                , mFragmentContext.getString(R.string.dismiss)
                , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        })
    }


    fun onClickDiscountCodeLabel() {
        if (mFragmentContext.mContentViewBinding.discountCode.visibility == View.VISIBLE) {
            mFragmentContext.mContentViewBinding.discountCode.visibility = View.GONE
            mFragmentContext.mContentViewBinding.discountCodeHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mFragmentContext.context!!, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mFragmentContext.mContentViewBinding.discountCode.visibility = View.VISIBLE
            mFragmentContext.mContentViewBinding.discountCodeHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mFragmentContext.context!!, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mFragmentContext.mContentViewBinding.mainScroller.top + mFragmentContext.mContentViewBinding.discountCodeHeading.top
                mFragmentContext.mContentViewBinding.mainScroller.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }


    fun onClickApplyOrRemoveCouponBtn(couponCode: String, isRemoveCoupon: Boolean) {
        if (!couponCode.isBlank()) {
            Utils.hideKeyboard(mFragmentContext.mContentViewBinding.discountCode)
            (mFragmentContext.context as CheckoutActivity).mContentViewBinding.loading = true
            ApiConnection.applyOrRemoveCoupon(mFragmentContext.context!!, couponCode, isRemoveCoupon)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : ApiCustomCallback<BaseModel>(mFragmentContext.context!!, true) {
                        override fun onNext(applyOrRemoveCouponResponse: BaseModel) {
                            super.onNext(applyOrRemoveCouponResponse)
                            (mFragmentContext.context as CheckoutActivity).mContentViewBinding.loading = false
                            ToastHelper.showToast(mFragmentContext.context!!, applyOrRemoveCouponResponse.message)
                            if (applyOrRemoveCouponResponse.success) {
                                mFragmentContext.callApi()
                                mFragmentContext.appliedCoupon = true
                            }
                        }

                        override fun onError(e: Throwable) {
                            super.onError(e)
                            (mFragmentContext.context as CheckoutActivity).mContentViewBinding.loading = false
                            onErrorResponse(e)
                        }
                    })
        } else {
            Utils.hideKeyboard(mFragmentContext.mContentViewBinding.discountCode)
            ToastHelper.showToast(mFragmentContext.context!!, mFragmentContext.getString(R.string.please_enter_coupon_code))
        }
    }

}