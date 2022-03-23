package com.webkul.mobikul.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.activities.ServiceProviderDetailActivity
import com.webkul.mobikul.activities.ServiceProviderListActivity
import com.webkul.mobikul.adapters.*
import com.webkul.mobikul.databinding.FragmentServiceProviderBinding
import com.webkul.mobikul.databinding.ImageCarouselLayoutBinding
import com.webkul.mobikul.databinding.TopBannerServiceProviderLayoutBinding
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.interfaces.OnServiceTypeSelectListener
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.models.service.*
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikul.network.Status
import com.webkul.mobikul.view_model.CommonViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ServiceProviderFragment : Fragment(), OnServiceTypeSelectListener {
    lateinit var mContentViewBinding: FragmentServiceProviderBinding
    private var commonViewModel: CommonViewModel? = null
    private var selectedArea = ""
    lateinit var imageCarouselLayoutBinding:ImageCarouselLayoutBinding
    lateinit var serviceBannerLayoutBinding:ImageCarouselLayoutBinding
    lateinit var topBannerServiceProviderBinding: TopBannerServiceProviderLayoutBinding
    lateinit var bannerImageList: ArrayList<BannerImage>
    lateinit var iconModel:Icon
    lateinit var mContext: Context
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mContentViewBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_service_provider, container, false)
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel::class.java)
        initSupportActionBar()
        mContentViewBinding.loading = true
        return mContentViewBinding.root
    }

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initSupportActionBar() {
       /* mContentViewBinding.toolbar.searchEditorBtn.setOnClickListener {
            (activity as HomeActivity).openMaterialSearchView()
        }*/
        topBannerServiceProviderBinding =  DataBindingUtil.inflate(
            layoutInflater,
            R.layout.top_banner_service_provider_layout,
            mContentViewBinding.carouselsLayout,
            false
        )
        serviceBannerLayoutBinding =  DataBindingUtil.inflate(
            layoutInflater,
            R.layout.image_carousel_layout,
            mContentViewBinding.carouselsLayout,
            false
        )

        imageCarouselLayoutBinding =  DataBindingUtil.inflate(
            layoutInflater,
            R.layout.image_carousel_layout,
            mContentViewBinding.carouselsLayout,
            false
        )

        mContentViewBinding.carouselsLayout.addView(topBannerServiceProviderBinding.root)
        mContentViewBinding.carouselsLayout.addView(imageCarouselLayoutBinding.root)
        mContentViewBinding.carouselsLayout.addView(serviceBannerLayoutBinding.root)


        mContentViewBinding.serviceChatBtn.setOnClickListener {
            makeRequestVisitor("whatsapp")
            Utils.openChat(mContext,iconModel.whatsapp_number)
        }
        
        mContentViewBinding.serviceCallBtn.setOnClickListener {
            makeRequestVisitor("phone")
            Utils.openDialer(mContext,iconModel.call_number)
        }

        makeRequestHomeInfo()
        mContentViewBinding.loading = false
        setUpAreaSpinner()
        makeRequestBottomIcon()
        mContentViewBinding.swipeRefreshLayout.setDistanceToTriggerSync(300)
        mContentViewBinding.swipeRefreshLayout.setOnRefreshListener {
            if (NetworkHelper.isNetworkAvailable(context!!)) {
                makeRequestHomeInfo()
            } else {
                mContentViewBinding.swipeRefreshLayout.isRefreshing = false
                ToastHelper.showToast(context!!, getString(R.string.you_are_offline))
            }
        }

        mContentViewBinding.toolbar.cityAreaSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedArea = if (p2 == 0) {
                    ""
                } else {
                    val areaModel = (p0?.getItemAtPosition(p2) as Area)
                    areaModel.business_address_city
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        mContentViewBinding.toolbar.searchEditorBtn.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                makeRequestSearchList(selectedArea,p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        mContentViewBinding.toolbar.searchEditorBtn.setOnItemClickListener { adapterView, view, i, l ->
            val selectedServiceId = (adapterView?.getItemAtPosition(i) as ServiceBannerListModel).service_id
            mContentViewBinding.toolbar.searchEditorBtn.setText((adapterView.getItemAtPosition(i) as ServiceBannerListModel).service_title)

            startActivity(
                Intent(mContext,
                    ServiceProviderListActivity::class.java
                ).putExtra("serviceId",selectedServiceId.toString().toInt())
            )
        }



    }

    private fun makeRequestHomeInfo() {
        ApiConnection.getServiceHomeTopBanner()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiCustomCallback<HomeTopBannerModel>(context!!, false) {
                override fun onNext(homeTopBannerResponse: HomeTopBannerModel) {
                    super.onNext(homeTopBannerResponse)
                    mContentViewBinding.swipeRefreshLayout.isRefreshing = false
                    mContentViewBinding.loading = false
                    if (homeTopBannerResponse.success) {
                        setUpTopBannerView(homeTopBannerResponse.top_banner)
                        getServiceList()
                    } else {
                        mContentViewBinding.loading = false
                        onFailureResponse(homeTopBannerResponse)
                    }
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    mContentViewBinding.swipeRefreshLayout.isRefreshing = false
                    onErrorResponse(e)
                }
            })
    }

    private fun makeRequestVisitor(clickOn:String) {
        commonViewModel!!.makeRequestHomeServiceVisitor("Homepage",clickOn)
        lifecycleScope.launch {
            commonViewModel!!.visitorHomeState.collect {
                when (it.status) {
                    Status.LOADING -> {
                        mContentViewBinding.loading = true
                    }
                    Status.SUCCESS -> {
                    }
                    else -> {
                        mContentViewBinding.loading = false
                        //  Toast.makeText(this@MainActivity, "${it.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun makeRequestBottomIcon() {
        commonViewModel!!.makeRequestBottomBanner()
        lifecycleScope.launch {
            commonViewModel!!.becomeServiceState.collect {
                when (it.status) {
                    Status.LOADING -> {
                        mContentViewBinding.loading = true

                    }
                    Status.SUCCESS -> {
                        mContentViewBinding.loading = false
                        iconModel = it.data!!.icon
                        mContentViewBinding.banner = it.data!!.icon.banner_image
                    }
                    else -> {
                        mContentViewBinding.loading = false
                        //  Toast.makeText(this@MainActivity, "${it.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getServiceList() {
        ApiConnection.getHomeServiceList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiCustomCallback<HomeServiceModel>(context!!, false) {
                override fun onNext(homeTopBannerResponse: HomeServiceModel) {
                    super.onNext(homeTopBannerResponse)
                    mContentViewBinding.swipeRefreshLayout.isRefreshing = false
                    mContentViewBinding.loading = false
                    if (homeTopBannerResponse.success) {
                        setUpHomeServiceList(homeTopBannerResponse.service)
                        makeRequestServiceBanner()
                    } else {
                        mContentViewBinding.loading = false
                        onFailureResponse(homeTopBannerResponse)
                    }
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    mContentViewBinding.swipeRefreshLayout.isRefreshing = false
                    onErrorResponse(e)
                }
            })
    }

    private fun makeRequestServiceBanner() {
        ApiConnection.getHomeServiceBannerList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiCustomCallback<HomeServiceBannerModel>(mContext, false) {
                override fun onNext(homeServiceBannerResponse: HomeServiceBannerModel) {
                    super.onNext(homeServiceBannerResponse)
                    mContentViewBinding.swipeRefreshLayout.isRefreshing = false
                    mContentViewBinding.loading = false
                    if (homeServiceBannerResponse.success) {
                        if(homeServiceBannerResponse.banners!=null){
                            bannerImageList = homeServiceBannerResponse.banners as ArrayList
                            setUpHomeServiceBannerList()
                            makeRequestBottomIcon()
                        }
                    } else {
                        mContentViewBinding.loading = false
                        onFailureResponse(homeServiceBannerResponse)
                    }
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    mContentViewBinding.swipeRefreshLayout.isRefreshing = false
                    onErrorResponse(e)
                }
            })
    }

    private fun setUpTopBannerView(list: ArrayList<TopBannerModel>) {
        if (list.isNotEmpty()) {
            topBannerServiceProviderBinding.carouselBannerViewPager.adapter = ServiceProviderTopBannerAdapter(this, list, "")

        }
    }


    private fun setUpAreaSpinner(){
        commonViewModel!!.makeRequestGetAllArea()
        lifecycleScope.launch{
            commonViewModel!!.areaState.collect {
                when (it.status) {
                    Status.LOADING -> {
                        mContentViewBinding.loading = true
                    }
                    Status.SUCCESS -> {
                        val areas = Area()
                        areas.business_address_city = "Please select area"
                        if(it.data!!.area.isNotEmpty()){
                            it.data.area.add(0,areas)
                            val areaAdapter = AreaListAdapter(mContext,it.data.area)
                            areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            mContentViewBinding.toolbar.cityAreaSpinner.adapter = areaAdapter
                        }
                    }
                    else -> {
                        mContentViewBinding.loading = false
                          Toast.makeText(mContext, "${it.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun setUpHomeServiceList(list: ArrayList<HomeServiceListModel>) {
        if (list.isNotEmpty()) {
            imageCarouselLayoutBinding.carouselBannerViewPager.layoutManager = LinearLayoutManager(mContext)
            imageCarouselLayoutBinding.carouselBannerViewPager.adapter = HomeServiceListAdapter(mContext, this, list)

        }

    }

    private fun setUpHomeServiceBannerList() {
        if (bannerImageList.isNotEmpty()) {
            serviceBannerLayoutBinding.carouselBannerViewPager.layoutManager =
                LinearLayoutManager(mContext)
            serviceBannerLayoutBinding.carouselBannerViewPager.adapter =
                ServiceHomeBannerAdapter(mContext, this, bannerImageList, this)
        }
    }

    override fun onServiceCategorySelect(serviceId: Int) {
        startActivity(
            Intent(
                mContext,
                ServiceProviderListActivity::class.java
            ).putExtra("serviceId", serviceId)
        )
    }

    override fun onServiceBannerSelect(serviceBannerModel: BannerImage) {
        if (serviceBannerModel.is_service_provider != null) {
            startActivity(
                Intent(
                    mContext,
                    ServiceProviderDetailActivity::class.java
                ).putExtra("serviceId", serviceBannerModel.service_provider_id.toString().toInt())
            )
        } else {
            if (serviceBannerModel.service_id != null) {
                startActivity(
                    Intent(
                        mContext,
                        ServiceProviderListActivity::class.java
                    ).putExtra("serviceId", serviceBannerModel.service_id.toString().toInt())
                )
            }
        }
    }


    private fun makeRequestSearchList(area:String,serviceName:String){
        commonViewModel!!.makeRequestSearchService(area,serviceName)
        lifecycleScope.launch{
            commonViewModel!!.searchState.collect {
                when (it.status) {
                    Status.LOADING -> {

                    }
                    Status.SUCCESS -> {
                        it.data?.let { comment ->
                            val areaAdapter = SuggestionListAdapter(mContext,comment.service)
                            mContentViewBinding.toolbar.searchEditorBtn.setAdapter(areaAdapter)
                            mContentViewBinding.toolbar.searchEditorBtn.showDropDown()
                        }
                    }
                    else -> {

                        Toast.makeText(mContext, "${it.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }



    fun onFailureResponse(response: Any) {
        when ((response as BaseModel).otherError) {
            ConstantsHelper.CUSTOMER_NOT_EXIST -> {
                AlertDialogHelper.showNewCustomDialog(
                    activity as BaseActivity,
                    getString(R.string.error),
                    response.message,
                    false,
                    getString(R.string.ok),
                    { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        Utils.logoutAndGoToHome(context!!)
                    }, "", null
                )
            }
            else -> {
                ToastHelper.showToast(context!!, response.message)
            }
        }
    }


    private fun onErrorResponse(error: Throwable) {
        if ((!NetworkHelper.isNetworkAvailable(context!!) || (error is HttpException && error.code() == 304))) {
            // Do Nothing as the data is already loaded
        } else {
            AlertDialogHelper.showNewCustomDialog(
                activity as BaseActivity,
                getString(R.string.error),
                NetworkHelper.getErrorMessage(context, error),
                false,
                getString(R.string.try_again),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    mContentViewBinding.swipeRefreshLayout.isRefreshing = true
                    makeRequestHomeInfo()
                },
                getString(R.string.dismiss),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                })
        }
    }


}
