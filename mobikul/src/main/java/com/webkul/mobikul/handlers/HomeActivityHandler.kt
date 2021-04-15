package com.webkul.mobikul.handlers

import android.app.Fragment
import android.content.Context
import android.util.Log
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.HomeActivity
import com.webkul.mobikul.fragments.HomeFragment


/**
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

class HomeActivityHandler(val mContext: Context) {

    fun onClickBackToTop() {
        mContext as HomeActivity
//        val myFragment: androidx.fragment.app.Fragment? = mContext.supportFragmentManager.fragments.get(mContext.supportFragmentManager.backStackEntryCount)
//        Log.d("asasas", "myFragment-------------------"+myFragment?.tag.toString())
//        if ((myFragment != null && myFragment is HomeFragment) || myFragment?.tag.toString().equals("com.bumptech.glide.manager")) {
//            Log.d("asasas", myFragment?.tag.toString())
//            (mContext as HomeActivity).homeFragment?.mContentViewBinding?.mainScroller?.smoothScrollTo(0, 0)
//        } else {
        Log.d("kk-", "-----------------: HOME CALL 1");
            mContext.initIntent()
//        }
    }

}