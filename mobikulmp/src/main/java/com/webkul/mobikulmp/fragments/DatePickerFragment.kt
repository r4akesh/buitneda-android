package com.webkul.mobikulmp.fragments


import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.webkul.mobikul.R
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_DATE_TYPE
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_DOB_VALUE
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_FROM_DATE
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val fromDate = arguments!!.getLong(BUNDLE_KEY_FROM_DATE)
        try {
            val dobValue = arguments!!.getString(BUNDLE_KEY_DOB_VALUE)!!.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val splittedDob = dobValue[0].split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val datePickerDialog = DatePickerDialog(activity!!, R.style.AlertDialogTheme, this, Integer.parseInt(splittedDob[0]), Integer.parseInt(splittedDob[1]), Integer.parseInt(splittedDob[2]))
            datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, getString(R.string.ok), datePickerDialog)
            datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, getString(R.string.cancel), datePickerDialog)
            if (fromDate > 0) {
                datePickerDialog.datePicker.minDate = fromDate
                datePickerDialog.datePicker.maxDate = Date().time
                return datePickerDialog
            } else if (fromDate == -1L) {
                datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
                return datePickerDialog
            }else
                return datePickerDialog

        } catch (e: Exception) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(activity!!, R.style.AlertDialogTheme, this, year, month, day)
            datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, getString(R.string.ok), datePickerDialog)
            datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, getString(R.string.cancel), datePickerDialog)
            if (fromDate > 0) {
                datePickerDialog.datePicker.minDate = fromDate
                datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
                return datePickerDialog
            } else if (fromDate == -1L) {
                datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
                return datePickerDialog
            } else {
                return datePickerDialog
            }
        }

    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        onDateSelected!!.onDateSet(year, month, day, arguments!!.getInt(BUNDLE_KEY_DATE_TYPE))
    }


    interface OnDateSelected {
        fun onDateSet(year: Int, month: Int, day: Int, type: Int)
    }

    companion object {
        val DEFAULT_DATE_FORMAT = "MM/dd/yy"

        /* Constant for date selected*/
        val UNKNOWN_DOB = 0
        val BILLING_ADDRESS_DOB = 1
        val SHIPPING_ADDRESS_DOB = 2
        val FROM_DATE_DOB = 3
        val TO_DATE_DOB = 4
        private var onDateSelected: OnDateSelected? = null


        fun newInstance(onDateSelected: OnDateSelected, dobValue: String, type: Int, fromDate: Long): DatePickerFragment {
            DatePickerFragment.onDateSelected = onDateSelected
            val args = Bundle()
            val fragment = DatePickerFragment()
            args.putString(BUNDLE_KEY_DOB_VALUE, dobValue)
            args.putInt(BUNDLE_KEY_DATE_TYPE, type)
            args.putLong(BUNDLE_KEY_FROM_DATE, fromDate)
            fragment.arguments = args
            return fragment
        }
    }
}