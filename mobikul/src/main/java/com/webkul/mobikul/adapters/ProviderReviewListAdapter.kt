package com.webkul.mobikul.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.ItemReviewRatingLayoutBinding
import com.webkul.mobikul.models.service.ServiceProviderReviewModel


class ProviderReviewListAdapter(
    private val mContext: Context,
    private val mListData: ArrayList<ServiceProviderReviewModel>
) : RecyclerView.Adapter<ProviderReviewListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.item_review_rating_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachListData = mListData[position]
        holder.mBinding.data = eachListData

    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemReviewRatingLayoutBinding = ItemReviewRatingLayoutBinding.bind(itemView)
    }


}