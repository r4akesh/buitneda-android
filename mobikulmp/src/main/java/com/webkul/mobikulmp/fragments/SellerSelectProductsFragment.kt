package com.webkul.mobikulmp.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.fragments.BaseFragment
import com.webkul.mobikul.helpers.AlertDialogHelper
import com.webkul.mobikul.helpers.NetworkHelper
import com.webkul.mobikul.helpers.ToastHelper.Companion.showToast
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.SellerAddProductActivity
import com.webkul.mobikulmp.adapters.AddProductProductsListRvAdapter
import com.webkul.mobikulmp.databinding.FragmentSellerSelectProductsBinding
import com.webkul.mobikulmp.handlers.SellerSelectProductsHandler
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_COLLECTION_TYPE
import com.webkul.mobikulmp.models.seller.AddProductFieldProductCollectionData
import com.webkul.mobikulmp.models.seller.ProductCollectionData
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import retrofit2.HttpException
import java.util.*

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.fragments
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 22/7/19
 */

class SellerSelectProductsFragment : BaseFragment(), View.OnClickListener {

    lateinit var mContentViewBinding: FragmentSellerSelectProductsBinding
    var mPageNumber = 1
    var isFirstCall = true
    var mSortData: JSONArray? = null
    var mFilterData: JSONArray? = null
    var mCollectionType: String? = ""
    lateinit var mCollectionData: AddProductFieldProductCollectionData

    private var onDetachInterface: OnDetachInterface? = null


    fun setOnDetachInterface(onDetachInterface: OnDetachInterface) {
        this.onDetachInterface = onDetachInterface
    }

    interface OnDetachInterface {
        fun onFragmentDetached()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_seller_select_products, container, false)
        return mContentViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCollectionType = arguments!!.getString(BUNDLE_KEY_COLLECTION_TYPE)
        mContentViewBinding.submitBtn.setOnClickListener(this)
        mContentViewBinding.filterBtn.setOnClickListener(this)
        setFragmentTitle()
        callApi()
    }

    private fun setFragmentTitle() {
        when (mCollectionType) {
            "related" -> (context as BaseActivity).supportActionBar?.title = getString(R.string.related_product_title)
            "upsell" -> (context as BaseActivity).supportActionBar?.title = getString(R.string.upsell_product)
            "crosssell" -> (context as BaseActivity).supportActionBar?.title = getString(R.string.cross_sell_product)
        }
    }

    fun callApi() {
        mContentViewBinding.loading = true
        MpApiConnection.getRelatedProductData(context!!, mCollectionType!!, (context as SellerAddProductActivity).mProductId, mPageNumber++, mSortData, mFilterData)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<AddProductFieldProductCollectionData>(context!!, true) {
                    override fun onNext(addProductFieldProductCollectionData: AddProductFieldProductCollectionData) {
                        super.onNext(addProductFieldProductCollectionData)
                        mContentViewBinding.loading = false
                        if (addProductFieldProductCollectionData.success) {
                            addProductFieldProductCollectionData.productCollectionData?.let { updateSelectedData(it) }
                            if (isFirstCall) {
                                isFirstCall = false
                                mCollectionData = addProductFieldProductCollectionData
                                mContentViewBinding.data = mCollectionData
                                mContentViewBinding.handler = SellerSelectProductsHandler(this@SellerSelectProductsFragment)
                                initProductRv()
                                if (mCollectionData.totalCount == 0) {
                                    mContentViewBinding.emptyLayout.visibility = View.VISIBLE
                                }
                            } else {
                                mCollectionData.productCollectionData?.addAll(addProductFieldProductCollectionData.productCollectionData!!)
                                mContentViewBinding.productsRv.adapter?.notifyDataSetChanged()
                            }


                        } else {
                            onFailureResponse(addProductFieldProductCollectionData)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        onErrorResponse(e)
                    }
                })
    }

    private fun onErrorResponse(error: Throwable) {
        mContentViewBinding.loading = false
        if ((!NetworkHelper.isNetworkAvailable(context!!) || (error is HttpException && error.code() == 304))) {

        } else {
            AlertDialogHelper.showNewCustomDialog(
                    context!! as BaseActivity,
                    getString(com.webkul.mobikul.R.string.oops),
                    NetworkHelper.getErrorMessage(context!!, error),
                    false,
                    getString(com.webkul.mobikul.R.string.try_again),
                    DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        mContentViewBinding.loading = true
                        callApi()
                    }
                    , getString(com.webkul.mobikul.R.string.close)
                    , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            })
        }
    }

    fun onFailureResponse(response: Any) {
        AlertDialogHelper.showNewCustomDialog(
                context!! as BaseActivity,
                getString(com.webkul.mobikul.R.string.error),
                (response as BaseModel).message,
                false,
                getString(com.webkul.mobikul.R.string.ok),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                }
                , ""
                , null)
    }


    private fun updateSelectedData(productCollectionData: ArrayList<ProductCollectionData>) {
        var selectProductIds: ArrayList<String>? = null
        when (mCollectionType) {
            "related" -> selectProductIds = (context as SellerAddProductActivity).mSellerAddProductResponseData!!.productData!!.related
            "upsell" -> selectProductIds = (context as SellerAddProductActivity).mSellerAddProductResponseData!!.productData!!.upsell
            "crosssell" -> selectProductIds = (context as SellerAddProductActivity).mSellerAddProductResponseData!!.productData!!.crossSell
        }
        if (selectProductIds != null && selectProductIds.size > 0) {
            for (noOfProducts in productCollectionData.indices) {
                productCollectionData[noOfProducts].isSelected = selectProductIds.contains(productCollectionData[noOfProducts].entityId)
            }
        }
    }

    private fun initProductRv() {
        if (mCollectionData.productCollectionData != null && mCollectionData.productCollectionData!!.size > 0) {
            mContentViewBinding.productsRv.adapter = AddProductProductsListRvAdapter(context!!, mCollectionData.productCollectionData!!)
            mContentViewBinding.productsRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val lastCompletelyVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    if (NetworkHelper.isNetworkAvailable(context!!)) {
                        if (mContentViewBinding.loading == false && mCollectionData.productCollectionData!!.size != mCollectionData.totalCount
                                && lastCompletelyVisibleItemPosition == mCollectionData.productCollectionData!!.size - 1 && lastCompletelyVisibleItemPosition < mCollectionData.totalCount) {
                            callApi()
                        }
                    }
                }
            })
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.submit_btn) {
            val selectProductIds = ArrayList<String>()
            for (noOfProducts in 0 until mCollectionData.productCollectionData!!.size) {
                if (mCollectionData.productCollectionData!![noOfProducts].isSelected)
                    selectProductIds.add(mCollectionData.productCollectionData!![noOfProducts].entityId!!)
            }
            when (mCollectionType) {
                "related" -> (context as SellerAddProductActivity).mSellerAddProductResponseData!!.productData!!.related = selectProductIds
                "upsell" -> (context as SellerAddProductActivity).mSellerAddProductResponseData!!.productData!!.upsell = selectProductIds
                "crosssell" -> (context as SellerAddProductActivity).mSellerAddProductResponseData!!.productData!!.crossSell = selectProductIds
            }
            activity!!.supportFragmentManager.popBackStack()
        } else if (v.id == R.id.filter_btn) {
            if (mCollectionData.filterOption != null && mCollectionData.filterOption!!.size > 0) {
                val supportFragmentManager = (v.context as SellerAddProductActivity).supportFragmentManager
                val sellerAddProductCollectionFilterDialogFragment = SellerAddProductCollectionFilterDialogFragment()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                transaction.add(R.id.main_container, sellerAddProductCollectionFilterDialogFragment, SellerAddProductCollectionFilterDialogFragment::class.java.simpleName)
                transaction.addToBackStack(SellerAddProductCollectionFilterDialogFragment::class.java.simpleName)
                transaction.commit()
            } else {
                showToast(context!!, context!!.getString(R.string.msg_no_filter_option_available), Toast.LENGTH_SHORT)
            }
        }
    }

    companion object {

        fun newInstance(collectionType: String): SellerSelectProductsFragment {
            val sellerSelectProductsDialogFragment = SellerSelectProductsFragment()
            val args = Bundle()
            args.putString(BUNDLE_KEY_COLLECTION_TYPE, collectionType)
            sellerSelectProductsDialogFragment.arguments = args
            return sellerSelectProductsDialogFragment
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (onDetachInterface != null) {
            onDetachInterface!!.onFragmentDetached()
        }
    }

}