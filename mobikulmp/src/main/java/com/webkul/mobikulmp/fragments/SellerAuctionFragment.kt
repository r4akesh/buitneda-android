package com.webkul.mobikulmp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.webkul.mobikul.databinding.FragmentHomeBinding
import com.webkul.mobikul.fragments.DashboardAddressesFragment
import com.webkul.mobikul.fragments.DashboardRecentOrdersFragment
import com.webkul.mobikul.fragments.DashboardReviewsFragment
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.FragmentSellerAuctionBinding
import com.webkul.mobikulmp.fragment.AddAuctionFragment
import com.webkul.mobikulmp.fragment.AuctionProductListFragment
import java.util.*

class SellerAuctionFragment : Fragment() {
    lateinit var mContentViewBinding: FragmentSellerAuctionBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mContentViewBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_seller_auction, container, false)
        return mContentViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        startInitialization()
    }

    private fun startInitialization() {
        mContentViewBinding.tabs .setupWithViewPager(mContentViewBinding.viewpager)
        setupViewPager()
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(fragmentManager)
        adapter.addFragment(AddAuctionFragment(), getString(R.string.add_auction_tab_label))
        adapter.addFragment(AuctionProductListFragment(), getString(R.string.auction_product_list_tab_label))
        mContentViewBinding.viewpager.setAdapter(adapter)
    }

    private inner class ViewPagerAdapter(manager: FragmentManager?) : FragmentPagerAdapter(manager!!) {
        private val mFragmentList: MutableList<Fragment> = ArrayList()
        private val mFragmentTitleList: MutableList<String> = ArrayList()
       override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

      override  fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }
    }
}