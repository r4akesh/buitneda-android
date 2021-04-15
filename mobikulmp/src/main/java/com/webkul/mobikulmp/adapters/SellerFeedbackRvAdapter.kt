package com.webkul.mobikulmp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.ItemSellerFeedbackBinding
import com.webkul.mobikulmp.models.seller.SellerReviewList

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.fragments
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 24/07/19
 */
class SellerFeedbackRvAdapter(private val mContext: Context, private val mSellerReviewDatas: List<SellerReviewList>) : RecyclerView.Adapter<SellerFeedbackRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.item_seller_feedback, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sellerReviewData = mSellerReviewDatas[position]
        holder.mBinding!!.data = sellerReviewData
    }

    override fun getItemCount(): Int {
        return mSellerReviewDatas.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemSellerFeedbackBinding? = DataBindingUtil.bind(itemView)
    }
}
