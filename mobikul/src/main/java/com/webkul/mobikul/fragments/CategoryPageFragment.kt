package com.webkul.mobikul.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.R
import com.webkul.mobikul.adapters.ItemCategoryListAdapter
import com.webkul.mobikul.adapters.ItemSubCategoryListAdapter
import com.webkul.mobikul.databinding.FragmentCategoryPageBinding
import com.webkul.mobikul.handlers.CategoryPageFragmentHandler
import com.webkul.mobikul.helpers.ToastHelper
import com.webkul.mobikul.models.homepage.Category
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CategoryPageFragment(var mcategories: ArrayList<Category>?) :
    FullScreenBottomSheetDialogFragment() {

    lateinit var mBinding: FragmentCategoryPageBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_category_page, container, false)
//        setHasOptionsMenu(true)
        return mBinding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        mBinding.data = mcategories?.get(0)
        mBinding.handler = CategoryPageFragmentHandler(this)
//        HomeActivity.mContentViewBinding.handler= HomeActivityHandler(this)
        mBinding.category1.adapter = ItemCategoryListAdapter(this, mcategories)
        if(mcategories!!.size>0){
            if (mcategories?.get(0) != null) {
                mBinding.category2.adapter =
                    ItemSubCategoryListAdapter(this, mcategories!![0].subCategories!!)

                onClickCategoryItem(mcategories!![0].id!!)
            }
        }

    }

    fun onClickCategoryItem(categoryId: String) {
        mBinding.loading = true
//        (context as BaseActivity).mHashIdentifier = Utils.getMd5String("getSubCategoryList" + AppSharedPref.getStoreId(context!!)+ categoryId)

        ApiConnection.getSubCategoryList(
            context!!
//                , BaseActivity.mDataBaseHandler.getETagFromDatabase((context as BaseActivity).mHashIdentifier)
            , categoryId
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiCustomCallback<Category>(context!!, false) {
                override fun onNext(category: Category) {
                    super.onNext(category)
                    mBinding.loading = false
                    if (category.success) {
                        onSuccessfulResponse(category)
                    } else {
                        onFailureResponse(category)
                    }
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    mBinding.loading = false
                    onErrorResponse(e)
                }
            })
    }

    private fun onSuccessfulResponse(data: Category) {
        mBinding.category2.adapter = ItemSubCategoryListAdapter(this, data.subCategories!!)


    }

    private fun onFailureResponse(data: Category) {
        ToastHelper.showToast(context!!, data.message)
    }

    private fun onErrorResponse(error: Throwable) {
        error.message?.let { ToastHelper.showToast(context!!, it) }

    }

    private fun addEmptyLayout() {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.add(
            R.id.ll_frag,
            EmptyFragment.newInstance(
                "empty_cart.json",
                getString(R.string.empty_product_catalog),
                getString(R.string.try_different_category_or_search_keyword_maybe),
                true,
                ""
            ),
            EmptyFragment::class.java.simpleName
        )
        fragmentTransaction.commitAllowingStateLoss()
    }

    private fun removeEmptyLayout() {
        val emptyFragment =
            childFragmentManager.findFragmentByTag(EmptyFragment::class.java.simpleName)
        if (emptyFragment != null) {
            childFragmentManager.beginTransaction().remove(emptyFragment).commitAllowingStateLoss()
        }
    }
}