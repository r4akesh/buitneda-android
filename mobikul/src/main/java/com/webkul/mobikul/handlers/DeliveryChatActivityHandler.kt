package com.webkul.mobikul.handlers


import com.google.firebase.database.ServerValue
import com.webkul.mobikul.activities.DeliveryChatActivity
import com.webkul.mobikul.helpers.AppSharedPref
import java.util.*


/**
 * Created by vedesh.kumar on 30/7/17. @Webkul Software Private limited
 */

class DeliveryChatActivityHandler(private val mContext: DeliveryChatActivity) {

    fun onClickSendButton(currentMsg: String?) {
        if (currentMsg != null && !currentMsg.trim { it <= ' ' }.isEmpty()) {
            val message_root = mContext.mDatabaseReference.push().key?.let { mContext.mDatabaseReference.child(it) }
            val map2 = HashMap<String, Any>()
            map2["msg"] = currentMsg.trim { it <= ' ' }
            map2["id"] = AppSharedPref.getCustomerId(mContext)
            map2["name"] = AppSharedPref.getCustomerName(mContext)
            map2["timestamp"] = ServerValue.TIMESTAMP
            message_root?.updateChildren(map2)
            mContext.mBinding.textMsgEt.setText("")
        }
    }
}
