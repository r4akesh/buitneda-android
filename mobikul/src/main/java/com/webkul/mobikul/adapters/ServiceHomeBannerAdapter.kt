package com.webkul.mobikul.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.ItemHomeServiceBannerViewPagerBinding
import com.webkul.mobikul.fragments.ServiceProviderFragment
import com.webkul.mobikul.interfaces.OnServiceTypeSelectListener
import com.webkul.mobikul.models.service.BannerImage


class ServiceHomeBannerAdapter(
    private val mContext: Context,
    private val mFragment: ServiceProviderFragment,
    private val mListData: ArrayList<BannerImage>,
    private val onServiceTypeSelectListener: OnServiceTypeSelectListener
) : RecyclerView.Adapter<ServiceHomeBannerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mFragment.context)
            .inflate(R.layout.item_home_service_banner_view_pager, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachListData = mListData[position]
        holder.mBinding.data = eachListData
        holder.itemView.setOnClickListener {
            onServiceTypeSelectListener.onServiceBannerSelect(eachListData)
        }
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemHomeServiceBannerViewPagerBinding =
            ItemHomeServiceBannerViewPagerBinding.bind(itemView)
    }


}