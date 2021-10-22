package com.webkul.mobikul.broadcast_receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.webkul.mobikul.interfaces.OnBadgeUpdateListener

class UpdateCartBadgeReceiver(val listener: OnBadgeUpdateListener?) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent){
        when (intent.action) {
            "UPDATE_CART" -> {
                listener!!.onUpdateBadgeOfCart()
            }

        }

    }
}