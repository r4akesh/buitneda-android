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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.ItemCategoryListBinding
import com.webkul.mobikul.databinding.ItemCriteriaDataBinding
import com.webkul.mobikul.databinding.ItemSubCategoryListBinding
import com.webkul.mobikul.databinding.ItemSubSubCategoryListBinding
import com.webkul.mobikul.fragments.CategoryPageFragment
import com.webkul.mobikul.handlers.ItemSubCategoryListHandler
import com.webkul.mobikul.handlers.ItemSubSubCategoryListHandler
import com.webkul.mobikul.models.homepage.Category
import com.webkul.mobikul.models.homepage.SubCategory
import com.webkul.mobikul.models.homepage.SubSubCategory

class ItemSubSubCategoryListAdapter(private val mContext: CategoryPageFragment, private val mListData: ArrayList<SubSubCategory>?) : RecyclerView.Adapter<ItemSubSubCategoryListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemSubSubCategoryListAdapter.ViewHolder {
        val view = LayoutInflater.from(mContext.context).inflate(R.layout.item_sub_sub_category_list, p0, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemSubSubCategoryListAdapter.ViewHolder, position: Int) {
        val eachListData = mListData?.get(position)
        holder.mBinding?.data = eachListData
        holder.mBinding?.handler= ItemSubSubCategoryListHandler(mContext)

        holder.mBinding?.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mListData!!.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemSubSubCategoryListBinding? = DataBindingUtil.bind(itemView)
    }
}