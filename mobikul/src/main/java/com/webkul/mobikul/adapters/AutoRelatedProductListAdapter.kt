/*
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

package com.webkul.mobikul.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.ItemAutoRelatedProductBinding
import com.webkul.mobikul.databinding.ItemAutoRelatedProductListBinding
import com.webkul.mobikul.databinding.ItemRelatedProductBinding
import com.webkul.mobikul.handlers.ProductTileHandler
import com.webkul.mobikul.models.extra.AutoRelatedProductList
import com.webkul.mobikul.models.extra.Product
import com.webkul.mobikul.models.product.ProductTileData

class AutoRelatedProductListAdapter(private val mContext: Context, private val mListData: ArrayList<Product>) : RecyclerView.Adapter<AutoRelatedProductListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_auto_related_product, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachListData = mListData[position]
        holder.mBinding?.position = position
        holder.mBinding?.data = eachListData
        //holder.mBinding?.handler = ProductTileHandler(mContext, mListData)
        holder.mBinding?.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemAutoRelatedProductBinding? = DataBindingUtil.bind(itemView)
    }
}