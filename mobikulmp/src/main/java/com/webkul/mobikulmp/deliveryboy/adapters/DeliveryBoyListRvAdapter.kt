package com.webkul.mobikulmp.deliveryboy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.helpers.ImageHelper.Companion.load
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.ItemDeliveryBoyBinding
import com.webkul.mobikulmp.deliveryboy.fragment.SelectDeliveryBoyBottomSheetFragment
import com.webkul.mobikulmp.deliveryboy.handlers.DeliveryBoyListRvHandler
import com.webkul.mobikulmp.deliveryboy.models.DeliveryBoyList


class DeliveryBoyListRvAdapter(private val mContext: SelectDeliveryBoyBottomSheetFragment, private val mDeliveryBoyList: ArrayList<DeliveryBoyList>, private val mDeliveryboyId: String?) : RecyclerView.Adapter<DeliveryBoyListRvAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val contactView = LayoutInflater.from(mContext.context).inflate(R.layout.item_delivery_boy, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachDeliveryBoy = mDeliveryBoyList[position]
        if (mDeliveryboyId != null && mDeliveryboyId == eachDeliveryBoy.id) {
            holder.mBinding!!.selected = true
        }
        holder.mBinding!!.data = eachDeliveryBoy
        holder.mBinding.handler = DeliveryBoyListRvHandler(mContext)
        load(holder.mBinding.profileImage
                , if (eachDeliveryBoy.avatar!!.isEmpty()) null else eachDeliveryBoy.avatar
                , null)
        holder.mBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mDeliveryBoyList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemDeliveryBoyBinding? = DataBindingUtil.bind(itemView)
    }

}