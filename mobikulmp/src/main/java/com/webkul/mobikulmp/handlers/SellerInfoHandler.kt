package com.webkul.mobikulmp.handlers

import android.content.Context
import android.content.Intent
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.helpers.BundleKeysHelper
import com.webkul.mobikul.helpers.MobikulApplication
import com.webkul.mobikulmp.activities.ContactSellerActivity
import com.webkul.mobikulmp.activities.ProductDetailsExtendedActivity
import com.webkul.mobikulmp.fragments.ReportSellerBottomSheetFragment
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_SELLER_TITLE
import com.webkul.mobikulmp.models.sellerinfo.ReportData

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikul.handlers
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 11/9/19
 */
class SellerInfoHandler(val mContext: Context) {

    fun onClickProfile(sellerId: String, shopTitle: String) {
        val intent = (mContext.applicationContext as MobikulApplication).getSellerProfileActivity(mContext)
        intent?.putExtra(MpBundleKeysHelper.BUNDLE_KEY_SELLER_ID, sellerId.toInt())
        intent?.putExtra(BUNDLE_KEY_SELLER_TITLE, shopTitle)
        mContext.startActivity(intent)
    }


    fun onClickContactUs(sellerId: String, productId: String) {
        val intent = Intent(mContext, ContactSellerActivity::class.java)
        intent.putExtra(MpBundleKeysHelper.BUNDLE_KEY_SELLER_ID, sellerId.toInt())
        intent.putExtra(BundleKeysHelper.BUNDLE_KEY_PRODUCT_ID, productId)
        mContext.startActivity(intent)
    }

    fun onClickReportProduct(reportData: ReportData, id:String,productName:String){
        val mReportSellerBottomSheetFragment=ReportSellerBottomSheetFragment.newInstance(2,reportData, id,productName)
        mReportSellerBottomSheetFragment.show((mContext as BaseActivity).supportFragmentManager, ReportSellerBottomSheetFragment::class.java.simpleName)
    }

}
