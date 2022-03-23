package com.webkul.mobikul.view_model


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.models.service.*
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ResponseState
import com.webkul.mobikul.network.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

open class CommonViewModel : ViewModel() {

    val homeTopBannerState =
        MutableStateFlow(ResponseState(Status.LOADING, HomeTopBannerModel(), ""))
    val homeServiceBannerState = MutableStateFlow(ResponseState(Status.LOADING, HomeServiceBannerModel(), ""))
    val homeServiceListState = MutableStateFlow(ResponseState(Status.LOADING, HomeServiceModel(), ""))
    val serviceListState = MutableStateFlow(ResponseState(Status.LOADING, ServiceListModel(), ""))
    val serviceInfoState = MutableStateFlow(ResponseState(Status.LOADING, ServiceInfoModel(), ""))
    val providerReviewListState = MutableStateFlow(ResponseState(Status.LOADING, ServiceReviewListModel(), ""))
    val submitReviewState = MutableStateFlow(ResponseState(Status.LOADING, BaseModel(), ""))
    val allServiceState = MutableStateFlow(ResponseState(Status.LOADING, AllServiceModel(), ""))
    val areaState = MutableStateFlow(ResponseState(Status.LOADING, AreaModel(), ""))
    val visitorState = MutableStateFlow(ResponseState(Status.LOADING, VisitorModel(), ""))
    val visitorHomeState = MutableStateFlow(ResponseState(Status.LOADING, VisitorModel(), ""))
    val searchState = MutableStateFlow(ResponseState(Status.LOADING, SearchServiceModel(), ""))
    val becomeServiceState = MutableStateFlow(ResponseState(Status.LOADING, BecomeServiceModel(), ""))



   /* fun getHomeTopBanner() {
        homeTopBannerState.value = ResponseState.loading()
        viewModelScope.launch {
            ApiConnection.getServiceHomeTopBanner().catch {
                homeTopBannerState.value = ResponseState.error(it.message.toString())
            }.collect {
                homeTopBannerState.value = ResponseState.success(it.data)
            }
        }
    }

    fun getHomeServiceBanner() {
        homeServiceBannerState.value = ResponseState.loading()
        viewModelScope.launch {
            ApiConnection.getHomeServiceBannerList().catch {
                homeServiceBannerState.value = ResponseState.error(it.message.toString())
            }.collect {
                homeServiceBannerState.value = ResponseState.success(it.data)
            }
        }
    }

     fun getHomeServiceList() {
        homeServiceListState.value = ResponseState.loading()
        viewModelScope.launch {
            ApiConnection.getHomeServiceList().catch {
                homeServiceListState.value = ResponseState.error(it.message.toString())
            }.collect {
                homeServiceListState.value = ResponseState.success(it.data)
            }
        }
    }*/

    fun getServiceListApi(serviceId: Int,sortByRating:String) {
        serviceListState.value = ResponseState.loading()
        viewModelScope.launch {
            ApiConnection.getServiceList(serviceId,sortByRating).catch {
                serviceListState.value = ResponseState.error(it.message.toString())
            }.collect {
                serviceListState.value = ResponseState.success(it.data)
            }
        }
    }

    fun getServiceProviderInfo(providerId: Int) {
        serviceInfoState.value = ResponseState.loading()
        viewModelScope.launch {
            ApiConnection.getServiceProviderInfo(providerId).catch {
                serviceInfoState.value = ResponseState.error(it.message.toString())
            }.collect {
                serviceInfoState.value = ResponseState.success(it.data)
            }
        }
    }

    fun getProviderReviewList(providerId: Int) {
        providerReviewListState.value = ResponseState.loading()
        viewModelScope.launch {
            ApiConnection.getProviderReviewList(providerId).catch {
                providerReviewListState.value = ResponseState.error(it.message.toString())
            }.collect {
                providerReviewListState.value = ResponseState.success(it.data)
            }
        }
    }

    fun makeRequestSubmitReview(
        providerId: String,
        rating: String,
        name: String,
        description: String
    ) {
        submitReviewState.value = ResponseState.loading()
        viewModelScope.launch {
            ApiConnection.makeRequestSubmitReview(providerId, rating, name, description).catch {
                submitReviewState.value = ResponseState.error(it.message.toString())
            }.collect {
                submitReviewState.value = ResponseState.success(it.data)
            }
        }
    }

    fun makeRequestGetAllService(){
        allServiceState.value = ResponseState.loading()
        viewModelScope.launch {
            ApiConnection.makeRequestGetAllService().catch {
                allServiceState.value = ResponseState.error(it.message.toString())
            }.collect {
                allServiceState.value = ResponseState.success(it.data)
            }
        }
    }

    fun makeRequestGetAllArea(){
        areaState.value = ResponseState.loading()
        viewModelScope.launch {
            ApiConnection.makeRequestGetArea().catch {
                areaState.value = ResponseState.error(it.message.toString())
            }.collect {
                areaState.value = ResponseState.success(it.data)
            }
        }
    }

    fun makeRequestVisitor(page:String,clickOn:String,providerId: Int){
        visitorState.value = ResponseState.loading()
        viewModelScope.launch {
            ApiConnection.makeRequestVisitor(page,clickOn,providerId).catch {
                visitorState.value = ResponseState.error(it.message.toString())
            }.collect {
                visitorState.value = ResponseState.success(it.data)
            }
        }
    }

    fun makeRequestHomeServiceVisitor(page:String,clickOn:String){
        visitorHomeState.value = ResponseState.loading()
        viewModelScope.launch {
            ApiConnection.makeRequestHomeVisitor(page,clickOn).catch {
                visitorHomeState.value = ResponseState.error(it.message.toString())
            }.collect {
                visitorHomeState.value = ResponseState.success(it.data)
            }
        }
    }

    fun makeRequestSearchService(area:String,service:String){
        searchState.value = ResponseState.loading()
        viewModelScope.launch {
            ApiConnection.makeRequestSearchService(area,service).catch {
                searchState.value = ResponseState.error(it.message.toString())
            }.collect {
                searchState.value = ResponseState.success(it.data)
            }
        }
    }

    fun makeRequestBottomBanner(){
        becomeServiceState.value = ResponseState.loading()
        viewModelScope.launch {
            ApiConnection.makeRequestBecomeServiceProvider().catch {
                becomeServiceState.value = ResponseState.error(it.message.toString())
            }.collect {
                becomeServiceState.value = ResponseState.success(it.data)
            }
        }
    }

}


