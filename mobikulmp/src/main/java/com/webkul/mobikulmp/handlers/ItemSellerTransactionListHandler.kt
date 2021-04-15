package com.webkul.mobikulmp.handlers

import android.content.Intent
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.SellerTransactionDetailsActivity
import com.webkul.mobikulmp.activities.SellerTransactionsListActivity
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_TRANSACTION_ID

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

class ItemSellerTransactionListHandler(private val mContext: SellerTransactionsListActivity) {

    fun onClickTransactionRemark(view: View) {
        val commentTv = (view.parent as LinearLayoutCompat).findViewById<AppCompatTextView>(R.id.comment_tv)
        if (commentTv.visibility == View.VISIBLE) {
            commentTv.visibility = View.GONE
        } else {
            commentTv.visibility = View.VISIBLE
        }
    }

    fun onClickItem(id: String) {
        val intent = Intent(mContext, SellerTransactionDetailsActivity::class.java)
        intent.putExtra(BUNDLE_KEY_TRANSACTION_ID, id)
        mContext.startActivity(intent)
    }
}