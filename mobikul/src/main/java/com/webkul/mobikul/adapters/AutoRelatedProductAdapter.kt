/*
 * Webkul Software.
 *
 * Kotlin
 *
 * @author Webkul <support@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

package com.webkul.mobikul.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.ItemProductReviewBinding
import com.webkul.mobikul.helpers.HorizontalMarginItemDecoration
import com.webkul.mobikul.models.extra.AutoRelatedProduct
import com.webkul.mobikul.models.extra.AutoRelatedProductList
import com.webkul.mobikul.models.extra.Product
import kotlinx.android.synthetic.main.item_auto_related_product_list.view.*

class AutoRelatedProductAdapter(
    private val mContext: Context,
    private val mListData: ArrayList<AutoRelatedProduct>
) : RecyclerView.Adapter<AutoRelatedProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.item_auto_related_product_list, p0, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachListData = mListData[position]
        holder.itemView.dynamicHeading.text = eachListData.groupTitle
        setupRelatedProductsRv(holder.itemView.related_products_rv, eachListData.products)

    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    private fun setupRelatedProductsRv(
        recyclerView: RecyclerView,
        list: List<Product>
    ) {
        if (recyclerView.adapter == null) {
            recyclerView.addItemDecoration(
                HorizontalMarginItemDecoration(
                    mContext.resources.getDimension(R.dimen.spacing_tiny).toInt()
                )
            )
            recyclerView.isNestedScrollingEnabled = false
        }
        recyclerView.adapter = AutoRelatedProductListAdapter(mContext, list as ArrayList)
    }
}