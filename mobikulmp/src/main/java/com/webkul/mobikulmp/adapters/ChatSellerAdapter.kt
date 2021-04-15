package com.webkul.mobikulmp.adapters

//import androidx.recyclerview.widget.RecyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.handlers.ChatSellerHandler
import com.webkul.mobikulmp.models.chat.ChatSellerData
import com.webkul.mobikulmp.models.chat.ChatSellerViewHolder
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

class ChatSellerAdapter(private val mContext: Context, private val sellerList: MutableList<ChatSellerData>) : RecyclerView.Adapter<ChatSellerViewHolder>(), Filterable {
    private var sellerFilter: SellerFilter? = null
    private val filteredSellerList: MutableList<ChatSellerData>


    init {
        this.filteredSellerList = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatSellerViewHolder {
        val inflater = LayoutInflater.from(mContext)
        val sellerView = inflater.inflate(R.layout.chat_seller_details, parent, false)

        return ChatSellerViewHolder(sellerView)
    }

    override fun onBindViewHolder(holder: ChatSellerViewHolder, position: Int) {
        val sellerData = sellerList[position]
        holder.mBinding?.seller = sellerData
        holder.mBinding?.handler = ChatSellerHandler(mContext)
        holder.mBinding?.executePendingBindings()

    }

    override fun getItemCount(): Int {
        return sellerList.size
    }

    override fun getFilter(): Filter {
        if (sellerFilter == null) {
            filteredSellerList.clear()
            filteredSellerList.addAll(this.sellerList)
            sellerFilter = SellerFilter(this, filteredSellerList)
        }

        return sellerFilter as SellerFilter
    }

    private class SellerFilter(private val sellerAdapter: ChatSellerAdapter, private val originalList: List<ChatSellerData>) : Filter() {
        private val filteredList: MutableList<ChatSellerData>

        init {
            this.filteredList = ArrayList()
        }

        override fun performFiltering(constraint: CharSequence): Filter.FilterResults {
            filteredList.clear()
            val results = Filter.FilterResults()
            if (constraint.length == 0) {
                filteredList.addAll(originalList)
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim { it <= ' ' }
                for (seller in originalList) {

                    if (seller.name.toLowerCase().contains(filterPattern)) {
                        filteredList.add(seller)
                    }
                }
            }

            results.values = filteredList
            results.count = filteredList.size
            return results
        }

        override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
            sellerAdapter.sellerList.clear()
            sellerAdapter.sellerList.addAll(results.values as ArrayList<ChatSellerData>)
            sellerAdapter.notifyDataSetChanged()
        }
    }
}
