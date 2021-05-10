package com.webkul.mobikul.network

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.webkul.mobikul.helpers.AppSharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.homepage.HomePageDataModel

class ServiceRepository(private val application: Application) {
    private var apiInterface: ApiDetails = ApiClient.makeRetrofitService()


   /* fun getHomePageData(pageNumber:Int): MutableLiveData<HomePageDataModel> {
        val homePageResponseModel = MutableLiveData<HomePageDataModel>()
        GlobalScope.launch(Dispatchers.Default) {
            try {
                val request = apiInterface.getTopSellingProducts(AppSharedPref.getStoreId(application)
                        , AppSharedPref.getWebsiteId(application)
                        , AppSharedPref.getQuoteId(application)
                        , AppSharedPref.getCustomerToken(application)
                        , AppSharedPref.getCurrencyCode(application)
                        , Utils.screenWidth
                        , Utils.screenDensity
                        , pageNumber)
                val response = request.await()
                if (response.success) {
                    homePageResponseModel.postValue(response)
                } else {
                    homePageResponseModel.postValue(response)
                }
            } catch (exception: Exception) {
                homePageResponseModel.postValue(null)
            }
        }
        return homePageResponseModel
    }*/


}


