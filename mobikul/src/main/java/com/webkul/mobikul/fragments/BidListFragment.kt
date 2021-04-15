package com.webkul.mobikul.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.webkul.mobikul.R
import com.webkul.mobikul.adapters.BidListRvAdapter
import com.webkul.mobikul.databinding.FragmentBidListBinding
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_ACTIVITY_TITLE
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_BID_LIST_DATA
import com.webkul.mobikul.models.auction.BidList


/**
 * Created by vedesh.kumar on 3/1/17. @Webkul Software Pvt. Ltd
 */
class BidListFragment : Fragment() {
    private var mBinding: FragmentBidListBinding? = null
    private var mBidList: ArrayList<BidList>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bid_list, container, false)
        //        getActivity().setTitle(getArguments().getString(BUNDLE_KEY_ACTIVITY_TITLE));
        mBidList = arguments!!.getParcelableArrayList(BUNDLE_KEY_BID_LIST_DATA)
        return mBinding!!.getRoot()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (mBidList != null && mBidList!!.size > 0) {
            mBinding!!.bidRv.layoutManager = LinearLayoutManager(context)
            mBinding!!.bidRv.adapter = BidListRvAdapter(context, mBidList)
        } else {
            mBinding!!.emptyString.visibility = View.VISIBLE
        }
    }

    companion object {
        fun newInstance(title: String?, bidList: java.util.ArrayList<BidList>?): BidListFragment {
            val productReviewListFragment = BidListFragment()
            val args = Bundle()
            args.putString(BUNDLE_KEY_ACTIVITY_TITLE, title)
            args.putParcelableArrayList(BUNDLE_KEY_BID_LIST_DATA, bidList)
            productReviewListFragment.arguments = args
            return productReviewListFragment
        }
    }
}