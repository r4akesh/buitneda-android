package com.webkul.mobikul.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.webkul.mobikul.models.homepage.HomePageDataModel
import com.webkul.mobikul.network.ServiceRepository

class ViewModel(application: Application) : AndroidViewModel(application) {
    private val webServiceRepository: ServiceRepository = ServiceRepository(application)
    private lateinit var homeResponseModel: MutableLiveData<HomePageDataModel>


   /* fun getHomeData(pageNumber:Int): MutableLiveData<HomePageDataModel> {
        homeResponseModel = webServiceRepository.getHomePageData(pageNumber)
        return homeResponseModel
    }*/


}