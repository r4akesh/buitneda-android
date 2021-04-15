package com.webkul.mobikul.wallet.activities

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.databinding.ActivityTransferWalletAmountBinding
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikul.wallet.adapters.PayeeRvAdapter
import com.webkul.mobikul.wallet.handlers.TransferWalletAmountHandler
import com.webkul.mobikul.wallet.models.wallet.SendCodeResponseData
import com.webkul.mobikul.wallet.models.wallet.TransferWalletAmountResponseData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.util.*

class TransferWalletAmountActivity : BaseActivity() {

    lateinit var mContentViewBinding: ActivityTransferWalletAmountBinding
    var sendCodeResponseData: SendCodeResponseData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_transfer_wallet_amount)
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
        supportActionBar?.title = getString(R.string.activity_title_transfer_wallet)
    }

    fun callApi() {
        mContentViewBinding.loading = true
        mHashIdentifier = Utils.getMd5String("getWalletTransferData" + AppSharedPref.getStoreId(this) + AppSharedPref.getCustomerToken(this))
        ApiConnection.getWalletTransferData(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<TransferWalletAmountResponseData>(this, true) {
                    override fun onNext(transferWalletAmountResponseData: TransferWalletAmountResponseData) {
                        super.onNext(transferWalletAmountResponseData)
                        mContentViewBinding.loading = false
                        if (transferWalletAmountResponseData.success) {
                            onSuccessfulResponse(transferWalletAmountResponseData)
                        } else {
                            onFailureResponse(transferWalletAmountResponseData)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mContentViewBinding.loading = false
                        onErrorResponse(e)
                    }
                })
    }

    private fun onSuccessfulResponse(transferWalletAmountResponseData: TransferWalletAmountResponseData) {
        mContentViewBinding.data = transferWalletAmountResponseData
        mContentViewBinding.handler = TransferWalletAmountHandler(this)

        setupPayeeSp()
        setupPayeeRv()
    }

    private fun setupPayeeSp() {
        val customerSpinnerData = ArrayList<String>()
        val customerIdData = ArrayList<String>()
        customerSpinnerData.add(getString(R.string.please_select_customer))
        customerIdData.add("0")
        for (customerIterator in mContentViewBinding.data!!.payeeList.indices) {
            customerSpinnerData.add(mContentViewBinding.data!!.payeeList[customerIterator].name)
            customerIdData.add(mContentViewBinding.data!!.payeeList[customerIterator].id)
        }

        mContentViewBinding.customerListSpinner.adapter = ArrayAdapter(this, R.layout.custom_spinner_item, customerSpinnerData)
        mContentViewBinding.customerListSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                mContentViewBinding.receiverId = customerIdData[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun setupPayeeRv() {
        mContentViewBinding.payeeRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mContentViewBinding.payeeRv.adapter = PayeeRvAdapter(this@TransferWalletAmountActivity, mContentViewBinding.data!!.payeeList)
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
            onSuccessfulResponse(mGson.fromJson(response, TransferWalletAmountResponseData::class.java))
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
