package com.webkul.mobikul.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.webkul.mobikul.R
import com.webkul.mobikul.adapters.AllServiceCategoryAdapter
import com.webkul.mobikul.databinding.ActivitySerivceAllBinding
import com.webkul.mobikul.models.service.ServiceBannerListModel
import com.webkul.mobikul.network.Status
import com.webkul.mobikul.view_model.CommonViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ServiceAllActivity : AppCompatActivity() {
    lateinit var mContentBinding: ActivitySerivceAllBinding
    private lateinit var commonViewModel: CommonViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentBinding = DataBindingUtil.setContentView(this, R.layout.activity_serivce_all)
        init()
    }

    private fun init() {
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel::class.java)
        mContentBinding.serviceToolbar.serviceBackBtn.setOnClickListener{
            finish()
        }
        makeRequestGetAllService()
    }

    private fun makeRequestGetAllService() {
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
                                setUpAllCategory(comment.services)
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

    private fun setUpAllCategory(list: ArrayList<ServiceBannerListModel>) {
        mContentBinding.allServiceList.layoutManager = GridLayoutManager(this, 2)
        val allServiceCategoryAdapter = AllServiceCategoryAdapter(this, list)
        mContentBinding.allServiceList.adapter = allServiceCategoryAdapter
    }


}