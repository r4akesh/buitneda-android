package com.webkul.mobikulmp.model.auction

import android.content.Context
import android.widget.Toast
import androidx.databinding.Bindable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.webkul.mobikul.helpers.ToastHelper
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikulmp.BR
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activity.AddAuctionOnProductActivity
import com.webkul.mobikulmp.models.auction.IncrementalRule
import org.json.JSONArray
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AuctionFormData : BaseModel() {
    @SerializedName("minQty")
    @Expose
    var minQty: String? = null

    @SerializedName("maxQty")
    @Expose
    var maxQty: String? = null

    @SerializedName("stopTime")
    @Expose
    private var stopTime: String? = null

    @SerializedName("startTime")
    @Expose
    private var startTime: String? = null

    @SerializedName("minAmount")
    @Expose
    var minAmount: String? = null

    @SerializedName("autoAuction")
    @Expose
    var autoAuction = false

    @SerializedName("auctionType")
    @Expose
    var auctionType: List<String>? = null

    @SerializedName("reservePrice")
    @Expose
    var reservePrice: String? = null

    @SerializedName("startingPrice")
    @Expose
    var startingPrice: String? = null

    @SerializedName("noOfDaysTillBuy")
    @Expose
    var noOfDaysTillBuy: String? = null

    @SerializedName("incrementalRule")
    @Expose
    private var incrementalRule: ArrayList<IncrementalRule>? = null

    @get:Bindable
    @SerializedName("incrementAuction")
    @Expose
    var incrementAuction = false
        set(incrementAuction) {
            field = incrementAuction
            notifyPropertyChanged(BR.incrementAuction)
        }

    @SerializedName("isAutoAuctionEnable")
    @Expose
    var isIsAutoAuctionEnable = false
        private set

    @SerializedName("isReservePriceEnable")
    @Expose
    var isIsReservePriceEnable = false
        private set

    @SerializedName("adminIncrementalRule")
    @Expose
    var adminIncrementalRule: ArrayList<IncrementalRule>? = null

    @SerializedName("isIncrementalAuctionEnable")
    @Expose
    var isIsIncrementalAuctionEnable = false
        private set

    @get:Bindable
    var isShowAdminIncrementBidRule = false
        set(showAdminIncrementBidRule) {
            field = showAdminIncrementBidRule
            notifyPropertyChanged(BR.showAdminIncrementBidRule)
        }
    var isButItNow = false
    var isAuction = false
    var startTimeAdjust: String? = null

    @get:Bindable
    var stopTimeAdjust: String? = null
        set(stopTimeAdjust) {
            field = stopTimeAdjust
            notifyPropertyChanged(BR.stopTimeAdjust)
        }

    @SerializedName("startTimeOffset")
    @Expose
    var startTimeOffset = 0

    @SerializedName("stopTimeOffset")
    @Expose
    var stopTimeOffset = 0

    fun getStopTime(): String? {
        val currentoffset = Date().timezoneOffset * -1 * 60000
        try {
            val df = SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US)
            val date = df.parse(stopTime)
            val toDate = Date(date.time + currentoffset + -1000 * stopTimeOffset)
            return df.format(toDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return stopTime
    }

    fun setStopTime(stopTime: String?) {
        this.stopTime = stopTime
    }

    fun getStartTime(): String? {
        val currentoffset = Date().timezoneOffset * -1 * 60000
        try {
            val df = SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US)
            val date = df.parse(startTime)
            val toDate = Date(date.time + currentoffset + -1000 * startTimeOffset)
            return df.format(toDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return startTime
    }

    fun setStartTime(startTime: String?) {
        this.startTime = startTime
    }

    @Bindable
    fun getIncrementalRule(): ArrayList<IncrementalRule>? {
        return incrementalRule
    }

    fun setIncrementalRule(incrementalRule: ArrayList<IncrementalRule>?) {
        this.incrementalRule = incrementalRule
        notifyPropertyChanged(BR.incrementalRule)
    }

    fun setIsAutoAuctionEnable(isAutoAuctionEnable: Boolean) {
        isIsAutoAuctionEnable = isAutoAuctionEnable
    }

    fun setIsReservePriceEnable(isReservePriceEnable: Boolean) {
        isIsReservePriceEnable = isReservePriceEnable
    }

    fun setIsIncrementalAuctionEnable(isIncrementalAuctionEnable: Boolean) {
        isIsIncrementalAuctionEnable = isIncrementalAuctionEnable
    }

    fun isFormValidated(context: Context): Boolean {
        var isFormValidated = true
        var message = context.getString(R.string.fill_all_req_option)
        for (incrementIndex in incrementalRule!!.indices) {
            val eachRule: IncrementalRule = incrementalRule!![incrementIndex]
            if (eachRule.getFrom() == null || eachRule.getFrom().trim().isEmpty() || eachRule.getTo() == null || eachRule.getTo().trim().isEmpty() || eachRule.getPrice() == null || eachRule.getPrice().trim().isEmpty()) {
                isFormValidated = false
                Utils.showShakeError(context, (context as AddAuctionOnProductActivity).mBinding!!.sellerIncrementBidRulesRv)
            }
        }
        if (maxQty == null || maxQty!!.trim { it <= ' ' }.isEmpty()) {
            (context as AddAuctionOnProductActivity).mBinding!!.maxQtyHeading.setTextColor(context.resources.getColor(R.color.red_600))
            Utils.showShakeError(context, (context as AddAuctionOnProductActivity).mBinding!!.maxQtyHeading)
            (context as AddAuctionOnProductActivity).mBinding!!.maxQtyValue.requestFocus()
            isFormValidated = false
        } else {
            (context as AddAuctionOnProductActivity).mBinding!!.maxQtyHeading.setTextColor(context.resources.getColor(android.R.color.black))
        }
        if (minQty == null || minQty!!.trim { it <= ' ' }.isEmpty()) {
            (context as AddAuctionOnProductActivity).mBinding!!.minQtyHeading.setTextColor(context.resources.getColor(R.color.red_600))
            Utils.showShakeError(context, (context as AddAuctionOnProductActivity).mBinding!!.minQtyHeading)
            (context as AddAuctionOnProductActivity).mBinding!!.minQtyValue.requestFocus()
            isFormValidated = false
        } else {
            (context as AddAuctionOnProductActivity).mBinding!!.minQtyHeading.setTextColor(context.resources.getColor(android.R.color.black))
        }
        if (noOfDaysTillBuy == null || noOfDaysTillBuy!!.trim { it <= ' ' }.isEmpty()) {
            (context as AddAuctionOnProductActivity).mBinding!!.noOfDaysTillBuyHeading.setTextColor(context.resources.getColor(R.color.red_600))
            Utils.showShakeError(context, (context as AddAuctionOnProductActivity).mBinding!!.noOfDaysTillBuyHeading)
            (context as AddAuctionOnProductActivity).mBinding!!.noOfDaysTillBuyValue.requestFocus()
            isFormValidated = false
        } else {
            (context as AddAuctionOnProductActivity).mBinding!!.noOfDaysTillBuyHeading.setTextColor(context.resources.getColor(android.R.color.black))
        }
        if (stopTime == null || stopTime!!.trim { it <= ' ' }.isEmpty()) {
            (context as AddAuctionOnProductActivity).mBinding!!.stopAuctionHeading.setTextColor(context.resources.getColor(R.color.red_600))
            Utils.showShakeError(context, (context as AddAuctionOnProductActivity).mBinding!!.stopAuctionHeading)
            (context as AddAuctionOnProductActivity).mBinding!!.stopActionTimeValue.requestFocus()
            isFormValidated = false
        } else {
            (context as AddAuctionOnProductActivity).mBinding!!.stopAuctionHeading.setTextColor(context.resources.getColor(android.R.color.black))
        }
        if (startTime == null || startTime!!.trim { it <= ' ' }.isEmpty()) {
            (context as AddAuctionOnProductActivity).mBinding!!.startAuctionHeading.setTextColor(context.resources.getColor(R.color.red_600))
            Utils.showShakeError(context, (context as AddAuctionOnProductActivity).mBinding!!.startAuctionHeading)
            (context as AddAuctionOnProductActivity).mBinding!!.startAuctionValue.requestFocus()
            isFormValidated = false
        } else {
            (context as AddAuctionOnProductActivity).mBinding!!.startAuctionHeading.setTextColor(context.resources.getColor(android.R.color.black))
        }
        if (isIsReservePriceEnable && (reservePrice == null || reservePrice!!.trim { it <= ' ' }.isEmpty())) {
            (context as AddAuctionOnProductActivity).mBinding!!.reservePriceHeading.setTextColor(context.resources.getColor(R.color.red_600))
            Utils.showShakeError(context, (context as AddAuctionOnProductActivity).mBinding!!.reservePriceHeading)
            (context as AddAuctionOnProductActivity).mBinding!!.reservePriceValue.requestFocus()
            isFormValidated = false
        } else {
            (context as AddAuctionOnProductActivity).mBinding!!.reservePriceHeading.setTextColor(context.resources.getColor(android.R.color.black))
        }
        if (startingPrice == null || startingPrice!!.trim { it <= ' ' }.isEmpty()) {
            (context as AddAuctionOnProductActivity).mBinding!!.startingPriceHeading.setTextColor(context.resources.getColor(R.color.red_600))
            Utils.showShakeError(context, (context as AddAuctionOnProductActivity).mBinding!!.startingPriceHeading)
            (context as AddAuctionOnProductActivity).mBinding!!.startingPriceValue.requestFocus()
            isFormValidated = false
        } else {
            (context as AddAuctionOnProductActivity).mBinding!!.startingPriceHeading.setTextColor(context.resources.getColor(android.R.color.black))
        }
        if (!(context as AddAuctionOnProductActivity).mBinding!!.buyItNowCb.isChecked() && !(context as AddAuctionOnProductActivity).mBinding!!.auctionCb.isChecked()) {
            (context as AddAuctionOnProductActivity).mBinding!!.auctionOptionsHeading.setTextColor(context.resources.getColor(R.color.red_600))
            Utils.showShakeError(context, (context as AddAuctionOnProductActivity).mBinding!!.auctionOptionsHeading)
            (context as AddAuctionOnProductActivity).mBinding!!.auctionOptionsHeading.requestFocus()
            isFormValidated = false
        } else {
            (context as AddAuctionOnProductActivity).mBinding!!.auctionOptionsHeading.setTextColor(context.resources.getColor(android.R.color.black))
        }
        if (reservePrice != null && startingPrice != null && !reservePrice!!.trim { it <= ' ' }.isEmpty() && !startingPrice!!.trim { it <= ' ' }.isEmpty()) {
            if (java.lang.Double.valueOf(reservePrice) < java.lang.Double.valueOf(startingPrice)) {
                (context as AddAuctionOnProductActivity).mBinding!!.reservePriceHeading.setTextColor(context.resources.getColor(R.color.red_600))
                Utils.showShakeError(context, (context as AddAuctionOnProductActivity).mBinding!!.reservePriceHeading)
                (context as AddAuctionOnProductActivity).mBinding!!.reservePriceValue.requestFocus()
                isFormValidated = false
                message = context.getString(R.string.ivalid_reserve_price)
            } else {
                (context as AddAuctionOnProductActivity).mBinding!!.reservePriceHeading.setTextColor(context.resources.getColor(android.R.color.black))
            }
        }
        //        try {
//            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
//            Date startDate = df.parse(startTimeAdjust);
//            Date stopDate = df.parse(stopTimeAdjust);
//            if ((startTimeAdjust != null && stopTimeAdjust != null) && (!startTimeAdjust.trim().isEmpty() && !stopTimeAdjust.trim().isEmpty())) {
//                if (stopDate.getTime() < startDate.getTime()) {
//                    ((AddAuctionOnProductActivity) context).mBinding.stopAuctionHeading.setTextColor(context.getResources().getColor(R.color.red_600));
//                    Utils.showShakeError(context, ((AddAuctionOnProductActivity) context).mBinding.stopAuctionHeading);
//                    ((AddAuctionOnProductActivity) context).mBinding.stopActionTimeValue.requestFocus();
//                    isFormValidated = false;
//                } else {
//                    ((AddAuctionOnProductActivity) context).mBinding.stopAuctionHeading.setTextColor(context.getResources().getColor(android.R.color.black));
//                }
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        if (noOfDaysTillBuy != null && !noOfDaysTillBuy!!.trim { it <= ' ' }.isEmpty()) {
            if (java.lang.Double.valueOf(noOfDaysTillBuy) == 0.0) {
                (context as AddAuctionOnProductActivity).mBinding!!.noOfDaysTillBuyHeading.setTextColor(context.resources.getColor(R.color.red_600))
                Utils.showShakeError(context, (context as AddAuctionOnProductActivity).mBinding!!.noOfDaysTillBuyHeading)
                (context as AddAuctionOnProductActivity).mBinding!!.noOfDaysTillBuyValue.requestFocus()
                isFormValidated = false
                message = context.getString(R.string.invalid_number)
            } else {
                (context as AddAuctionOnProductActivity).mBinding!!.noOfDaysTillBuyHeading.setTextColor(context.resources.getColor(android.R.color.black))
            }
        }
        if (minQty != null && maxQty != null && !minQty!!.trim { it <= ' ' }.isEmpty() && !maxQty!!.trim { it <= ' ' }.isEmpty()) {
            if (java.lang.Double.valueOf(minQty) > java.lang.Double.valueOf(maxQty)) {
                (context as AddAuctionOnProductActivity).mBinding!!.maxQtyHeading.setTextColor(context.resources.getColor(R.color.red_600))
                Utils.showShakeError(context, (context as AddAuctionOnProductActivity).mBinding!!.maxQtyHeading)
                (context as AddAuctionOnProductActivity).mBinding!!.maxQtyValue.requestFocus()
                isFormValidated = false
                message = context.getString(R.string.invalid_max_qty)
            } else {
                (context as AddAuctionOnProductActivity).mBinding!!.maxQtyHeading.setTextColor(context.resources.getColor(android.R.color.black))
            }
        }
        if (!isFormValidated) {
            ToastHelper.showToast(context, message, Toast.LENGTH_LONG)
        }
        return isFormValidated
    }

    val incrementForReq: JSONObject
        get() {
            val increment = JSONObject()
            val from = JSONArray()
            val to = JSONArray()
            val price = JSONArray()
            try {
                for (incrementIndex in incrementalRule!!.indices) {
                    val eachRule: IncrementalRule = incrementalRule!![incrementIndex]
                    from.put(eachRule.getFrom())
                    to.put(eachRule.getTo())
                    price.put(eachRule.getPrice())
                }
                increment.put("from", from)
                increment.put("to", to)
                increment.put("price", price)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return increment
        }

    val auctionTypeForReq: String
        get() {
            var auctionType = ""
            if (isButItNow) {
                auctionType = "1"
            }
            if (isAuction) {
                auctionType = if (auctionType.isEmpty()) auctionType + "2" else "$auctionType,2"
            }
            return auctionType
        }

}