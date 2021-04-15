package com.webkul.mobikul.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.R
import com.webkul.mobikul.adapters.AuctionBidListRvAdapter
import com.webkul.mobikul.databinding.FragmentAuctionDetailsBinding
import com.webkul.mobikul.databinding.FragmentAutoBiddingDetailsBinding
import com.webkul.mobikul.helpers.ApplicationConstants.DEFAULT_PAGE_COUNT
import com.webkul.mobikul.helpers.NetworkHelper
import com.webkul.mobikul.models.auction.GetAutoBidListResponseData
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * A simple [Fragment] subclass.
 * Use the [AutoBiddingDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AutoBiddingDetailsFragment : Fragment() {

    private var mBinding: FragmentAutoBiddingDetailsBinding? = null
    private var mPageNumber: Int = DEFAULT_PAGE_COUNT
    private var mGetAutoBidListResponseData: GetAutoBidListResponseData? = null

    var mAutoBidListRvDecorations: DividerItemDecoration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_auto_bidding_details, container, false)
        return mBinding!!.root
    }

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callApi()
    }
    
    private fun callApi() {
        mBinding!!.loading = true
        ApiConnection.getAutoBidList(context!!, mPageNumber++)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<GetAutoBidListResponseData>(context!!, false) {
                    override fun onNext(orderListResponseModel: GetAutoBidListResponseData) {
                        super.onNext(orderListResponseModel)
                        mBinding!!.loading = false
                        if (orderListResponseModel != null && orderListResponseModel.success) {
                            mGetAutoBidListResponseData = orderListResponseModel
                            mBinding!!.data = orderListResponseModel
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
        mBinding!!.autoBidListRv.setAdapter(mGetAutoBidListResponseData?.autoBidList?.let { AuctionBidListRvAdapter(context!!, it) })
        if (mAutoBidListRvDecorations == null) {
            mAutoBidListRvDecorations = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            mBinding!!.autoBidListRv.addItemDecoration(mAutoBidListRvDecorations!!)


//            mBinding!!.autoBidListRv.adapter = mGetAutoBidListResponseData?.autoBidList?.let { AuctionBidListRvAdapter(context!!, it) }
            mBinding!!.autoBidListRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val lastCompletelyVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    if (mBinding!!.loading != true && mGetAutoBidListResponseData!!.autoBidList?.size ?: 0 < mGetAutoBidListResponseData!!.totalCount && lastCompletelyVisibleItemPosition == (mGetAutoBidListResponseData!!.autoBidList?.size
                                    ?: 0) - 1) {

                        callApi()
                    }
                }
            })
        }
    }


}