package com.webkul.mobikulmp.activities

import android.content.DialogInterface
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.helpers.AlertDialogHelper
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikul.helpers.NetworkHelper
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.adapters.SellerListActivityRvAdapter
import com.webkul.mobikulmp.databinding.ActivitySellerListBinding
import com.webkul.mobikulmp.handlers.SellerListActivityHandler
import com.webkul.mobikulmp.models.SellerListResponseData
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class
SellerListActivity : BaseActivity() {

    lateinit var mContentViewBinding: ActivitySellerListBinding
    var mStoreName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_seller_list)
        startInitialization()
    }

    private fun startInitialization() {
        initSupportActionBar()
        callApi()
        checkAndLoadLocalData()
    }


    private fun initSupportActionBar() {
        supportActionBar?.title = getString(R.string.all_sellers)
    }

    fun callApi() {
        mContentViewBinding.loading = true
        mHashIdentifier = Utils.getMd5String("getSellerList" + AppSharedPref.getStoreId(this) + mStoreName)
        MpApiConnection.getSellerList(
                this,
                mDataBaseHandler.getETagFromDatabase(mHashIdentifier),
                mStoreName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<SellerListResponseData>(this, true) {
                    override fun onNext(t: SellerListResponseData) {
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
            onSuccessfulResponse(mGson.fromJson(response, SellerListResponseData::class.java))
        }
    }

    private fun onSuccessfulResponse(response: SellerListResponseData?) {
        mContentViewBinding.data = response
        mContentViewBinding.handler = SellerListActivityHandler(this@SellerListActivity)
        if (response?.sellerList!!.isNotEmpty()) {
            mContentViewBinding.sellerListRv.adapter = SellerListActivityRvAdapter(this@SellerListActivity, response!!.sellerList)
        } else {
            Snackbar.make(mContentViewBinding.mainContent, getString(R.string.no_seller_available), Snackbar.LENGTH_INDEFINITE)
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
                    finish()
                }
                , ""
                , null)
    }

    private fun onErrorResponse(error: Throwable) {
        val response = mDataBaseHandler.getResponseFromDatabase(mHashIdentifier)
        if ((!NetworkHelper.isNetworkAvailable(this) || (error is HttpException && error.code() == 304))) {
            onSuccessfulResponse(mGson.fromJson(response, SellerListResponseData::class.java))
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
                        //  mPageNumber--
                        callApi()
                    }
                    , getString(com.webkul.mobikul.R.string.close)
                    , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            })
        }
    }
}
