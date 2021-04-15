package com.webkul.mobikulmp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.fragments.FullScreenBottomSheetDialogFragment
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.SellerTransactionsListActivity
import com.webkul.mobikulmp.databinding.FragmentSellerTransactionsFilterBinding
import com.webkul.mobikulmp.handlers.SellerTransactionsFilterFragHandler

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.fragments
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 16/6/19
 */

class SellerTransactionsFilterFragment : FullScreenBottomSheetDialogFragment() {

    internal var onDetachInterface: OnDetachInterface? = null


    fun setOnDetachInterface(onDetachInterface: OnDetachInterface) {
        this.onDetachInterface = onDetachInterface
    }

    interface OnDetachInterface {
        fun onFragmentDetached()
    }

    lateinit var mContentViewBinding: FragmentSellerTransactionsFilterBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_seller_transactions_filter, container, false)
        mContentViewBinding.transactionId = (context as SellerTransactionsListActivity).mTransactionId
        mContentViewBinding.fromDate = (context as SellerTransactionsListActivity).mDateFrom
        mContentViewBinding.toDate = (context as SellerTransactionsListActivity).mDateTo
        mContentViewBinding.handler = SellerTransactionsFilterFragHandler(this)
        return mContentViewBinding.root
    }
}