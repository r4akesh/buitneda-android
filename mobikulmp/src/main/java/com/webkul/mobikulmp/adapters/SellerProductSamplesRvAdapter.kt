package com.webkul.mobikulmp.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.SellerAddProductActivity
import com.webkul.mobikulmp.databinding.ItemSellerProductSampleBinding
import com.webkul.mobikulmp.handlers.SellerProductSamplesRvHandler
import com.webkul.mobikulmp.models.seller.SampleDatum
import java.util.*

/**
 * Webkul Software.
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

class SellerProductSamplesRvAdapter(private val mContext: SellerAddProductActivity, val itemList: ArrayList<SampleDatum>) : RecyclerView.Adapter<SellerProductSamplesRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.item_seller_product_sample, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sampleData = this.itemList[position]
        holder.mBinding!!.position = position
        holder.mBinding.data = sampleData
        holder.mBinding.handler = SellerProductSamplesRvHandler(mContext)

        val arrayShareableData = mContext.resources.getStringArray(R.array.file_type_sample)
        holder.mBinding.sampleTypeSpinner.adapter = ArrayAdapter<String>(mContext, R.layout.custom_spinner_item, arrayShareableData)
        holder.mBinding.sampleTypeSpinner.setSelection(sampleData.sampleTypeSelected)

        holder.mBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemSellerProductSampleBinding? = DataBindingUtil.bind(itemView)
    }
}