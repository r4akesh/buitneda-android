package com.webkul.mobikul.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.ItemAutionBidListBinding
import com.webkul.mobikul.databinding.ItemBrandListGridBinding
import com.webkul.mobikul.handlers.AuctionBidListRvHandler
import com.webkul.mobikul.handlers.FeaturedCategoriesRvHandler
import com.webkul.mobikul.models.auction.AuctonBidList

/**
 * Created by vedesh.kumar on 25/2/17. @Webkul Software Pvt. Ltd
 */
class AuctionBidListRvAdapter(private val mContext: Context, private val mAuctionBidList: List<AuctonBidList>) : RecyclerView.Adapter<AuctionBidListRvAdapter.ViewHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuctionBidListRvAdapter.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_aution_bid_list,parent, false)
        return AuctionBidListRvAdapter.ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachListData = mAuctionBidList[position]
        (holder.mBinding as ItemAutionBidListBinding).data = eachListData
        holder.mBinding.handler=AuctionBidListRvHandler(mContext)
        holder.mBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mAuctionBidList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemAutionBidListBinding? = DataBindingUtil.bind(itemView)
    }



}