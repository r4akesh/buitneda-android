/*
 * Webkul Software.
 *
 * Kotlin
 *
 * @author Webkul <support@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2019 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

package com.webkul.mobikul.handlers

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import com.google.android.material.textfield.TextInputEditText
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.activities.HomeActivity
import com.webkul.mobikul.activities.LoginAndSignUpActivity
import com.webkul.mobikul.activities.OrderPlacedActivity
import com.webkul.mobikul.fragments.SignUpBottomSheetFragment
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.models.user.AddressDetailsData
import com.webkul.mobikul.models.user.SignUpFormModel
import com.webkul.mobikul.models.user.SignUpResponseModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

open class SignUpBottomSheetHandler(val mFragmentContext: SignUpBottomSheetFragment) {

    fun onClickCancelBtn() {
        Utils.hideKeyboard(mFragmentContext.mContentViewBinding.prefix)
        mFragmentContext.dismiss()
    }

    fun onClickSignUp(signUpFormModel: SignUpFormModel) {
        if (signUpFormModel.isFormValidated(mFragmentContext)) {
            Utils.hideKeyboard(mFragmentContext.mContentViewBinding.prefix)
            mFragmentContext.mContentViewBinding.loading = true
            if (signUpFormModel.signUpAsSeller){
                signUpFormModel.shopURL = signUpFormModel.firstName + "-" + UUID.randomUUID().toString()
            }

            ApiConnection.signUp(mFragmentContext.context!!, signUpFormModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :
                    ApiCustomCallback<SignUpResponseModel>(mFragmentContext.context!!, true) {
                    override fun onNext(signUpResponseModel: SignUpResponseModel) {
                        super.onNext(signUpResponseModel)
                        mFragmentContext.mContentViewBinding.loading = false
                        ToastHelper.showToast(
                            mFragmentContext.context!!,
                            signUpResponseModel.message
                        )
                        if (signUpResponseModel.success) {
                            FirebaseAnalyticsHelper.logSignUpEvent(
                                signUpResponseModel.customerName,
                                signUpResponseModel.customerEmail
                            )
                            AppSharedPref.setCustomerCachedNewAddress(
                                mFragmentContext.context!!,
                                AddressDetailsData()
                            )
                            updateSharedPref(signUpResponseModel)
                            if (mFragmentContext.context is LoginAndSignUpActivity) {
                                (mFragmentContext.context as LoginAndSignUpActivity).finishActivity()
                                val intent =
                                    Intent(mFragmentContext.context, HomeActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                mFragmentContext.context!!.startActivity(intent)
                            } else if (mFragmentContext.context is OrderPlacedActivity) {
                                (mFragmentContext.context as OrderPlacedActivity).onCreateAccountSuccess()
                                mFragmentContext.dismiss()
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mFragmentContext.mContentViewBinding.loading = false
                        onErrorResponse(e, signUpFormModel)
                    }
                })
        }
    }

    private fun onErrorResponse(error: Throwable, signUpFormModel: SignUpFormModel) {
        AlertDialogHelper.showNewCustomDialog(
            mFragmentContext.context!! as BaseActivity,
            mFragmentContext.getString(R.string.error),
            NetworkHelper.getErrorMessage(mFragmentContext.context!!, error),
            false,
            mFragmentContext.getString(R.string.try_again),
            DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                onClickSignUp(signUpFormModel)
            },
            mFragmentContext.getString(R.string.dismiss),
            DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            })
    }

    open fun updateSharedPref(signUpResponseModel: SignUpResponseModel) {
        val customerDataSharedPref = AppSharedPref.getSharedPreferenceEditor(
            mFragmentContext.context!!,
            AppSharedPref.CUSTOMER_PREF
        )
        customerDataSharedPref.putBoolean(AppSharedPref.KEY_LOGGED_IN, true)
        customerDataSharedPref.putInt(AppSharedPref.KEY_QUOTE_ID, 0)
        customerDataSharedPref.putInt(AppSharedPref.KEY_CART_COUNT, signUpResponseModel.cartCount)
        customerDataSharedPref.putString(
            AppSharedPref.KEY_CUSTOMER_TOKEN,
            signUpResponseModel.customerToken
        )
        customerDataSharedPref.putString(
            AppSharedPref.KEY_CUSTOMER_ID,
            signUpResponseModel.customerId
        )
        customerDataSharedPref.putString(
            AppSharedPref.KEY_CUSTOMER_NAME,
            signUpResponseModel.customerName
        )
        customerDataSharedPref.putString(
            AppSharedPref.KEY_CUSTOMER_EMAIL,
            signUpResponseModel.customerEmail
        )
        customerDataSharedPref.putString(
            AppSharedPref.KEY_CUSTOMER_IMAGE_URL,
            signUpResponseModel.profileImage
        )
        customerDataSharedPref.putString(
            AppSharedPref.KEY_CUSTOMER_IMAGE_DOMINANT_COLOR,
            signUpResponseModel.profileDominantColor
        )
        customerDataSharedPref.putString(
            AppSharedPref.KEY_CUSTOMER_BANNER_URL,
            signUpResponseModel.bannerImage
        )
        customerDataSharedPref.putString(
            AppSharedPref.KEY_CUSTOMER_BANNER_DOMINANT_COLOR,
            signUpResponseModel.bannerDominantColor
        )
        customerDataSharedPref.apply()
    }

    fun onClickLogin() {
        mFragmentContext.dismiss()
        (mFragmentContext.context as LoginAndSignUpActivity).mContentViewBinding.handler!!.onClickLogin()
    }


    fun onSplitTypeChanged() {
    }

    fun onClickDatePicker(v: View, selectedDate: String, dateFormat: String) {
        val dobCalendar = Calendar.getInstance()
        var selectedYear = dobCalendar.get(Calendar.YEAR)
        var selectedMonth = dobCalendar.get(Calendar.MONTH)
        var selectedDay = dobCalendar.get(Calendar.DAY_OF_MONTH)
        try {
            if (selectedDate.isNotBlank()) {
                val sdf = SimpleDateFormat(dateFormat, Locale.US)
                val d = sdf.parse(selectedDate)
                dobCalendar.time = d
                selectedYear = dobCalendar.get(Calendar.YEAR)
                selectedMonth = dobCalendar.get(Calendar.MONTH)
                selectedDay = dobCalendar.get(Calendar.DAY_OF_MONTH)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val dateOfBirth = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            dobCalendar.set(Calendar.YEAR, year)
            dobCalendar.set(Calendar.MONTH, month)
            dobCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val dob = SimpleDateFormat(dateFormat, Locale.US)
            (v as TextInputEditText).setText(dob.format(dobCalendar.time))
        }

        val datePickerDialog = DatePickerDialog(
            mFragmentContext.context!!,
            R.style.AlertDialogTheme,
            dateOfBirth,
            selectedYear,
            selectedMonth,
            selectedDay
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 1000
        datePickerDialog.setButton(
            DatePickerDialog.BUTTON_POSITIVE,
            mFragmentContext.getString(R.string.ok),
            datePickerDialog
        )
        datePickerDialog.setButton(
            DatePickerDialog.BUTTON_NEGATIVE,
            mFragmentContext.getString(R.string.cancel),
            datePickerDialog
        )
        datePickerDialog.show()
    }
}