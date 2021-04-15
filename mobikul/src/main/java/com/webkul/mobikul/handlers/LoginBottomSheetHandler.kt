package com.webkul.mobikul.handlers

import android.content.DialogInterface
import android.content.Intent
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.activities.HomeActivity
import com.webkul.mobikul.activities.LoginAndSignUpActivity
import com.webkul.mobikul.fragments.ForgotPasswordDialogFragment
import com.webkul.mobikul.fragments.LoginBottomSheetFragment
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.helpers.AppSharedPref.Companion.CUSTOMER_PREF
import com.webkul.mobikul.helpers.AppSharedPref.Companion.KEY_CART_COUNT
import com.webkul.mobikul.helpers.AppSharedPref.Companion.KEY_CUSTOMER_BANNER_DOMINANT_COLOR
import com.webkul.mobikul.helpers.AppSharedPref.Companion.KEY_CUSTOMER_BANNER_URL
import com.webkul.mobikul.helpers.AppSharedPref.Companion.KEY_CUSTOMER_EMAIL
import com.webkul.mobikul.helpers.AppSharedPref.Companion.KEY_CUSTOMER_ID
import com.webkul.mobikul.helpers.AppSharedPref.Companion.KEY_CUSTOMER_IMAGE_DOMINANT_COLOR
import com.webkul.mobikul.helpers.AppSharedPref.Companion.KEY_CUSTOMER_IMAGE_URL
import com.webkul.mobikul.helpers.AppSharedPref.Companion.KEY_CUSTOMER_NAME
import com.webkul.mobikul.helpers.AppSharedPref.Companion.KEY_CUSTOMER_TOKEN
import com.webkul.mobikul.helpers.AppSharedPref.Companion.KEY_LOGGED_IN
import com.webkul.mobikul.helpers.AppSharedPref.Companion.KEY_QUOTE_ID
import com.webkul.mobikul.models.user.AddressDetailsData
import com.webkul.mobikul.models.user.LoginFormModel
import com.webkul.mobikul.models.user.LoginResponseModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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

open class LoginBottomSheetHandler(val mFragmentContext: LoginBottomSheetFragment) {

    fun onClickCancelBtn() {
        Utils.hideKeyboard(mFragmentContext.mContentViewBinding.emailTil)
        mFragmentContext.dismiss()
    }

    fun onClickLogin(loginFormModel: LoginFormModel) {
        if (loginFormModel.isFormValidated(mFragmentContext)) {
            Utils.hideKeyboard(mFragmentContext.mContentViewBinding.emailTil)
            mFragmentContext.mContentViewBinding.loading = true
            ApiConnection.login(mFragmentContext.context!!, loginFormModel)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : ApiCustomCallback<LoginResponseModel>(mFragmentContext.context!!, true) {
                        override fun onNext(loginResponseModel: LoginResponseModel) {
                            super.onNext(loginResponseModel)
                            mFragmentContext.mContentViewBinding.loading = false
                            if (loginResponseModel.success) {
                                checkFingerprintData(loginFormModel.isFingerPrintEnable(mFragmentContext), loginResponseModel, loginFormModel.username, loginFormModel.password)
                              /*  val intent = Intent(context, HomeActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                context.startActivity(intent)*/
//                                (mFragmentContext.context as BaseActivity).finish()
                            } else {
                                ToastHelper.showToast(mFragmentContext.context!!, loginResponseModel.message)
                            }
                        }

                        override fun onError(e: Throwable) {
                            super.onError(e)
                            mFragmentContext.mContentViewBinding.loading = false
                            onErrorResponse(e, loginFormModel)
                        }
                    })
        }
    }

    open fun updateSharedPref(loginResponseModel: LoginResponseModel) {
        val customerDataSharedPref = AppSharedPref.getSharedPreferenceEditor(mFragmentContext.context!!, CUSTOMER_PREF)
        customerDataSharedPref.putBoolean(KEY_LOGGED_IN, true)
        customerDataSharedPref.putInt(KEY_QUOTE_ID, 0)
        customerDataSharedPref.putInt(KEY_CART_COUNT, loginResponseModel.cartCount)
        customerDataSharedPref.putString(KEY_CUSTOMER_TOKEN, loginResponseModel.customerToken)
        customerDataSharedPref.putString(KEY_CUSTOMER_ID, loginResponseModel.customerId)
        customerDataSharedPref.putString(KEY_CUSTOMER_NAME, loginResponseModel.customerName)
        customerDataSharedPref.putString(KEY_CUSTOMER_EMAIL, loginResponseModel.customerEmail)
        customerDataSharedPref.putString(KEY_CUSTOMER_IMAGE_URL, loginResponseModel.profileImage)
        customerDataSharedPref.putString(KEY_CUSTOMER_IMAGE_DOMINANT_COLOR, loginResponseModel.profileDominantColor)
        customerDataSharedPref.putString(KEY_CUSTOMER_BANNER_URL, loginResponseModel.bannerImage)
        customerDataSharedPref.putString(KEY_CUSTOMER_BANNER_DOMINANT_COLOR, loginResponseModel.bannerDominantColor)
        customerDataSharedPref.apply()
    }

    private fun onErrorResponse(error: Throwable, loginFormModel: LoginFormModel) {
        AlertDialogHelper.showNewCustomDialog(
                mFragmentContext.context as BaseActivity,
                mFragmentContext.getString(R.string.error),
                NetworkHelper.getErrorMessage(mFragmentContext.context!!, error),
                false,
                mFragmentContext.getString(R.string.try_again),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    onClickLogin(loginFormModel)
                }
                , mFragmentContext.getString(R.string.dismiss)
                , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        })
    }

    fun onClickSignUp() {
        mFragmentContext.dismiss()
        (mFragmentContext.context as LoginAndSignUpActivity).mContentViewBinding.handler!!.onClickSignUp()
    }

    fun onClickForgotPassword(email: String) {
        ForgotPasswordDialogFragment.newInstance(email).show(mFragmentContext.childFragmentManager, ForgotPasswordDialogFragment::class.java.simpleName)
    }


    private fun checkFingerprintData(isFingerPrintEnable: Boolean, loginResponseModel: LoginResponseModel, userName: String, userPassword: String) {
        if (isFingerPrintEnable) {
            if (AppSharedPref.getCustomerFingerUserName(mFragmentContext.context!!).isBlank() || AppSharedPref.getCustomerFingerPassword(mFragmentContext.context!!).isBlank()) {
                AlertDialogHelper.showNewCustomDialog(mFragmentContext.context as BaseActivity?
                        , mFragmentContext.context?.getString(R.string.finger_print_login)
                        , mFragmentContext.context?.getString(R.string.finger_print_login_set_msg)
                        , false
                        , mFragmentContext.context?.getString(R.string.yes)
                        , DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                    setFingerprintData(userName, userPassword)
                    goToDashboard(loginResponseModel)
                }
                        , mFragmentContext.context?.getString(R.string.no)
                        , DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                    goToDashboard(loginResponseModel)
                })
            } else if (AppSharedPref.getCustomerFingerUserName(mFragmentContext.context!!) != userName || AppSharedPref.getCustomerFingerPassword(mFragmentContext.context!!) != userPassword) {
                AlertDialogHelper.showNewCustomDialog(mFragmentContext.context as BaseActivity?
                        , mFragmentContext.context?.getString(R.string.finger_print_login)
                        , mFragmentContext.context?.getString(R.string.finger_print_login_update_msg)
                        , false
                        , mFragmentContext.context?.getString(R.string.yes)
                        , DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                    setFingerprintData(userName, userPassword)
                    goToDashboard(loginResponseModel)
                }
                        , mFragmentContext.context?.getString(R.string.no)
                        , DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                    goToDashboard(loginResponseModel)
                })
            } else {
                goToDashboard(loginResponseModel)
            }
        } else {
            goToDashboard(loginResponseModel)
        }
    }

    private fun goToDashboard(loginResponseModel: LoginResponseModel) {
        FirebaseAnalyticsHelper.logLoginEvent(loginResponseModel.customerName)
        ToastHelper.showToast(mFragmentContext.context!!, mFragmentContext.getString(R.string.logged_in))
        updateSharedPref(loginResponseModel)
        AppSharedPref.setCustomerCachedNewAddress(mFragmentContext.context!!, AddressDetailsData())
        (mFragmentContext.context as LoginAndSignUpActivity).finishActivity()
         val intent = Intent(mFragmentContext.context, HomeActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                mFragmentContext.context!!.startActivity(intent)
    }

    private fun setFingerprintData(userName: String, userPassword: String) {
        mFragmentContext.context?.let { AppSharedPref.setCustomerFingerUserName(it, userName) }
        mFragmentContext.context?.let { AppSharedPref.setCustomerFingerPassword(it, userPassword) }
    }


}