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
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.FragmentAddPayeeDialogBinding
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PAYEE_DETAILS
import com.webkul.mobikul.wallet.handlers.AddPayeeDialogFragmentHandler
import com.webkul.mobikul.wallet.models.wallet.AddPayeeModel
import com.webkul.mobikul.wallet.models.wallet.AddedPayeeList
import com.webkul.mobikul.wallet.models.wallet.CustomerList

class AddPayeeDialogFragment : DialogFragment() {

    lateinit var mContentViewBinding: FragmentAddPayeeDialogBinding

    companion object {
        fun newInstance(customerList: CustomerList): AddPayeeDialogFragment {
            val addPayeeDialogFragment = AddPayeeDialogFragment()
            val args = Bundle()
            args.putParcelable(BUNDLE_KEY_PAYEE_DETAILS, customerList)
            addPayeeDialogFragment.arguments = args
            return addPayeeDialogFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_payee_dialog, container, false)
        val addPayeeData = AddPayeeModel()
        if (arguments != null && arguments!!.containsKey(BUNDLE_KEY_PAYEE_DETAILS)) {
            mContentViewBinding.isUpdating = true
            val customerList: CustomerList = arguments!!.getParcelable(BUNDLE_KEY_PAYEE_DETAILS)
                    ?: CustomerList()
            addPayeeData.id = customerList.id
            addPayeeData.nickName = customerList.name
            addPayeeData.email = customerList.email
            addPayeeData.confirmEmail = customerList.email
        }
        mContentViewBinding.data = addPayeeData
        mContentViewBinding.handler = AddPayeeDialogFragmentHandler(this)
        return mContentViewBinding.root
    }
}