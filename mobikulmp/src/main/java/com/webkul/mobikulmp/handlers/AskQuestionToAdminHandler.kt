package com.webkul.mobikulmp.handlers

import com.webkul.mobikul.helpers.ToastHelper
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.AskQuestionsToAdminActivity
import com.webkul.mobikulmp.models.AskQuestionsToAdminRequestData
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.handlers
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 20/5/19
 */
class AskQuestionToAdminHandler(val mContext: AskQuestionsToAdminActivity) {

    fun onClickSubmitQuery(data: AskQuestionsToAdminRequestData) {
        if (data.isFormValidated(mContext)) {
            mContext.mContentViewBinding.loading = true
            Utils.hideKeyboard(mContext)
            MpApiConnection.askQuestionToAdmin(mContext, data)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : ApiCustomCallback<BaseModel>(mContext, true) {
                        override fun onNext(baseModel: BaseModel) {
                            super.onNext(baseModel)
                            mContext.mContentViewBinding.loading = false
                            if (baseModel.success) {
                                ToastHelper.showToast(context, mContext.getString(R.string.query_submitted))
                                mContext.mContentViewBinding.data = AskQuestionsToAdminRequestData()
                                mContext.mContentViewBinding.executePendingBindings()
                            }
                        }

                        override fun onError(e: Throwable) {
                            super.onError(e)
                            mContext.mContentViewBinding.loading = false
                            ToastHelper.showToast(context, mContext.getString(R.string.something_went_wrong))
                        }
                    })
        }
    }
}
