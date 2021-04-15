package com.webkul.mobikulmp.fragments

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.models.seller
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 04/7/19
 */


class SellerAddProductsFilterFragHandler(private val mFragmentContext: SellerAddProductCollectionFilterDialogFragment) {
/*
    fun applyFilters() {
        val filterJsonArray = JSONArray()
        val filterValuesJsonArray = JSONArray()
        val filterIdsJsonArray = JSONArray()
        for (noOfFilter in mFragmentContext.mFilterOption!!.indices) {
            if (mFragmentContext.mFilterOption!![noOfFilter].valueFrom != null && !mFragmentContext.mFilterOption!![noOfFilter].valueFrom!!.isEmpty()) {
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
    }*/
}
