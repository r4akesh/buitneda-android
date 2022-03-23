/*
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

package com.webkul.mobikul.handlers

import android.content.DialogInterface
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.activities.CatalogActivity
import com.webkul.mobikul.fragments.FilterBottomSheetFragment
import com.webkul.mobikul.fragments.SortBottomSheetFragment
import com.webkul.mobikul.helpers.AlertDialogHelper
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikul.helpers.ConstantsHelper.VIEW_TYPE_GRID
import com.webkul.mobikul.helpers.ConstantsHelper.VIEW_TYPE_LIST
import com.webkul.mobikul.helpers.NetworkHelper
import com.webkul.mobikul.helpers.ToastHelper
import com.webkul.mobikul.models.checkout.AddToCartResponseModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import org.json.JSONArray
import org.json.JSONObject

class CatalogActivityHandler(private val mContext: CatalogActivity) {
    private var mProductParamsJSON: JSONObject = JSONObject()
    private val mFiles = ArrayList<MultipartBody.Part>()
    private var mRelatedProductsJSONArray: JSONArray = JSONArray()
    fun onClickSortBtn() {
        if (mContext.mContentViewBinding.data!!.sortingData.isNotEmpty()) {
            val sortBottomSheetFragment = SortBottomSheetFragment()
            sortBottomSheetFragment.show(
                mContext.supportFragmentManager,
                sortBottomSheetFragment.tag
            )
        } else {
            ToastHelper.showToast(mContext, mContext.getString(R.string.sort_options_not_available))
        }
    }

    fun onClickFilterBtn() {
        if (mContext.mFilterInputJson.length() > 0 || mContext.mContentViewBinding.data!!.layeredData.isNotEmpty()) {
            val filterBottomSheetFragment = FilterBottomSheetFragment()
            filterBottomSheetFragment.show(
                mContext.supportFragmentManager,
                filterBottomSheetFragment.tag
            )
        } else {
            ToastHelper.showToast(
                mContext,
                mContext.getString(R.string.filter_options_not_available)
            )
        }
    }

    fun onClickViewSwitcher(view: View) {
        if (mContext.mContentViewBinding.productsRv.adapter!!.getItemViewType(0) == VIEW_TYPE_LIST) {
            mContext.mContentViewBinding.productsRv.layoutManager = GridLayoutManager(mContext, 2)
            (view as AppCompatTextView).setCompoundDrawablesRelativeWithIntrinsicBounds(
                ContextCompat.getDrawable(mContext, R.drawable.ic_grid_view_wrapper),
                null,
                null,
                null
            )
            view.text = mContext.resources.getString(R.string.grid)
            AppSharedPref.setViewType(mContext, VIEW_TYPE_GRID)
        } else {
            mContext.mContentViewBinding.productsRv.layoutManager = LinearLayoutManager(mContext)
            (view as AppCompatTextView).setCompoundDrawablesRelativeWithIntrinsicBounds(
                ContextCompat.getDrawable(mContext, R.drawable.ic_list_view_wrapper),
                null,
                null,
                null
            )
            view.text = mContext.resources.getString(R.string.list)
            AppSharedPref.setViewType(mContext, VIEW_TYPE_LIST)
        }
    }

    /* fun onClickAdd(hasRequiredOptions: Boolean, entityId: String, name: String, thumbNail: String, dominantColor: String,position:Int,type:String,pageNumber:Int) {
         (mContext).mContentViewBinding.loading = true
         if (NetworkHelper.isNetworkAvailable(mContext)) {
             if(type==""){
                 ApiConnection.addToCart(mContext, entityId, "1", JSONObject(), null, JSONArray())
                     .observeOn(AndroidSchedulers.mainThread())
                     .subscribeOn(Schedulers.io())
                     .subscribe(object : ApiCustomCallback<AddToCartResponseModel>(mContext, true) {
                         override fun onNext(addToCartResponse: AddToCartResponseModel) {
                             super.onNext(addToCartResponse)
                             mContext.mContentViewBinding.loading = false
                             ToastHelper.showToast(mContext, addToCartResponse.message)
                             mContext.updateCartCount(addToCartResponse.cartCount)
                             if (addToCartResponse.success) {
                                 mContext.clickedPageNumber  = pageNumber
                                 mContext.callApi(true,position)
                                 if (addToCartResponse.quoteId != 0) {
                                     AppSharedPref.setQuoteId(mContext, addToCartResponse.quoteId)
                                 }
                             }
                         }

                         override fun onError(e: Throwable) {
                             super.onError(e)
                             mContext.mContentViewBinding.loading = false
                             ToastHelper.showToast(mContext, mContext.resources.getString(R.string.something_went_wrong))
                         }
                     })
             }else{
                 ApiConnection.updateProduct(
                     mContext,
                     mContext.mContentViewBinding.data!!.productList[position].id!!,
                     mContext.mContentViewBinding.data!!.productList[position].cartQty.toString(),
                     mProductParamsJSON,
                     mFiles,
                     mRelatedProductsJSONArray,
                     mContext.mContentViewBinding.data!!.productList[position].cartItemId!!
                 )
                     .observeOn(AndroidSchedulers.mainThread())
                     .subscribeOn(Schedulers.io())
                     .subscribe(object : ApiCustomCallback<AddToCartResponseModel>(mContext, true) {
                         override fun onNext(addToCartResponse: AddToCartResponseModel) {
                             super.onNext(addToCartResponse)
                             mContext.mContentViewBinding.loading = false
                             ToastHelper.showToast(mContext, addToCartResponse.message)
                             mContext.updateCartCount(addToCartResponse.cartCount)
                             mContext.callApi(true,position)


                         }

                         override fun onError(e: Throwable) {
                             super.onError(e)
                             mContext.mContentViewBinding.loading = false
                             ToastHelper.showToast(
                                 mContext,
                                 mContext.resources.getString(R.string.something_went_wrong)
                             )
                         }
                     })
             }
         } else {
             addToOfflineCart(entityId)
         }
     }*/

    fun onClickAdd(
        hasRequiredOptions: Boolean,
        entityId: String,
        name: String,
        thumbNail: String,
        dominantColor: String,
        position: Int,
        type: String,
        quoteId: Int
    ) {
        (mContext).mContentViewBinding.loading = true
        if (NetworkHelper.isNetworkAvailable(mContext)) {
            /* if (type == "add") {
                 mContext.mContentViewBinding.data!!.productList[position].cartQty++
                 ApiConnection.addToCart(
                     mContext,
                     entityId,
                     "1",
                     JSONObject(),
                     null,
                     JSONArray()
                 )
                     .observeOn(AndroidSchedulers.mainThread())
                     .subscribeOn(Schedulers.io())
                     .subscribe(object : ApiCustomCallback<AddToCartResponseModel>(mContext, true) {
                         override fun onNext(addToCartResponse: AddToCartResponseModel) {
                             super.onNext(addToCartResponse)
                             mContext.mContentViewBinding.loading = false
                             ToastHelper.showToast(mContext, addToCartResponse.message)
                             mContext.updateCartCount(addToCartResponse.cartCount)
                             if (addToCartResponse.success) {
                                 mContext.mContentViewBinding.data!!.productList[position].addedInfoCart =
                                     "true"
                                 mContext.mContentViewBinding.data!!.productList[position].quoteId =
                                     addToCartResponse.quoteId
                                 mContext.callApi(true, position)
                                 mContext.mContentViewBinding.productsRv.adapter!!.notifyItemChanged(
                                     position
                                 )
                             }
                         }

                         override fun onError(e: Throwable) {
                             super.onError(e)
                             mContext.mContentViewBinding.loading = false
                             ToastHelper.showToast(
                                 mContext,
                                 mContext.resources.getString(R.string.something_went_wrong)
                             )
                         }
                     })
             } else {
                 mContext.mContentViewBinding.data!!.productList[position].cartQty--
                 ApiConnection.addToCart(
                     mContext,
                     entityId,
                     "0",
                     JSONObject(),
                     null,
                     JSONArray()
                 )
                     .observeOn(AndroidSchedulers.mainThread())
                     .subscribeOn(Schedulers.io())
                     .subscribe(object : ApiCustomCallback<AddToCartResponseModel>(mContext, true) {
                         override fun onNext(addToCartResponse: AddToCartResponseModel) {
                             super.onNext(addToCartResponse)
                             mContext.mContentViewBinding.loading = false
                             ToastHelper.showToast(mContext, addToCartResponse.message)
                             mContext.updateCartCount(addToCartResponse.cartCount)
                             if (addToCartResponse.success) {
                                 mContext.mContentViewBinding.data!!.productList[position].addedInfoCart =
                                     "true"
                                 mContext.mContentViewBinding.data!!.productList[position].quoteId =
                                     addToCartResponse.quoteId
                                 mContext.callApi(true, position)
                                 mContext.mContentViewBinding.productsRv.adapter!!.notifyItemChanged(
                                     position
                                 )
                             }
                         }

                         override fun onError(e: Throwable) {
                             super.onError(e)
                             mContext.mContentViewBinding.loading = false
                             ToastHelper.showToast(
                                 mContext,
                                 mContext.resources.getString(R.string.something_went_wrong)
                             )
                         }
                     })
             }*/

            if (mContext.mContentViewBinding.data!!.productList[position].cartItemId == "" && mContext.mContentViewBinding.data!!.productList[position].cartQty == 0) {
                ApiConnection.addToCart(mContext, entityId, "1", JSONObject(), null, JSONArray())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : ApiCustomCallback<AddToCartResponseModel>(mContext, true) {
                        override fun onNext(addToCartResponse: AddToCartResponseModel) {
                            super.onNext(addToCartResponse)
                            mContext.mContentViewBinding.loading = false
                            ToastHelper.showToast(mContext, addToCartResponse.message)
                            mContext.updateCartCount(addToCartResponse.cartCount)
                            if (addToCartResponse.success) {
                                mContext.mContentViewBinding.data!!.productList[position].addedInfoCart =
                                    "true"
                                mContext.mContentViewBinding.data!!.productList[position].quoteId = addToCartResponse.cartQuoteId
                                AppSharedPref.setQuoteId(mContext,addToCartResponse.cartQuoteId)
                                mContext.mContentViewBinding.data!!.productList[position].cartItemId = addToCartResponse.cartItemId

                                if (type == "add") {
                                    mContext.mContentViewBinding.data!!.productList[position].cartQty++
                                } else {
                                    mContext.mContentViewBinding.data!!.productList[position].cartQty--
                                }
                                mContext.mContentViewBinding.productsRv.adapter!!.notifyItemChanged(
                                    position
                                )
                                mContext.updateBadge()
                                //mContext.callApi(true, position)
                            }
                        }

                        override fun onError(e: Throwable) {
                            super.onError(e)
                            mContext.mContentViewBinding.loading = false
                            ToastHelper.showToast(
                                mContext,
                                mContext.resources.getString(R.string.something_went_wrong)
                            )
                        }
                    })
            } else {
                if (type == "add") {
                    mContext.mContentViewBinding.data!!.productList[position].cartQty++
                } else {
                    mContext.mContentViewBinding.data!!.productList[position].cartQty--
                }
                ApiConnection.updateDirectProduct(
                    mContext,
                    mContext.mContentViewBinding.data!!.productList[position].id!!,
                    mContext.mContentViewBinding.data!!.productList[position].cartQty.toString(),
                    mProductParamsJSON,
                    mFiles,
                    mRelatedProductsJSONArray,
                    mContext.mContentViewBinding.data!!.productList[position].cartItemId!!,
                    if(AppSharedPref.getQuoteId(mContext)==0) mContext.mContentViewBinding.data!!.productList[position].quoteId else AppSharedPref.getQuoteId(mContext)
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : ApiCustomCallback<AddToCartResponseModel>(mContext, true) {
                        override fun onNext(addToCartResponse: AddToCartResponseModel) {
                            super.onNext(addToCartResponse)
                            mContext.mContentViewBinding.loading = false
                            ToastHelper.showToast(mContext, addToCartResponse.message)
                            if(addToCartResponse.success){
                                mContext.updateCartCount(addToCartResponse.cartCount)
                                mContext.mContentViewBinding.productsRv.adapter!!.notifyItemChanged(
                                    position
                                )
                                mContext.updateBadge()
                            }

                            //mContext.callApi(true, position)

                        }

                        override fun onError(e: Throwable) {
                            super.onError(e)
                            mContext.mContentViewBinding.loading = false
                            ToastHelper.showToast(
                                mContext,
                                mContext.resources.getString(R.string.something_went_wrong)
                            )
                        }
                    })
            }
        } else {
            addToOfflineCart(entityId)
        }


        /* if (mContext.mContentViewBinding.data!!.productList[position].cartItemId == "" && mContext.mContentViewBinding.data!!.productList[position].cartQty==0 ) {
             ApiConnection.addToCart(mContext, entityId, "1", JSONObject(), null, JSONArray())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribeOn(Schedulers.io())
                 .subscribe(object : ApiCustomCallback<AddToCartResponseModel>(mContext, true) {
                     override fun onNext(addToCartResponse: AddToCartResponseModel) {
                         super.onNext(addToCartResponse)
                         mContext.mContentViewBinding.loading = false
                         ToastHelper.showToast(mContext, addToCartResponse.message)
                         mContext.updateCartCount(addToCartResponse.cartCount)
                         if (addToCartResponse.success) {
                             mContext.mContentViewBinding.data!!.productList[position].addedInfoCart = "true"
                             mContext.mContentViewBinding.data!!.productList[position].quoteId = addToCartResponse.quoteId
                             if (type == "add") {
                                 mContext.mContentViewBinding.data!!.productList[position].cartQty++
                             } else {
                                 mContext.mContentViewBinding.data!!.productList[position].cartQty--
                             }
                             mContext.mContentViewBinding.productsRv.adapter!!.notifyItemChanged(
                                 position
                             )

                            *//* if (addToCartResponse.quoteId != 0) {
                                    AppSharedPref.setQuoteId(mContext, addToCartResponse.quoteId)
                                    mContext.callApi(true, position)
                                }else{
                                    AppSharedPref.setQuoteId(mContext, addToCartResponse.quoteId)
                                    mContext.callApi(true, position)
                                }*//*
                            }
                        }

                        override fun onError(e: Throwable) {
                            super.onError(e)
                            mContext.mContentViewBinding.loading = false
                            ToastHelper.showToast(
                                mContext,
                                mContext.resources.getString(R.string.something_went_wrong)
                            )
                        }
                    })
            } else {
                if (type == "add") {
                    mContext.mContentViewBinding.data!!.productList[position].cartQty++
                } else {
                    mContext.mContentViewBinding.data!!.productList[position].cartQty--
                }
                ApiConnection.updateDirectProduct(
                    mContext,
                    mContext.mContentViewBinding.data!!.productList[position].id!!,
                    mContext.mContentViewBinding.data!!.productList[position].cartQty.toString(),
                    mProductParamsJSON,
                    mFiles,
                    mRelatedProductsJSONArray,
                    mContext.mContentViewBinding.data!!.productList[position].cartItemId!!,
                    mContext.mContentViewBinding.data!!.productList[position].quoteId
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : ApiCustomCallback<AddToCartResponseModel>(mContext, true) {
                        override fun onNext(addToCartResponse: AddToCartResponseModel) {
                            super.onNext(addToCartResponse)
                            mContext.mContentViewBinding.loading = false
                            ToastHelper.showToast(mContext, addToCartResponse.message)
                            mContext.updateCartCount(addToCartResponse.cartCount)
                            mContext.mContentViewBinding.productsRv.adapter!!.notifyItemChanged(
                                position
                            )
                            mContext.callApi(true, position)

                        }

                        override fun onError(e: Throwable) {
                            super.onError(e)
                            mContext.mContentViewBinding.loading = false
                            ToastHelper.showToast(
                                mContext,
                                mContext.resources.getString(R.string.something_went_wrong)
                            )
                        }
                    })
            }*/

    }

    private fun addToOfflineCart(entityId: String) {
        AlertDialogHelper.showNewCustomDialog(
            mContext as BaseActivity,
            mContext.getString(R.string.added_to_offline_cart),
            mContext.getString(R.string.offline_mode_add_to_cart_message),
            false,
            mContext.getString(R.string.ok),
            DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            })

        try {
            BaseActivity.mDataBaseHandler.addToCartOffline(
                entityId, "1", "", ""
            )
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ToastHelper.showToast(mContext, mContext.getString(R.string.something_went_wrong))
        }
    }
}