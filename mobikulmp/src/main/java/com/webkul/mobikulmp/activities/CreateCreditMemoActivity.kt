package com.webkul.mobikulmp.activities

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_INCREMENT_ID
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.adapters.RefundOrderItemsRvAdapter
import com.webkul.mobikulmp.databinding.ActivityCreateCreditMemoBinding
import com.webkul.mobikulmp.handlers.CreateCreditMemoActivityHandler
import com.webkul.mobikulmp.models.seller.CreditMemoFormResponseData
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.models.seller
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 23/5/19
 */
class CreateCreditMemoActivity : BaseActivity() {
    lateinit var mContentViewBinding: ActivityCreateCreditMemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_credit_memo)
        startInitialization()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    private fun startInitialization() {
        initSupportActionBar()
        mContentViewBinding.incrementId = intent.getStringExtra(BUNDLE_KEY_INCREMENT_ID)
        callApi()
    }

    private fun initSupportActionBar() {
        supportActionBar?.title = getString(R.string.activity_create_credit_memo_title)
    }

    private fun callApi() {
        mContentViewBinding.loading = true
        mHashIdentifier = Utils.getMd5String("getCreditMemoFormData" + AppSharedPref.getStoreId(this) + AppSharedPref.getCustomerToken(this) + mContentViewBinding.incrementId)
        MpApiConnection.getCreditMemoFormData(this, mDataBaseHandler.getETagFromDatabase(mHashIdentifier), mContentViewBinding.incrementId!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<CreditMemoFormResponseData>(this, false) {
                    override fun onNext(t: CreditMemoFormResponseData) {
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

    private fun onSuccessfulResponse(t: CreditMemoFormResponseData) {
        mContentViewBinding.data = t
        mContentViewBinding.handler = CreateCreditMemoActivityHandler(this)
        setupRefundOrderItemsRv()
    }

    private fun setupRefundOrderItemsRv() {
        mContentViewBinding.refundOrderItemsRv.adapter = RefundOrderItemsRvAdapter(this, mContentViewBinding.data!!.itemList)
        mContentViewBinding.refundOrderItemsRv.isNestedScrollingEnabled = false
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
                            if (mContentViewBinding.data == null)
                                finish()
                        }
                        , ""
                        , null)
            }
        }
    }

    private fun onErrorResponse(error: Throwable) {
        if ((!NetworkHelper.isNetworkAvailable(this) || (error is HttpException && error.code() == 304)) && mContentViewBinding.data != null) {
            // Do Nothing as the data is already loaded
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