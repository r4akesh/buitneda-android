package com.webkul.mobikulmp.activities


import android.content.DialogInterface
import android.content.pm.PackageManager
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
import com.webkul.mobikulmp.adapters.SellerTransactionsListRvAdapter
import com.webkul.mobikulmp.databinding.ActivitySellerTransactionsListBinding
import com.webkul.mobikulmp.fragments.SellerTransactionsFilterFragment
import com.webkul.mobikulmp.handlers.SellerTransactionsListActivityHandler
import com.webkul.mobikulmp.models.seller.SellerTransactionListResponseData
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

class SellerTransactionsListActivity : BaseActivity(), SellerTransactionsFilterFragment.OnDetachInterface {

    lateinit var mContentViewBinding: ActivitySellerTransactionsListBinding

    var mPageNumber = 1

    var mTransactionId = ""
    var mDateFrom = ""
    var mDateTo = ""
    private var mMenuItemFilter: MenuItem? = null
    private var mMenuItemCSV: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_seller_transactions_list)
        mContentViewBinding.handler = SellerTransactionsListActivityHandler(this)
        startInitialization()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.clear()
        menuInflater.inflate(R.menu.filter_menu, menu)
        mMenuItemFilter = menu.findItem(R.id.menu_item_filter)
        mMenuItemCSV = menu.findItem(R.id.menu_item_csv)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.menu_item_filter -> {
                mContentViewBinding.handler?.viewProductsFilterDialog()
            }
            R.id.menu_item_csv -> {
                mContentViewBinding.handler?.onClickDownloadCsv()
            }
        }
        return true
    }

    private fun startInitialization() {
        initSupportActionBar()
        callApi()
    }

    private fun initSupportActionBar() {
        supportActionBar?.title = getString(R.string.transactions)
    }

    fun callApi() {
        mContentViewBinding.loading = true
        mHashIdentifier = Utils.getMd5String("getSellerTransactionsListData" + AppSharedPref.getStoreId(this) + AppSharedPref.getCustomerToken(this) + mPageNumber + mDateTo + mDateFrom + mTransactionId)
        MpApiConnection.getSellerTransactionsListData(
                this,
                mDataBaseHandler.getETagFromDatabase(mHashIdentifier),
                mPageNumber++,
                mDateTo,
                mDateFrom,
                mTransactionId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<SellerTransactionListResponseData>(this, true) {
                    override fun onNext(t: SellerTransactionListResponseData) {
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

    private fun onSuccessfulResponse(sellerTransactionListResponseData: SellerTransactionListResponseData) {
        if (mPageNumber == 2) {
            mContentViewBinding.data = sellerTransactionListResponseData
            if (sellerTransactionListResponseData.sellerTransactionList.isNotEmpty()) {
                mMenuItemFilter?.isVisible = true
                mMenuItemCSV?.isVisible = true
            }
            setupTransactionsRv()
        } else {
            mContentViewBinding.data!!.sellerTransactionList.addAll(sellerTransactionListResponseData.sellerTransactionList)
            mContentViewBinding.transactionsRv.adapter!!.notifyDataSetChanged()
        }
    }

    private fun setupTransactionsRv() {
        mContentViewBinding.transactionsRv.adapter = SellerTransactionsListRvAdapter(this, mContentViewBinding.data!!.sellerTransactionList)
        mContentViewBinding.transactionsRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val lastCompletelyVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (!mContentViewBinding.loading!! && mContentViewBinding.data!!.sellerTransactionList.size < mContentViewBinding.data!!.totalCount
                        && lastCompletelyVisibleItemPosition > mContentViewBinding.data!!.sellerTransactionList.size - 4) {
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
            onSuccessfulResponse(mGson.fromJson(response, SellerTransactionListResponseData::class.java))
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
                        mPageNumber--
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


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var allPermissionsGranted = true
        for (eachGrantResult in grantResults) {
            if (eachGrantResult != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false
            }
        }
        if (allPermissionsGranted) {
            if (requestCode == ConstantsHelper.RC_WRITE_TO_EXTERNAL_STORAGE) {
                mContentViewBinding.handler?.onClickDownloadCsv()
            }
        }
    }

}