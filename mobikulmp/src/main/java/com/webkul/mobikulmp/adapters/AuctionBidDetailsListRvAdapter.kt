package com.webkul.mobikulmp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.models.auction.BidList
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.ItemAutionBidDetailsListBinding

/**
 * Created by vedesh.kumar on 25/2/17. @Webkul Software Pvt. Ltd
 */
class AuctionBidDetailsListRvAdapter(private val mContext: Context, private val mAuctionBidList: ArrayList<BidList>) : RecyclerView.Adapter<AuctionBidDetailsListRvAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(mContext)
        return ViewHolder(inflater.inflate(R.layout.item_aution_bid_details_list, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bidList = mAuctionBidList[position]
        holder.mBinding!!.data = bidList
        holder.mBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mAuctionBidList.size
    }

    fun remove(position: Int) {
        mAuctionBidList.removeAt(position)
        notifyItemRemoved(position)
    }

    class ViewHolder  constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemAutionBidDetailsListBinding?

        init {
            mBinding = DataBindingUtil.bind(itemView)
        }
    }

}