package com.webkul.mobikulmp.activities


import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.helpers.AlertDialogHelper
import com.webkul.mobikul.helpers.NetworkHelper
import com.webkul.mobikul.helpers.ToastHelper.Companion.showToast
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.adapters.SellerProductsListRvAdapter
import com.webkul.mobikulmp.databinding.ActivitySellerProductsBinding
import com.webkul.mobikulmp.fragments.SellerProductsFilterFragment
import com.webkul.mobikulmp.handlers.SellerProductsListActivityHandler
import com.webkul.mobikulmp.helpers.MpConstantsHelper.RC_EDIT_SELLER_PRODUCT
import com.webkul.mobikulmp.models.seller.SellerOrderFilterFragData
import com.webkul.mobikulmp.models.seller.SellerProductListResponseData
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

class SellerProductsListActivity : BaseActivity(), SellerProductsFilterFragment.OnDetachInterface {


    internal var mPageNumber = 1
    lateinit var mSellerOrderFilterFragData: SellerOrderFilterFragData
    lateinit var mSellerProductListResponseData: SellerProductListResponseData
    lateinit var mContentViewBinding: ActivitySellerProductsBinding
    var mProductName = ""
    var mDateFrom = ""
    var mDateTo = ""
    var mProductStatusPosition = 0
    var mProductStatus = ""
    var isFirstCall = true
    lateinit var mSellerProductListRvAdapter: SellerProductsListRvAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_seller_products)
        supportActionBar?.title = getString(R.string.seller_products)
        callApi()
        mSellerOrderFilterFragData = SellerOrderFilterFragData(this)
    }

    fun callApi() {
        mContentViewBinding.loading = true
        MpApiConnection.getSellerProductsListData(
                this@SellerProductsListActivity,
                mPageNumber++,
                mDateTo,
                mDateFrom,
                mProductName,
                mProductStatus)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<SellerProductListResponseData>(this@SellerProductsListActivity, true) {
                    override fun onNext(t: SellerProductListResponseData) {
                        super.onNext(t)
                        mContentViewBinding.loading = false
                        if (t.success) {
                            onResponseReceived(t)
                        } else {
                            onFailureResponse(t)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        onErrorResponse(e)
                    }
                })
    }

    private fun onResponseReceived(productsData: SellerProductListResponseData) {
        mContentViewBinding.loading = false
        if (isFirstCall) {
            isFirstCall = false
            mSellerProductListResponseData = productsData
            mContentViewBinding.data = mSellerProductListResponseData
            if (productsData.productList?.isEmpty() == false) {
                mContentViewBinding.handler = SellerProductsListActivityHandler(this)
            }

            mContentViewBinding.executePendingBindings()
            mSellerProductListRvAdapter = SellerProductsListRvAdapter(this, mSellerProductListResponseData.productList!!)
            mContentViewBinding.productsRv.adapter = mSellerProductListRvAdapter
            mContentViewBinding.productsRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val lastCompletelyVisibleItemPosition: Int = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val toastString = (lastCompletelyVisibleItemPosition + 1).toString() + " " + getString(R.string.of_toast_for_no_of_item) + " " + mSellerProductListResponseData.totalCount
                    showToast(this@SellerProductsListActivity, toastString, Toast.LENGTH_LONG)
                    if (!mContentViewBinding.loading!! && mContentViewBinding.data!!.productList!!.size < mContentViewBinding.data!!.totalCount
                            && lastCompletelyVisibleItemPosition > mContentViewBinding.data!!.productList!!.size - 4) {
                        callApi()
                    }
                }
            })
            mContentViewBinding.productsRv.itemAnimator!!.removeDuration = 500

            // gestureDetector = GestureDetectorCompat(this, RecyclerViewOnGestureListener())
        } else {
            mContentViewBinding.data!!.productList!!.addAll(productsData.productList!!)
            mContentViewBinding.productsRv.adapter!!.notifyDataSetChanged()
        }
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
                }
                , ""
                , null)
    }


    private fun onErrorResponse(error: Throwable) {
        mContentViewBinding.loading = false
        if ((!NetworkHelper.isNetworkAvailable(this) || (error is HttpException && error.code() == 304))) {

        } else {
            AlertDialogHelper.showNewCustomDialog(
                    this,
                    getString(R.string.oops),
                    NetworkHelper.getErrorMessage(this, error),
                    false,
                    getString(R.string.try_again),
                    DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        mContentViewBinding.loading = true
                        callApi()
                    }
                    , getString(com.webkul.mobikul.R.string.close)
                    , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            })
        }
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
                mContentViewBinding.handler?.viewProductsFilterDialog()
            }
        }
        return true
    }


    override fun onFragmentDetached() {
        Handler().postDelayed({
            callApi()
        }, 200)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == RC_EDIT_SELLER_PRODUCT) {
            mPageNumber = 1
            isFirstCall = true
            callApi()
        }
    }
}