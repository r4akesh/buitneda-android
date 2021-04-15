package com.webkul.mobikulmp.handlers

import com.webkul.mobikulmp.activities.SellerAddProductActivity

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.handlers
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 12/6/19
 */
class SelectedCategoriesHandler(val mContext: SellerAddProductActivity) {

    fun onClickDeleteItem(id: String, name: String) {
        if (mContext.mSelectedCategoriesAdapter!!.itemCount > 0) {
            mContext.mSelectedCategoriesAdapter!!.removeItem(id, name)
            mContext.mSellerAddProductResponseData?.productData?.categoryIds?.remove(id)

            for (noOfCategories in mContext.mCategoryList.indices) {
                if (mContext.mCategoryList[noOfCategories].id == id.toInt()) {
                    mContext.mCategoryList[noOfCategories].isChecked = false
                }
                if (mContext.mCategoryList[noOfCategories].childrenData!!.size != 0) {
                    for (noOfSubcategory in 0 until mContext.mCategoryList[noOfCategories].childrenData!!.size) {
                        if (mContext.mCategoryList[noOfCategories].childrenData!![noOfSubcategory].id == id.toInt()) {
                            mContext.mCategoryList[noOfCategories].childrenData!![noOfSubcategory].isChecked = false
                        }
                    }
                }
            }

        }
    }

}

