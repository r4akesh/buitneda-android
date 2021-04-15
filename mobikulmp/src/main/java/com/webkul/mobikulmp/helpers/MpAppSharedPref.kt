package com.webkul.mobikulmp.helpers

import android.content.Context
import com.webkul.mobikul.helpers.AppSharedPref

/**
 *
 * Webkul Software.
 *
 * @category Mobikul
 * @package com.webkul.mobikulmp.helpers
 * @author Webkul
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 15/5/19
 *
 */
class MpAppSharedPref : AppSharedPref() {
    companion object {

        private const val KEY_CUSTOMER_IS_SELLER = "isSeller"
        private const val KEY_IS_SELLER_PENDING = "isPendingSeller"
        private const val KEY_CUSTOMER_IS_ADMIN = "isAdmin"

        /*Seller*/

        fun isSeller(context: Context): Boolean {
            try {
                return getSharedPreference(context, AppSharedPref.CUSTOMER_PREF).getBoolean(KEY_CUSTOMER_IS_SELLER, false)
            } catch (e: ClassCastException) {
                return java.lang.Boolean.parseBoolean(getSharedPreference(context, AppSharedPref.CUSTOMER_PREF).getString(KEY_CUSTOMER_IS_SELLER, false.toString()))
            }

        }

        fun setIsSeller(context: Context, isSeller: Boolean) {
            getSharedPreferenceEditor(context, AppSharedPref.CUSTOMER_PREF).putBoolean(KEY_CUSTOMER_IS_SELLER, isSeller).commit()
        }

        @JvmStatic
        fun isSellerPending(context: Context): Boolean {
            try {

                return getSharedPreference(context, AppSharedPref.CUSTOMER_PREF).getBoolean(KEY_IS_SELLER_PENDING, false)
            } catch (e: ClassCastException) {
                return java.lang.Boolean.parseBoolean(getSharedPreference(context, AppSharedPref.CUSTOMER_PREF).getString(KEY_IS_SELLER_PENDING, false.toString()))
            }

        }

        fun setIsSellerPending(context: Context, isPending: Boolean) {
            getSharedPreferenceEditor(context, AppSharedPref.CUSTOMER_PREF).putBoolean(KEY_IS_SELLER_PENDING, isPending).commit()
        }

        fun isAdmin(context: Context): Boolean {

            try {
                return getSharedPreference(context, AppSharedPref.CUSTOMER_PREF).getBoolean(KEY_CUSTOMER_IS_ADMIN, false)
            } catch (e: ClassCastException) {
                return java.lang.Boolean.parseBoolean(getSharedPreference(context, AppSharedPref.CUSTOMER_PREF).getString(KEY_CUSTOMER_IS_ADMIN, false.toString()))
            }

        }

        fun setIsAdmin(context: Context, isAdmin: Boolean) {
            getSharedPreferenceEditor(context, AppSharedPref.CUSTOMER_PREF).putBoolean(KEY_CUSTOMER_IS_ADMIN, isAdmin).commit()
        }

    }
}