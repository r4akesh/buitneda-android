package com.webkul.mobikul.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.ServiceAllActivity
import com.webkul.mobikul.databinding.FilterTextLayoutBinding
import com.webkul.mobikul.databinding.ServiceCategoryLayoutBinding
import com.webkul.mobikul.fragments.ServiceProviderFragment
import com.webkul.mobikul.interfaces.OnFilterItemSelectListener
import com.webkul.mobikul.interfaces.OnServiceTypeSelectListener
import com.webkul.mobikul.models.service.BannerImage
import com.webkul.mobikul.models.service.HomeServiceListModel
import com.webkul.mobikul.models.service.ServiceBannerListModel
import com.webkul.mobikul.models.service.ServiceBannerModel


class ServiceFilterAdapter(
    private val mContext: Context,
    private val mListData: ArrayList<ServiceBannerListModel>,
    private val onFilterItemSelectListener: OnFilterItemSelectListener
) : RecyclerView.Adapter<ServiceFilterAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.filter_text_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachListData = mListData[position]
        holder.mBinding.data = eachListData
        holder.mBinding.root.setOnClickListener {
            onFilterItemSelectListener.onFilterItemSelect(eachListData.service_id)

        }
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding:FilterTextLayoutBinding = FilterTextLayoutBinding.bind(itemView)
    }

}