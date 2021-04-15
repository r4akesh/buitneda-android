package com.webkul.mobikulmp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.adapters.ProductCarouselHorizontalRvAdapter
import com.webkul.mobikul.models.product.ProductTileData
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.ItemMarketplaceLandingPageSellerBinding
import com.webkul.mobikulmp.handlers.MarketplaceLandingPageSellerHandler
import com.webkul.mobikulmp.models.landingpage.SellersData

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.adapters
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 1/7/19
 */

class MarketplaceLandingPageBestSellerListRvAdapter(private val mContext: Context, private val mSellerList: List<SellersData>) : RecyclerView.Adapter<MarketplaceLandingPageBestSellerListRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.item_marketplace_landing_page_seller, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val marketplaceLandingPageSellerData = mSellerList[position]
        holder.mBinding!!.data = marketplaceLandingPageSellerData
        holder.mBinding.handler = MarketplaceLandingPageSellerHandler(mContext)
        holder.mBinding.sellerProductRv.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        holder.mBinding.sellerProductRv.adapter = ProductCarouselHorizontalRvAdapter(mContext, marketplaceLandingPageSellerData.products as ArrayList<ProductTileData>)
        holder.mBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mSellerList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemMarketplaceLandingPageSellerBinding? = DataBindingUtil.bind(itemView)
    }
}
