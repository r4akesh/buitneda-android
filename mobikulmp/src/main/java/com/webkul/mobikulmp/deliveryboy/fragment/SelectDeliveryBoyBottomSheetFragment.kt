


package com.webkul.mobikulmp.deliveryboy.fragment


import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.fragments.FullScreenBottomSheetDialogFragment
import com.webkul.mobikul.helpers.AlertDialogHelper
import com.webkul.mobikul.helpers.NetworkHelper
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.FragmentSelectDeliveryBoyBinding
import com.webkul.mobikulmp.deliveryboy.adapters.DeliveryBoyListRvAdapter
import com.webkul.mobikulmp.deliveryboy.handlers.DeliveryBoyListRvHandler
import com.webkul.mobikulmp.deliveryboy.models.DeliveryBoyList
import com.webkul.mobikulmp.deliveryboy.models.GetDeliveryBoyListResponseData
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_DELIVERY_BOY_ID
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_INCREMENT_ID
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_SELLER_ID
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SelectDeliveryBoyBottomSheetFragment : FullScreenBottomSheetDialogFragment() {

    internal var onSelectDeliveryBoy: OnSelectDeliveryBoy? = null

    fun setOnSelectInterface(onSelectDeliveryBoy: OnSelectDeliveryBoy) {
        this.onSelectDeliveryBoy = onSelectDeliveryBoy
    }

    interface OnSelectDeliveryBoy {
        fun onSelectDeliveryBoy(data: DeliveryBoyList)
    }

    lateinit var mContentViewBinding: FragmentSelectDeliveryBoyBinding
    private var mPageNumber = 1
    private var sellerId: String? = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_delivery_boy, container, false)
        mContentViewBinding.handler = DeliveryBoyListRvHandler(this)
        return mContentViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sellerId = arguments?.getString(BUNDLE_KEY_SELLER_ID)
        callApi()
    }

    fun callApi() {
        mContentViewBinding.loading = true
        MpApiConnection.getDeliveryBoyList(context!!,
                mPageNumber++,
                "assignment",
                sellerId
        ).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<GetDeliveryBoyListResponseData>(context!!, false) {
                    override fun onNext(t: GetDeliveryBoyListResponseData) {
                        super.onNext(t)
                        mContentViewBinding.loading = false
                        if (t.success) {
                            onSuccessfulResponse(t)
                        } else {
                            onFailureResponse(t)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mContentViewBinding.loading = false
                        onErrorResponse(e)
                    }
                })
    }

    fun onSuccessfulResponse(response: GetDeliveryBoyListResponseData) {

        if (mPageNumber == 2) {
            mContentViewBinding.data = response
            Log.d("Tag","onSuccessfulResponse"+response.deliveryboyList)
            Log.d("Tag","onSuccessfulResponse"+response.totalCount)
            if (response.deliveryboyList.isNullOrEmpty()) {
                mContentViewBinding.emptyView.visibility = View.VISIBLE
            } else {
                mContentViewBinding.emptyView.visibility = View.GONE
                setupDeliveryBoyListRv()
            }
        } else {
            response.deliveryboyList?.let { mContentViewBinding.data!!.deliveryboyList?.addAll(it) }
            mContentViewBinding.deliveryBoyListRv.adapter!!.notifyDataSetChanged()
        }
    }

    private fun setupDeliveryBoyListRv() {
        mContentViewBinding.deliveryBoyListRv.adapter = mContentViewBinding.data!!.deliveryboyList?.let { DeliveryBoyListRvAdapter(this, it, arguments?.getString(BUNDLE_DELIVERY_BOY_ID)) }
        mContentViewBinding.deliveryBoyListRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val lastCompletelyVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (!mContentViewBinding.loading!! && mContentViewBinding.data!!.deliveryboyList!!.size < mContentViewBinding.data!!.totalCount
                        && lastCompletelyVisibleItemPosition > mContentViewBinding.data!!.deliveryboyList!!.size - 4) {
                    callApi()
                }
            }
        })
    }

    fun onFailureResponse(response: GetDeliveryBoyListResponseData) {
        AlertDialogHelper.showNewCustomDialog(
                activity as BaseActivity,
                getString(R.string.error),
                response.message,
                false,
                getString(R.string.ok),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    if (mContentViewBinding.data == null) {
                        activity?.supportFragmentManager?.popBackStack()
                    }
                }
                , ""
                , null)
    }


    fun onErrorResponse(error: Throwable) {
        AlertDialogHelper.showNewCustomDialog(
                activity as BaseActivity,
                getString(R.string.error),
                NetworkHelper.getErrorMessage(context, error),
                false,
                getString(R.string.try_again),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    callApi()
                }
                , getString(R.string.dismiss)
                , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
            activity?.supportFragmentManager?.popBackStack()

        })
    }

    companion object {
        fun newInstance(deliveryboyId: String, sellerId: String, incrementId: String): SelectDeliveryBoyBottomSheetFragment {
            val fragment = SelectDeliveryBoyBottomSheetFragment()
            val args = Bundle()
            args.putString(BUNDLE_KEY_SELLER_ID, sellerId)
            args.putString(BUNDLE_DELIVERY_BOY_ID, deliveryboyId)
            args.putString(BUNDLE_INCREMENT_ID, incrementId)
            fragment.arguments = args
            return fragment
        }
    }
}