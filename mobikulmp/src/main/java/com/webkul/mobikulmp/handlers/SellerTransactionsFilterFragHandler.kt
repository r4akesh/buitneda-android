package com.webkul.mobikulmp.handlers

import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.SellerTransactionsListActivity
import com.webkul.mobikulmp.fragments.DatePickerFragment
import com.webkul.mobikulmp.fragments.DatePickerFragment.Companion.FROM_DATE_DOB
import com.webkul.mobikulmp.fragments.DatePickerFragment.Companion.TO_DATE_DOB
import com.webkul.mobikulmp.fragments.SellerTransactionsFilterFragment
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.fragments
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 16/6/19
 */

class SellerTransactionsFilterFragHandler(private val mFragmentContext: SellerTransactionsFilterFragment) : DatePickerFragment.OnDateSelected {

    fun applyFilters() {
        (mFragmentContext.context as SellerTransactionsListActivity).mPageNumber = 1
        (mFragmentContext.context as SellerTransactionsListActivity).mTransactionId = mFragmentContext.mContentViewBinding.transactionId!!
        (mFragmentContext.context as SellerTransactionsListActivity).mDateFrom = mFragmentContext.mContentViewBinding.fromDate!!
        (mFragmentContext.context as SellerTransactionsListActivity).mDateTo = mFragmentContext.mContentViewBinding.toDate!!
        Utils.hideKeyboard(mFragmentContext.mContentViewBinding.transactionEt)
        mFragmentContext.dismiss()
        mFragmentContext.onDetachInterface?.onFragmentDetached()

    }

    fun resetFilters() {
        (mFragmentContext.context as SellerTransactionsListActivity).mPageNumber = 1
        mFragmentContext.mContentViewBinding.transactionId = ""
        mFragmentContext.mContentViewBinding.fromDate = ""
        mFragmentContext.mContentViewBinding.toDate = ""
        mFragmentContext.onDetachInterface?.onFragmentDetached()
    }

    fun pickFromDate() {
        val newFragment = DatePickerFragment.newInstance(this, "", FROM_DATE_DOB, 0)
        newFragment.show((mFragmentContext.context as BaseActivity).supportFragmentManager, DatePickerFragment::class.java.simpleName)
    }

    fun pickToDate(fromDateData: String) {
        if (fromDateData.isNotEmpty()) {
            var fromDate: Long = 0
            try {
                val formatter = SimpleDateFormat(DatePickerFragment.DEFAULT_DATE_FORMAT, Locale.getDefault())
                val date = formatter.parse(fromDateData) as Date
                fromDate = date.time
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            val newFragment = DatePickerFragment.newInstance(this, "", TO_DATE_DOB, fromDate)
            newFragment.show((mFragmentContext.context as BaseActivity).supportFragmentManager, DatePickerFragment::class.java.simpleName)
        } else {
            Toast.makeText(mFragmentContext.context, mFragmentContext.context!!.getString(R.string.please_select_from_date), Toast.LENGTH_SHORT).show()
        }
    }

    fun onClickCancelFilter() {
        Utils.hideKeyboard(mFragmentContext.mContentViewBinding.transactionEt)
        mFragmentContext.dismiss()
    }

    override fun onDateSet(year: Int, month: Int, day: Int, type: Int) {
        when (type) {
            FROM_DATE_DOB -> {
                mFragmentContext.mContentViewBinding.toDate = ""
                mFragmentContext.mContentViewBinding.fromDate = Utils.formatDate(null, year, month, day)
            }
            TO_DATE_DOB -> {
                mFragmentContext.mContentViewBinding.toDate = Utils.formatDate(null, year, month, day)
            }
        }
        mFragmentContext.mContentViewBinding.executePendingBindings()
    }

    fun onClickSearchTransactionBtn() {
        if (mFragmentContext.mContentViewBinding.searchTransactionInformation.visibility == View.VISIBLE) {
            mFragmentContext.mContentViewBinding.searchTransactionInformation.visibility = View.GONE
            mFragmentContext.mContentViewBinding.searchTransactionHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mFragmentContext.context!!, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mFragmentContext.mContentViewBinding.searchTransactionInformation.visibility = View.VISIBLE
            mFragmentContext.mContentViewBinding.searchTransactionHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mFragmentContext.context!!, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mFragmentContext.mContentViewBinding.scrollView.top + mFragmentContext.mContentViewBinding.searchTransactionHeading.top
                mFragmentContext.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }

    fun onClickTransactionDateBtn() {
        if (mFragmentContext.mContentViewBinding.transactionDateInformation.visibility == View.VISIBLE) {
            mFragmentContext.mContentViewBinding.transactionDateInformation.visibility = View.GONE
            mFragmentContext.mContentViewBinding.transactionDateHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mFragmentContext.context!!, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mFragmentContext.mContentViewBinding.transactionDateInformation.visibility = View.VISIBLE
            mFragmentContext.mContentViewBinding.transactionDateHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mFragmentContext.context!!, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mFragmentContext.mContentViewBinding.scrollView.top + mFragmentContext.mContentViewBinding.transactionDateHeading.top
                mFragmentContext.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }
}