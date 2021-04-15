package com.webkul.mobikulmp.handlers

import android.content.Context
import android.content.Intent
import android.view.View

import com.webkul.mobikulmp.activities.ChatActivity

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

class ChatSellerHandler(private val mContext: Context) {

    fun onClickSeller(view: View, customerName: String, customerToken: String, token: String, profileImage: String) {
        val intent = Intent(mContext, ChatActivity::class.java)
        intent.putExtra("user_name", customerName)
        intent.putExtra("user_id", customerToken)
        intent.putExtra("token", token)
        mContext.startActivity(intent)
    }
}