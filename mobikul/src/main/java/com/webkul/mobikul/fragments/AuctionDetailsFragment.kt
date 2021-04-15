package com.webkul.mobikul.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.webkul.mobikul.R
import com.webkul.mobikul.adapters.AuctionBidListRvAdapter
import com.webkul.mobikul.databinding.FragmentAuctionDetailsBinding
import com.webkul.mobikul.helpers.ApplicationConstants.DEFAULT_PAGE_COUNT
import com.webkul.mobikul.helpers.NetworkHelper
import com.webkul.mobikul.models.auction.GetAuctionBidListResponseData
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

// TODO: Rename parameter arguments, choose names that match


/**
 * A simple [Fragment] subclass.
 * Use the [AuctionDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AuctionDetailsFragment : Fragment() {


    var mAuctionBidListRvDecorations: DividerItemDecoration? = null
    private var mBinding: FragmentAuctionDetailsBinding? = null
    private var mPageNumber: Int =0// DEFAULT_PAGE_COUNT
    private var mGetAuctionBidListResponseData: GetAuctionBidListResponseData? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_auction_details, container, false)
        return mBinding!!.root
    }

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callApi()
    }

    private fun callApi() {
        mBinding!!.loading = true
        ApiConnection.getAuctionBidList(context!!, mPageNumber)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<GetAuctionBidListResponseData>(context!!, false) {
                    override fun onNext(orderListResponseModel: GetAuctionBidListResponseData) {
                        super.onNext(orderListResponseModel)
                        mBinding!!.loading = false
                        if (orderListResponseModel != null && orderListResponseModel.success) {
                            mGetAuctionBidListResponseData = orderListResponseModel
                            mBinding!!.data=orderListResponseModel
                            startInitialization()

                        }
                    }


                    override fun onError(e: Throwable) {
                        super.onError(e)
                        NetworkHelper.getErrorMessage(getActivity(), e)
                        mBinding!!.loading = false
//                                onErrorResponse(e)
                    }
                })

    }

    private fun startInitialization() {
        mBinding!!.auctionBidListRv.adapter = mGetAuctionBidListResponseData?.auctonBidList?.let { AuctionBidListRvAdapter(context!!, it) }
        if (mAuctionBidListRvDecorations == null) {
            mAuctionBidListRvDecorations = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            mBinding!!.auctionBidListRv.addItemDecoration(mAuctionBidListRvDecorations!!)

//            mBinding!!.auctionBidListRv.adapter = mGetAuctionBidListResponseData?.auctonBidList?.let { AuctionBidListRvAdapter(context!!, it) }
            mBinding!!.auctionBidListRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val lastCompletelyVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    if (mBinding!!.loading != true && mGetAuctionBidListResponseData!!.auctonBidList?.size ?: 0 < mGetAuctionBidListResponseData!!.totalCount && lastCompletelyVisibleItemPosition == (mGetAuctionBidListResponseData!!.auctonBidList?.size
                                    ?: 0) - 1) {

                        callApi()
                    }
                }
            })

        }
    }

}