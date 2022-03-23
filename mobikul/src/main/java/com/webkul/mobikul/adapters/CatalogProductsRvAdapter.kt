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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.CatalogActivity
import com.webkul.mobikul.databinding.ItemCatalogProductGridBinding
import com.webkul.mobikul.databinding.ItemCatalogProductListBinding
import com.webkul.mobikul.handlers.CatalogActivityHandler
import com.webkul.mobikul.handlers.ProductTileHandler
import com.webkul.mobikul.helpers.ConstantsHelper.VIEW_TYPE_GRID
import com.webkul.mobikul.helpers.ConstantsHelper.VIEW_TYPE_LIST
import com.webkul.mobikul.models.product.ProductTileData

class CatalogProductsRvAdapter(private val mContext: CatalogActivity, private val mListData: ArrayList<ProductTileData>) : RecyclerView.Adapter<CatalogProductsRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == VIEW_TYPE_LIST) {
            ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_catalog_product_list, parent, false))
        } else {
            ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_catalog_product_grid, parent, false))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachListData = mListData[position]
        eachListData.productPosition = position
        if(eachListData.cartItemId!=""){
            eachListData.addedInfoCart = "true"
        }


        if (getItemViewType(position) == VIEW_TYPE_LIST) {
            (holder.mBinding as ItemCatalogProductListBinding).position = position
            holder.mBinding.data = eachListData
            holder.mBinding.handler = ProductTileHandler(mContext, mListData)
        } else {
            (holder.mBinding as ItemCatalogProductGridBinding).position = position
            holder.mBinding.data = eachListData
            holder.mBinding.handler = ProductTileHandler(mContext, mListData)
            holder.mBinding.baseHandler = CatalogActivityHandler(mContext)
            /*android:visibility="@{data.addedInfoCart== `false` &amp;&amp; data.cartItemId==`` || data.typeId!=`configurable`  ? View.VISIBLE : View.GONE}"*/

            if(eachListData.typeId!="configurable"){
                      if(!eachListData.isAuctionProduct && eachListData.isAvailable){
                          if(eachListData.addedInfoCart=="false" && eachListData.cartItemId==""){
                              holder.mBinding.addBtn.visibility = View.VISIBLE
                          }else{
                              holder.mBinding.addBtn.visibility = View.GONE
                          }
                      }else{
                          holder.mBinding.addBtn.visibility = View.GONE
                      }
            }else{
                holder.mBinding.addBtn.visibility = View.GONE
            }

        }
        holder.mBinding.executePendingBindings()
    }

    override fun getItemViewType(position: Int): Int {
        return if (mContext.mContentViewBinding.productsRv.layoutManager is GridLayoutManager) {
            VIEW_TYPE_GRID
        } else {
            VIEW_TYPE_LIST
        }
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ViewDataBinding? = DataBindingUtil.bind(itemView)
    }
}