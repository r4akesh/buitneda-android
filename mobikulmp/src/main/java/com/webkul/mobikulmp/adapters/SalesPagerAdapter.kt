package com.webkul.mobikulmp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.ItemSellerSalesPagerBinding

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
class SalesPagerAdapter(private val mContext: Context, private var mLocationReportImage: String, private var mSalesGraphImage: String, private val mCategoryChartImage: String) : PagerAdapter() {

    private val tabTitles: Array<String> = arrayOf(mContext.getString(R.string.sales_map), mContext.getString(R.string.sales_graph), mContext.getString(R.string.top_selling_category))

    override fun getCount(): Int {
        return tabTitles.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemSellerSalesPagerBinding = DataBindingUtil.bind<ItemSellerSalesPagerBinding>(LayoutInflater.from(mContext).inflate(R.layout.item_seller_sales_pager, container, false))
        itemSellerSalesPagerBinding?.root?.tag = position


        when (position) {
            0 -> {
                itemSellerSalesPagerBinding?.imageUrl = mLocationReportImage
            }
            1 -> {
                itemSellerSalesPagerBinding?.imageUrl = mSalesGraphImage
            }
            2 -> {
                itemSellerSalesPagerBinding?.imageUrl = mCategoryChartImage
            }
        }
        itemSellerSalesPagerBinding!!.executePendingBindings()
        container.addView(itemSellerSalesPagerBinding.root)
        return itemSellerSalesPagerBinding.root
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    fun setLocationReportImage(imageUrl: String) {
        mLocationReportImage = imageUrl
        notifyDataSetChanged()
    }

    fun setSalesGraphImage(imageUrl: String) {
        mSalesGraphImage = imageUrl
        notifyDataSetChanged()
    }


}
