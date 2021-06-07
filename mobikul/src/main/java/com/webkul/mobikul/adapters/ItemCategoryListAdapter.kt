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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.ItemCategoryListBinding
import com.webkul.mobikul.fragments.CategoryPageFragment
import com.webkul.mobikul.handlers.ItemCategoryFragmentHandler
import com.webkul.mobikul.models.homepage.Category

class ItemCategoryListAdapter(
    private val mContext: CategoryPageFragment,
    private val mListData: ArrayList<Category>?
) : RecyclerView.Adapter<ItemCategoryListAdapter.ViewHolder>() {

    open var selectedPosition: Int = 0
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemCategoryListAdapter.ViewHolder {
        val view =
            LayoutInflater.from(mContext.context).inflate(R.layout.item_category_list, p0, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemCategoryListAdapter.ViewHolder, position: Int) {
        val eachListData = mListData?.get(position)
        holder.mBinding?.isSelected =
            selectedPosition != -1 && selectedPosition == position
        holder.mBinding?.data = eachListData
        holder.mBinding?.position = position
        holder.mBinding?.handler = ItemCategoryFragmentHandler(mContext, this)

        holder.mBinding?.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mListData!!.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemCategoryListBinding? = DataBindingUtil.bind(itemView)
    }
}