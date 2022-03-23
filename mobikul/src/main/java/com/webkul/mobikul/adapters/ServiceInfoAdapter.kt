package com.webkul.mobikul.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.ServiceCategoryLayoutBinding
import com.webkul.mobikul.fragments.ServiceProviderFragment
import com.webkul.mobikul.interfaces.OnServiceTypeSelectListener
import com.webkul.mobikul.models.service.HomeServiceListModel
import com.webkul.mobikul.models.service.ServiceBannerListModel
import kotlinx.android.synthetic.main.item_text_service_info_layout.view.*


class ServiceInfoAdapter(
    private val mContext: Context,
    private val mListData: List<String>
) : RecyclerView.Adapter<ServiceInfoAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_text_service_info_layout, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = mListData[position]
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.itemView.serviceInfoTextView.text = Html.fromHtml(info,Html.FROM_HTML_MODE_LEGACY)
        }else{
            holder.itemView.serviceInfoTextView.text = Html.fromHtml(info)
        }
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }




}