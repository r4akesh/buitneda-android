package com.webkul.mobikulmp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.ItemAttributeOptionBinding
import com.webkul.mobikulmp.handlers.AttributeOptionItemHandler
import com.webkul.mobikulmp.models.seller.AttributeOptionItemData
import java.util.*


/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.models.seller
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 23/5/19
 */

class AttributeOptionRvAdapter(private val mContext: Context, private val mAttributeOptionList: ArrayList<AttributeOptionItemData>) : RecyclerView.Adapter<AttributeOptionRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(mContext)
        return ViewHolder(inflater.inflate(R.layout.item_attribute_option, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val attributeOptionItemData = mAttributeOptionList[position]
        holder.mBinding!!.data = attributeOptionItemData
        holder.mBinding.removeBtn.tag = position
        holder.mBinding.handler = AttributeOptionItemHandler()
        holder.mBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mAttributeOptionList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemAttributeOptionBinding? = DataBindingUtil.bind(itemView)
    }
}
