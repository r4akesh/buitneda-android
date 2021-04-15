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

package com.webkul.mobikul.handlers

import android.content.Intent
import com.webkul.mobikul.activities.CompareProductsActivity
import com.webkul.mobikul.activities.ContactUsActivity
import com.webkul.mobikul.activities.HomeActivity
import com.webkul.mobikul.fragments.*
import com.webkul.mobikul.helpers.MobikulApplication
import com.webkul.mobikul.models.user.PreferencesRvModel.Companion.ASK
import com.webkul.mobikul.models.user.PreferencesRvModel.Companion.COMPARE
import com.webkul.mobikul.models.user.PreferencesRvModel.Companion.CONTACT
import com.webkul.mobikul.models.user.PreferencesRvModel.Companion.CURRENCY
import com.webkul.mobikul.models.user.PreferencesRvModel.Companion.LANGUAGE
import com.webkul.mobikul.models.user.PreferencesRvModel.Companion.SETTINGS
import com.webkul.mobikul.models.user.PreferencesRvModel.Companion.WEBSITE

class PreferencesFragmentRvHandler(val mFragmentContext: AccountDetailsFragment) {

    fun onClickRvItem(type: Int) {
        when (type) {
            WEBSITE -> {
                WebsiteBottomSheetFragment().show(mFragmentContext.activity!!.supportFragmentManager, WebsiteBottomSheetFragment::class.java.simpleName)
            }
            LANGUAGE -> {
                LanguagesBottomSheetFragment().show(mFragmentContext.activity!!.supportFragmentManager, LanguagesBottomSheetFragment::class.java.simpleName)
            }
            CURRENCY -> {
                CurrencyBottomSheetFragment().show(mFragmentContext.activity!!.supportFragmentManager, CurrencyBottomSheetFragment::class.java.simpleName)
            }
            SETTINGS -> {
                SettingsBottomSheetFragment().show(mFragmentContext.activity!!.supportFragmentManager, SettingsBottomSheetFragment::class.java.simpleName)
            }
            COMPARE ->{
                mFragmentContext.context?.startActivity(Intent(mFragmentContext.context, CompareProductsActivity::class.java))

            }
            CONTACT ->{
                mFragmentContext.context?.startActivity(Intent(mFragmentContext.context, ContactUsActivity::class.java))

            }
            ASK ->{
                mFragmentContext.startActivity((mFragmentContext.context!!.applicationContext as MobikulApplication).getAskAdminActivity(mFragmentContext.context!!))

            }

        }
//        (mFragmentContext.context as HomeActivity).mContentViewBinding.drawerLayout.closeDrawers()
    }
}