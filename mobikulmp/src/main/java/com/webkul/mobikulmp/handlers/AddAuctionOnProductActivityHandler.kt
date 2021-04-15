package com.webkul.mobikulmp.handlers

import android.content.Context
import android.view.View
import android.widget.EditText
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activity.AddAuctionOnProductActivity
import com.webkul.mobikulmp.model.auction.AuctionFormData
import com.webkul.mobikulmp.models.auction.IncrementalRule
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by vedesh.kumar on 19/4/18.
 */
class AddAuctionOnProductActivityHandler(private val mContext: Context?) {

    fun onClickSelectAuctionTime(auctionFormData: AuctionFormData, editText: View) {
        val calender = Calendar.getInstance()
        val df = SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US)
        calender.timeInMillis = System.currentTimeMillis()
        val timePickerListener = TimePickerDialog.OnTimeSetListener { timePickerDialog, i, i1, i2 ->
            calender[Calendar.HOUR_OF_DAY] = i
            calender[Calendar.MINUTE] = i1
            calender[Calendar.SECOND] = i2
            if (editText.id == R.id.start_auction_value) {
                try {
                    if (auctionFormData.stopTimeAdjust != null && !auctionFormData.stopTimeAdjust!!.trim { it <= ' ' }.isEmpty()) {
                        val startDate = df.parse(df.format(calender.time))
                        val stopDate = df.parse(auctionFormData.stopTimeAdjust)
                        if (stopDate.time < startDate.time) {
                            auctionFormData.stopTimeAdjust = df.format(calender.time)
                        }
                    }
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                (editText as EditText).setText(df.format(calender.time))
            } else {
                (editText as EditText).setText(df.format(calender.time))
            }
        }
        val datePickerListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calender[Calendar.YEAR] = year
            calender[Calendar.MONTH] = monthOfYear
            calender[Calendar.DAY_OF_MONTH] = dayOfMonth
            val tpd = TimePickerDialog.newInstance(
                    timePickerListener,
                    calender[Calendar.HOUR_OF_DAY],
                    calender[Calendar.MINUTE],
                    true
            )
            tpd.setOkText(mContext!!.getString(R.string.yes))
            tpd.setCancelText(mContext.getString(R.string.no))
            tpd.show((mContext as AddAuctionOnProductActivity).fragmentManager, "Timepickerdialog")
        }
        val dpd: DatePickerDialog = DatePickerDialog.newInstance(
                datePickerListener,
                calender[Calendar.YEAR],
                calender[Calendar.MONTH],
                calender[Calendar.DAY_OF_MONTH]
        )
        if (editText.id == R.id.stop_action_time_value && auctionFormData.startTimeAdjust != null && !auctionFormData.startTimeAdjust!!.isEmpty()) {
            try {
                val startDate = df.parse(auctionFormData.startTimeAdjust)
                val cal = Calendar.getInstance()
                cal.time = startDate
                dpd.setMinDate(cal)
            } catch (e: ParseException) {
                e.printStackTrace()
                dpd.setMinDate(calender)
            }
        } else {
            dpd.setMinDate(calender)
        }
        dpd.setOkText(mContext!!.getString(R.string.yes))
        dpd.setCancelText(mContext.getString(R.string.no))
        dpd.show((mContext as AddAuctionOnProductActivity).fragmentManager, "Datepickerdialog")
    }

    fun onClickAddAnother() {
        (mContext as AddAuctionOnProductActivity).mAuctionFormData!!.getIncrementalRule()!!.add(IncrementalRule())
        mContext.mBinding!!.sellerIncrementBidRulesRv.getAdapter()!!.notifyDataSetChanged()
    }

    fun onClickSaveAuction(auctionFormData: AuctionFormData) {
        if ((mContext as AddAuctionOnProductActivity).mBinding!!.loading== false) {
            if (auctionFormData.isFormValidated(mContext)) {
                mContext.mBinding!!.loading = true
                MpApiConnection.saveAuction(mContext, auctionFormData, mContext.mProductId, mContext.mAuctionId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(object : ApiCustomCallback<BaseModel>(mContext, false) {
                            override fun onNext(orderListResponseModel: BaseModel) {
                                super.onNext(orderListResponseModel)
                                mContext.mBinding!!.loading=false
                                if (orderListResponseModel.success) {
                                    mContext.finish()
                                } else {
                                    mContext.finish()
                                }
                            }


                            override fun onError(e: Throwable) {
                                super.onError(e)
                               mContext.mBinding!!.loading=false
//                                onErrorResponse(e)
                            }
                        })

            }
        }
    }

}





















