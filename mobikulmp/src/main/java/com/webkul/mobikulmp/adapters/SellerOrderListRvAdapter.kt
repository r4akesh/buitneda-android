package com.webkul.mobikulmp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.ItemSellerOrderBinding
import com.webkul.mobikulmp.databinding.ItemSellerOrderProductBinding
import com.webkul.mobikulmp.handlers.SellerOrderItemRecyclerHandler
import com.webkul.mobikulmp.handlers.SellerOrderProductHandler
import com.webkul.mobikulmp.models.seller.SellerOrderData

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.fragments
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 28/6/19
 */
class SellerOrderListRvAdapter(private val mContext: Context, private val mOrderLists: List<SellerOrderData>) : RecyclerView.Adapter<SellerOrderListRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(mContext)
        val contactView = inflater.inflate(R.layout.item_seller_order, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachOrderData = mOrderLists[position]
        if (holder.mBinding!!.productListContainer.childCount > 0) {
            holder.mBinding.productListContainer.removeAllViews()
        }

        if (eachOrderData.sellerOrderProductList?.size != 0) {
            for (eachSellerOrderProduct in eachOrderData.sellerOrderProductList!!) {
                val itemSellerOrderProductBinding = DataBindingUtil.inflate<ItemSellerOrderProductBinding>(LayoutInflater.from(mContext), R.layout.item_seller_order_product, holder.mBinding.productListContainer, true)
                itemSellerOrderProductBinding.data = eachSellerOrderProduct
                itemSellerOrderProductBinding.handler = SellerOrderProductHandler(mContext, eachSellerOrderProduct)
            }
        }
        holder.mBinding.data = eachOrderData
        holder.mBinding.handler = SellerOrderItemRecyclerHandler()
        holder.mBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mOrderLists.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemSellerOrderBinding? = DataBindingUtil.bind(itemView)

    }
}
