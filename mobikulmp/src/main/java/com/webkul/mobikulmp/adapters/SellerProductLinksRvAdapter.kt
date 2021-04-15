package com.webkul.mobikulmp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.SellerAddProductActivity
import com.webkul.mobikulmp.databinding.ItemSellerProductLinkBinding
import com.webkul.mobikulmp.handlers.SellerProductLinksRvHandler
import com.webkul.mobikulmp.models.seller.LinkDatum
import java.util.*

/**
 * Webkul Software.
 *
 *
 * Java
 *
 * @author Webkul <support></support>@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

class SellerProductLinksRvAdapter(private val mContext: SellerAddProductActivity, val itemList: ArrayList<LinkDatum>) : RecyclerView.Adapter<SellerProductLinksRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.item_seller_product_link, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val linkData = itemList[position]
        holder.mBinding!!.position = position
        holder.mBinding.data = linkData
        holder.mBinding.handler = SellerProductLinksRvHandler(mContext)


        val arrayData = mContext.resources.getStringArray(R.array.file_type)
        holder.mBinding.fileTypeSpinner.adapter = ArrayAdapter<String>(mContext, R.layout.custom_spinner_item, arrayData)
        holder.mBinding.fileTypeSpinner.setSelection(linkData.fileTypeSelected)


        val arrayFileSampleData = mContext.resources.getStringArray(R.array.file_type_sample)
        holder.mBinding.sampleTypeSpinner.adapter = ArrayAdapter<String>(mContext, R.layout.custom_spinner_item, arrayFileSampleData)
        holder.mBinding.sampleTypeSpinner.setSelection(linkData.sampleTypeSelected)

        val arrayShareableData = mContext.resources.getStringArray(R.array.shareable_label)
        holder.mBinding.shareableSpinner.adapter = ArrayAdapter<String>(mContext, R.layout.custom_spinner_item, arrayShareableData)
        holder.mBinding.shareableSpinner.setSelection(linkData.shareable)


        holder.mBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemSellerProductLinkBinding? = DataBindingUtil.bind(itemView)

    }

    fun getItemsList(): ArrayList<LinkDatum> {
        return itemList
    }
}