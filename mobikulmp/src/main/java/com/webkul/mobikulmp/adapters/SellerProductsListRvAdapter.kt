package com.webkul.mobikulmp.adapters

import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.ItemSellerProductBinding
import com.webkul.mobikulmp.handlers.ItemSellerProductListHandler
import com.webkul.mobikulmp.models.seller.SellerProductData
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
class SellerProductsListRvAdapter(private val mContext: Context, private val mProductsList: ArrayList<SellerProductData>) : RecyclerView.Adapter<SellerProductsListRvAdapter.ViewHolder>() {

    private val selectedItems: SparseBooleanArray = SparseBooleanArray()

    val selectedItemCount: Int
        get() = selectedItems.size()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(mContext)
        val contactView = inflater.inflate(R.layout.item_seller_product, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachProductData = mProductsList[position]
        eachProductData.position = position
        holder.mBinding!!.data = eachProductData
        holder.mBinding.handler = ItemSellerProductListHandler(mContext)
        holder.itemView.isActivated = selectedItems.get(position, false)
        holder.mBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mProductsList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemSellerProductBinding? = DataBindingUtil.bind(itemView)
    }


    fun remove(position: Int) {
        mProductsList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun toggleSelection(pos: Int) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos)
            mProductsList[pos].isSelected = false
        } else {
            selectedItems.put(pos, true)
            mProductsList[pos].isSelected = true
        }
        notifyItemChanged(pos)
    }

    fun clearSelections() {
        selectedItems.clear()
        for (noOfProduct in mProductsList.indices) {
            mProductsList[noOfProduct].isSelected = false
            mProductsList[noOfProduct].isSelectionModeOn = false
        }
        notifyDataSetChanged()
    }

    fun getSelectedItems(): List<Int> {
        val items = ArrayList<Int>(selectedItems.size())
        for (i in 0 until selectedItems.size()) {
            items.add(selectedItems.keyAt(i))
        }
        return items
    }

    fun getItem(position: Int): SellerProductData {
        return mProductsList[position]
    }
}
