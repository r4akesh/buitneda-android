package com.webkul.mobikulmp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.SellerAddProductActivity
import com.webkul.mobikulmp.databinding.ItemSelectedCategoriesBinding
import com.webkul.mobikulmp.handlers.SelectedCategoriesHandler
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

class SelectedCategoriesRvAdapter(var context: SellerAddProductActivity, var mSelectedCategoriesId: ArrayList<String>, var mSelectedCategoryName: ArrayList<String>) : RecyclerView.Adapter<SelectedCategoriesRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        return ViewHolder(inflater.inflate(R.layout.item_selected_categories, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mBinding?.id = mSelectedCategoriesId[position]
        holder.mBinding?.name = mSelectedCategoryName[position]
        holder.mBinding?.handler = SelectedCategoriesHandler(context)
        holder.mBinding?.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mSelectedCategoryName.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemSelectedCategoriesBinding? = DataBindingUtil.bind(itemView)
    }

    fun setData(mSelectedCategoriesId: ArrayList<String>, mSelectedCategoryName: ArrayList<String>) {
        this.mSelectedCategoriesId = mSelectedCategoriesId
        this.mSelectedCategoryName = mSelectedCategoryName
        notifyDataSetChanged()
    }

    fun removeItem(id: String, name: String) {
        for (noOfCategory in 0 until mSelectedCategoriesId.size) {
            if (id == mSelectedCategoriesId[noOfCategory]) {
                mSelectedCategoriesId.remove(id)
                mSelectedCategoryName.remove(name)
                notifyItemRemoved(noOfCategory)
                break
            }
        }

    }
}
