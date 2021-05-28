package com.webkul.mobikul.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.ItemBrandListGridBinding
import com.webkul.mobikul.fragments.HomeFragment
import com.webkul.mobikul.handlers.FeaturedCategoriesRvHandler
import com.webkul.mobikul.models.Brandlist
import com.webkul.mobikul.models.product.AnalysisModel

/**
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
class BrandListRvAdapter(
    private val mContext: HomeFragment,
    private val mListData: List<Brandlist>,
    val layoutView: Int,
    private val carouselType: String

) : RecyclerView.Adapter<BrandListRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext.context)
            .inflate(R.layout.item_brand_list_grid, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachListData = mListData[position]
        (holder.mBinding as ItemBrandListGridBinding).data = eachListData
        holder.mBinding.handler = FeaturedCategoriesRvHandler(mContext)

        carouselType.let {
            holder.mBinding.analysisData = AnalysisModel(it, eachListData.id)
        }

        holder.mBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemBrandListGridBinding? = DataBindingUtil.bind(itemView)
    }
//
//    override fun getItemViewType(position: Int): Int {
//        return if (mContext.mContentViewBinding.featuredCategoriesRv.layoutManager is GridLayoutManager) {
//            ConstantsHelper.VIEW_TYPE_GRID
//        } else {
//            ConstantsHelper.VIEW_TYPE_LIST
//        }
//    }
}