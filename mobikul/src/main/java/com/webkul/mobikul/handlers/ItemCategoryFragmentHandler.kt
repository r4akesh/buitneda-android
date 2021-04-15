package com.webkul.mobikul.handlers

import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.adapters.ItemCategoryListAdapter
import com.webkul.mobikul.adapters.ItemSubCategoryListAdapter
import com.webkul.mobikul.fragments.CategoryPageFragment
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikul.helpers.ToastHelper
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.homepage.Category
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
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

class ItemCategoryFragmentHandler(private val mContext: CategoryPageFragment,var adapter: ItemCategoryListAdapter) {

   /* fun onClickItem(data : SubCategory) {
        mContext.mBinding!!.category2.setAdapter(ItemSubCategoryListAdapter(mContext!!, data))        mHashIdentifier = Utils.getMd5String("getProductPageData" + AppSharedPref.getStoreId(this) + AppSharedPref.getCustomerToken(this) + AppSharedPref.getQuoteId(this) + AppSharedPref.getCurrencyCode(this) + mProductId)


    }*/

    fun onClickItem(data : Category, position:Int) {
        mContext.mBinding!!.category2.setAdapter(ItemSubCategoryListAdapter(mContext!!, data?.subCategories!!))
        adapter.selectedPosition=position
        adapter.notifyDataSetChanged()
    }

    fun onClickCategoryItem(category:Category, position: Int) {
        mContext.mBinding.loading = true
//        (mContext.context as BaseActivity).mHashIdentifier = Utils.getMd5String("getSubCategoryList" + AppSharedPref.getStoreId(mContext.context!!)+ category.id)

        ApiConnection.getSubCategoryList(mContext.context!!
                , category.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<Category>(mContext.context!!, false) {
                    override fun onNext(category: Category) {
                        super.onNext(category)
                        mContext.mBinding.loading = false
                        if (category.success) {
                            onSuccessfulResponse(category,position)
                        } else {
                            onFailureResponse(category)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mContext.mBinding.loading = false
                        onErrorResponse(e)
                    }
                })
    }

    private fun onSuccessfulResponse(data: Category, position: Int) {
        mContext.mBinding!!.category2.setAdapter(ItemSubCategoryListAdapter(mContext!!, data?.subCategories!!))
        adapter.selectedPosition=position
        adapter.notifyDataSetChanged()


    }
    private fun onFailureResponse(data: Category) {
        ToastHelper.showToast(mContext.context!!,data.message)
    }
    private fun onErrorResponse(error: Throwable) {
        error.message?.let { ToastHelper.showToast(mContext.context!!, it) }

    }
}