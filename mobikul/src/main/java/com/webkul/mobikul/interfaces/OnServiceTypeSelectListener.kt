package com.webkul.mobikul.interfaces

import com.webkul.mobikul.models.service.BannerImage
import com.webkul.mobikul.models.service.ServiceBannerModel

interface OnServiceTypeSelectListener {
    fun onServiceCategorySelect(serviceId:Int)
    fun onServiceBannerSelect(serviceBannerModel: BannerImage)
}