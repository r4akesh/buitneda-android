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
import com.webkul.mobikulmp.databinding.FragmentSellerProfileMoreMenuBinding
import com.webkul.mobikulmp.handlers.SellerProfileMoreMenuHandler
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_PRIVACY_POLICY
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_RETURN_POLICY
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_SHIPPING_POLICY

class SellerProfileMoreMenuFragment : BaseFragment() {
    lateinit var mContentViewBinding: FragmentSellerProfileMoreMenuBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_seller_profile_more_menu, container, false)
        return mContentViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mContentViewBinding.handler = SellerProfileMoreMenuHandler(this)
        mContentViewBinding.returnPolicy = arguments?.getString(BUNDLE_KEY_RETURN_POLICY)
        mContentViewBinding.shippingPolicy = arguments?.getString(BUNDLE_KEY_SHIPPING_POLICY)
        mContentViewBinding.privacyPolicy = arguments?.getString(BUNDLE_KEY_PRIVACY_POLICY)
    }

    companion object {
        fun newInstance(returnPolicy: String?, shippingPolicy: String?, privacyPolicy: String?): SellerProfileMoreMenuFragment {
            val sellerProfileMoreMenuFragment = SellerProfileMoreMenuFragment()
            val args = Bundle()
            args.putString(BUNDLE_KEY_RETURN_POLICY, returnPolicy)
            args.putString(BUNDLE_KEY_SHIPPING_POLICY, shippingPolicy)
            args.putString(BUNDLE_KEY_PRIVACY_POLICY, privacyPolicy)
            sellerProfileMoreMenuFragment.arguments = args
            return sellerProfileMoreMenuFragment
        }
    }

}
