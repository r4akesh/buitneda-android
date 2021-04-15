package com.webkul.mobikulmp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.fragments.BaseFragment
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.adapters.ProductCategoryRvAdapter
import com.webkul.mobikulmp.databinding.ProductCategoriesSelectBinding
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_CATEGORY_DATA
import com.webkul.mobikulmp.models.seller.CustomCategoryData
import java.util.*


/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.fragments
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 16/6/19
 */
class ProductCategorySelectFragment : BaseFragment() {

    lateinit var mBinding: ProductCategoriesSelectBinding
    lateinit var mCategoryList: ArrayList<CustomCategoryData>

    private var onDetachInterface: OnDetachInterface? = null


    fun setOnDetachInterface(onDetachInterface: OnDetachInterface) {
        this.onDetachInterface = onDetachInterface
    }

    interface OnDetachInterface {
        fun onFragmentDetached()
        fun onSelectCategories(categoryList: ArrayList<CustomCategoryData>)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.product_categories_select, container, false)
        setHasOptionsMenu(true)
        return mBinding.root
    }

    override fun onDetach() {
        super.onDetach()
        if (onDetachInterface != null) {
            onDetachInterface!!.onFragmentDetached()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpDirectoryCategory()
        mBinding.doneBtn.setOnClickListener {
            try {
                if (onDetachInterface != null) {
                    onDetachInterface!!.onSelectCategories(mCategoryList)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            fragmentManager!!.popBackStack()
        }
    }

    private fun setUpDirectoryCategory() {
        mCategoryList = arguments?.getParcelableArrayList(BUNDLE_CATEGORY_DATA)?: ArrayList()
        mBinding.categoryRv.adapter = ProductCategoryRvAdapter(mCategoryList)
        mBinding.categoryRv.isNestedScrollingEnabled = false
    }

    companion object {

        fun newInstance(categories: ArrayList<CustomCategoryData>): ProductCategorySelectFragment {
            val businessDirectorySelectFragment = ProductCategorySelectFragment()
            val args = Bundle()
            args.putParcelableArrayList(BUNDLE_CATEGORY_DATA, categories)
            businessDirectorySelectFragment.arguments = args
            return businessDirectorySelectFragment
        }
    }

}