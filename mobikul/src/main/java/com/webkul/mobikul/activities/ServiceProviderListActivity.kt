package com.webkul.mobikul.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.webkul.mobikul.R
import com.webkul.mobikul.adapters.ServiceListAdapter
import com.webkul.mobikul.databinding.ActivityServiceProviderListBinding
import com.webkul.mobikul.fragments.FilterServiceBottomSheetFragment
import com.webkul.mobikul.fragments.SortServiceBottomSheetFragment
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikul.helpers.ToastHelper
import com.webkul.mobikul.interfaces.OnFilterItemSelectListener
import com.webkul.mobikul.interfaces.OnSelectSortBy
import com.webkul.mobikul.models.service.ServiceBannerListModel
import com.webkul.mobikul.models.service.ServiceProviderModel
import com.webkul.mobikul.network.Status
import com.webkul.mobikul.view_model.CommonViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class ServiceProviderListActivity : BaseActivity(), OnSelectSortBy, OnFilterItemSelectListener {
    private lateinit var mContentBinding: ActivityServiceProviderListBinding
    private lateinit var commonViewModel: CommonViewModel
    private lateinit var serviceListAdapter: ServiceListAdapter
    private var serviceId: Int = 0
    var intentFilter: IntentFilter? = null
    private lateinit var filterList: ArrayList<ServiceBannerListModel>
    private var list: MutableList<ServiceProviderModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentBinding = ActivityServiceProviderListBinding.inflate(layoutInflater)
        setContentView(mContentBinding.root)
        initialization()
    }

    //initialization of the content
    private fun initialization() {
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel::class.java)
        intentFilter = IntentFilter("com.journaldev.broadcastreceiver.SOME_ACTION")
        if (intent != null && intent.hasExtra("serviceId")) {
            serviceId = intent.getStringExtra("serviceId")!!.toInt()
        }
        commonViewModel.getServiceListApi(serviceId, "")
        mContentBinding.toolbar.backBtn.setOnClickListener {
            finish()
        }

        mContentBinding.sortTv.setOnClickListener {
            val sortServiceBottomSheetFragment = SortServiceBottomSheetFragment(this)
            sortServiceBottomSheetFragment.show(
                supportFragmentManager,
                "sortServiceBottomSheetFragment"
            )
            sortServiceBottomSheetFragment.isCancelable = true
        }

        mContentBinding.filterTv.setOnClickListener {
            if (::filterList.isInitialized) {
                val filterServiceBottomSheetFragment =
                    FilterServiceBottomSheetFragment(this, filterList)
                filterServiceBottomSheetFragment.show(
                    supportFragmentManager,
                    "filterServiceBottomSheetFragment"
                )
                filterServiceBottomSheetFragment.isCancelable = true
            }

        }

        makeRequestProviderList()
        mContentBinding.bottomNavView.menu.getItem(1).isChecked = true
        mContentBinding.bottomNavView.setOnNavigationItemSelectedListener { menuItem ->
            updateCartBadge()
            updateCartCount(AppSharedPref.getCartCount(this@ServiceProviderListActivity))
            when (menuItem.itemId) {
                R.id.bottom_category -> {
                    val intent = Intent()
                    intent.action = "bottom.menu.action"
                    intent.putExtra("menuName", "category")
                    intent.putExtra("menuIndex", 0)
                    sendBroadcast(intent)
                    finish()
                }
                R.id.bottom_auction -> {
                    val intent = Intent()
                    intent.action = "bottom.menu.action"
                    intent.putExtra("menuName", "auction")
                    intent.putExtra("menuIndex", 1)
                    sendBroadcast(intent)
                    finish()
                }
                R.id.bottom_home -> {
                    val intent = Intent()
                    intent.action = "bottom.menu.action"
                    intent.putExtra("menuName", "Home")
                    intent.putExtra("menuIndex", 2)
                    sendBroadcast(intent)
                    finish()
                }
                R.id.bottom_cart -> {
                    val intent = Intent()
                    intent.action = "bottom.menu.action"
                    intent.putExtra("menuName", "cart")
                    intent.putExtra("menuIndex", 3)
                    sendBroadcast(intent)
                    finish()
                }
                R.id.bottom_profile -> {
                    val intent = Intent()
                    intent.action = "bottom.menu.action"
                    intent.putExtra("menuName", "profile")
                    intent.putExtra("menuIndex", 4)
                    sendBroadcast(intent)
                    finish()
                }
            }

            true
        }
        updateCartBadge()
        inflateBadge()


        mContentBinding.toolbar.searchEditorBtn.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            @SuppressLint("DefaultLocale")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val query = p0.toString().lowercase(Locale.getDefault()).trim()
                val filteredList: ArrayList<ServiceProviderModel> = ArrayList()
                for (i in list.indices) {
                    var text = ""
                    if (list[i].company_name != null) {
                        if (list[i].company_name!!.lowercase(Locale.getDefault()) != "") {
                            text = list[i].company_name!!.lowercase(Locale.getDefault())
                        }
                        if (text.contains(query)) {
                            filteredList.add(list[i])
                        }
                    }
                }
                setUpServiceProviderList(filteredList)
            }
            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    private fun inflateBadge() {
        val bottomNavigationMenuView =
            mContentBinding.bottomNavView.getChildAt(0) as BottomNavigationMenuView
        val v = bottomNavigationMenuView.getChildAt(3)
        val itemView = v as BottomNavigationItemView
        mBadge = LayoutInflater.from(this).inflate(R.layout.notification_badge, itemView, true)
    }

    private fun makeRequestProviderList() {
        lifecycleScope.launch {
            commonViewModel.serviceListState.collect {
                when (it.status) {
                    Status.LOADING -> {
                        mContentBinding.loading = true
                    }

                    Status.SUCCESS -> {
                        mContentBinding.loading = false
                        it.data?.let { comment ->
                            list = it.data.service_provider
                            if(list.isEmpty()){
                                ToastHelper.showToast(this@ServiceProviderListActivity,"There are no service providers in this service", Toast.LENGTH_LONG)

                            }else{
                                setUpServiceProviderList(list as ArrayList)
                                filterItemList()
                            }

                            // setUpTopBannerView(comment.top_banner)
                        }
                    }
                    else -> {
                        mContentBinding.loading = false
                        //  Toast.makeText(this@MainActivity, "${it.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun filterItemList() {
        commonViewModel.makeRequestGetAllService()
        lifecycleScope.launch {
            commonViewModel.allServiceState.collect {
                when (it.status) {
                    Status.LOADING -> {
                        mContentBinding.loading = true
                    }
                    Status.SUCCESS -> {
                        mContentBinding.loading = false
                        it.data?.let { comment ->
                            if (comment.services.isNotEmpty()) {
                                filterList = comment.services
                            }
                        }
                    }
                    else -> {
                        mContentBinding.loading = false
                        //  Toast.makeText(this@MainActivity, "${it.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setUpServiceProviderList(serviceList: ArrayList<ServiceProviderModel>) {
        mContentBinding.productsRv.layoutManager = LinearLayoutManager(this)
        serviceListAdapter = ServiceListAdapter(this, serviceList)
        mContentBinding.productsRv.adapter = serviceListAdapter
    }

    override fun onResume() {
        super.onResume()
        updateBadge()
    }

    override fun onSortBy(type: String) {
        if (type == "ascending") {
            commonViewModel.getServiceListApi(serviceId, "asc")
        } else {
            commonViewModel.getServiceListApi(serviceId, "desc")
        }
    }

    override fun onFilterItemSelect(serviceProviderId: String) {
        commonViewModel.getServiceListApi(serviceProviderId.toInt(), "")
    }
}