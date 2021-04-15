package com.webkul.mobikulmp.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.fragments.BaseFragment
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_IMAGE
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.FragmentEditProductImageBinding
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_PRODUCT_IMAGE_ROLE
import com.webkul.mobikulmp.models.seller.ImageRole
import com.webkul.mobikulmp.models.seller.MediaGallery

/**
 * Webkul Software.
 *
 * Java
 *
 * @author Webkul <support></support>@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */
class EditAddProductImageFragment : BaseFragment() {

    private lateinit var mContentViewBinding: FragmentEditProductImageBinding
    private lateinit var mProductImageData: MediaGallery
    private var onDetachInterface: OnDetachInterface? = null

    fun setOnDetachInterface(onDetachInterface: OnDetachInterface) {
        this.onDetachInterface = onDetachInterface
    }

    interface OnDetachInterface {
        fun onFragmentDetached()
        fun onEditAddProductImage(imageRole: List<ImageRole>, mediaGallery: MediaGallery)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_product_image, container, false)
        mProductImageData = arguments?.getParcelable(BUNDLE_KEY_PRODUCT_IMAGE)?: MediaGallery()
        return mContentViewBinding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mContentViewBinding!!.data = mProductImageData
        startInitialization()
    }

    private fun startInitialization() {
        val imageRolesList: ArrayList<ImageRole> = arguments?.getParcelableArrayList(BUNDLE_KEY_PRODUCT_IMAGE_ROLE)?:ArrayList()
        for (roleIndex in imageRolesList!!.indices) {
            val eachCheckBox = CheckBox(context)
            eachCheckBox.text = imageRolesList[roleIndex].label
            eachCheckBox.tag = imageRolesList[roleIndex].value
            eachCheckBox.textSize = 12f
            eachCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    for (roleIndex1 in imageRolesList.indices) {
                        if (imageRolesList[roleIndex1].value == buttonView.tag) {
                            imageRolesList[roleIndex1].id = mProductImageData!!.id
                        }
                    }
                } else {
                    for (roleIndex1 in imageRolesList.indices) {
                        if (imageRolesList[roleIndex1].value == buttonView.tag) {
                            imageRolesList[roleIndex1].id = ""
                        }
                    }
                }
            }
            if (imageRolesList[roleIndex].id != null && imageRolesList[roleIndex].id == mProductImageData!!.id)
                eachCheckBox.isChecked = true
            mContentViewBinding!!.additionalRoleLayout.addView(eachCheckBox)
        }

        mContentViewBinding!!.saveBtn.setOnClickListener(View.OnClickListener {
            if (onDetachInterface != null) {
                onDetachInterface!!.onEditAddProductImage(imageRolesList, mProductImageData)
                activity?.onBackPressed()
            }
        })


        mContentViewBinding!!.roleHeading.setOnClickListener(View.OnClickListener {
            if (mContentViewBinding!!.roleLayout.visibility == View.VISIBLE) {
                mContentViewBinding!!.roleLayout.visibility = View.GONE
                mContentViewBinding!!.roleHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context!!, R.drawable.ic_down_arrow_grey_wrapper), null)
            } else {
                mContentViewBinding!!.roleLayout.visibility = View.VISIBLE
                mContentViewBinding!!.roleHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context!!, R.drawable.ic_up_arrow_grey_wrapper), null)
                Handler().postDelayed({
                    val scrollTo = mContentViewBinding!!.scrollView.top + mContentViewBinding!!.roleHeading.top
                    mContentViewBinding!!.scrollView.smoothScrollTo(0, scrollTo)
                }, 200)
            }

        })
    }

    override fun onDetach() {
        super.onDetach()
        if (onDetachInterface != null) {
            onDetachInterface!!.onFragmentDetached()
        }
    }

    companion object {
        fun newInstance(mediaGallery: MediaGallery, imageRole: ArrayList<ImageRole>?): EditAddProductImageFragment {
            val args = Bundle()
            args.putParcelable(BUNDLE_KEY_PRODUCT_IMAGE, mediaGallery)
            args.putParcelableArrayList(BUNDLE_KEY_PRODUCT_IMAGE_ROLE, imageRole)
            val fragment = EditAddProductImageFragment()
            fragment.arguments = args
            return fragment
        }
    }
}