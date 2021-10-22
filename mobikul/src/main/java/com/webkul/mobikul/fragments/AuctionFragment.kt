package com.webkul.mobikul.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.FragmentAuctionBinding

import java.util.*

class AuctionFragment : Fragment() {
    lateinit var mContentViewBinding: FragmentAuctionBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_auction, container, false)
        return mContentViewBinding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        startInitialization()
    }

    private fun startInitialization() {
        mContentViewBinding.tabs.setupWithViewPager(mContentViewBinding.viewpager)
        setupViewPager()
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(fragmentManager)
        adapter.addFragment(AuctionDetailsFragment(), getString(R.string.auction_details))
        adapter.addFragment(AutoBiddingDetailsFragment(), getString(R.string.auction_bid_details))
        mContentViewBinding.viewpager.adapter = adapter
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

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }
    }
}