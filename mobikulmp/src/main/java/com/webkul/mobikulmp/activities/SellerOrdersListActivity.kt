package com.webkul.mobikulmp.activities


import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.adapters.SellerOrderListRvAdapter
import com.webkul.mobikulmp.databinding.ActivitySellerOrdersListBinding
import com.webkul.mobikulmp.fragments.SellerOrdersFilterFragment
import com.webkul.mobikulmp.handlers.SellerOrdersListActivityHandler
import com.webkul.mobikulmp.models.seller.OrderStatus
import com.webkul.mobikulmp.models.seller.SellerOrderListData
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.fragments
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 16/6/19
 */

class SellerOrdersListActivity : BaseActivity(), SellerOrdersFilterFragment.OnDetachInterface {
    lateinit var mContentViewBinding: ActivitySellerOrdersListBinding
    var mPageNumber = 1
    var mIncrementId = ""
    var mDateFrom = ""
    var mDateTo = ""
    var mStatusPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_seller_orders_list)
        mContentViewBinding.progressBar = true
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
                mContentViewBinding.handler?.showOrdersFilterDialog()
            }
        }
        return true
    }

    private fun startInitialization() {
        initSupportActionBar()
        callApi()
        initSwipeRefresh()
    }

    private fun initSwipeRefresh() {
        mContentViewBinding.swipeRefreshLayout.setDistanceToTriggerSync(300)
        mContentViewBinding.swipeRefreshLayout.setOnRefreshListener {
            if (NetworkHelper.isNetworkAvailable(this)) {
                mPageNumber=1
                callApi()
            } else {
                mContentViewBinding.swipeRefreshLayout.isRefreshing = false
                ToastHelper.showToast(this@SellerOrdersListActivity, getString(com.webkul.mobikul.R.string.you_are_offline))
            }
        }
    }

    private fun initSupportActionBar() {
        supportActionBar?.title = getString(R.string.seller_orders)
    }

    fun callApi() {
        mContentViewBinding.swipeRefreshLayout.isRefreshing = true
        mContentViewBinding.loading=true
        mHashIdentifier = Utils.getMd5String("getSellerOrdersListData" + AppSharedPref.getStoreId(this) + AppSharedPref.getCustomerToken(this) + mPageNumber + mStatusPosition + mDateTo + mDateFrom + mIncrementId)
        MpApiConnection.getSellerOrdersListData(
                this,
                mDataBaseHandler.getETagFromDatabase(mHashIdentifier),
                mPageNumber++,
                if (mContentViewBinding.data == null) "" else mContentViewBinding.data!!.orderStatus[mStatusPosition].status,
                mDateTo,
                mDateFrom,
                mIncrementId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<SellerOrderListData>(this, true) {
                    override fun onNext(t: SellerOrderListData) {
                        super.onNext(t)
                        mContentViewBinding.progressBar = false
                        mContentViewBinding.swipeRefreshLayout.isRefreshing = false
                        mContentViewBinding.loading=false
                        if (t.success) {
                            onSuccessfulResponse(t)
                        } else {
                            onFailureResponse(t)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mContentViewBinding.progressBar = false
                        mContentViewBinding.swipeRefreshLayout.isRefreshing = false
                        mContentViewBinding.loading=false
                        onErrorResponse(e)
                    }
                })
    }

    private fun onSuccessfulResponse(sellerOrderListData: SellerOrderListData) {
        if (mPageNumber == 2) {
            mContentViewBinding.data = sellerOrderListData
            if (sellerOrderListData.orderList.size > 0) {
                mContentViewBinding.handler = SellerOrdersListActivityHandler(this)
            }
            addItemAllInOrderStatus()
            setupOrdersRv()
        } else {
            mContentViewBinding.data!!.orderList.addAll(sellerOrderListData.orderList)
            mContentViewBinding.ordersRv.adapter!!.notifyDataSetChanged()
        }
    }

    private fun addItemAllInOrderStatus() {
        val allOrderStatus = OrderStatus()
        allOrderStatus.label = getString(R.string.all)
        allOrderStatus.status = ""
        mContentViewBinding.data!!.orderStatus.add(0, allOrderStatus)
    }

    private fun setupOrdersRv() {
        mContentViewBinding.ordersRv.adapter = SellerOrderListRvAdapter(this, mContentViewBinding.data!!.orderList)
        mContentViewBinding.ordersRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val lastCompletelyVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (!mContentViewBinding.swipeRefreshLayout.isRefreshing?:false && mContentViewBinding.data!!.orderList.size < mContentViewBinding.data!!.totalCount
                        && lastCompletelyVisibleItemPosition > mContentViewBinding.data!!.orderList.size - 4) {
                    callApi()
                }
            }
        })
    }

    override fun onFailureResponse(response: Any) {
        AlertDialogHelper.showNewCustomDialog(
                this,
                getString(R.string.error),
                (response as BaseModel).message,
                false,
                getString(R.string.ok),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    finish()
                }
                , ""
                , null)
    }

    private fun onErrorResponse(error: Throwable) {
        val response = mDataBaseHandler.getResponseFromDatabase(mHashIdentifier)
        if ((!NetworkHelper.isNetworkAvailable(this) || (error is HttpException && error.code() == 304))) {
            onSuccessfulResponse(mGson.fromJson(response, SellerOrderListData::class.java))
        } else {
            AlertDialogHelper.showNewCustomDialog(
                    this,
                    getString(R.string.oops),
                    NetworkHelper.getErrorMessage(this, error),
                    false,
                    getString(R.string.try_again),
                    DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        mContentViewBinding.swipeRefreshLayout.isRefreshing = true
                        mContentViewBinding.loading=true
                        mPageNumber--
                        mContentViewBinding.progressBar = true
                        callApi()
                    }
                    , getString(com.webkul.mobikul.R.string.close)
                    , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            })
        }
    }

    override fun onFragmentDetached() {
        Handler().postDelayed({
            callApi()
        }, 200)
    }
}