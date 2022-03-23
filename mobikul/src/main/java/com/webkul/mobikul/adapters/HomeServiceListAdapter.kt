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
import com.webkul.mobikul.databinding.ServiceCategoryLayoutBinding
import com.webkul.mobikul.fragments.ServiceProviderFragment
import com.webkul.mobikul.interfaces.OnServiceTypeSelectListener
import com.webkul.mobikul.models.service.BannerImage
import com.webkul.mobikul.models.service.HomeServiceListModel
import com.webkul.mobikul.models.service.ServiceBannerListModel
import com.webkul.mobikul.models.service.ServiceBannerModel


class HomeServiceListAdapter(
    private val mContext: Context,
    private val mFragment: ServiceProviderFragment,
    private val mListData: ArrayList<HomeServiceListModel>
) : RecyclerView.Adapter<HomeServiceListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mFragment.context).inflate(R.layout.service_category_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachListData = mListData[position]
        holder.mBinding.data = eachListData
        setUpServiceList(holder.mBinding.serviceCategoryList,eachListData.services)
        holder.mBinding.allBtn.setOnClickListener{
            mContext.startActivity(Intent(mContext,ServiceAllActivity::class.java))
        }
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding:ServiceCategoryLayoutBinding = ServiceCategoryLayoutBinding.bind(itemView)
    }

    private fun setUpServiceList(recyclerView:RecyclerView, bannerList:ArrayList<ServiceBannerListModel>){
        recyclerView.layoutManager = LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false)
        val serviceCategoryListAdapter = ServiceCategoryListAdapter(mContext,mFragment,bannerList)
        recyclerView.adapter = serviceCategoryListAdapter
    }





}