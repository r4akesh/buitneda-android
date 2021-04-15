package com.webkul.mobikulmp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.ItemCreditMemoFormItemBinding
import com.webkul.mobikulmp.models.seller.CreditMemoFormItemList
import java.util.*

/**
 * Webkul Software.
 *
 * Kotlin
 *
 * @author Webkul <support@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

class RefundOrderItemsRvAdapter(private val mContext: Context, private val mListData: ArrayList<CreditMemoFormItemList>) : RecyclerView.Adapter<RefundOrderItemsRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_credit_memo_form_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachListData = mListData[position]
        holder.mBinding?.data = eachListData
        var qty = ""
        for (noOfQty in 0 until mListData[position].qty.size) {
            if (noOfQty == mListData[position].qty.size - 1)
                qty += (mListData[position].qty[noOfQty].label + " : " + mListData[position].qty[noOfQty].value)
            else
                qty += (mListData[position].qty[noOfQty].label + " : " + mListData[position].qty[noOfQty].value + "\n")
        }
        holder.mBinding?.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemCreditMemoFormItemBinding? = DataBindingUtil.bind(itemView)
    }
}