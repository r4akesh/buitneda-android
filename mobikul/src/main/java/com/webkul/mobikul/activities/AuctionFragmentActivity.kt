package com.webkul.mobikul.activities

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.FragmentAuctionBinding
import com.webkul.mobikul.fragments.AuctionDetailsFragment
import com.webkul.mobikul.fragments.AutoBiddingDetailsFragment
import java.util.*

class AuctionFragmentActivity : BaseActivity() {
    private var mBinding: FragmentAuctionBinding? = null
    private val TAG = "AuctionFragmentActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.fragment_auction)
        //        setActionbarTitle(getString(R.string.aution_activity_title));
        startInitialization()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }

    private fun startInitialization() {
      //  setUpCustomToolbar("activity","")
        mBinding!!.tabs.setupWithViewPager(mBinding!!.viewpager)
        setupViewPager()
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(AuctionDetailsFragment(), getString(R.string.auction_details))
        adapter.addFragment(AutoBiddingDetailsFragment(), getString(R.string.auction_bid_details))
        mBinding!!.viewpager.adapter = adapter
    }

    private inner class ViewPagerAdapter(manager: FragmentManager?) :
        FragmentPagerAdapter(manager!!) {
        private val mFragmentList: MutableList<Fragment> = ArrayList()
        private val mFragmentTitleList: MutableList<String> = ArrayList()
        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }
    }
}