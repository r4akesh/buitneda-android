package com.webkul.mobikul.interfaces

import com.webkul.mobikul.models.extra.NotificationList

interface OnNotificationListener {
    fun onNotificationClick(notificationModel: NotificationList)
    fun onNotificationFragmentClose()
}