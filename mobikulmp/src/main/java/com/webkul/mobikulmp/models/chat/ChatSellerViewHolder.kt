package com.webkul.mobikulmp.models.chat

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikulmp.databinding.ChatSellerDetailsBinding

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

class ChatSellerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var mBinding: ChatSellerDetailsBinding? = null

    init {
        mBinding = DataBindingUtil.bind(itemView)
    }
}
