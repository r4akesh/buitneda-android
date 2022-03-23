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

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.ProductNotFound
import com.webkul.mobikul.databinding.FragmentEmptyBinding
import com.webkul.mobikul.handlers.EmptyFragmentHandler
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_EMPTY_FRAGMENT_DRAWABLE_ID
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_EMPTY_FRAGMENT_HIDE_CONTINUE_SHOPPING_BTN
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_EMPTY_FRAGMENT_SUBTITLE_ID
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_EMPTY_FRAGMENT_TITLE_ID
import com.webkul.mobikul.helpers.ToastHelper

class EmptyFragment : Fragment() {

    companion object {
        fun newInstance(drawableIconId: String, title: String, subtitle: String, hideContinueShoppingBtn: Boolean = false,from:String): EmptyFragment {
            val emptyFragment = EmptyFragment()
            val bundle = Bundle()
            bundle.putString(BUNDLE_KEY_EMPTY_FRAGMENT_DRAWABLE_ID, drawableIconId)
            bundle.putString(BUNDLE_KEY_EMPTY_FRAGMENT_TITLE_ID, title)
            bundle.putString(BUNDLE_KEY_EMPTY_FRAGMENT_SUBTITLE_ID, subtitle)
            bundle.putBoolean(BUNDLE_KEY_EMPTY_FRAGMENT_HIDE_CONTINUE_SHOPPING_BTN, hideContinueShoppingBtn)
            bundle.putString("from",from)
            emptyFragment.arguments = bundle
            return emptyFragment
        }
    }




    private lateinit var mContentViewBinding: FragmentEmptyBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_empty, container, false)
        return mContentViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mContentViewBinding.animationView.setAnimation(arguments!!.getString(BUNDLE_KEY_EMPTY_FRAGMENT_DRAWABLE_ID))
        mContentViewBinding.title = arguments!!.getString(BUNDLE_KEY_EMPTY_FRAGMENT_TITLE_ID)
        mContentViewBinding.subtitle = arguments!!.getString(BUNDLE_KEY_EMPTY_FRAGMENT_SUBTITLE_ID)
        mContentViewBinding.hideContinueShoppingBtn = arguments!!.getBoolean(BUNDLE_KEY_EMPTY_FRAGMENT_HIDE_CONTINUE_SHOPPING_BTN)
        mContentViewBinding.handler = EmptyFragmentHandler()

        if(arguments!!.getString("from").equals("search")){
            mContentViewBinding.fromSearchTwoBtn.visibility = View.VISIBLE
        }else{
            mContentViewBinding.fromSearchTwoBtn.visibility = View.GONE
        }


        mContentViewBinding.sendMailBtn.setOnClickListener {
            context!!.startActivity(Intent(context!!, ProductNotFound::class.java))
        }

        mContentViewBinding.sendMessageBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setPackage("com.whatsapp")
            intent.data = Uri.parse("https://api.whatsapp.com/send?phone=${context!!.getString(R.string.whats_app_number)}")
            if (context!!.packageManager.resolveActivity(intent, 0) != null) {
                context!!.startActivity(intent)
            } else {
                ToastHelper.showToast(context!!, context!!.getString(R.string.please_install_whatsapp))
                val url = "https://play.google.com/store/search?q=whatsapp&c=apps"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                context!!.startActivity(intent)
            }
        }


    }
}
