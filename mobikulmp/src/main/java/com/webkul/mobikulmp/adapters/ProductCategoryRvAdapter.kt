package com.webkul.mobikulmp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.databinding.DataBindingUtil
import com.bignerdranch.expandablerecyclerview.ChildViewHolder
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter
import com.bignerdranch.expandablerecyclerview.ParentViewHolder
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.ItemProductCategoryBinding
import com.webkul.mobikulmp.databinding.ItemProductCategoryChildBinding
import com.webkul.mobikulmp.models.seller.CustomCategoryData
import java.util.*

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.handler.pricerule
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 27/4/19
 */
class ProductCategoryRvAdapter(categoriesData: ArrayList<CustomCategoryData>) : ExpandableRecyclerAdapter<CustomCategoryData, CustomCategoryData, ProductCategoryRvAdapter.CategoryParentViewHolder, ProductCategoryRvAdapter.CategoryChildViewHolder>(categoriesData) {
    private val mCategoriesData: List<CustomCategoryData>

    init {
        mCategoriesData = categoriesData
    }

    override fun onCreateParentViewHolder(parentViewGroup: ViewGroup, viewType: Int): CategoryParentViewHolder {
        val inflater = LayoutInflater.from(parentViewGroup.rootView.context)
        val parentView = inflater.inflate(R.layout.item_product_category, parentViewGroup, false)
        return CategoryParentViewHolder(parentView)
    }

    override fun onCreateChildViewHolder(childViewGroup: ViewGroup, viewType: Int): CategoryChildViewHolder {
        val inflater = LayoutInflater.from(childViewGroup.rootView.context)
        val childView = inflater.inflate(R.layout.item_product_category_child, childViewGroup, false)
        return CategoryChildViewHolder(childView)
    }

    override fun onBindParentViewHolder(parentViewHolder: CategoryParentViewHolder, parentPosition: Int, parent: CustomCategoryData) {
        val categoriesData = mCategoriesData[parentPosition]
        if (AppSharedPref.getStoreCode(parentViewHolder.mBinding!!.root.context) == "ar") {
            parentViewHolder.mBinding.itemParentLayout.setPadding(0, 0, 15 * categoriesData.level, 0)
        } else {
            parentViewHolder.mBinding.itemParentLayout.setPadding(15 * categoriesData.level, 0, 0, 0)

        }
        parentViewHolder.mBinding.data = categoriesData

        if (parentViewHolder.isExpanded) {
            if (AppSharedPref.getStoreCode(parentViewHolder.mBinding.root.context) == "ar") {
                parentViewHolder.mBinding.navDrawerParentTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_down_arrow_grey_wrapper, 0, 0, 0)
            } else {
                parentViewHolder.mBinding.navDrawerParentTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down_arrow_grey_wrapper, 0)
            }
        } else if (categoriesData.childList != null && categoriesData.childList!!.isNotEmpty()) {
            if (AppSharedPref.getStoreCode(parentViewHolder.mBinding.root.context) == "ar") {
                parentViewHolder.mBinding.navDrawerParentTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_left_arrow_grey_wrapper, 0, 0, 0)
            } else {
                parentViewHolder.mBinding.navDrawerParentTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_right_arrow_grey_wrapper, 0)
            }
        }
        parentViewHolder.mBinding.executePendingBindings()
    }

    override fun onBindChildViewHolder(childViewHolder: CategoryChildViewHolder, parentPosition: Int, childPosition: Int, subCategoryData: CustomCategoryData) {
        childViewHolder.mBinding!!.data = subCategoryData
        if (AppSharedPref.getStoreCode(childViewHolder.mBinding.root.context) == "ar") {
            childViewHolder.mBinding.itemLayout.setPadding(0, 0, 15 * subCategoryData.level, 0)
        } else {
            childViewHolder.mBinding.itemLayout.setPadding(15 * subCategoryData.level, 0, 0, 0)

        }
        childViewHolder.mBinding.executePendingBindings()
    }

    class CategoryParentViewHolder(itemView: View) : ParentViewHolder<CustomCategoryData, CustomCategoryData>(itemView) {
        val mBinding: ItemProductCategoryBinding? = DataBindingUtil.bind(itemView)

        @UiThread
        override fun onClick(v: View?) {
            super.onClick(v)

            if (mBinding!!.data != null && mBinding.data!!.childList != null && mBinding.data!!.childList!!.isNotEmpty()) {
                if (isExpanded) {
                    if (AppSharedPref.getStoreCode(v!!.context) == "ar") {
                        mBinding.navDrawerParentTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_down_arrow_grey_wrapper, 0, 0, 0)
                    } else {
                        mBinding.navDrawerParentTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, com.webkul.mobikul.R.drawable.ic_down_arrow_grey_wrapper, 0)
                    }
                } else {
                    if (AppSharedPref.getStoreCode(v!!.context) == "ar") {
                        mBinding.navDrawerParentTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_left_arrow_grey_wrapper, 0, 0, 0)
                    } else {
                        mBinding.navDrawerParentTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_right_arrow_grey_wrapper, 0)
                    }
                }
            }
        }
    }

    class CategoryChildViewHolder(itemView: View) : ChildViewHolder<CustomCategoryData>(itemView) {
        val mBinding: ItemProductCategoryChildBinding? = DataBindingUtil.bind(itemView)

    }

}