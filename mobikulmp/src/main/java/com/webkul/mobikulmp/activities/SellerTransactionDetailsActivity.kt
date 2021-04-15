package com.webkul.mobikulmp.activities


import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.helpers.AlertDialogHelper
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikul.helpers.NetworkHelper
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.adapters.SellerTransactionOrderRvAdapter
import com.webkul.mobikulmp.databinding.ActivitySellerTransactionsDetailsBinding
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_TRANSACTION_ID
import com.webkul.mobikulmp.models.seller.ViewTransactionResponseData
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

class SellerTransactionDetailsActivity : BaseActivity() {

    lateinit var mContentViewBinding: ActivitySellerTransactionsDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_seller_transactions_details)
        startInitialization()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    private fun startInitialization() {
        initSupportActionBar()
        callApi()
        checkAndLoadLocalData()
    }

    private fun initSupportActionBar() {
        supportActionBar?.title = getString(R.string.transactions)
    }

    private fun callApi() {
        mContentViewBinding.loading = true
        mHashIdentifier = Utils.getMd5String("getSellerTransactionsDetailsData" + AppSharedPref.getStoreId(this) + AppSharedPref.getCustomerToken(this) + intent.getStringExtra(BUNDLE_KEY_TRANSACTION_ID))
        MpApiConnection.getSellerTransactionsDetailsData(
                this,
                mDataBaseHandler.getETagFromDatabase(mHashIdentifier),
                intent.getStringExtra(BUNDLE_KEY_TRANSACTION_ID)!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<ViewTransactionResponseData>(this, true) {
                    override fun onNext(t: ViewTransactionResponseData) {
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
            onSuccessfulResponse(mGson.fromJson(response, ViewTransactionResponseData::class.java))
        }
    }

    private fun onSuccessfulResponse(viewTransactionResponseData: ViewTransactionResponseData) {
        mContentViewBinding.data = viewTransactionResponseData
        supportActionBar?.title = getString(R.string.transaction_id_x, mContentViewBinding.data!!.transactionId)
        setupTransactionOrdersRv()
    }

    private fun setupTransactionOrdersRv() {
        mContentViewBinding.transactionOrdersRv.adapter = SellerTransactionOrderRvAdapter(this, mContentViewBinding.data!!.transactionOrderList)
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
            onSuccessfulResponse(mGson.fromJson(response, ViewTransactionResponseData::class.java))
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
}