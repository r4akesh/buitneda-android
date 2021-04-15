package com.webkul.mobikulmp.models.seller

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.webkul.mobikulmp.BR
import com.webkul.mobikulmp.R

/**
 * Webkul Software.
 *
 *
 * Java
 *
 * @author Webkul <support></support>@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

class SellerOrderFilterFragData : BaseObservable, Parcelable {
    private var mContext: Context? = null
    private var orderId:String? = ""
    private var fromDate :String?= ""
    private var toDate :String?= ""
    private var selectedOrderStatusPos: Int = 0

    val orderStatus: String
        @Bindable("selectedOrderStatusPos")
        get() = mContext?.resources?.getStringArray(R.array.order_status_code)?.get(getSelectedOrderStatusPos())!!


    constructor(context: Context) {
        mContext = context
    }

    private constructor(parcel: Parcel) {
        orderId = parcel.readString()
        fromDate = parcel.readString()
        toDate = parcel.readString()
        selectedOrderStatusPos = parcel.readInt()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(orderId)
        dest.writeString(fromDate)
        dest.writeString(toDate)
        dest.writeInt(selectedOrderStatusPos)
    }

    override fun describeContents(): Int {
        return 0
    }

    @Bindable
    fun getOrderId(): String {
        return orderId?:""
    }

    fun setOrderId(orderId: String) {
        this.orderId = orderId
        notifyPropertyChanged(BR.orderId)
    }

    @Bindable
    fun getFromDate(): String {
        return fromDate?:""
    }


    fun setFromDate(fromDate: String) {
        this.fromDate = fromDate
        notifyPropertyChanged(BR.fromDate)
    }

    @Bindable
    fun getToDate(): String {
        return toDate?:""
    }

    fun setToDate(toDate: String) {
        this.toDate = toDate
        notifyPropertyChanged(BR.toDate)
    }

    @Bindable
    fun getSelectedOrderStatusPos(): Int {
        return selectedOrderStatusPos
    }

    fun setSelectedOrderStatusPos(selectedOrderStatusPos: Int) {
        this.selectedOrderStatusPos = selectedOrderStatusPos
        notifyPropertyChanged(BR.selectedOrderStatusPos)
    }

    fun resetFilters() {
        setOrderId("")
        setFromDate("")
        setToDate("")
        setSelectedOrderStatusPos(0)
    }

    companion object {
        val CREATOR: Parcelable.Creator<SellerOrderFilterFragData> = object : Parcelable.Creator<SellerOrderFilterFragData> {
            override fun createFromParcel(`in`: Parcel): SellerOrderFilterFragData {
                return SellerOrderFilterFragData(`in`)
            }

            override fun newArray(size: Int): Array<SellerOrderFilterFragData?> {
                return arrayOfNulls(size)
            }
        }
        private val TAG = "SellerOrderFilterFragDa"
    }
}
