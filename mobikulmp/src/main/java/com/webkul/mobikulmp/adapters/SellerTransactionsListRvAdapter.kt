package com.webkul.mobikulmp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.SellerTransactionsListActivity
import com.webkul.mobikulmp.databinding.ItemSellerTransactionBinding
import com.webkul.mobikulmp.handlers.ItemSellerTransactionListHandler
import com.webkul.mobikulmp.models.seller.SellerTransactionList

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
class SellerTransactionsListRvAdapter(private val mContext: SellerTransactionsListActivity, private val mListData: ArrayList<SellerTransactionList>) : RecyclerView.Adapter<SellerTransactionsListRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_seller_transaction, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachProductData = mListData[position]
        holder.mBinding!!.data = eachProductData
        holder.mBinding.handler = ItemSellerTransactionListHandler(mContext)
        holder.mBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemSellerTransactionBinding? = DataBindingUtil.bind(itemView)
    }
}
