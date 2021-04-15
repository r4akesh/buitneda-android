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
import com.webkul.mobikul.fragments.BaseFragment
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.FragmentSellerProfileAboutStoreBinding
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_ABOUT_STORE

class SellerProfileAboutStoreFragment : BaseFragment() {
    lateinit var mContentViewBinding: FragmentSellerProfileAboutStoreBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_seller_profile_about_store, container, false)
        return mContentViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mContentViewBinding.description = arguments?.getString(BUNDLE_KEY_ABOUT_STORE)

    }

    companion object {
        fun newInstance(description: String?): SellerProfileAboutStoreFragment {
            val sellerProfileAboutStoreFragment = SellerProfileAboutStoreFragment()
            val args = Bundle()
            args.putString(BUNDLE_KEY_ABOUT_STORE, description)
            sellerProfileAboutStoreFragment.arguments = args
            return sellerProfileAboutStoreFragment
        }
    }

}
