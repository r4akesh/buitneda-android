package com.webkul.mobikulmp.handlers

import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.SellerOrdersListActivity
import com.webkul.mobikulmp.fragments.DatePickerFragment
import com.webkul.mobikulmp.fragments.DatePickerFragment.Companion.FROM_DATE_DOB
import com.webkul.mobikulmp.fragments.DatePickerFragment.Companion.TO_DATE_DOB
import com.webkul.mobikulmp.fragments.SellerOrdersFilterFragment
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

class SellerOrdersFilterFragHandler(private val mFragmentContext: SellerOrdersFilterFragment) : DatePickerFragment.OnDateSelected {

    fun applyFilters() {
        (mFragmentContext.context as SellerOrdersListActivity).mPageNumber = 1
        (mFragmentContext.context as SellerOrdersListActivity).mIncrementId = mFragmentContext.mContentViewBinding.incrementId!!
        (mFragmentContext.context as SellerOrdersListActivity).mDateFrom = mFragmentContext.mContentViewBinding.fromDate!!
        (mFragmentContext.context as SellerOrdersListActivity).mDateTo = mFragmentContext.mContentViewBinding.toDate!!
        Utils.hideKeyboard(mFragmentContext.mContentViewBinding.orderEt)
        mFragmentContext.dismiss()
        mFragmentContext.onDetachInterface?.onFragmentDetached()
    }

    fun resetFilters() {
        mFragmentContext.mContentViewBinding.incrementId = ""
        mFragmentContext.mContentViewBinding.fromDate = ""
        mFragmentContext.mContentViewBinding.toDate = ""
        mFragmentContext.mContentViewBinding.orderStatusSpinner.setSelection(0)
    }

    fun pickFromDate() {
        val newFragment = DatePickerFragment.newInstance(this, "", FROM_DATE_DOB, 0)
        newFragment.show((mFragmentContext.context as BaseActivity).supportFragmentManager, DatePickerFragment::class.java.simpleName)
    }

    fun pickToDate(fromDateData: String) {
        if (!fromDateData.isEmpty()) {
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
        Utils.hideKeyboard(mFragmentContext.mContentViewBinding.orderEt)
        mFragmentContext.dismiss()
    }

    override fun onDateSet(year: Int, month: Int, day: Int, type: Int) {
        when (type) {
            FROM_DATE_DOB -> {
                mFragmentContext.mContentViewBinding.fromDate = Utils.formatDate(null, year, month, day)
            }
            TO_DATE_DOB -> {
                mFragmentContext.mContentViewBinding.toDate = Utils.formatDate(null, year, month, day)
            }
        }
        mFragmentContext.mContentViewBinding.executePendingBindings()

    }

    fun onClickSearchProductBtn() {
        if (mFragmentContext.mContentViewBinding.searchProductInformation.visibility == View.VISIBLE) {
            mFragmentContext.mContentViewBinding.searchProductInformation.visibility = View.GONE
            mFragmentContext.mContentViewBinding.searchProductHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mFragmentContext.context!!, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mFragmentContext.mContentViewBinding.searchProductInformation.visibility = View.VISIBLE
            mFragmentContext.mContentViewBinding.searchProductHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mFragmentContext.context!!, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mFragmentContext.mContentViewBinding.scrollView.top + mFragmentContext.mContentViewBinding.searchProductHeading.top
                mFragmentContext.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }

    fun onClickProductCreationDateBtn() {
        if (mFragmentContext.mContentViewBinding.productCreationDateInformation.visibility == View.VISIBLE) {
            mFragmentContext.mContentViewBinding.productCreationDateInformation.visibility = View.GONE
            mFragmentContext.mContentViewBinding.productCreationDateHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mFragmentContext.context!!, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mFragmentContext.mContentViewBinding.productCreationDateInformation.visibility = View.VISIBLE
            mFragmentContext.mContentViewBinding.productCreationDateHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mFragmentContext.context!!, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mFragmentContext.mContentViewBinding.scrollView.top + mFragmentContext.mContentViewBinding.productCreationDateHeading.top
                mFragmentContext.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }

    fun onClickProductStatusBtn() {
        if (mFragmentContext.mContentViewBinding.productStatusInformation.visibility == View.VISIBLE) {
            mFragmentContext.mContentViewBinding.productStatusInformation.visibility = View.GONE
            mFragmentContext.mContentViewBinding.productStatusHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mFragmentContext.context!!, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mFragmentContext.mContentViewBinding.productStatusInformation.visibility = View.VISIBLE
            mFragmentContext.mContentViewBinding.productStatusHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mFragmentContext.context!!, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mFragmentContext.mContentViewBinding.scrollView.top + mFragmentContext.mContentViewBinding.productStatusHeading.top
                mFragmentContext.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }

}