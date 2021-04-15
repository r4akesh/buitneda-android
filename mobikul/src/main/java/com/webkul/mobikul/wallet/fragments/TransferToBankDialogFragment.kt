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

package com.webkul.mobikul.wallet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.FragmentTransferToBankDialogBinding
import com.webkul.mobikul.helpers.BundleKeysHelper
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CURRENCY_CODE
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_MAXIMUM_AMT
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_MINIMUM_AMT
import com.webkul.mobikul.wallet.handlers.TransferToBankDialogFragmentHandler
import com.webkul.mobikul.wallet.models.wallet.CustomerList
import com.webkul.mobikul.wallet.models.wallet.TransferToBankModel

class TransferToBankDialogFragment : DialogFragment() {

    lateinit var mContentViewBinding: FragmentTransferToBankDialogBinding
    var minimumAmount: String? = null
    var maximumAmount: String? = null

    companion object {
        fun newInstance(accountDetails: ArrayList<CustomerList>, minimumAmount: String, maximumAmount: String, currencyCode:String): TransferToBankDialogFragment {
            val transferToBankDialogFragment = TransferToBankDialogFragment()
            val args = Bundle()
            args.putParcelableArrayList(BundleKeysHelper.BUNDLE_KEY_ACCOUNT_DETAILS, accountDetails)
            args.putString(BundleKeysHelper.BUNDLE_KEY_MINIMUM_AMT, minimumAmount)
            args.putString(BundleKeysHelper.BUNDLE_KEY_MAXIMUM_AMT, maximumAmount)
            args.putString(BUNDLE_KEY_CURRENCY_CODE,currencyCode)
            transferToBankDialogFragment.arguments = args
            return transferToBankDialogFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_transfer_to_bank_dialog, container, false)
        minimumAmount = arguments?.getString(BUNDLE_KEY_MINIMUM_AMT)
        maximumAmount = arguments?.getString(BUNDLE_KEY_MAXIMUM_AMT)
        mContentViewBinding.currencyCode=arguments?.getString(BUNDLE_KEY_CURRENCY_CODE)
//        setupAccountDetailsSp(arguments?.getParcelableArrayList<CustomerList>(BUNDLE_KEY_ACCOUNT_DETAILS)
//                ?: ArrayList())
        mContentViewBinding.data = TransferToBankModel()
        mContentViewBinding.handler = TransferToBankDialogFragmentHandler(this)
        return mContentViewBinding.root
    }

    private fun setupAccountDetailsSp(accountDetails: ArrayList<CustomerList>) {
        if (accountDetails.isEmpty()) {
            mContentViewBinding.accountDetailsSp.visibility = GONE
            mContentViewBinding.submitBtn.visibility = GONE
            mContentViewBinding.emptyAccountDetails.visibility = VISIBLE
        } else {
            val customerSpinnerData = java.util.ArrayList<String>()
            for (customerIterator in accountDetails.indices) {
                customerSpinnerData.add(accountDetails[customerIterator].name)
            }

            mContentViewBinding.accountDetailsSp.adapter = context?.let { ArrayAdapter(it, R.layout.custom_spinner_item, customerSpinnerData) }
            mContentViewBinding.accountDetailsSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    mContentViewBinding.data!!.accountDetails = accountDetails[position].id
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
        }
    }
}