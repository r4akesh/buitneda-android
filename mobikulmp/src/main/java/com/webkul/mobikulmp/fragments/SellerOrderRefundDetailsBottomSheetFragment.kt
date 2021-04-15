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
import com.webkul.mobikul.helpers.NetworkHelper
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.adapters.SellerOrderCreditMemoItemsRvAdapter
import com.webkul.mobikulmp.databinding.FragmentSellerOrderCreditMemoDetailsBottomSheetBinding
import com.webkul.mobikulmp.handlers.SellerOrderCreditMemoDetailsBottomSheetFragmentHandler
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_CREDIT_MEMO_ID
import com.webkul.mobikulmp.models.seller.CreditMemoDetailsResponseData
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException


class SellerOrderRefundDetailsBottomSheetFragment : FullScreenBottomSheetDialogFragment() {

    companion object {
        fun newInstance(incrementId: String?, creditMemoId: String): SellerOrderRefundDetailsBottomSheetFragment {
            val orderShipmentDetailsBottomSheetFragment = SellerOrderRefundDetailsBottomSheetFragment()
            val args = Bundle()
            args.putString(BUNDLE_KEY_INCREMENT_ID, incrementId)
            args.putString(BUNDLE_KEY_CREDIT_MEMO_ID, creditMemoId)
            orderShipmentDetailsBottomSheetFragment.arguments = args
            return orderShipmentDetailsBottomSheetFragment
        }
    }

    lateinit var mContentViewBinding: FragmentSellerOrderCreditMemoDetailsBottomSheetBinding
    var mIncrementId: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_seller_order_credit_memo_details_bottom_sheet, container, false)
        return mContentViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        startInitialization()
    }

    private fun startInitialization() {
        mIncrementId = arguments?.getString(BUNDLE_KEY_INCREMENT_ID)?:""
        mContentViewBinding.creditMemoId = arguments!!.getString(BUNDLE_KEY_CREDIT_MEMO_ID)
        callApi()
    }

    private fun callApi() {
        mContentViewBinding.loading = true
        (context as BaseActivity).mHashIdentifier = Utils.getMd5String("getSellerCreditMemoDetails" + AppSharedPref.getStoreId(context!!) + AppSharedPref.getCustomerToken(context!!) + mContentViewBinding.creditMemoId!! + mIncrementId)
        MpApiConnection.getSellerCreditMemoDetails(context!!, BaseActivity.mDataBaseHandler.getETagFromDatabase((context as BaseActivity).mHashIdentifier), mContentViewBinding.creditMemoId!!, mIncrementId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<CreditMemoDetailsResponseData>(context!!, true) {
                    override fun onNext(t: CreditMemoDetailsResponseData) {
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
        checkAndLoadLocalData()
    }

    private fun checkAndLoadLocalData() {
        val response = BaseActivity.mDataBaseHandler.getResponseFromDatabase((context as BaseActivity).mHashIdentifier)
        if (response.isNotBlank()) {
            onSuccessfulResponse(BaseActivity.mGson.fromJson(response, CreditMemoDetailsResponseData::class.java))
        }
    }

    private fun onSuccessfulResponse(creditMemoDetailsResponseData: CreditMemoDetailsResponseData) {
        mContentViewBinding.data = creditMemoDetailsResponseData
        mContentViewBinding.handler = SellerOrderCreditMemoDetailsBottomSheetFragmentHandler(this)
        setupOrderItemsRv()
        setupOrderTotalsRv()
    }

    private fun setupOrderItemsRv() {
        mContentViewBinding.orderItemsRv.adapter = SellerOrderCreditMemoItemsRvAdapter(context!!, mContentViewBinding.data!!.itemList)
        mContentViewBinding.orderItemsRv.isNestedScrollingEnabled = false
    }

    private fun setupOrderTotalsRv() {
        mContentViewBinding.orderTotalsRv.adapter = OrderTotalsRvAdapter(context!!, mContentViewBinding.data!!.totals)
        mContentViewBinding.orderTotalsRv.isNestedScrollingEnabled = false
    }

    private fun onFailureResponse(shipmentDetailsData: CreditMemoDetailsResponseData) {
        AlertDialogHelper.showNewCustomDialog(
                context as BaseActivity,
                getString(R.string.error),
                shipmentDetailsData.message,
                false,
                getString(R.string.ok),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
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