package com.webkul.mobikulmp.fragment

import android.os.Bundle
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.view.GestureDetectorCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.helpers.ApplicationConstants.DEFAULT_PAGE_COUNT
import com.webkul.mobikul.helpers.NetworkHelper
import com.webkul.mobikul.helpers.ToastHelper
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.adapters.AuctionListRvAdapter
import com.webkul.mobikulmp.databinding.FragmentAuctionProductListBinding
import com.webkul.mobikulmp.models.auction.GetAuctionListData
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray

class AuctionProductListFragment : Fragment(), View.OnClickListener, ActionMode.Callback, RecyclerView.OnItemTouchListener {
    var mBinding: FragmentAuctionProductListBinding? = null
    var mAuctionListRvDecorations: DividerItemDecoration? = null
    var mSelectedItemPositions: List<Int>? = null
    private var mPageNumber: Int = DEFAULT_PAGE_COUNT
    private var mGetAuctionListData: GetAuctionListData? = null
    private var mAuctionListRvAdapter: AuctionListRvAdapter? = null
    private var actionMode: ActionMode? = null
    private var gestureDetector: GestureDetectorCompat? = null

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_auction_product_list, container, false)
        return mBinding!!.root
    }

  override  fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callApi()
    }

    private fun callApi() {
        mBinding!!.loading = true
        MpApiConnection.getAuctionList(getContext(), mPageNumber++, mBinding!!.searchQuery)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<GetAuctionListData>(context!!, false) {
                    override fun onNext(orderListResponseModel: GetAuctionListData) {
                        super.onNext(orderListResponseModel)
                        mBinding!!.loading=false
                        if (orderListResponseModel != null && orderListResponseModel.success) {
                            mBinding!!.searchBtn.setOnClickListener(this@AuctionProductListFragment)
                            if (mPageNumber == 2) {
                                mGetAuctionListData = orderListResponseModel
                                startInitialization()
                            } else {
                                mGetAuctionListData!!.getProductList().addAll(orderListResponseModel.productList)
                                mBinding!!.auctionListRv.adapter!!.notifyDataSetChanged()
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
        mAuctionListRvAdapter = AuctionListRvAdapter(context!!, mGetAuctionListData!!.getProductList())
        mBinding!!.auctionListRv.adapter = mAuctionListRvAdapter
        if (mAuctionListRvDecorations == null) {
            mAuctionListRvDecorations = DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL)
            mBinding!!.auctionListRv.addItemDecoration(mAuctionListRvDecorations!!)
            mBinding!!.auctionListRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
              override  fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val lastCompletelyVisibleItemPosition: Int = (recyclerView.getLayoutManager() as LinearLayoutManager).findLastVisibleItemPosition()
                    if (mBinding!!.loading==false && mGetAuctionListData!!.productList.size < mGetAuctionListData!!.totalCount && lastCompletelyVisibleItemPosition == mGetAuctionListData!!.productList.size - 1) {
                        callApi()
                    }
                }
            })
            mBinding!!.auctionListRv.addOnItemTouchListener(this)
            gestureDetector = GestureDetectorCompat(getContext(), RecyclerViewOnGestureListener())
        }
    }

    override fun onClick(view: View) {
        if (view != null) {
            if (view.id == R.id.search_btn) {
                if (mBinding!!.searchQuery == null || mBinding!!.searchQuery!!.trim().isEmpty()) {
                    ToastHelper.showToast(context!!, getString(R.string.enter_the_product_name), Toast.LENGTH_LONG)
                } else {
                    Utils.hideKeyboard(activity!!)
                    mPageNumber = DEFAULT_PAGE_COUNT
                    callApi()
                }
            } else if (view.id == R.id.main_container) {
                val idx = mBinding!!.auctionListRv.getChildPosition(view)
                if (actionMode != null) {
                    myToggleSelection(idx)
                }
            }
        }
    }

    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        val inflater = mode.menuInflater
        inflater.inflate(R.menu.disable_menu, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        return false
    }

    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_disable -> {
                mSelectedItemPositions = mAuctionListRvAdapter!!.getSelectedItems()
                val selectedProductsIds = JSONArray()
                var noOfItem = mSelectedItemPositions!!.size - 1
                while (noOfItem >= 0) {
                    selectedProductsIds.put(mAuctionListRvAdapter!!.getItem(mSelectedItemPositions!![noOfItem]).auctionId)
                    noOfItem--
                }
                actionMode!!.finish()
                MpApiConnection.massDisableAuction(context, selectedProductsIds)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(object : ApiCustomCallback<BaseModel>(context!!, false) {
                            override fun onNext(orderListResponseModel: BaseModel) {
                                super.onNext(orderListResponseModel)
                                mBinding!!.loading=false
                                if (orderListResponseModel != null && orderListResponseModel.success) {
                                    mBinding!!.searchBtn.setOnClickListener(this@AuctionProductListFragment)
                                    ToastHelper.showToast(context, orderListResponseModel.message, Toast.LENGTH_LONG)

                                    if (mPageNumber == 2) {
                                        mPageNumber = DEFAULT_PAGE_COUNT
                                        callApi()
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

                true
            }
            else -> false
        }
    }

    override fun onDestroyActionMode(mode: ActionMode) {
        actionMode = null
        mAuctionListRvAdapter!!.clearSelections()
    }


    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        gestureDetector!!.onTouchEvent(e)
        return false
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
    private fun myToggleSelection(idx: Int) {
        mAuctionListRvAdapter!!.toggleSelection(idx)
        val title: String = getString(R.string.x_selected_count, mAuctionListRvAdapter!!.selectedItemCount)
        if (title == getString(R.string.x_selected_count, 0)) {
            actionMode!!.finish()
        } else {
            actionMode!!.title = title
        }
    }

   override fun onResume() {
        super.onResume()
        mPageNumber = DEFAULT_PAGE_COUNT
        callApi()
    }

    private inner class RecyclerViewOnGestureListener : SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            val view = mBinding!!.auctionListRv.findChildViewUnder(e.x, e.y)
            onClick(view!!)
            return super.onSingleTapConfirmed(e)
        }

        override fun onLongPress(e: MotionEvent) {
            val view = mBinding!!.auctionListRv.findChildViewUnder(e.x, e.y)
            if (actionMode != null) {
                return
            }
            // Start the CAB using the ActionMode.Callback defined above
            val idx = mBinding!!.auctionListRv.getChildPosition(view!!)
            if (idx > -1) {
                actionMode = activity!!.startActionMode(this@AuctionProductListFragment)
                myToggleSelection(idx)
                for (i in 0 until mGetAuctionListData!!.getProductList().size) {
                    mGetAuctionListData!!.getProductList().get(i).isSelectionModeOn=true
                }
            }
            super.onLongPress(e)
        }
    }
}