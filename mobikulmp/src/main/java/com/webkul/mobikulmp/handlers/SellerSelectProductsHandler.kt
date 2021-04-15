package com.webkul.mobikulmp.handlers

import com.webkul.mobikulmp.activities.SellerAddProductActivity
import com.webkul.mobikulmp.fragments.SellerSelectProductsFragment
import java.util.*

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.handlers
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 3/9/19
 */
class SellerSelectProductsHandler(private val mFragmentContext: SellerSelectProductsFragment) {

    fun onClickAddProduct() {
        val selectProductIds = ArrayList<String>()
        for (noOfProducts in 0 until mFragmentContext.mCollectionData.productCollectionData!!.size) {
            if (mFragmentContext.mCollectionData.productCollectionData!![noOfProducts].isSelected)
                mFragmentContext.mCollectionData.productCollectionData!![noOfProducts].entityId?.let { selectProductIds.add(it) }
        }
        when (mFragmentContext.mCollectionType) {
            "related" -> (mFragmentContext.context as SellerAddProductActivity).mSellerAddProductResponseData?.productData?.related = selectProductIds
            "upsell" -> (mFragmentContext.context as SellerAddProductActivity).mSellerAddProductResponseData?.productData?.upsell = selectProductIds
            "crosssell" -> (mFragmentContext.context as SellerAddProductActivity).mSellerAddProductResponseData?.productData?.crossSell = selectProductIds
        }
        mFragmentContext.activity?.supportFragmentManager?.popBackStack()
    }
}
