package com.webkul.mobikul.wallet.activities

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.databinding.ActivityAddAccountDetailsBinding
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikul.wallet.adapters.SavedAccountsRvAdapter
import com.webkul.mobikul.wallet.handlers.AddAccountDetailsActivityHandler
import com.webkul.mobikul.wallet.models.wallet.AccountDetailsResponseData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class AddAccountDetailsActivity : BaseActivity() {

    lateinit var mContentViewBinding: ActivityAddAccountDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_account_details)
        startInitialization()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    private fun startInitialization() {
        initSupportActionBar()
        callApi()
    }

    private fun initSupportActionBar() {
        supportActionBar?.title = getString(R.string.activity_title_account_details)
    }

    fun callApi() {
        mContentViewBinding.loading = true
        mHashIdentifier = Utils.getMd5String("getWalletAccountDetails" + AppSharedPref.getStoreId(this) + AppSharedPref.getCustomerToken(this))
        ApiConnection.getWalletAccountDetails(this, mDataBaseHandler.getETagFromDatabase(mHashIdentifier))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<AccountDetailsResponseData>(this, true) {
                    override fun onNext(accountDetailsResponseData: AccountDetailsResponseData) {
                        super.onNext(accountDetailsResponseData)
                        mContentViewBinding.loading = false
                        if (accountDetailsResponseData.success) {
                            onSuccessfulResponse(accountDetailsResponseData)
                        } else {
                            onFailureResponse(accountDetailsResponseData)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mContentViewBinding.loading = false
                        onErrorResponse(e)
                    }
                })
    }

    private fun onSuccessfulResponse(accountDetailsResponseData: AccountDetailsResponseData) {
        mContentViewBinding.data = accountDetailsResponseData
        mContentViewBinding.handler = AddAccountDetailsActivityHandler(this)

        setupSavedAccountsRv()
    }

    private fun setupSavedAccountsRv() {
        mContentViewBinding.savedAccountsRv.adapter = SavedAccountsRvAdapter(this, mContentViewBinding.data!!.getSavedAccountList())
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
            onSuccessfulResponse(mGson.fromJson(response, AccountDetailsResponseData::class.java))
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
