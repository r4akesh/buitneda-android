package com.webkul.mobikulmp.handlers

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikulmp.fragments.DatePickerFragment
import com.webkul.mobikulmp.fragments.DatePickerFragment.Companion.FROM_DATE_DOB
import com.webkul.mobikulmp.fragments.DatePickerFragment.Companion.TO_DATE_DOB
import com.webkul.mobikulmp.models.seller.SellerOrderFilterFragData

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

class SellerOrderFilterFragHandler : DatePickerFragment.OnDateSelected {
    private val mContext: Context? = null

    //private SellerOrderFilterDialogFragment mSellerOrderFilterDialogFragment;
    private val mData: SellerOrderFilterFragData? = null

    /*public SellerOrderFilterFragHandler(Context context, SellerOrderFilterDialogFragment sellerOrderFilterDialogFragment, SellerOrderFilterFragData data) {
        mContext = context;
        mSellerOrderFilterDialogFragment = sellerOrderFilterDialogFragment;
        mData = data;
    }*/

    /* public void applyFilters() {
        ((SellerOrderActivity) mContext).onOrderFilterApplied(mData);
        mSellerOrderFilterDialogFragment.dismiss();
    }*/

    fun resetFilters() {
        mData!!.resetFilters()
    }

    fun pickFromDate() {
        val newFragment = DatePickerFragment.newInstance(this, "", FROM_DATE_DOB, 0)
        newFragment.show((mContext as AppCompatActivity).supportFragmentManager, DatePickerFragment::class.java.simpleName)
    }

    /* public void pickToDate(SellerOrderFilterFragData data) {
        if (!data.getFromDate().isEmpty()) {
            long fromDate = 0;
            try {
                DateFormat formatter = new SimpleDateFormat(DatePickerFragment.DEFAULT_DATE_FORMAT, Locale.getDefault());
                Date date = (Date) formatter.parse(data.getFromDate());
                fromDate = date.getTime() ;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DialogFragment newFragment = DatePickerFragment.newInstance(this, "", TO_DATE_DOB, fromDate);
            newFragment.show(((AppCompatActivity) mContext).getSupportFragmentManager(), DatePickerFragment.class.getSimpleName());
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.please_select_from_date), Toast.LENGTH_SHORT).show();
        }
    }*/

    fun onClickOutsideFilter() {
        (mContext as BaseActivity).onBackPressed()
    }

    override fun onDateSet(year: Int, month: Int, day: Int, type: Int) {
        when (type) {
            FROM_DATE_DOB -> {
            }
            TO_DATE_DOB -> {
            }
        }// mData.setFromDate(Utils.formatDate(null, year, month, day));
        //  mData.setToDate(Utils.formatDate(null, year, month, day));
    }


    interface OnOrderFilterAppliedListener {
        fun onOrderFilterApplied(sellerOrderFilterFragData: SellerOrderFilterFragData)
    }

    companion object {
        private val TAG = "SellerOrderFilterFragHa"
    }
}
