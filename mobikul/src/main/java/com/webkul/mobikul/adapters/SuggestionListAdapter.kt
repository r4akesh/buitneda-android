package com.webkul.mobikul.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.webkul.mobikul.R
import com.webkul.mobikul.models.service.Area
import com.webkul.mobikul.models.service.ServiceBannerListModel
import kotlinx.android.synthetic.main.custom_spinner_item.view.*

class SuggestionListAdapter(mContext: Context, countries: ArrayList<ServiceBannerListModel>) : ArrayAdapter<ServiceBannerListModel>(mContext, 0, countries) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent);
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent);
    }

    fun createItemView(position: Int, recycledView: View?, parent: ViewGroup):View {
        val serviceModel = getItem(position)

        val view = recycledView ?: LayoutInflater.from(context).inflate(
            R.layout.custom_spinner_item,
            parent,
            false
        )

        serviceModel?.let {
            view.spinnerItem.text = serviceModel.service_title
        }
        return view
    }
}