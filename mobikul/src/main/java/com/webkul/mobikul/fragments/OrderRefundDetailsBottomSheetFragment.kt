package com.webkul.mobikul.fragments

import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.adapters.OrderRefundItemRvAdapter
import com.webkul.mobikul.adapters.OrderTotalsRvAdapter
import com.webkul.mobikul.databinding.FragmentRefundDetailsBottomSheetBinding
import com.webkul.mobikul.handlers.OrderRefundDetailsBottomSheetFragmentHandler
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.helpers.ConstantsHelper.RC_WRITE_TO_EXTERNAL_STORAGE
import com.webkul.mobikul.models.user.RefundDetailsData
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class OrderRefundDetailsBottomSheetFragment : FullScreenBottomSheetDialogFragment() {

    companion object {
        fun newInstance(invoiceId: String, id: String): OrderRefundDetailsBottomSheetFragment {
            val orderInvoiceDetailsBottomSheetFragment = OrderRefundDetailsBottomSheetFragment()
            val args = Bundle()
            args.putString(BundleKeysHelper.BUNDLE_KEY_INVOICE_ID, invoiceId)
            args.putString(BundleKeysHelper.BUNDLE_KEY__ID_FROM_ORDER_DETAIL, id)
            orderInvoiceDetailsBottomSheetFragment.arguments = args
            return orderInvoiceDetailsBottomSheetFragment
        }
    }

    lateinit var mContentViewBinding: FragmentRefundDetailsBottomSheetBinding
    var id: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_refund_details_bottom_sheet, container, false)
        return mContentViewBinding.root
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        var allPermissionsGranted = true
        for (eachGrantResult in grantResults) {
            if (eachGrantResult != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false
            }
        }
        if (allPermissionsGranted) {
            if (requestCode == RC_WRITE_TO_EXTERNAL_STORAGE)
                mContentViewBinding.handler!!.onClickSaveInvoice(mContentViewBinding.data!!, mContentViewBinding.invoiceId
                        ?: "")
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        startInitialization()
    }

    private fun startInitialization() {
        mContentViewBinding.invoiceId = arguments!!.getString(BundleKeysHelper.BUNDLE_KEY_INVOICE_ID)
        id = arguments!!.getString(BundleKeysHelper.BUNDLE_KEY__ID_FROM_ORDER_DETAIL, "")

        callApi()
        mContentViewBinding.handler = OrderRefundDetailsBottomSheetFragmentHandler(this)

    }

    private fun callApi() {
        mContentViewBinding.loading = true
        (context as BaseActivity).mHashIdentifier = Utils.getMd5String("getRefundDetailsData" + AppSharedPref.getStoreId(context!!) + AppSharedPref.getCustomerToken(context!!) + mContentViewBinding.invoiceId!!)
        ApiConnection.getRefundDetailsData(context!!, BaseActivity.mDataBaseHandler.getETagFromDatabase((context as BaseActivity).mHashIdentifier), /*mContentViewBinding.invoiceId!!*/ id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<RefundDetailsData>(context!!, true) {
                    override fun onNext(invoiceDetailsData: RefundDetailsData) {
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
            onSuccessfulResponse(BaseActivity.mGson.fromJson(response, RefundDetailsData::class.java))
        }
    }

    private fun onSuccessfulResponse(invoiceDetailsData: RefundDetailsData) {
        mContentViewBinding.data = invoiceDetailsData
        setupOrderItemsRv()
        setupOrderTotalsRv()
    }

    private fun setupOrderItemsRv() {
        mContentViewBinding.orderItemsRv.adapter = OrderRefundItemRvAdapter(context!!, mContentViewBinding.data!!.items)
        mContentViewBinding.orderItemsRv.isNestedScrollingEnabled = false
    }

    private fun setupOrderTotalsRv() {
        mContentViewBinding.orderTotalsRv.adapter = OrderTotalsRvAdapter(context!!, mContentViewBinding.data!!.totals)
        mContentViewBinding.orderTotalsRv.isNestedScrollingEnabled = false
    }

    private fun onFailureResponse(invoiceDetailsData: RefundDetailsData) {
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