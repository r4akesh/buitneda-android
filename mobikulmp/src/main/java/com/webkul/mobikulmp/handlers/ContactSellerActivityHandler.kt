package com.webkul.mobikulmp.handlers

import android.content.DialogInterface
import android.widget.Toast
import com.webkul.mobikul.helpers.AlertDialogHelper
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_ID
import com.webkul.mobikul.helpers.NetworkHelper
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.ContactSellerActivity
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_SELLER_ID
import com.webkul.mobikulmp.models.seller.ContactSellerRequestData
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
 * @date 16/7/19
 */


class ContactSellerActivityHandler(val mContactSellerActivity: ContactSellerActivity) {

    fun submitQuery(mData: ContactSellerRequestData) {
        if (mData.isFormValidated(mContactSellerActivity)) {
            mContactSellerActivity.mContentViewBinding.loading = true
            Utils.hideKeyboard(mContactSellerActivity)
            MpApiConnection.contactSeller(
                    mContactSellerActivity,
                    mData,
                    mContactSellerActivity.intent.getIntExtra(BUNDLE_KEY_SELLER_ID, 0),
                    mContactSellerActivity.intent.getStringExtra(BUNDLE_KEY_PRODUCT_ID)
            ).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : ApiCustomCallback<BaseModel>(mContactSellerActivity, true) {
                        override fun onNext(t: BaseModel) {
                            super.onNext(t)

                            mContactSellerActivity.mContentViewBinding.loading = false
                            if (t.success) {
                                Toast.makeText(mContactSellerActivity, t.message, Toast.LENGTH_LONG).show()
                                mContactSellerActivity.finish()
                            } else {
                                onFailureResponse(t)
                            }
                        }

                        override fun onError(e: Throwable) {
                            super.onError(e)
                            mContactSellerActivity.mContentViewBinding.loading = false
                            onErrorResponse(e)
                        }
                    })


        }
    }


    fun onFailureResponse(response: Any) {
        AlertDialogHelper.showNewCustomDialog(
                mContactSellerActivity,
                mContactSellerActivity.getString(R.string.error),
                (response as BaseModel).message,
                false,
                mContactSellerActivity.getString(R.string.ok),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    mContactSellerActivity.finish()
                }
                , ""
                , null)
    }

    private fun onErrorResponse(error: Throwable) {
        AlertDialogHelper.showNewCustomDialog(
                mContactSellerActivity,
                mContactSellerActivity.getString(R.string.oops),
                NetworkHelper.getErrorMessage(mContactSellerActivity, error),
                false,
                mContactSellerActivity.getString(R.string.try_again),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    submitQuery(mContactSellerActivity.mContentViewBinding.data!!)
                }
                , mContactSellerActivity.getString(R.string.close)
                , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        })
    }
}
