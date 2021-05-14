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

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.ItemNotificationListBinding
import com.webkul.mobikul.handlers.NotificationActivityHandler
import com.webkul.mobikul.interfaces.OnNotificationListener
import com.webkul.mobikul.models.extra.NotificationList

class NotificationListRvAdapter(private val mContext: Context,
                                private val mListData: ArrayList<NotificationList>,
                                private val onNotificationListener: OnNotificationListener

                                ) : RecyclerView.Adapter<NotificationListRvAdapter.ViewHolder>(),
    OnNotificationListener {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_notification_list, p0, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachListData = mListData[position]
        holder.mBinding?.data = eachListData
        holder.itemView.setOnClickListener { onNotificationListener.onNotificationClick(eachListData) }
        holder.mBinding?.handler = NotificationActivityHandler(this)
        holder.mBinding?.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemNotificationListBinding? = DataBindingUtil.bind(itemView)
    }

    override fun onNotificationClick(notificationModel: NotificationList) {
        onNotificationListener.onNotificationClick(notificationModel)
    }
}