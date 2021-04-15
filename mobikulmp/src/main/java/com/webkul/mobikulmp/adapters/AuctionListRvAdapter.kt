package com.webkul.mobikulmp.adapters

import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.ItemAutionListBinding
import com.webkul.mobikulmp.handlers.AuctionListRvHandler
import com.webkul.mobikulmp.models.auction.AuctionList
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by vedesh.kumar on 25/2/17. @Webkul Software Pvt. Ltd
 */
class AuctionListRvAdapter(private val mContext: Context, auctionList: ArrayList<AuctionList>) : RecyclerView.Adapter<AuctionListRvAdapter.ViewHolder?>() {
    private val mAuctionList: ArrayList<AuctionList>
    private val selectedItems: SparseBooleanArray
    fun toggleSelection(pos: Int) {
        if (selectedItems[pos, false]) {
            selectedItems.delete(pos)
            mAuctionList[pos].isSelected=false
        } else {
            selectedItems.put(pos, true)
            mAuctionList[pos].isSelected=true
        }
        notifyItemChanged(pos)
    }

    fun clearSelections() {
        selectedItems.clear()
        for (noOfProduct in mAuctionList.indices) {
            mAuctionList[noOfProduct].isSelected=false
            mAuctionList[noOfProduct].isSelectionModeOn=false
        }
        notifyDataSetChanged()
    }

    val selectedItemCount: Int
        get() = selectedItems.size()

    fun getSelectedItems(): List<Int> {
        val items: MutableList<Int> = ArrayList(selectedItems.size())
        for (i in 0 until selectedItems.size()) {
            items.add(selectedItems.keyAt(i))
        }
        return items
    }

    fun getItem(position: Int): AuctionList {
        return mAuctionList[position]
    }

  /* override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(mContext)
        return ViewHolder(inflater.inflate(R.layout.item_aution_list, parent, false))
    }*/

  override  fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val auctionList: AuctionList = mAuctionList[position]
        holder.mBinding.data = auctionList
        holder.mBinding.setHandler(AuctionListRvHandler(mContext))
        holder.itemView.setActivated(selectedItems[position, false])
        holder.mBinding.executePendingBindings()
    }


    fun remove(position: Int) {
        mAuctionList.removeAt(position)
        notifyItemRemoved(position)
    }


    class ViewHolder  constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val mBinding: ItemAutionListBinding

        init {
            mBinding = DataBindingUtil.bind(itemView)!!
        }
    }

    init {
        mAuctionList = auctionList
        selectedItems = SparseBooleanArray()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(mContext)
        return ViewHolder(inflater.inflate(R.layout.item_aution_list, parent, false))    }

    override fun getItemCount(): Int {
return mAuctionList.size
    }
}