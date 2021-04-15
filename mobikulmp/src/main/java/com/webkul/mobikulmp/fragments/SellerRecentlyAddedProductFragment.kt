/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.models.seller
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 15/7/19
 */

package com.webkul.mobikulmp.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.webkul.mobikul.fragments.BaseFragment
import com.webkul.mobikul.models.product.ProductTileData
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.adapters.RecentlyAddedProductsRvAdapter
import com.webkul.mobikulmp.databinding.FragmentSellerRecentlyAddedProductBinding
import com.webkul.mobikulmp.handlers.SellerRecentlyAddedProductHandler
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_PRODUCT_DATA
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_SELLER_ID
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_SELLER_TITLE
import java.util.*

class SellerRecentlyAddedProductFragment : BaseFragment() {
    lateinit var mContentViewBinding: FragmentSellerRecentlyAddedProductBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_seller_recently_added_product, container, false)
        mContentViewBinding.handler = SellerRecentlyAddedProductHandler(this)
        return mContentViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val data: ArrayList<ProductTileData>? = arguments?.getParcelableArrayList(BUNDLE_KEY_PRODUCT_DATA)

        data?.let {
            mContentViewBinding.productsCarouselRv.layoutManager = GridLayoutManager(context!!, 2)
            mContentViewBinding.productsCarouselRv.adapter = RecentlyAddedProductsRvAdapter(context!!, data)
            mContentViewBinding.productsCarouselRv.isNestedScrollingEnabled = false
        }
        if (data?.isEmpty() == true) {
            mContentViewBinding.carouselHeading.visibility = View.GONE
        }

    }

    companion object {
        fun newInstance(data: ArrayList<ProductTileData>, shopTitle: String, sellerId: Int): SellerRecentlyAddedProductFragment {
            val sellerRecentlyAddedProductFragment = SellerRecentlyAddedProductFragment()
            val args = Bundle()
            args.putParcelableArrayList(BUNDLE_KEY_PRODUCT_DATA, data)
            args.putString(BUNDLE_KEY_SELLER_TITLE, shopTitle)
            args.putInt(BUNDLE_KEY_SELLER_ID, sellerId)
            sellerRecentlyAddedProductFragment.arguments = args
            return sellerRecentlyAddedProductFragment
        }
    }

}
