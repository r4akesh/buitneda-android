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

package com.webkul.mobikul.adapters

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.ItemOrderInvoiceItemsBinding
import com.webkul.mobikul.helpers.ApplicationConstants
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.user.OrderInvoiceItem
import io.github.inflationx.calligraphy3.TypefaceUtils


class OrderInvoiceItemsRvAdapter(private val mContext: Context, private val mListData: ArrayList<OrderInvoiceItem>) : RecyclerView.Adapter<OrderInvoiceItemsRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderInvoiceItemsRvAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_order_invoice_items, parent, false))
    }

    override fun onBindViewHolder(holder: OrderInvoiceItemsRvAdapter.ViewHolder, position: Int) {
        val eachListData = mListData[position]
        holder.mBinding?.data = eachListData

        if (eachListData.itemOption?.size?:0 > 0) {
            holder.mBinding?.optionTableLayout?.removeAllViews()
            for (optionIterator in 0 until (eachListData.itemOption?.size?:0)) {

                val optionItem = eachListData.itemOption?.get(optionIterator)

                val tableRow = TableRow(mContext)
                tableRow.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT)
                tableRow.gravity = Gravity.CENTER_VERTICAL

                val labelTv = TextView(mContext)
                labelTv.layoutParams = TableRow.LayoutParams((Utils.screenWidth / 4), TableRow.LayoutParams.WRAP_CONTENT)
                labelTv.setPadding(0, 12, 0, 0)
                labelTv.textSize = 12f
                labelTv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_secondary))
                labelTv.typeface = TypefaceUtils.load(mContext.assets, ApplicationConstants.CALLIGRAPHY_FONT_PATH_SEMI_BOLD)
                labelTv.text = optionItem?.label + mContext.getString(R.string.colon)
                tableRow.addView(labelTv)

                val valueTv = TextView(mContext)
                valueTv.layoutParams = TableRow.LayoutParams((Utils.screenWidth / 3.5).toInt(), TableRow.LayoutParams.WRAP_CONTENT)
                valueTv.setPadding(10, 12, 10, 0)
                valueTv.textSize = 12f
                valueTv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_primary))
                valueTv.typeface = TypefaceUtils.load(mContext.assets, ApplicationConstants.CALLIGRAPHY_FONT_PATH_SEMI_BOLD)
                var value = optionItem?.value?.get(0)
                for (noOfValues in 1 until (optionItem?.value?.size ?:0)) {
                    value = value + "\n" + optionItem?.value?.get(noOfValues)
                }
                valueTv.text = value
                tableRow.addView(valueTv)
                holder.mBinding?.optionTableLayout?.addView(tableRow)
            }
        }

        holder.mBinding?.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemOrderInvoiceItemsBinding? = DataBindingUtil.bind(itemView)
    }
}