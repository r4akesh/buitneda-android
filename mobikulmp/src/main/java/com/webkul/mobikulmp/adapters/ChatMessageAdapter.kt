package com.webkul.mobikulmp.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.helpers.MpAppSharedPref
import com.webkul.mobikulmp.models.chat.ChatMessage
import com.webkul.mobikulmp.models.chat.ChatMessageViewHolder

/**
 * Webkul Software.
 *
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

class ChatMessageAdapter(private val mContext: Context, private val messageList: List<ChatMessage>) : RecyclerView.Adapter<ChatMessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        return ChatMessageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.chat_message, parent, false))
    }

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) {
        val chatMessage = messageList[position]
        if (MpAppSharedPref.isAdmin(mContext)) {
            if (chatMessage.sender.equals(mContext.getString(R.string.admin), ignoreCase = true)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    holder.mBinding?.mainContainer?.layoutDirection = View.LAYOUT_DIRECTION_RTL
                }
                holder.mBinding?.message?.setBackgroundResource(R.drawable.bubble_right)
                holder.mBinding?.chatMessageIv?.visibility = View.GONE
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    holder.mBinding?.mainContainer?.layoutDirection = View.LAYOUT_DIRECTION_LTR
                }
                holder.mBinding?.message?.setBackgroundResource(R.drawable.bubble_left)
                holder.mBinding?.chatMessageIv?.visibility = View.VISIBLE
            }
        } else {
            if (chatMessage.sender.equals(mContext.getString(R.string.admin), ignoreCase = true)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    holder.mBinding?.mainContainer?.layoutDirection = View.LAYOUT_DIRECTION_LTR
                }
                holder.mBinding?.message?.setBackgroundResource(R.drawable.bubble_left)
                holder.mBinding?.chatMessageIv?.visibility = View.VISIBLE
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    holder.mBinding?.mainContainer?.layoutDirection = View.LAYOUT_DIRECTION_RTL
                }
                holder.mBinding?.message?.setBackgroundResource(R.drawable.bubble_right)
                holder.mBinding?.chatMessageIv?.visibility = View.GONE
            }

        }
        holder.mBinding?.data = chatMessage
        holder.mBinding?.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
}
