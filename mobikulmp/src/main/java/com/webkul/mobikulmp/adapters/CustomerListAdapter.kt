package com.webkul.mobikulmp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.ItemCustomerListBinding
import com.webkul.mobikulmp.handlers.CustomerListHandler
import com.webkul.mobikulmp.models.CustomerList


class CustomerListAdapter(private val mContext: Context, private val customerList: ArrayList<CustomerList>) : RecyclerView.Adapter<CustomerListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_customer_list, viewGroup, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return customerList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val customerData = customerList[position]
        holder.mContentViewBinding!!.data = customerData
        holder.mContentViewBinding.handler = CustomerListHandler(mContext)
        holder.mContentViewBinding.executePendingBindings()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mContentViewBinding: ItemCustomerListBinding? = DataBindingUtil.bind(itemView)
    }
}