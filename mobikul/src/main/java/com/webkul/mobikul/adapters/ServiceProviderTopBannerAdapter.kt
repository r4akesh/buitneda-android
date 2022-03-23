package com.webkul.mobikul.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.ItemHomeTopBannerServiceViewPagerBinding
import com.webkul.mobikul.fragments.HomeFragment
import com.webkul.mobikul.fragments.ServiceProviderFragment
import com.webkul.mobikul.handlers.HomePageBannerVpHandler
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikul.models.product.AnalysisModel
import com.webkul.mobikul.models.service.TopBannerModel


class ServiceProviderTopBannerAdapter(
    private val mContext: ServiceProviderFragment,
    private val mListData: ArrayList<TopBannerModel>?,
    private val carouselType: String
) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemViewPagerBannerBinding =
            DataBindingUtil.bind<ItemHomeTopBannerServiceViewPagerBinding>(
                LayoutInflater.from(mContext.context)
                    .inflate(R.layout.item_home_top_banner_service_view_pager, container, false)
            )
        itemViewPagerBannerBinding!!.data = mListData!![position]
      //  itemViewPagerBannerBinding.handler = HomePageBannerVpHandler(mContext)
        /*carouselType.let {
            itemViewPagerBannerBinding.analysisData = AnalysisModel(it, mListData[position].id)
        }*/
        itemViewPagerBannerBinding.executePendingBindings()
        container.addView(itemViewPagerBannerBinding.root)
        if (AppSharedPref.getStoreCode(mContext.context!!) == "ar")
            itemViewPagerBannerBinding.mainContainer.rotationY = 180.0f
        return itemViewPagerBannerBinding.root
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        return mListData!!.size
    }

    override fun isViewFromObject(view: View, p1: Any): Boolean {
        return view === p1
    }
}