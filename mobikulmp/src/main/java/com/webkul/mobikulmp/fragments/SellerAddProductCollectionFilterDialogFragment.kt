package com.webkul.mobikulmp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.DialogFragmentSellerAddProductCollectionFilterBinding
import com.webkul.mobikulmp.models.seller.FilterOption
import java.util.*

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

class SellerAddProductCollectionFilterDialogFragment : DialogFragment() {

    lateinit var mBinding: DialogFragmentSellerAddProductCollectionFilterBinding
    var mFilterOption: ArrayList<FilterOption>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_seller_add_product_collection_filter, container, false)
        initializeFilters()
        return mBinding.root
    }

    private fun initializeFilters() {
        /* mBinding.setHandler(SellerAddProductsFilterFragHandler(this))
         mFilterOption = ((context as SellerAddProductActivity)
                 .supportFragmentManager.findFragmentByTag(SellerSelectProductsFragment::class.java.simpleName) as SellerSelectProductsFragment).mCollectionData.filterOption
         for (noOfFilters in mFilterOption!!.indices) {
             when (mFilterOption!![noOfFilters].type) {
                 "textRange" -> {
                     val itemFilterOptionTextRangeBinding = ItemFilterOptionTextRangeBinding.inflate(activity!!.layoutInflater, mBinding.filterOptionsContainer, true)
                     itemFilterOptionTextRangeBinding.setData(mFilterOption!![noOfFilters])
                 }
                 "text" -> {
                     val itemFilterOptionTextBinding = ItemFilterOptionTextBinding.inflate(activity!!.layoutInflater, mBinding.filterOptionsContainer, true)
                     itemFilterOptionTextBinding.setData(mFilterOption!![noOfFilters])
                 }
                 "select" -> {
                     val itemFilterOptionSelectBinding = ItemFilterOptionSelectBinding.inflate(activity!!.layoutInflater, mBinding.filterOptionsContainer, true)
                     itemFilterOptionSelectBinding.setData(mFilterOption!![noOfFilters])
                     val optionLabels = ArrayList<String>()
                     optionLabels.add("")
                     for (noOfCountries in 0 until mFilterOption!![noOfFilters].options.size) {
                         optionLabels.add(mFilterOption!![noOfFilters].options[noOfCountries].label)
                     }
                     itemFilterOptionSelectBinding.optionSpinner.setAdapter(ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item`, optionLabels))
                     itemFilterOptionSelectBinding.optionSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener() {
                         fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                             if (position == 0) {
                                 mFilterOption!![noOfFilters].valueFrom = ""
                             } else {
                                 mFilterOption!![noOfFilters].valueFrom = mFilterOption!![noOfFilters].options[position - 1].value
                             }
                         }

                         fun onNothingSelected(parent: AdapterView<*>) {

                         }
                     })
                     itemFilterOptionSelectBinding.optionSpinner.setSelection(mFilterOption!![noOfFilters].selectedOptionData)
                 }
             }
         }*/
    }
}