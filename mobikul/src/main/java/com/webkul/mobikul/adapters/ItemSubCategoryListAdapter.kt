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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.ItemCategoryListBinding
import com.webkul.mobikul.databinding.ItemCriteriaDataBinding
import com.webkul.mobikul.databinding.ItemSubCategoryListBinding
import com.webkul.mobikul.fragments.CategoryPageFragment
import com.webkul.mobikul.handlers.ItemSubCategoryListHandler
import com.webkul.mobikul.models.homepage.Category
import com.webkul.mobikul.models.homepage.SubCategory

class ItemSubCategoryListAdapter(private val mContext: CategoryPageFragment, private val mListData: ArrayList<SubCategory>) : RecyclerView.Adapter<ItemSubCategoryListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemSubCategoryListAdapter.ViewHolder {
        val view = LayoutInflater.from(mContext.context).inflate(R.layout.item_sub_category_list, p0, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemSubCategoryListAdapter.ViewHolder, position: Int) {
        val eachListData = mListData.get(position)
        holder.mBinding?.data = eachListData
        holder.mBinding?.handler= ItemSubCategoryListHandler(mContext)

        if(eachListData.hasChildren){
            holder.mBinding!!.category3.layoutManager= GridLayoutManager(mContext.context, 3)

            holder.mBinding?.category3?.setAdapter(ItemSubSubCategoryListAdapter(mContext!!, eachListData.subSubCategory))
        }
        holder.mBinding?.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mListData!!.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemSubCategoryListBinding? = DataBindingUtil.bind(itemView)
    }
}