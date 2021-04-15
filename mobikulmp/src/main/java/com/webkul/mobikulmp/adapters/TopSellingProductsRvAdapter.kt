package com.webkul.mobikulmp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.adapters.TopSellingProductsRvAdapter.ViewHolder
import com.webkul.mobikulmp.databinding.ItemTopSellingProductBinding
import com.webkul.mobikulmp.handlers.TopSellingProductsHandler
import com.webkul.mobikulmp.models.seller.TopSellingProduct
import java.util.*

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
class TopSellingProductsRvAdapter(private val mContext: Context, private val mTopSellingProductsData: ArrayList<TopSellingProduct>) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_top_selling_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val topSellingProduct = mTopSellingProductsData[position]
        holder.mBinding!!.data = topSellingProduct
        holder.mBinding.handler = TopSellingProductsHandler()
        holder.mBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mTopSellingProductsData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemTopSellingProductBinding? = DataBindingUtil.bind(itemView)

    }
}
