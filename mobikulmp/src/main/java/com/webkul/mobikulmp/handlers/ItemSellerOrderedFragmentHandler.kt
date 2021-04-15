package com.webkul.mobikulmp.handlers

import android.content.DialogInterface
import android.content.Intent
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.helpers.AlertDialogHelper
import com.webkul.mobikul.helpers.BundleKeysHelper
import com.webkul.mobikul.helpers.ToastHelper
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.CreateCreditMemoActivity
import com.webkul.mobikulmp.activities.SellerOrderDetailsActivity
import com.webkul.mobikulmp.fragments.ItemSellerOrderedFragment
import com.webkul.mobikulmp.helpers.MpConstantsHelper.RC_CREATE_CREDIT_MEMO
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Webkul Software.
 *
 * Kotlin
 *
 * @author Webkul <support@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

class ItemSellerOrderedFragmentHandler(val mFragmentContext: ItemSellerOrderedFragment, val mIncrementId: String) {

    fun onClickCancelOrder() {
        AlertDialogHelper.showNewCustomDialog(
                mFragmentContext.context as BaseActivity,
                mFragmentContext.getString(R.string.are_you_sure),
                mFragmentContext.getString(R.string.cancel_order_confirm_message),
                true,
                mFragmentContext.getString(R.string.yes),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    mFragmentContext.mContentViewBinding.loading = true
                    MpApiConnection.cancelOrder(mFragmentContext.context!!, mIncrementId)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(object : ApiCustomCallback<BaseModel>(mFragmentContext.context!!, true) {
                                override fun onNext(cancelOrder: BaseModel) {
                                    super.onNext(cancelOrder)
                                    mFragmentContext.mContentViewBinding.loading = false
                                    ToastHelper.showToast(mFragmentContext.context!!, cancelOrder.message)
                                    (mFragmentContext.context as SellerOrderDetailsActivity).callApi()
                                }

                                override fun onError(e: Throwable) {
                                    super.onError(e)
                                    mFragmentContext.mContentViewBinding.loading = false
                                    ToastHelper.showToast(mFragmentContext.context!!, mFragmentContext.getString(R.string.something_went_wrong))
                                }
                            })
                }
                , mFragmentContext.getString(R.string.cancel)
                , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        })
    }

    fun onClickSendEmail() {
        AlertDialogHelper.showNewCustomDialog(
                mFragmentContext.context as BaseActivity,
                mFragmentContext.getString(R.string.are_you_sure),
                mFragmentContext.getString(R.string.confirm_email_send),
                true,
                mFragmentContext.getString(R.string.yes),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    mFragmentContext.mContentViewBinding.loading = true
                    MpApiConnection.sendOrderMailData(mFragmentContext.context!!, mIncrementId)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(object : ApiCustomCallback<BaseModel>(mFragmentContext.context!!, true) {
                                override fun onNext(sendOrderMailData: BaseModel) {
                                    super.onNext(sendOrderMailData)
                                    mFragmentContext.mContentViewBinding.loading = false
                                    ToastHelper.showToast(mFragmentContext.context!!, sendOrderMailData.message)
                                }

                                override fun onError(e: Throwable) {
                                    super.onError(e)
                                    mFragmentContext.mContentViewBinding.loading = false
                                    ToastHelper.showToast(mFragmentContext.context!!, mFragmentContext.getString(R.string.something_went_wrong))
                                }
                            })
                }
                , mFragmentContext.getString(R.string.cancel)
                , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        })
    }

    fun onClickCreateInvoice() {
        AlertDialogHelper.showNewCustomDialog(
                mFragmentContext.context as BaseActivity,
                mFragmentContext.getString(R.string.are_you_sure),
                mFragmentContext.getString(R.string.create_invoice_confirm_message),
                true,
                mFragmentContext.getString(R.string.yes),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    mFragmentContext.mContentViewBinding.loading = true
                    MpApiConnection.createInvoice(mFragmentContext.context!!, mIncrementId)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(object : ApiCustomCallback<BaseModel>(mFragmentContext.context!!, true) {
                                override fun onNext(createInvoice: BaseModel) {
                                    super.onNext(createInvoice)
                                    mFragmentContext.mContentViewBinding.loading = false
                                    if (createInvoice.success) {
                                        ToastHelper.showToast(mFragmentContext.context!!, createInvoice.message)
                                        (mFragmentContext.context as SellerOrderDetailsActivity).callApi()
                                    } else {
                                        ToastHelper.showToast(mFragmentContext.context!!, createInvoice.message)
                                    }
                                }

                                override fun onError(e: Throwable) {
                                    super.onError(e)
                                    mFragmentContext.mContentViewBinding.loading = false
                                    ToastHelper.showToast(mFragmentContext.context!!, mFragmentContext.getString(R.string.something_went_wrong))
                                }
                            })
                }
                , mFragmentContext.getString(R.string.cancel)
                , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        })
    }

    fun onClickShip(carrier: String, trackingNumber: String) {
        if (trackingNumber.isNotEmpty()) {
            mFragmentContext.mContentViewBinding.trackingNumberTil.isErrorEnabled = false
            mFragmentContext.mContentViewBinding.trackingNumberTil.error = null
            AlertDialogHelper.showNewCustomDialog(
                    mFragmentContext.context as BaseActivity,
                    mFragmentContext.getString(R.string.are_you_sure),
                    mFragmentContext.getString(R.string.shipping_confirm_message),
                    true,
                    mFragmentContext.getString(R.string.yes),
                    DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        mFragmentContext.mContentViewBinding.loading = true
                        MpApiConnection.createShipment(mFragmentContext.context!!, mIncrementId, carrier, trackingNumber)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(object : ApiCustomCallback<BaseModel>(mFragmentContext.context!!, true) {
                                    override fun onNext(createShipment: BaseModel) {
                                        super.onNext(createShipment)
                                        mFragmentContext.mContentViewBinding.loading = false
                                        ToastHelper.showToast(mFragmentContext.context!!, createShipment.message)
                                        if (createShipment.success)
                                            (mFragmentContext.context as SellerOrderDetailsActivity).callApi()
                                    }

                                    override fun onError(e: Throwable) {
                                        super.onError(e)
                                        mFragmentContext.mContentViewBinding.loading = false
                                        ToastHelper.showToast(mFragmentContext.context!!, mFragmentContext.getString(R.string.something_went_wrong))
                                    }
                                })
                    }
                    , mFragmentContext.getString(R.string.cancel)
                    , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            })
        } else {
            mFragmentContext.mContentViewBinding.trackingNumberTil.error = mFragmentContext.getString(R.string.tracking_number) + " " + mFragmentContext.getString(R.string.is_required)
            Utils.showShakeError(mFragmentContext.context!!, mFragmentContext.mContentViewBinding.trackingNumberTil)
            mFragmentContext.mContentViewBinding.trackingNumberTil.requestFocus()
        }
    }

    fun onClickCreateMemo() {
        val intent = Intent(mFragmentContext.context, CreateCreditMemoActivity::class.java)
        intent.putExtra(BundleKeysHelper.BUNDLE_KEY_INCREMENT_ID, mIncrementId)
        mFragmentContext.activity?.startActivityForResult(intent, RC_CREATE_CREDIT_MEMO)
    }
    // #Delivery boy

    fun onClickChooseDeliveryBoy(deliveryBoyId: String, sellerId: String) {
        val selectDeliveryBoy = com.webkul.mobikulmp.deliveryboy.fragment.SelectDeliveryBoyBottomSheetFragment.newInstance(deliveryBoyId, sellerId, mIncrementId)
        selectDeliveryBoy.setOnSelectInterface(object : com.webkul.mobikulmp.deliveryboy.fragment.SelectDeliveryBoyBottomSheetFragment.OnSelectDeliveryBoy {
            override fun onSelectDeliveryBoy(data: com.webkul.mobikulmp.deliveryboy.models.DeliveryBoyList) {
                mFragmentContext.mContentViewBinding.data?.deliveryBoyDetail?.deliveryBoyId=data.id
                mFragmentContext.mContentViewBinding.data?.deliveryBoyDetail?.deliveryBoyName=data.name
            }

        })
        mFragmentContext.activity?.supportFragmentManager?.let { selectDeliveryBoy.show(it, com.webkul.mobikulmp.deliveryboy.fragment.SelectDeliveryBoyBottomSheetFragment::class.java.simpleName) }
    }
}