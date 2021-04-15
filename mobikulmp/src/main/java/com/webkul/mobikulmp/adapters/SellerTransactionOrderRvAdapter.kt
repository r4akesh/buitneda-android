package com.webkul.mobikulmp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.SellerTransactionDetailsActivity
import com.webkul.mobikulmp.databinding.ItemSellerTransactionOrderBinding
import com.webkul.mobikulmp.models.seller.TransactionOrderList
import java.util.*

/**
 * Webkul Software.
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
class SellerTransactionOrderRvAdapter(private val mContext: SellerTransactionDetailsActivity, private val mListData: ArrayList<TransactionOrderList>) : RecyclerView.Adapter<SellerTransactionOrderRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_seller_transaction_order, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachListData = mListData[position]
        holder.mBinding!!.data = eachListData
        holder.mBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemSellerTransactionOrderBinding? = DataBindingUtil.bind(itemView)
    }
}
