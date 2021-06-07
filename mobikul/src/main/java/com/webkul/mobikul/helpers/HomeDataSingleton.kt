package com.webkul.mobikul.helpers

import com.webkul.mobikul.models.homepage.HomePageDataModel

class HomeDataSingleton {
    var mHomePageDataModel: HomePageDataModel? = null
    companion object{
        private var homeDataSingleton:HomeDataSingleton? =null
        fun  getInstance():HomeDataSingleton{
            homeDataSingleton?.let {
                return it
            }
            homeDataSingleton = HomeDataSingleton()

            return homeDataSingleton!!
        }
    }
}