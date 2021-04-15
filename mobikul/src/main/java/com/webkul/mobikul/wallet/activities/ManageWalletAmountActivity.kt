package com.webkul.mobikul.wallet.activities

import android.content.DialogInterface
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.databinding.ActivityManageWalletAmountBinding
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikul.wallet.adapters.TransactionsRvAdapter
import com.webkul.mobikul.wallet.handlers.ManageWalletAmountActivityHandler
import com.webkul.mobikul.wallet.models.wallet.ManageWalletAmountResponseData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class ManageWalletAmountActivity : BaseActivity() {

    lateinit var mContentViewBinding: ActivityManageWalletAmountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_manage_wallet_amount)
        startInitialization()
    }

    private fun startInitialization() {
        initSupportActionBar()
        callApi()
    }

    private fun initSupportActionBar() {
        supportActionBar?.title = getString(R.string.activity_title_manage_wallet)
    }

    fun callApi() {
        mContentViewBinding.loading = true
        mHashIdentifier = Utils.getMd5String("getManageWalletData" + AppSharedPref.getStoreId(this) + AppSharedPref.getCustomerToken(this))
        ApiConnection.getManageWalletData(this, mDataBaseHandler.getETagFromDatabase(mHashIdentifier))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<ManageWalletAmountResponseData>(this, true) {
                    override fun onNext(manageWalletAmountResponseData: ManageWalletAmountResponseData) {
                        super.onNext(manageWalletAmountResponseData)
                        mContentViewBinding.loading = false
                        if (manageWalletAmountResponseData.success) {
                            onSuccessfulResponse(manageWalletAmountResponseData)
                        } else {
                            onFailureResponse(manageWalletAmountResponseData)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mContentViewBinding.loading = false
                        onErrorResponse(e)
                    }
                })
    }

    private fun onSuccessfulResponse(manageWalletAmountResponseData: ManageWalletAmountResponseData) {
        mContentViewBinding.data = manageWalletAmountResponseData
        if (mContentViewBinding.handler == null)
            mContentViewBinding.handler = ManageWalletAmountActivityHandler(this)

        setupTransactionsRv()
    }

    private fun setupTransactionsRv() {
        mContentViewBinding.transactionsRv.adapter = TransactionsRvAdapter(this, mContentViewBinding.data!!.transactionList)
    }

    override fun onFailureResponse(response: Any) {
        super.onFailureResponse(response)
        when ((response as BaseModel).otherError) {
            ConstantsHelper.CUSTOMER_NOT_EXIST -> {
                // Do nothing as it will be handled from the super.
            }
            else -> {
                AlertDialogHelper.showNewCustomDialog(
                        this,
                        getString(R.string.error),
                        response.message,
                        false,
                        getString(R.string.ok),
                        DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                            dialogInterface.dismiss()
                            finish()
                        }
                        , ""
                        , null)
            }
        }
    }

    private fun onErrorResponse(error: Throwable) {
        val response = mDataBaseHandler.getResponseFromDatabase(mHashIdentifier)
        if ((!NetworkHelper.isNetworkAvailable(this) || (error is HttpException && error.code() == 304)) && response.isNotBlank()) {
            onSuccessfulResponse(mGson.fromJson(response, ManageWalletAmountResponseData::class.java))
        } else {
            AlertDialogHelper.showNewCustomDialog(
                    this,
                    getString(R.string.error),
                    NetworkHelper.getErrorMessage(this, error),
                    false,
                    getString(R.string.try_again),
                    DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        callApi()
                    }
                    , getString(R.string.dismiss)
                    , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                finish()
            })
        }
    }
}