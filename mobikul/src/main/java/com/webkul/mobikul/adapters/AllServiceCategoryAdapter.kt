package com.webkul.mobikul.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.ItemServiceCategoryLayoutBinding
import com.webkul.mobikul.databinding.ItemServiceGirdCategoryLayoutBinding
import com.webkul.mobikul.handlers.ServiceCategoryHandler
import com.webkul.mobikul.models.service.ServiceBannerListModel


class AllServiceCategoryAdapter(
    private val mContext: Context,
    private val mBannerListData: ArrayList<ServiceBannerListModel>
) : RecyclerView.Adapter<AllServiceCategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext)
                .inflate(R.layout.item_service_gird_category_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachListData = mBannerListData[position]
        holder.mBinding.data = eachListData
        holder.mBinding.handler = ServiceCategoryHandler(mContext)
    }

    override fun getItemCount(): Int {
        return mBannerListData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemServiceGirdCategoryLayoutBinding =
            ItemServiceGirdCategoryLayoutBinding.bind(itemView)
    }

}