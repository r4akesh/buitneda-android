package com.webkul.mobikul.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.ItemBigBannerProductBinding
import com.webkul.mobikul.databinding.ItemHorizontalScrollerProductViewBinding
import com.webkul.mobikul.handlers.ProductTileHandler
import com.webkul.mobikul.models.product.ProductTileData

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

class BigBannerRvAdapter(private val mContext: Context, private val mListData: ArrayList<ProductTileData>) : RecyclerView.Adapter<BigBannerRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_big_banner_product, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachListData = mListData.get(position)
        holder.mBinding?.position = position
        holder.mBinding?.data = eachListData
        holder.mBinding?.handler = ProductTileHandler(mContext, mListData)
        holder.mBinding?.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemBigBannerProductBinding? = DataBindingUtil.bind(itemView)
    }
}