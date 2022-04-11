package com.webkul.mobikul.broadcast_receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.webkul.mobikul.interfaces.OnMenuSelectListener

open class BottomMenuReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null && intent.hasExtra("menuIndex")) {
            val menuIndex = intent.getIntExtra("menuIndex", 0)
            val menuName = intent.getStringExtra("menuName")!!
            onMenuSelectListener?.onMenuSelected(menuName, menuIndex)
        }
    }
    companion object {
        var onMenuSelectListener: OnMenuSelectListener? = null
    }
}