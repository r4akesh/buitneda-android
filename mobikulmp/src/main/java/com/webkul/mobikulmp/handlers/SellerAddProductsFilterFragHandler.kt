package com.webkul.mobikulmp.handlers

import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikulmp.activities.SellerAddProductActivity
import com.webkul.mobikulmp.fragments.SellerAddProductCollectionFilterDialogFragment
import com.webkul.mobikulmp.fragments.SellerSelectProductsFragment
import org.json.JSONArray

/**
 * Created by vedesh.kumar on 24/3/17.
 */

class SellerAddProductsFilterFragHandler(private val mFragmentContext: SellerAddProductCollectionFilterDialogFragment) {

    fun applyFilters() {
        val filterJsonArray = JSONArray()
        val filterValuesJsonArray = JSONArray()
        val filterIdsJsonArray = JSONArray()
        for (noOfFilter in mFragmentContext.mFilterOption!!.indices) {
            if (mFragmentContext.mFilterOption!![noOfFilter].valueFrom != null && !mFragmentContext.mFilterOption?.get(noOfFilter)?.valueFrom?.isEmpty()!!) {
                if (mFragmentContext.mFilterOption!![noOfFilter].type == "textRange") {
                    filterValuesJsonArray.put(mFragmentContext.mFilterOption!![noOfFilter].valueFrom + "-" + mFragmentContext.mFilterOption!![noOfFilter].valueTo)
                } else {
                    filterValuesJsonArray.put(mFragmentContext.mFilterOption!![noOfFilter].valueFrom)
                }
                filterIdsJsonArray.put(mFragmentContext.mFilterOption!![noOfFilter].id)
            }
        }
        filterJsonArray.put(filterValuesJsonArray)
        filterJsonArray.put(filterIdsJsonArray)
        ((mFragmentContext.context as SellerAddProductActivity)
                .supportFragmentManager.findFragmentByTag(SellerSelectProductsFragment::class.java.simpleName) as SellerSelectProductsFragment).mFilterData = filterJsonArray
        ((mFragmentContext.context as SellerAddProductActivity)
                .supportFragmentManager.findFragmentByTag(SellerSelectProductsFragment::class.java.simpleName) as SellerSelectProductsFragment).isFirstCall = true
        ((mFragmentContext.context as SellerAddProductActivity)
                .supportFragmentManager.findFragmentByTag(SellerSelectProductsFragment::class.java.simpleName) as SellerSelectProductsFragment).mPageNumber = 0
        ((mFragmentContext.context as SellerAddProductActivity)
                .supportFragmentManager.findFragmentByTag(SellerSelectProductsFragment::class.java.simpleName) as SellerSelectProductsFragment).callApi()
        (mFragmentContext.context as BaseActivity).onBackPressed()
    }

    fun onClickOutsideFilter() {
        (mFragmentContext.context as BaseActivity).onBackPressed()
    }
}
