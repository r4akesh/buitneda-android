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

package com.webkul.mobikulmp.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.adapters.OrderTotalsRvAdapter
import com.webkul.mobikul.fragments.FullScreenBottomSheetDialogFragment
import com.webkul.mobikul.helpers.AlertDialogHelper
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_INCREMENT_ID
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_INVOICE_ID
import com.webkul.mobikul.helpers.NetworkHelper
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.adapters.SellerOrderInvoiceItemsRvAdapter
import com.webkul.mobikulmp.databinding.FragmentSellerOrderInvoiceDetailsBottomSheetBinding
import com.webkul.mobikulmp.handlers.SellerOrderInvoiceDetailsBottomSheetFragmentHandler
import com.webkul.mobikulmp.models.seller.SellerInvoiceDetailsData
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException


class SellerOrderInvoiceDetailsBottomSheetFragment : FullScreenBottomSheetDialogFragment() {

    companion object {
        fun newInstance(incrementId: String, invoiceId: String): SellerOrderInvoiceDetailsBottomSheetFragment {
            val orderInvoiceDetailsBottomSheetFragment = SellerOrderInvoiceDetailsBottomSheetFragment()
            val args = Bundle()
            args.putString(BUNDLE_KEY_INCREMENT_ID, incrementId)
            args.putString(BUNDLE_KEY_INVOICE_ID, invoiceId)
            orderInvoiceDetailsBottomSheetFragment.arguments = args
            return orderInvoiceDetailsBottomSheetFragment
        }
    }

    lateinit var mContentViewBinding: FragmentSellerOrderInvoiceDetailsBottomSheetBinding
    var mIncrementId: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_seller_order_invoice_details_bottom_sheet, container, false)
        return mContentViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        startInitialization()
    }

    private fun startInitialization() {
        mIncrementId = arguments?.getString(BUNDLE_KEY_INCREMENT_ID)?:""
        mContentViewBinding.invoiceId = arguments!!.getString(BUNDLE_KEY_INVOICE_ID)
        callApi()
    }

    private fun callApi() {
        mContentViewBinding.loading = true
        (context as BaseActivity).mHashIdentifier = Utils.getMd5String("getSellerInvoiceDetails" + AppSharedPref.getStoreId(context!!) + AppSharedPref.getCustomerToken(context!!) + mIncrementId + mContentViewBinding.invoiceId!!)
        MpApiConnection.getSellerInvoiceDetails(context!!,
                BaseActivity.mDataBaseHandler.getETagFromDatabase((context as BaseActivity).mHashIdentifier),
                mContentViewBinding.invoiceId!!,
                mIncrementId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<SellerInvoiceDetailsData>(context!!, true) {
                    override fun onNext(sellerInvoiceDetailsData: SellerInvoiceDetailsData) {
                        super.onNext(sellerInvoiceDetailsData)
                        mContentViewBinding.loading = false
                        if (sellerInvoiceDetailsData.success) {
                            onSuccessfulResponse(sellerInvoiceDetailsData)
                        } else {
                            onFailureResponse(sellerInvoiceDetailsData)
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
            onSuccessfulResponse(BaseActivity.mGson.fromJson(response, SellerInvoiceDetailsData::class.java))
        }
    }

    private fun onSuccessfulResponse(sellerInvoiceDetailsData: SellerInvoiceDetailsData) {
        mContentViewBinding.data = sellerInvoiceDetailsData
        setupOrderItemsRv()
        setupOrderTotalsRv()
        mContentViewBinding.handler = SellerOrderInvoiceDetailsBottomSheetFragmentHandler(this)
    }

    private fun setupOrderItemsRv() {
        mContentViewBinding.orderItemsRv.adapter = SellerOrderInvoiceItemsRvAdapter(context!!, mContentViewBinding.data!!.items)
        mContentViewBinding.orderItemsRv.isNestedScrollingEnabled = false
    }

    private fun setupOrderTotalsRv() {
        mContentViewBinding.orderTotalsRv.adapter = OrderTotalsRvAdapter(context!!, mContentViewBinding.data!!.totals)
        mContentViewBinding.orderTotalsRv.isNestedScrollingEnabled = false
    }

    private fun onFailureResponse(sellerInvoiceDetailsData: SellerInvoiceDetailsData) {
        AlertDialogHelper.showNewCustomDialog(
                context as BaseActivity,
                getString(R.string.error),
                sellerInvoiceDetailsData.message,
                false,
                getString(R.string.ok),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    callApi()
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