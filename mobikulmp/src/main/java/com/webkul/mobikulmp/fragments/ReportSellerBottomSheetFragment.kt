package com.webkul.mobikulmp.fragments

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.webkul.mobikul.activities.MyOrdersActivity
import com.webkul.mobikul.activities.OrderDetailsActivity
import com.webkul.mobikul.activities.ProductDetailsActivity
import com.webkul.mobikul.fragments.FullScreenBottomSheetDialogFragment
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikul.helpers.ApplicationConstants
import com.webkul.mobikul.helpers.BundleKeysHelper
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.FragmentReportSellerBottomSheetBinding
import com.webkul.mobikulmp.handlers.ReportSellerBottomSheetFragmentHandler
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper
import com.webkul.mobikulmp.models.sellerinfo.ReportData

class ReportSellerBottomSheetFragment : FullScreenBottomSheetDialogFragment() {

    companion object {
        fun newInstance(fromPage: Int, reportData: ReportData, id: String,name:String): ReportSellerBottomSheetFragment {
            val reportSellerBottomSheetFragment = ReportSellerBottomSheetFragment()
            val args = Bundle()
            args.putInt(MpBundleKeysHelper.BUNDLE_FROM_PAGE, fromPage)
            args.putString(MpBundleKeysHelper.BUNDLE_PRODUCT_SELLER_ID, id)
            args.putString(BundleKeysHelper.BUNDLE_KEY_PRODUCT_NAME, name)
            args.putParcelable(MpBundleKeysHelper.BUNDLE_REPORT_DATA, reportData)
            reportSellerBottomSheetFragment.arguments = args
            return reportSellerBottomSheetFragment
        }
    }

    lateinit var mContentViewBinding: FragmentReportSellerBottomSheetBinding
    lateinit var otherMethodRb: RadioButton
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        dialog?.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheetInternal = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheetInternal?.let {
                BottomSheetBehavior.from(it).peekHeight = Utils.screenHeight

            }
        }
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_report_seller_bottom_sheet, container, false)
        return mContentViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        startInitialization()
        if (ApplicationConstants.ENABLE_KEYBOARD_OBSERVER)
            attachKeyboardListeners()
    }

    private fun startInitialization() {
        val reportData: ReportData? = arguments?.getParcelable(MpBundleKeysHelper.BUNDLE_REPORT_DATA)
        mContentViewBinding.data = reportData
        mContentViewBinding.handler = ReportSellerBottomSheetFragmentHandler(this)
        mContentViewBinding.fromPage = arguments?.getInt(MpBundleKeysHelper.BUNDLE_FROM_PAGE)
        mContentViewBinding.data?.flagMessage = ""
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        mContentViewBinding.data?.productFlagReasons?.forEach { eachProductFlagReason ->
            val eachMethodRb = RadioButton(context)
            eachMethodRb.layoutParams = layoutParams
            eachMethodRb.text = eachProductFlagReason.reason
            eachMethodRb.textSize = 14f
            eachMethodRb.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
            eachMethodRb.setTextColor(resources.getColor(R.color.text_color_primary))
            eachMethodRb.tag = eachProductFlagReason.entityId

            eachMethodRb.highlightColor = resources.getColor(R.color.actionBarItemsColor)
            mContentViewBinding.flagRg.addView(eachMethodRb)
        }

        mContentViewBinding.flagRg.setOnCheckedChangeListener { group, checkedId ->
            val selectedRb = group.findViewById<RadioButton>(checkedId)
            if (this::otherMethodRb.isInitialized && selectedRb.tag == otherMethodRb.tag) {
                mContentViewBinding.data?.selectedOtherReasonMethod = true
                mContentViewBinding.flagReasonTil.visibility = View.VISIBLE
            } else {
                mContentViewBinding.data?.selectedReasonMethod = selectedRb.tag.toString()
                mContentViewBinding.data?.selectedOtherReasonMethod = false
                mContentViewBinding.flagReasonTil.visibility = View.GONE
                mContentViewBinding.flagReasonEt.setText("")
            }
        }
        if (reportData?.showReportOtherReason == true) {
            val eachMethodRb = RadioButton(context)
            eachMethodRb.layoutParams = layoutParams
            eachMethodRb.text = mContentViewBinding.data?.productOtherReasonLabel
                    ?: getString(R.string.other)
            eachMethodRb.textSize = 14f
            eachMethodRb.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
            eachMethodRb.setTextColor(resources.getColor(com.webkul.mobikul.R.color.text_color_primary))
            eachMethodRb.tag = "Other"
            eachMethodRb.highlightColor = resources.getColor(com.webkul.mobikul.R.color.actionBarItemsColor)
            otherMethodRb = eachMethodRb
            mContentViewBinding.flagRg.addView(eachMethodRb)
            eachMethodRb.isChecked = true
        }

        mContentViewBinding.data?.name = AppSharedPref.getCustomerName(context!!)
        mContentViewBinding.data?.email = AppSharedPref.getCustomerEmail(context!!)
        mContentViewBinding.executePendingBindings()
    }



    override fun onDestroy() {
        super.onDestroy()
        Utils.hideKeyboard(mContentViewBinding.reportProductBottomSheet)
        if (keyboardListenersAttached) {
            mContentViewBinding.reportProductBottomSheet.viewTreeObserver.removeGlobalOnLayoutListener(keyboardLayoutListener)
        }
    }

    private val keyboardLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        var heightDiff: Int = 0
        if (context is ProductDetailsActivity)
            heightDiff = Utils.screenHeight - (context as ProductDetailsActivity).mContentViewBinding.root.height

        if (heightDiff < 200) {
            onHideKeyboard()
        } else {
            onShowKeyboard(heightDiff)
        }
    }

    private var keyboardListenersAttached = false

    protected fun onShowKeyboard(keyboardHeight: Int) {
        val layoutParams = mContentViewBinding.keyboardHeightLayout.layoutParams
        layoutParams.height = keyboardHeight
        mContentViewBinding.keyboardHeightLayout.layoutParams = layoutParams
    }

    protected fun onHideKeyboard() {
        val layoutParams = mContentViewBinding.keyboardHeightLayout.layoutParams
        layoutParams.height = 0
        mContentViewBinding.keyboardHeightLayout.layoutParams = layoutParams
    }

    protected fun attachKeyboardListeners() {
        if (keyboardListenersAttached) {
            return
        }
        mContentViewBinding.reportProductBottomSheet.viewTreeObserver.addOnGlobalLayoutListener(keyboardLayoutListener)
        keyboardListenersAttached = true
    }


}