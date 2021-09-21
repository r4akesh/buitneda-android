package com.webkul.mobikulmp.activities

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.helpers.AlertDialogHelper
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikul.helpers.NetworkHelper
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.adapters.CustomerListAdapter
import com.webkul.mobikulmp.databinding.ActivityCustomerListBinding
import com.webkul.mobikulmp.fragments.CustomerListFilterFragment
import com.webkul.mobikulmp.handlers.CustomerListFilterHandler
import com.webkul.mobikulmp.models.CustomerListResponse
import com.webkul.mobikulmp.network.MpApiConnection

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class CustomerListActivity : BaseActivity(), CustomerListFilterFragment.OnDetachInterface {

    lateinit var mContentViewBinding: ActivityCustomerListBinding
    var name = ""
    var contact = ""
    var gender: Int? = null
    var email = ""
    var address = ""
    var mPageNumber = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_customer_list)
        startInitialization()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.clear()
        menuInflater.inflate(R.menu.filter_menu, menu)
        menu.findItem(R.id.menu_item_filter).isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.menu_item_filter -> {
                mContentViewBinding.handler?.onClickFilterCustomerList()
            }
        }
        return true
    }

    private fun startInitialization() {
        initSupportActionBar()
        callApi()
        checkAndLoadLocalData()
    }

    private fun initSupportActionBar() {
        supportActionBar?.title = getString(R.string.customer_list)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    private fun callApi() {
        mHashIdentifier = Utils.getMd5String("getCustomerList" + AppSharedPref.getStoreId(this) + AppSharedPref.getCustomerToken(this))
        mContentViewBinding.loading = true
        mContentViewBinding.emptyView.visibility = View.INVISIBLE
        MpApiConnection.getCustomerList(
                this,
                name,
                contact,
                gender,
                email,
                address,
                mDataBaseHandler.getETagFromDatabase(mHashIdentifier),
                mPageNumber++
        ).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<CustomerListResponse>(this, true) {
                    override fun onNext(t: CustomerListResponse) {
                        super.onNext(t)
                        mContentViewBinding.loading = false
                        if (t.success) {
                            onSuccessfulResponse(t)
                        } else {
                            onFailureResponse(t)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mContentViewBinding.loading = false
                        onErrorResponse(e)
                    }
                })
    }

    private fun checkAndLoadLocalData() {
        val response = mDataBaseHandler.getResponseFromDatabase(mHashIdentifier)
        if (response.isNotBlank()) {
            onSuccessfulResponse(mGson.fromJson(response, CustomerListResponse::class.java))
        }
    }


    override fun onFailureResponse(response: Any) {
        AlertDialogHelper.showNewCustomDialog(
                this,
                getString(R.string.error),
                (response as BaseModel).message,
                false,
                getString(R.string.ok),
            { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                finish()
            }
                , ""
                , null)
    }

    private fun onErrorResponse(error: Throwable) {
        val response = mDataBaseHandler.getResponseFromDatabase(mHashIdentifier)
        if ((!NetworkHelper.isNetworkAvailable(this) || (error is HttpException && error.code() == 304))) {
            onSuccessfulResponse(mGson.fromJson(response, CustomerListResponse::class.java))
        } else {
            AlertDialogHelper.showNewCustomDialog(
                    this,
                    getString(R.string.oops),
                    NetworkHelper.getErrorMessage(this, error),
                    false,
                    getString(R.string.try_again),
                    DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        mPageNumber--
                        mContentViewBinding.loading = true
                        callApi()
                    }
                    , getString(com.webkul.mobikul.R.string.close)
                    , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            })
        }
    }

    private fun onSuccessfulResponse(customerListResponse: CustomerListResponse) {
        if (mPageNumber == 2) {
            mContentViewBinding.data = customerListResponse
            if (mContentViewBinding.data!!.customerList!!.isEmpty()) {
                mContentViewBinding.emptyView.visibility = View.VISIBLE
            } else {
                mContentViewBinding.handler = CustomerListFilterHandler(this)
                setupCustomerListRv()
            }
        } else {
            customerListResponse.customerList?.let { mContentViewBinding.data!!.customerList!!.addAll(it) }
            mContentViewBinding.customerListRv.adapter?.notifyItemRangeChanged(mContentViewBinding.data!!.customerList!!.size - customerListResponse.customerList!!.size, customerListResponse.customerList!!.size)
        }
    }

    private fun setupCustomerListRv() {
        mContentViewBinding.customerListRv.isNestedScrollingEnabled = false
        mContentViewBinding.customerListRv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mContentViewBinding.customerListRv.adapter = CustomerListAdapter(this@CustomerListActivity, mContentViewBinding.data!!.customerList!!)
        mContentViewBinding.loading = false

        mContentViewBinding.customerListRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val lastCompletelyVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (!mContentViewBinding.loading!! && mContentViewBinding.data!!.customerList!!.size < mContentViewBinding.data!!.totalCount
                        && lastCompletelyVisibleItemPosition > mContentViewBinding.data!!.customerList!!.size - 3) {
                    callApi()
                }
            }
        })
    }

    override fun onFragmentDetached() {
        Handler().postDelayed({
            callApi()
        }, 200)
    }

}
