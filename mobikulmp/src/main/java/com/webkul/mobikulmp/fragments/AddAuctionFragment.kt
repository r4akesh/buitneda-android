package com.webkul.mobikulmp.fragment

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.activities.HomeActivity
import com.webkul.mobikul.helpers.ApplicationConstants.DEFAULT_PAGE_COUNT
import com.webkul.mobikul.helpers.BundleKeysHelper
import com.webkul.mobikul.helpers.NetworkHelper
import com.webkul.mobikul.helpers.ToastHelper
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.models.checkout.CheckoutAddressInfoResponseModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback

import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.adapters.AuctionProductListRvAdapter
import com.webkul.mobikulmp.databinding.FragmentAddAuctionBinding
import com.webkul.mobikulmp.models.auction.GetProductListForAuctionData
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddAuctionFragment : androidx.fragment.app.Fragment(), View.OnClickListener {
    var mBinding: FragmentAddAuctionBinding? = null
    var mPageNumber: Int = DEFAULT_PAGE_COUNT
    var mGetProductListForAuctionData: GetProductListForAuctionData? = null
    var mProductListRvDecorations: DividerItemDecoration? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_auction, container, false)
        return mBinding!!.getRoot()
    }




    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callApi()
    }

    private fun callApi() {
        mBinding!!.setLoading(true)
        MpApiConnection.getProductListForAuction(context, mPageNumber++, mBinding!!.getSearchQuery())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<GetProductListForAuctionData>(context!!, false) {
                    override fun onNext(orderListResponseModel: GetProductListForAuctionData) {
                        super.onNext(orderListResponseModel)
                        mBinding!!.loading=false
                        if (orderListResponseModel != null && orderListResponseModel.success) {
                            if (mPageNumber == 2) {
                                mGetProductListForAuctionData = orderListResponseModel
                                startInitialization()
                            } else {
                                mGetProductListForAuctionData!!.productList.addAll(orderListResponseModel.productList)
                                mBinding!!.productsRv.adapter!!.notifyDataSetChanged()
                            }
                        }
                    }


                    override fun onError(e: Throwable) {
                        super.onError(e)
                        NetworkHelper.getErrorMessage( getActivity(),e)
                        mBinding!!.loading=false
//                                onErrorResponse(e)
                    }
                })

    }

    private fun startInitialization() {
        mBinding!!.productsRv.setAdapter(AuctionProductListRvAdapter(activity, mGetProductListForAuctionData!!.productList))
        if (mProductListRvDecorations == null) {
            mProductListRvDecorations = DividerItemDecoration(activity, LinearLayoutManager.VERTICAL)
            mBinding!!.productsRv.addItemDecoration(mProductListRvDecorations!!)
            mBinding!!.productsRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
               override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val lastCompletelyVisibleItemPosition: Int = (recyclerView.getLayoutManager() as LinearLayoutManager).findLastVisibleItemPosition()
                    if (mBinding!!.loading==false && mGetProductListForAuctionData!!.productList.size < mGetProductListForAuctionData!!.totalCount && lastCompletelyVisibleItemPosition == mGetProductListForAuctionData!!.productList.size - 1) {
                        callApi()
                    }
                }
            })
        }
    }

    override fun onClick(view: View) {
        if (view.id == R.id.search_btn) {
            if (mBinding!!.searchQuery == null || mBinding!!.searchQuery!!.trim().isEmpty()) {
                ToastHelper.showToast(context!! , getString(R.string.enter_the_product_name), Toast.LENGTH_LONG)
            } else {
                Utils.hideKeyboard(activity!!)
                mPageNumber = DEFAULT_PAGE_COUNT
                callApi()
            }
        }
    }

  override  fun onResume() {
        super.onResume()
        if (mProductListRvDecorations != null) {
            mPageNumber = DEFAULT_PAGE_COUNT
            callApi()
        }
    }
}