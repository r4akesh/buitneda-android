package com.webkul.mobikulmp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.ItemSellerListBinding
import com.webkul.mobikulmp.handlers.SellerListItemHandler
import com.webkul.mobikulmp.models.SellerListData

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.models.seller
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 10/7/19
 */
class SellerListActivityRvAdapter(private val mContext: Context, private val mSellerListDatas: List<SellerListData>) : RecyclerView.Adapter<SellerListActivityRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(mContext)
        val contactView = inflater.inflate(R.layout.item_seller_list, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sellerListData = mSellerListDatas[position]
        holder.mBinding!!.data = sellerListData
        holder.mBinding.handler = SellerListItemHandler(mContext)
        holder.mBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mSellerListDatas.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemSellerListBinding? = DataBindingUtil.bind(itemView)
    }
}
