/*
 * Webkul Software.
 *
 * Kotlin
 *
 * @author Webkul <support@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2019 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

package com.webkul.mobikulmp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.ItemSellerOrderInvoiceItemsBinding
import com.webkul.mobikulmp.models.seller.SellerOrderInvoiceItem

class SellerOrderInvoiceItemsRvAdapter(private val mContext: Context, private val mListData: ArrayList<SellerOrderInvoiceItem>) : RecyclerView.Adapter<SellerOrderInvoiceItemsRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_seller_order_invoice_items, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachListData = mListData[position]
        holder.mBinding?.data = eachListData
        var qtyString = ""
        eachListData.qty?.forEach { eachQtyOptions ->
            qtyString += "${eachQtyOptions.label} : ${eachQtyOptions.value}\n"
        }
        holder.mBinding?.qtyValue?.text = qtyString

        holder.mBinding?.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemSellerOrderInvoiceItemsBinding? = DataBindingUtil.bind(itemView)
    }
}