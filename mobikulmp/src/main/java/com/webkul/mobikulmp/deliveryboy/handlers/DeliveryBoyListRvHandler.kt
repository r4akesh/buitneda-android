package com.webkul.mobikulmp.deliveryboy.handlers

import android.view.View
import com.webkul.mobikul.helpers.ToastHelper
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.deliveryboy.fragment.SelectDeliveryBoyBottomSheetFragment
import com.webkul.mobikulmp.deliveryboy.models.DeliveryBoyList
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class DeliveryBoyListRvHandler(var fragment: SelectDeliveryBoyBottomSheetFragment) {

    fun onClickCancelBtn() {
        fragment.dismissAllowingStateLoss()
    }

    fun onClickDeliveryBoy(view: View, data: DeliveryBoyList) {
        fragment.mContentViewBinding.loading = true
        MpApiConnection.assignOrder(
                fragment.context,
                fragment.arguments?.getString(MpBundleKeysHelper.BUNDLE_INCREMENT_ID),
                data.id,
                fragment.arguments?.getString(MpBundleKeysHelper.BUNDLE_KEY_SELLER_ID)).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<BaseModel>(fragment.context!!, true) {
                    override fun onNext(t: BaseModel) {
                        super.onNext(t)
                        fragment.mContentViewBinding.loading = false
                        if (t.success) {
                            fragment.onSelectDeliveryBoy?.onSelectDeliveryBoy(data)
                            fragment.dismiss()
                        } else {
                            ToastHelper.showToast(fragment.context!!, t.message)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        fragment.mContentViewBinding.loading = false
                        e.message?.let { ToastHelper.showToast(fragment.context!!, it) }
                        e.printStackTrace()
                    }
                })
    }
}
