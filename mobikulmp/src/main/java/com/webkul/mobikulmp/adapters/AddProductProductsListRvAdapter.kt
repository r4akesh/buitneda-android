package com.webkul.mobikulmp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.ItemSellerAddProductBinding
import com.webkul.mobikulmp.models.seller.ProductCollectionData
import java.util.*

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.fragments
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 22/7/19
 */
class AddProductProductsListRvAdapter(private val mContext: Context, private val mProductsList: ArrayList<ProductCollectionData>) : RecyclerView.Adapter<AddProductProductsListRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_seller_add_product, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachProductData = mProductsList[position]
        holder.mBinding!!.data = eachProductData
        holder.mBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mProductsList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemSellerAddProductBinding? = DataBindingUtil.bind(itemView)
    }
}
