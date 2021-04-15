/*
 * Webkul Software.
 *
 * Kotlin
 *
 * @author Webkul <support@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

package com.webkul.mobikul.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.FragmentProductCommentBottomSheetBinding
import com.webkul.mobikul.handlers.ProductCommentBottomSheetFragmentHandler
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_COMMENT
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_SELECTED_ITEM_POSITION


class ProductCommentBottomSheetFragment : FullScreenBottomSheetDialogFragment() {

    companion object {
        fun newInstance(position: Int, comment: String?): ProductCommentBottomSheetFragment {
            val productCommentBottomSheetFragment = ProductCommentBottomSheetFragment()
            val args = Bundle()
            args.putInt(BUNDLE_KEY_SELECTED_ITEM_POSITION, position)
            args.putString(BUNDLE_KEY_PRODUCT_COMMENT, comment)
            productCommentBottomSheetFragment.arguments = args
            return productCommentBottomSheetFragment
        }
    }

    lateinit var mContentViewBinding: FragmentProductCommentBottomSheetBinding
    var mSelectedItemPosition: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_comment_bottom_sheet, container, false)
        return mContentViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mSelectedItemPosition = arguments!!.getInt(BUNDLE_KEY_SELECTED_ITEM_POSITION)
        startInitialization()
    }

    private fun startInitialization() {
        mContentViewBinding.comment = arguments!!.getString(BUNDLE_KEY_PRODUCT_COMMENT)
        mContentViewBinding.handler = ProductCommentBottomSheetFragmentHandler(this)
    }
}