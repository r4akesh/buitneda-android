package com.webkul.mobikulmp.handlers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.webkul.mobikul.helpers.ToastHelper
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.CreateCreditMemoActivity
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Webkul Software.
 *
 * kotlin
 *
 * @author Webkul <support></support>@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

class CreateCreditMemoActivityHandler(private val mContext: CreateCreditMemoActivity) {

    fun onClickRefundBtn(doOffline: Int) {
        mContext.mContentViewBinding.loading = true
        MpApiConnection.createCreditMemo(mContext, mContext.mContentViewBinding.data!!, mContext.mContentViewBinding.incrementId!!, doOffline)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<BaseModel>(mContext, false) {
                    override fun onNext(t: BaseModel) {
                        super.onNext(t)
                        mContext.mContentViewBinding.loading = false
                        ToastHelper.showToast(mContext, t.message)
                        if (t.success) {
                            mContext.setResult(AppCompatActivity.RESULT_OK, Intent())
                            mContext.finish()
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mContext.mContentViewBinding.loading = false
                        ToastHelper.showToast(mContext, mContext.getString(R.string.something_went_wrong))
                    }
                })
    }
}