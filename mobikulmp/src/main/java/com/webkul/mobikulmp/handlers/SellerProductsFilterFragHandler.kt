package com.webkul.mobikulmp.handlers

import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.SellerProductsListActivity
import com.webkul.mobikulmp.fragments.DatePickerFragment
import com.webkul.mobikulmp.fragments.DatePickerFragment.Companion.FROM_DATE_DOB
import com.webkul.mobikulmp.fragments.DatePickerFragment.Companion.TO_DATE_DOB
import com.webkul.mobikulmp.fragments.SellerProductsFilterFragment
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

class SellerProductsFilterFragHandler(private val mSellerProductsFilterDialogFragment: SellerProductsFilterFragment) : DatePickerFragment.OnDateSelected {

    fun applyFilters() {
        (mSellerProductsFilterDialogFragment.context as SellerProductsListActivity).isFirstCall = true
        (mSellerProductsFilterDialogFragment.context as SellerProductsListActivity).mPageNumber = 1
        (mSellerProductsFilterDialogFragment.context as SellerProductsListActivity).mProductName = mSellerProductsFilterDialogFragment.mContentViewBinding.productName!!
        (mSellerProductsFilterDialogFragment.context as SellerProductsListActivity).mDateFrom = mSellerProductsFilterDialogFragment.mContentViewBinding.fromDate!!
        (mSellerProductsFilterDialogFragment.context as SellerProductsListActivity).mDateTo = mSellerProductsFilterDialogFragment.mContentViewBinding.toDate!!
        (mSellerProductsFilterDialogFragment.context as SellerProductsListActivity).mProductStatusPosition = mSellerProductsFilterDialogFragment.mContentViewBinding.productStatusSpinner.selectedItemPosition
        (mSellerProductsFilterDialogFragment.context as SellerProductsListActivity).mProductStatus = if (mSellerProductsFilterDialogFragment.mContentViewBinding.productStatusSpinner.selectedItemPosition == 0) "" else {
            mSellerProductsFilterDialogFragment.mContentViewBinding.productStatusSpinner.selectedItemPosition.toString()
        }
        Utils.hideKeyboard(mSellerProductsFilterDialogFragment.mContentViewBinding.productNameEt)
        mSellerProductsFilterDialogFragment.dismiss()
        mSellerProductsFilterDialogFragment.onDetachInterface?.onFragmentDetached()
    }

    fun resetFilters() {
        mSellerProductsFilterDialogFragment.mContentViewBinding.productName = ""
        mSellerProductsFilterDialogFragment.mContentViewBinding.fromDate = ""
        mSellerProductsFilterDialogFragment.mContentViewBinding.toDate = ""



        (mSellerProductsFilterDialogFragment.context as SellerProductsListActivity).isFirstCall = true
        (mSellerProductsFilterDialogFragment.context as SellerProductsListActivity).mPageNumber = 1
        (mSellerProductsFilterDialogFragment.context as SellerProductsListActivity).mProductName = ""
        (mSellerProductsFilterDialogFragment.context as SellerProductsListActivity).mDateFrom = ""
        (mSellerProductsFilterDialogFragment.context as SellerProductsListActivity).mDateTo = ""
        (mSellerProductsFilterDialogFragment.context as SellerProductsListActivity).mProductStatusPosition = 0
        (mSellerProductsFilterDialogFragment.context as SellerProductsListActivity).mProductStatus = ""

        mSellerProductsFilterDialogFragment.dismiss()
        mSellerProductsFilterDialogFragment.onDetachInterface?.onFragmentDetached()
    }

    fun pickFromDate() {
        val newFragment = DatePickerFragment.newInstance(this, "", FROM_DATE_DOB, -1)
        newFragment.show((mSellerProductsFilterDialogFragment.context as BaseActivity).supportFragmentManager, DatePickerFragment::class.java.simpleName)
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
            newFragment.show((mSellerProductsFilterDialogFragment.context as BaseActivity).supportFragmentManager, DatePickerFragment::class.java.simpleName)
        } else {
            Toast.makeText(mSellerProductsFilterDialogFragment.context, mSellerProductsFilterDialogFragment.context!!.getString(R.string.please_select_from_date), Toast.LENGTH_SHORT).show()
        }
    }

    fun onClickCancelFilter() {
        Utils.hideKeyboard(mSellerProductsFilterDialogFragment.mContentViewBinding.productNameEt)
        mSellerProductsFilterDialogFragment.dismiss()
    }

    override fun onDateSet(year: Int, month: Int, day: Int, type: Int) {
        when (type) {
            FROM_DATE_DOB -> {
                mSellerProductsFilterDialogFragment.mContentViewBinding.fromDate = Utils.formatDate(null, year, month, day)
            }
            TO_DATE_DOB -> {
                mSellerProductsFilterDialogFragment.mContentViewBinding.toDate = Utils.formatDate(null, year, month, day)
            }
        }
        mSellerProductsFilterDialogFragment.mContentViewBinding.executePendingBindings()

    }

    fun onClickSearchProductBtn() {
        if (mSellerProductsFilterDialogFragment.mContentViewBinding.searchProductInformation.visibility == View.VISIBLE) {
            mSellerProductsFilterDialogFragment.mContentViewBinding.searchProductInformation.visibility = View.GONE
            mSellerProductsFilterDialogFragment.mContentViewBinding.searchProductHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mSellerProductsFilterDialogFragment.context!!, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mSellerProductsFilterDialogFragment.mContentViewBinding.searchProductInformation.visibility = View.VISIBLE
            mSellerProductsFilterDialogFragment.mContentViewBinding.searchProductHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mSellerProductsFilterDialogFragment.context!!, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mSellerProductsFilterDialogFragment.mContentViewBinding.scrollView.top + mSellerProductsFilterDialogFragment.mContentViewBinding.searchProductHeading.top
                mSellerProductsFilterDialogFragment.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }

    fun onClickProductCreationDateBtn() {
        if (mSellerProductsFilterDialogFragment.mContentViewBinding.productCreationDateInformation.visibility == View.VISIBLE) {
            mSellerProductsFilterDialogFragment.mContentViewBinding.productCreationDateInformation.visibility = View.GONE
            mSellerProductsFilterDialogFragment.mContentViewBinding.productCreationDateHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mSellerProductsFilterDialogFragment.context!!, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mSellerProductsFilterDialogFragment.mContentViewBinding.productCreationDateInformation.visibility = View.VISIBLE
            mSellerProductsFilterDialogFragment.mContentViewBinding.productCreationDateHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mSellerProductsFilterDialogFragment.context!!, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mSellerProductsFilterDialogFragment.mContentViewBinding.scrollView.top + mSellerProductsFilterDialogFragment.mContentViewBinding.productCreationDateHeading.top
                mSellerProductsFilterDialogFragment.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }

    fun onClickProductStatusBtn() {
        if (mSellerProductsFilterDialogFragment.mContentViewBinding.productStatusInformation.visibility == View.VISIBLE) {
            mSellerProductsFilterDialogFragment.mContentViewBinding.productStatusInformation.visibility = View.GONE
            mSellerProductsFilterDialogFragment.mContentViewBinding.productStatusHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mSellerProductsFilterDialogFragment.context!!, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mSellerProductsFilterDialogFragment.mContentViewBinding.productStatusInformation.visibility = View.VISIBLE
            mSellerProductsFilterDialogFragment.mContentViewBinding.productStatusHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mSellerProductsFilterDialogFragment.context!!, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mSellerProductsFilterDialogFragment.mContentViewBinding.scrollView.top + mSellerProductsFilterDialogFragment.mContentViewBinding.productStatusHeading.top
                mSellerProductsFilterDialogFragment.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }

}