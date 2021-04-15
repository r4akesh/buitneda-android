package com.webkul.mobikulmp.models.chat

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikulmp.databinding.ChatMessageBinding

/**
 *
 * Webkul Software.
 *
 * @category Mobikul
 * @package com.webkul.mobikulmp.models
 * @author Webkul
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 20/5/19
 *
 */
class ChatMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val mBinding: ChatMessageBinding? = DataBindingUtil.bind(itemView)
}
