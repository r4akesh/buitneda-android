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

package com.webkul.mobikulmp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.ItemSellerOrderRefundsBinding
import com.webkul.mobikulmp.fragments.SellerRefundsFragment
import com.webkul.mobikulmp.handlers.SellerOrderRefundsRvHandler
import com.webkul.mobikulmp.models.seller.SellerCreditMemoList

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

class SellerOrderRefundsRvAdapter(private val mFragmentContext: SellerRefundsFragment, private val mListData: ArrayList<SellerCreditMemoList>) : RecyclerView.Adapter<SellerOrderRefundsRvAdapter.ViewHolder>() {
    var adapterPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mFragmentContext.context).inflate(R.layout.item_seller_order_refunds, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachListData = mListData[position]
        eachListData.adapterPosition = holder.adapterPosition
        holder.mBinding?.data = eachListData
        holder.mBinding?.handler = SellerOrderRefundsRvHandler(mFragmentContext)
        holder.mBinding?.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemSellerOrderRefundsBinding? = DataBindingUtil.bind(itemView)
    }
}