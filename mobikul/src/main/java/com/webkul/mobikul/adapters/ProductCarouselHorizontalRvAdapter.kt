package com.webkul.mobikul.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.ItemHorizontalScrollerProductViewBinding
import com.webkul.mobikul.handlers.ProductTileHandler
import com.webkul.mobikul.models.product.AnalysisModel
import com.webkul.mobikul.models.product.ProductTileData


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

class ProductCarouselHorizontalRvAdapter(
    private val mContext: Context,
    private val mListData: ArrayList<ProductTileData>,
    private val carouselType: String?
) : RecyclerView.Adapter<ProductCarouselHorizontalRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.item_horizontal_scroller_product_view, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachListData = mListData.get(position)
        holder.mBinding?.position = position
        holder.mBinding?.data = eachListData
        carouselType?.let {
            holder.mBinding?.analysisData = AnalysisModel(it, eachListData.id)
        }

        holder.mBinding?.handler = ProductTileHandler(mContext, mListData)
        //homeItemImage
//        holder.mBinding?.itemProductGridView?.homeItemImage

//        categoryFragment?.let {categoryFragmentSafe->
//            supportFragmentManager.beginTransaction().hide(categoryFragmentSafe).commit()
//            println("HomeActivity:: categoryFragmentSafe")
//        } ?: run {
//
//        }


//        holder.mBinding?.itemProductGridView?.homeItemImage.let { safeImageView ->
//            Glide.with(mContext)
//                .load(eachListData.thumbNail)
////            .placeholder(R.drawable.placeholder)
//                .into(safeImageView!!)
//        }


        holder.mBinding?.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemHorizontalScrollerProductViewBinding? = DataBindingUtil.bind(itemView)
    }

//    @BindingAdapter("bind:imageUrl")
//    fun loadImage(view: ImageView, imageUrl: String?) {
//        Glide.with(view.context)
//            .load(imageUrl)
////            .placeholder(R.drawable.placeholder)
//            .into(view)
//    }
}