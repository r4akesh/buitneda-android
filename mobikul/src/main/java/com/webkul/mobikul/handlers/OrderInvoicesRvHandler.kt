/*
 * Webkul Software.
 *
 * Kotlin
 *
 * @author Webkul <support@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2019 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

package com.webkul.mobikul.handlers

import com.webkul.mobikul.fragments.InvoicesFragment
import com.webkul.mobikul.fragments.OrderInvoiceDetailsBottomSheetFragment


class OrderInvoicesRvHandler(private val mFragmentContext: InvoicesFragment) {

    fun onClickViewInvoice(invoiceIncrementId: String,invoiceId:String) {
        OrderInvoiceDetailsBottomSheetFragment.newInstance(invoiceIncrementId,invoiceId,mFragmentContext.mContentViewBinding.data!!.incrementId).show(mFragmentContext.childFragmentManager, OrderInvoiceDetailsBottomSheetFragment::class.java.simpleName)
    }

    fun onClickSaveInvoice() {

    }
}