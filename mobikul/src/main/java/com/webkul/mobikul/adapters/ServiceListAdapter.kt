package com.webkul.mobikul.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.ServiceProviderDetailActivity
import com.webkul.mobikul.databinding.ItemServiceProvidersListLayoutBinding
import com.webkul.mobikul.models.service.ServiceProviderModel
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList


class ServiceListAdapter(private val mContext: Context, private var mListData: ArrayList<ServiceProviderModel>) : RecyclerView.Adapter<ServiceListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_service_providers_list_layout, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachListData = mListData[position]
        holder.mBinding.data = eachListData
        if(eachListData.service_description!=null){
           // val serviceList:List<String> = eachListData.service_description!!.split("\\.")
           // setUpServiceList(holder.mBinding.serviceInfoList,serviceList)
            holder.mBinding.serviceInfoList.settings.javaScriptEnabled = true
            holder.mBinding.serviceInfoList.settings.defaultFontSize = 14
            holder.mBinding.serviceInfoList.loadData(eachListData.service_description.toString(), "text/html", "UTF-8");
        }
        holder.mBinding.viewFullInfoBtn.setOnClickListener{
            val providerId = eachListData.serviceprovider_id
            mContext.startActivity(Intent(mContext,ServiceProviderDetailActivity::class.java).putExtra("serviceId",eachListData.serviceprovider_id))
        }
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding:ItemServiceProvidersListLayoutBinding = ItemServiceProvidersListLayoutBinding.bind(itemView)
    }

   /* private fun setUpServiceList(recyclerView:RecyclerView, serviceInfoList:List<String>){
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        val serviceInfoAdapter = ServiceInfoAdapter(mContext,serviceInfoList)
        recyclerView.adapter = serviceInfoAdapter
    }*/


    @SuppressLint("NotifyDataSetChanged")
    fun ascendingOrder(){
     /*   mListData.sortWith(Comparator { lhs, rhs ->  Integer.compare(rhs!!.review_avg!!.toInt(), lhs!!.review_avg!!.toInt())
        })
        notifyDataSetChanged()*/
    }


}