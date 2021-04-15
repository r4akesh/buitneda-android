package com.webkul.mobikul.wallet.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.ItemSavedAccountsBinding
import com.webkul.mobikul.wallet.activities.AddAccountDetailsActivity
import com.webkul.mobikul.wallet.handlers.SavedAccountsRvHandler
import com.webkul.mobikul.wallet.models.wallet.SavedAccountsList

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
class SavedAccountsRvAdapter(private val mContext: AddAccountDetailsActivity, private val mListData: ArrayList<SavedAccountsList>) : RecyclerView.Adapter<SavedAccountsRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_saved_accounts, p0, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachListData = mListData.get(position)
        holder.mBinding?.data = eachListData
        holder.mBinding?.handler = SavedAccountsRvHandler(mContext)
        holder.mBinding?.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemSavedAccountsBinding? = androidx.databinding.DataBindingUtil.bind(itemView)
    }
}