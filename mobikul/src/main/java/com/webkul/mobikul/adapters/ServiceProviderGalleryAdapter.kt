package com.webkul.mobikul.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.*
import com.webkul.mobikul.fragments.ServiceProviderFragment
import com.webkul.mobikul.interfaces.OnServiceTypeSelectListener
import com.webkul.mobikul.models.service.HomeServiceListModel
import com.webkul.mobikul.models.service.ServiceBannerListModel
import com.webkul.mobikul.models.service.ServiceProviderModel
import com.webkul.mobikul.models.service.ServiceProviderReviewModel


class ServiceProviderGalleryAdapter(private val mContext: Context, private val mListData: ArrayList<String>) : RecyclerView.Adapter<ServiceProviderGalleryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_service_provider_gallery, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mBinding.data = mListData[position]

    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding:ItemServiceProviderGalleryBinding = ItemServiceProviderGalleryBinding.bind(itemView)
    }





}