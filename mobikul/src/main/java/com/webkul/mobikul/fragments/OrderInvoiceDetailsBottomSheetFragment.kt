/*
 * Webkul Software.
 *
 * Kotlin
 *
 * @author Webkul <support@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2019 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

package com.webkul.mobikul.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.adapters.OrderInvoiceItemsRvAdapter
import com.webkul.mobikul.adapters.OrderTotalsRvAdapter
import com.webkul.mobikul.databinding.FragmentOrderInvoiceDetailsBottomSheetBinding
import com.webkul.mobikul.handlers.OrderInvoiceDetailsBottomSheetFragmentHandler
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_INCREMENT_ID
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_INVOICE_ID
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_INVOICE_INCREMENT_ID
import com.webkul.mobikul.models.user.InvoiceDetailsData
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException


class OrderInvoiceDetailsBottomSheetFragment : FullScreenBottomSheetDialogFragment() {

    companion object {
        fun newInstance(invoiceIncrementId: String, invoiceId: String,incrementId:String?): OrderInvoiceDetailsBottomSheetFragment {
            val orderInvoiceDetailsBottomSheetFragment = OrderInvoiceDetailsBottomSheetFragment()
            val args = Bundle()
            args.putString(BUNDLE_KEY_INVOICE_INCREMENT_ID, invoiceIncrementId)
            args.putString(BUNDLE_KEY_INCREMENT_ID, incrementId)
            args.putString(BUNDLE_KEY_INVOICE_ID, invoiceId)
            orderInvoiceDetailsBottomSheetFragment.arguments = args
            return orderInvoiceDetailsBottomSheetFragment
        }
    }

    lateinit var mContentViewBinding: FragmentOrderInvoiceDetailsBottomSheetBinding
    var id:String=""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_invoice_details_bottom_sheet, container, false)
        return mContentViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        startInitialization()
    }

    private fun startInitialization() {
        mContentViewBinding.invoiceId = arguments!!.getString(BUNDLE_KEY_INVOICE_INCREMENT_ID)
        id=arguments!!.getString(BundleKeysHelper.BUNDLE_KEY_INVOICE_ID,"")

        callApi()
        mContentViewBinding.handler = OrderInvoiceDetailsBottomSheetFragmentHandler(this)

    }

    private fun callApi() {
        mContentViewBinding.loading = true
        (context as BaseActivity).mHashIdentifier = Utils.getMd5String("getInvoiceDetailsData" + AppSharedPref.getStoreId(context!!) + AppSharedPref.getCustomerToken(context!!) + mContentViewBinding.invoiceId!!)
        ApiConnection.getInvoiceDetailsData(context!!,
                BaseActivity.mDataBaseHandler.getETagFromDatabase((context as BaseActivity).mHashIdentifier),
                /*mContentViewBinding.invoiceId!!*/ id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<InvoiceDetailsData>(context!!, true) {
                    override fun onNext(invoiceDetailsData: InvoiceDetailsData) {
                        super.onNext(invoiceDetailsData)
                        mContentViewBinding.loading = false
                        if (invoiceDetailsData.success) {
                            onSuccessfulResponse(invoiceDetailsData)
                        } else {
                            onFailureResponse(invoiceDetailsData)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mContentViewBinding.loading = false
                        onErrorResponse(e)
                    }
                })
        checkAndLoadLocalData()
    }

    private fun checkAndLoadLocalData() {
        val response = BaseActivity.mDataBaseHandler.getResponseFromDatabase((context as BaseActivity).mHashIdentifier)
        if (response.isNotBlank()) {
            onSuccessfulResponse(BaseActivity.mGson.fromJson(response, InvoiceDetailsData::class.java))
        }
    }

    private fun onSuccessfulResponse(invoiceDetailsData: InvoiceDetailsData) {
        mContentViewBinding.data = invoiceDetailsData
        setupOrderItemsRv()
        setupOrderTotalsRv()
    }

    private fun setupOrderItemsRv() {
        mContentViewBinding.orderItemsRv.adapter = OrderInvoiceItemsRvAdapter(context!!, mContentViewBinding.data!!.items)
        mContentViewBinding.orderItemsRv.isNestedScrollingEnabled = false
    }

    private fun setupOrderTotalsRv() {
        mContentViewBinding.orderTotalsRv.adapter = OrderTotalsRvAdapter(context!!, mContentViewBinding.data!!.totals)
        mContentViewBinding.orderTotalsRv.isNestedScrollingEnabled = false
    }

    private fun onFailureResponse(invoiceDetailsData: InvoiceDetailsData) {
        AlertDialogHelper.showNewCustomDialog(
                context as BaseActivity,
                getString(R.string.error),
                invoiceDetailsData.message,
                false,
                getString(R.string.ok),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    dismiss()
                }
                , ""
                , null)
    }

    private fun onErrorResponse(error: Throwable) {
        if ((!NetworkHelper.isNetworkAvailable(context!!) || (error is HttpException && error.code() == 304))) {
            // Do Nothing as the data is already loaded
        } else {
            AlertDialogHelper.showNewCustomDialog(
                    context as BaseActivity,
                    getString(R.string.error),
                    NetworkHelper.getErrorMessage(context!!, error),
                    false,
                    getString(R.string.try_again),
                    DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        callApi()
                    }
                    , getString(R.string.dismiss)
                    , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                dismiss()
            })
        }
    }
}