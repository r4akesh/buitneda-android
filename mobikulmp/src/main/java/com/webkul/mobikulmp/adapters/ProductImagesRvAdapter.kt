package com.webkul.mobikulmp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.SellerAddProductActivity
import com.webkul.mobikulmp.databinding.ItemAddProductImageBinding
import com.webkul.mobikulmp.handlers.SellerAddProductActivityHandler
import com.webkul.mobikulmp.models.seller.MediaGallery

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.fragments
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 16/6/19
 */
class ProductImagesRvAdapter(private val mContext: Context, private var mediaGallery: ArrayList<MediaGallery>) : RecyclerView.Adapter<ProductImagesRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_add_product_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mediaGallery[position]
        if (data.removed == 1) {
            holder.itemView.visibility = View.GONE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
        } else {
            holder.itemView.visibility = View.VISIBLE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            data.adapterPosition = holder.adapterPosition
            holder.mBinding!!.data = data
            holder.mBinding.handler = SellerAddProductActivityHandler(mContext as SellerAddProductActivity)
            holder.mBinding.executePendingBindings()
        }
    }

    override fun getItemCount(): Int {
        return mediaGallery.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemAddProductImageBinding? = DataBindingUtil.bind(itemView)
    }

    fun removeImageItem(data: MediaGallery) {
        mediaGallery.remove(data)
        notifyDataSetChanged()
    }

    fun addImages(mediaGallery: ArrayList<MediaGallery>) {
        this.mediaGallery = mediaGallery
        notifyDataSetChanged()
    }

}
