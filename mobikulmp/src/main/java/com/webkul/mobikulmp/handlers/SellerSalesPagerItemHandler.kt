package com.webkul.mobikulmp.handlers

import android.view.View
import android.widget.PopupWindow
import android.widget.Toast
import com.webkul.mobikul.helpers.ToastHelper
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.SellerDashboardActivity
import java.util.*

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.handlers
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 28/6/19
 */
class SellerSalesPagerItemHandler(private val mContext: SellerDashboardActivity, private val mTimeRangePopupWindow: PopupWindow, private val mListOfImages: ArrayList<String>) {

    fun onClickDayBtn(view: View) {
        if (!mListOfImages[0].isEmpty()) {
            mContext.setImage(mListOfImages[0], 0)
        } else {
            ToastHelper.showToast(view.context, view.context.getString(R.string.stats_not_found), Toast.LENGTH_SHORT)
        }
        mTimeRangePopupWindow.dismiss()
    }

    fun onClickWeekBtn(view: View) {
        if (!mListOfImages[1].isEmpty()) {
            mContext.setImage(mListOfImages[1], 1)
        } else {
            ToastHelper.showToast(view.context, view.context.getString(R.string.stats_not_found), Toast.LENGTH_SHORT)
        }
        mTimeRangePopupWindow.dismiss()
    }

    fun onClickMonthBtn(view: View) {
        if (!mListOfImages[2].isEmpty()) {
            mContext.setImage(mListOfImages[2], 2)
        } else {
            ToastHelper.showToast(view.context, view.context.getString(R.string.stats_not_found), Toast.LENGTH_SHORT)
        }
        mTimeRangePopupWindow.dismiss()
    }

    fun onClickYearBtn(view: View) {
        if (!mListOfImages[3].isEmpty()) {
            mContext.setImage(mListOfImages[3], 3)
        } else {
            ToastHelper.showToast(view.context, view.context.getString(R.string.stats_not_found), Toast.LENGTH_SHORT)
        }
        mTimeRangePopupWindow.dismiss()
    }

}