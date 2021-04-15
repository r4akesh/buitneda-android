package com.webkul.mobikulmp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.fragments.FullScreenBottomSheetDialogFragment
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.FragmentSellerMakeReviewBinding
import com.webkul.mobikulmp.handlers.SellerMakeReviewHandler
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_SELLER_ID
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_SHOP_URL
import com.webkul.mobikulmp.models.SellerMakeReviewData

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.fragments
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 26/07/19
 */
class SellerMakeReviewFragment : FullScreenBottomSheetDialogFragment() {

    lateinit var mContentViewBinding: FragmentSellerMakeReviewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_seller_make_review, container, false)
        return mContentViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mContentViewBinding.data = SellerMakeReviewData()
        mContentViewBinding.handler = arguments?.getString(BUNDLE_KEY_SHOP_URL)?.let { SellerMakeReviewHandler(this, arguments!!.getInt(BUNDLE_KEY_SELLER_ID), it) }

        if (AppSharedPref.isLoggedIn(context!!)) {
            mContentViewBinding.data!!.nickName = AppSharedPref.getCustomerName(context!!)
            mContentViewBinding.executePendingBindings()
        }

        mContentViewBinding.commentEt.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                if (mContentViewBinding.commentEt.hasFocus()) {
                    v.parent.requestDisallowInterceptTouchEvent(true)
                    when (event.action and MotionEvent.ACTION_MASK) {
                        MotionEvent.ACTION_SCROLL -> {
                            v.parent.requestDisallowInterceptTouchEvent(false)
                            return true
                        }
                    }
                }
                return false
            }
        })
    }

    companion object {

        fun newInstance(sellerId: Int, shopUrl: String): SellerMakeReviewFragment {
            val args = Bundle()
            args.putInt(BUNDLE_KEY_SELLER_ID, sellerId)
            args.putString(BUNDLE_KEY_SHOP_URL, shopUrl)
            val fragment = SellerMakeReviewFragment()
            fragment.arguments = args
            return fragment
        }
    }


}
