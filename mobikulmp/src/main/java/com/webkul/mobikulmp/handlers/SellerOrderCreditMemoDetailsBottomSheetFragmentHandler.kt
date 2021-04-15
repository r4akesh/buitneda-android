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

package com.webkul.mobikulmp.handlers

import android.content.DialogInterface
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.helpers.AlertDialogHelper
import com.webkul.mobikul.helpers.ToastHelper
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.fragments.SellerOrderRefundDetailsBottomSheetFragment
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class SellerOrderCreditMemoDetailsBottomSheetFragmentHandler(private var mFragmentContext: SellerOrderRefundDetailsBottomSheetFragment) {

    fun onClickCancelBtn() {
        mFragmentContext.dismiss()
    }

    fun onClickSendEmail(creditMemoId: String) {
        AlertDialogHelper.showNewCustomDialog(
                mFragmentContext.context as BaseActivity,
                mFragmentContext.getString(R.string.are_you_sure),
                "",
                true,
                mFragmentContext.getString(R.string.yes),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    mFragmentContext.mContentViewBinding.loading = true
                    MpApiConnection.sendCreditMemoMail(mFragmentContext.context!!, creditMemoId, mFragmentContext.mIncrementId)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(object : ApiCustomCallback<BaseModel>(mFragmentContext.context!!, true) {
                                override fun onNext(t: BaseModel) {
                                    super.onNext(t)
                                    mFragmentContext.mContentViewBinding.loading = false
                                    ToastHelper.showToast(mFragmentContext.context!!, t.message)
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
}