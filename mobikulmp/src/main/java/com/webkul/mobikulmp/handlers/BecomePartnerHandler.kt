package com.webkul.mobikulmp.handlers

import android.widget.Toast
import com.webkul.mobikul.helpers.ToastHelper
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.helpers.Utils.Companion.validateUrlForSpecialCharacter
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.BecomeSellerActivity
import com.webkul.mobikulmp.helpers.MpAppSharedPref
import com.webkul.mobikulmp.models.BecomePartnerData
import com.webkul.mobikulmp.models.BecomePartnerResponseData
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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

class BecomePartnerHandler(private val mContext: BecomeSellerActivity) {
    private fun validateFormData(data: BecomePartnerData): Boolean {
        var isDataValid = true
        if (data.storeUrl.trim { it <= ' ' }.isEmpty()) {
            mContext.mContentViewBinding.storeUrlLayout.error = mContext.getString(R.string.shop_url) + mContext.getString(R.string.is_require_text)
            mContext.mContentViewBinding.storeUrlLayout.requestFocus()
            isDataValid = false
        } else if (!validateUrlForSpecialCharacter(data.storeUrl.trim { it <= ' ' })) {
            mContext.mContentViewBinding.storeUrlLayout.error = mContext.getString(R.string.enter_a_valid) + " " + mContext.getString(R.string.shop_url)
            mContext.mContentViewBinding.storeUrlLayout.requestFocus()
            isDataValid = false
        } else {
            mContext.mContentViewBinding.storeUrlLayout.error = null
        }
        return isDataValid
    }


    fun onClickMakeSeller(data: BecomePartnerData) {
        if (validateFormData(data)) {
            mContext.mContentViewBinding.loading = true
            Utils.hideKeyboard(mContext)
            MpApiConnection.makeSeller(mContext!!, data.storeUrl)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : ApiCustomCallback<BecomePartnerResponseData>(mContext, true) {
                        override fun onNext(becomePartnerResponseData: BecomePartnerResponseData) {
                            super.onNext(becomePartnerResponseData)
                            mContext.mContentViewBinding.loading = false
                            Toast.makeText(mContext, becomePartnerResponseData.message, Toast.LENGTH_LONG).show()
                            if (becomePartnerResponseData.success) {
                                MpAppSharedPref.setIsSeller(mContext, true)
                                MpAppSharedPref.setIsSellerPending(mContext, becomePartnerResponseData.isPending)
                                mContext.finish()
                            }
                        }

                        override fun onError(e: Throwable) {
                            super.onError(e)
                            mContext.mContentViewBinding.loading = false
                            ToastHelper.showToast(mContext, mContext.getString(com.webkul.mobikul.R.string.something_went_wrong))
                        }
                    })
        }
    }
}
