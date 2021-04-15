package com.webkul.mobikulmp.handlers

import android.content.DialogInterface
import android.view.View
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.helpers.AlertDialogHelper
import com.webkul.mobikul.helpers.NetworkHelper
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.fragments.SellerMakeReviewFragment
import com.webkul.mobikulmp.models.SellerMakeReviewData
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.fragments
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 26/07/19
 */
class SellerMakeReviewHandler(private val mFragmentContext: SellerMakeReviewFragment, private val mSellerId: Int, private val mShopUrl: String) {

    fun onClickCancelBtn() {
        mFragmentContext.dismiss()
    }

    fun onClickSaveReview(view: View, data: SellerMakeReviewData) {
        if (data.isFormValidated(mFragmentContext)) {
            Utils.hideKeyboard(view)
            callApi(data)
        }
    }

    private fun callApi(data: SellerMakeReviewData) {
        (mFragmentContext.activity as BaseActivity).mHashIdentifier = ""
        mFragmentContext.mContentViewBinding.loading = true
        MpApiConnection.saveReview(
                mFragmentContext.context!!,
                mSellerId,
                data.nickName,
                data.summary,
                data.comment,
                data.priceRating * 20,
                data.valueRating * 20,
                data.quantityRating * 20,
                mShopUrl)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<BaseModel>(mFragmentContext.context!!, true) {
                    override fun onNext(t: BaseModel) {
                        super.onNext(t)
                        mFragmentContext.mContentViewBinding.loading = false
                        if (t.success) {
                            mFragmentContext.dismiss()
                        } else {
                            onFailureResponse(t)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mFragmentContext.mContentViewBinding.loading = false
                        onErrorResponse(e)
                    }
                })
    }

    fun onFailureResponse(response: Any) {
        AlertDialogHelper.showNewCustomDialog(
                mFragmentContext.activity as BaseActivity,
                mFragmentContext.getString(R.string.error),
                (response as BaseModel).message,
                false,
                mFragmentContext.getString(R.string.ok),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    mFragmentContext.dismiss()
                }
                , ""
                , null)
    }

    private fun onErrorResponse(error: Throwable) {
        AlertDialogHelper.showNewCustomDialog(
                mFragmentContext.activity!! as BaseActivity,
                mFragmentContext.getString(R.string.oops),
                NetworkHelper.getErrorMessage(mFragmentContext.context, error),
                false,
                mFragmentContext.getString(R.string.try_again),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    callApi(mFragmentContext.mContentViewBinding.data!!)
                }
                , mFragmentContext.getString(com.webkul.mobikul.R.string.close)
                , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        })

    }

}
