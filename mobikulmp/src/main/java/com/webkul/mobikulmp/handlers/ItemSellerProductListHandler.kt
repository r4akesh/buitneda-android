package com.webkul.mobikulmp.handlers

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_DOMINANT_COLOR
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_ID
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_IMAGE
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_NAME
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.SellerAddProductActivity
import com.webkul.mobikulmp.activities.SellerProductsListActivity
import com.webkul.mobikulmp.helpers.MpConstantsHelper.RC_EDIT_SELLER_PRODUCT
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

/**
 * Webkul Software.
 *
 * Java
 *
 * @author Webkul <support></support>@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

class ItemSellerProductListHandler(private val mContext: Context) {
    private var mPosition: Int = 0
    private var mId: Int = 0

    fun onClickProductListItem(id: Int, name: String, isSelectionModeOn: Boolean) {
        if (!isSelectionModeOn) {
            val intent = (mContext.applicationContext as MobikulApplication).getProductDetailsActivity(mContext)
            intent.putExtra(BUNDLE_KEY_PRODUCT_ID, id.toString())
            intent.putExtra(BUNDLE_KEY_PRODUCT_NAME, name)
            intent.putExtra(BUNDLE_KEY_PRODUCT_IMAGE, "")
            intent.putExtra(BUNDLE_KEY_PRODUCT_DOMINANT_COLOR, "")
            mContext.startActivity(intent)
        }
    }

    fun onClickDeleteItem(id: Int, position: Int) {
        mPosition = position
        mId = id
        AlertDialogHelper.showNewCustomDialog(
                mContext as BaseActivity,
                mContext.getString(R.string.delete),
                mContext.getString(R.string.are_you_sure_you_want_to_delete_this_product),
                false,
                mContext.getString(R.string.ok),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    callDeleteAPI(id, position)
                },
                mContext.getString(R.string.cancel),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                })


    }

    private fun callDeleteAPI(id: Int, position: Int) {
        (mContext as SellerProductsListActivity).mContentViewBinding.loading = true
        MpApiConnection.deleteProduct(mContext, id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<BaseModel>(mContext, true) {
                    override fun onNext(t: BaseModel) {
                        super.onNext(t)
                        mContext.mContentViewBinding.loading = false
                        if (t.success) {
                            ToastHelper.showToast(mContext, t.message, Toast.LENGTH_LONG)
                            mContext.mSellerProductListRvAdapter.remove(position)
                            mContext.mSellerProductListResponseData.totalCount = mContext.mContentViewBinding.data!!.totalCount - 1
                            Handler().postDelayed({
                                mContext.mContentViewBinding.productsRv.adapter!!.notifyDataSetChanged()
                                if (mContext.mSellerProductListRvAdapter.itemCount == 0) {
                                    mContext.mContentViewBinding.data = mContext.mSellerProductListResponseData
                                    mContext.mContentViewBinding.executePendingBindings()
                                }
                            }, 1000)

                        } else {
                            onFailureResponse(t)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        onErrorResponse(e)
                    }
                })
    }

    fun onFailureResponse(response: Any) {
        AlertDialogHelper.showNewCustomDialog(
                mContext as BaseActivity,
                mContext.getString(R.string.error),
                (response as BaseModel).message,
                false,
                mContext.getString(R.string.ok),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                }
                , ""
                , null)
    }


    private fun onErrorResponse(error: Throwable) {
        if ((!NetworkHelper.isNetworkAvailable(mContext) || (error is HttpException && error.code() == 304))) {

        } else {
            AlertDialogHelper.showNewCustomDialog(
                    mContext as BaseActivity,
                    mContext.getString(R.string.oops),
                    NetworkHelper.getErrorMessage(mContext, error),
                    false,
                    mContext.getString(R.string.try_again),
                    DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        callDeleteAPI(mPosition, mId)
                    }
                    , mContext.getString(com.webkul.mobikul.R.string.close)
                    , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            })
        }
    }

    fun onClickProductEditBtn(view: View, id: Int) {

        AlertDialogHelper.showNewCustomDialog(
                mContext as BaseActivity,
                mContext.getString(R.string.edit),
                mContext.getString(R.string.are_you_sure_you_want_to_edit_this_product),
                false,
                mContext.getString(R.string.ok),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    val intent = Intent(mContext, SellerAddProductActivity::class.java)
                    intent.putExtra(BUNDLE_KEY_PRODUCT_ID, id.toString())
                    (mContext as SellerProductsListActivity).startActivityForResult(intent, RC_EDIT_SELLER_PRODUCT)
                },
                mContext.getString(R.string.cancel),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                })

    }

    fun onClickShowHideInfo(view: View, isVisible: Boolean, position: Int) {
        if (isVisible) {
            (mContext as SellerProductsListActivity).mContentViewBinding.data!!.productList!![position].visible = false
            (view as AppCompatTextView).text = mContext.getString(R.string.more_info)
            if (AppSharedPref.getStoreCode(view.context) != "ar")
                view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down_arrow_blue_wrapper, 0)
            else
                view.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_down_arrow_blue_wrapper, 0, 0, 0)

        } else {
            (mContext as SellerProductsListActivity).mContentViewBinding.data!!.productList!![position].visible = true
            (view as AppCompatTextView).text = mContext.getString(R.string.less_info)
            if (AppSharedPref.getStoreCode(view.context) != "ar")
                view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_up_arrow_blue_wrapper, 0)
            else
                view.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_up_arrow_blue_wrapper, 0, 0, 0)
        }


    }
}