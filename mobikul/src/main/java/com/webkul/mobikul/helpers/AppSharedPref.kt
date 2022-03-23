package com.webkul.mobikul.helpers

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.databinding.BaseObservable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.webkul.mobikul.helpers.ApplicationConstants.DEFAULT_CURRENCY_CODE
import com.webkul.mobikul.helpers.ApplicationConstants.DEFAULT_STORE_CODE
import com.webkul.mobikul.helpers.ApplicationConstants.DEFAULT_STORE_ID
import com.webkul.mobikul.helpers.ApplicationConstants.DEFAULT_WEBSITE_ID
import com.webkul.mobikul.helpers.ConstantsHelper.VIEW_TYPE_GRID
import com.webkul.mobikul.models.homepage.Category
import com.webkul.mobikul.models.user.AddressDetailsData


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
open class AppSharedPref {

    companion object {
        const val SETTINGS_PREF = "settingsPreference"
        const val CONFIGURATION_PREF = "configurationPreference"
        const val CUSTOMER_PREF = "customerPreference"
        const val PRICE_FORMAT_PREF = "priceFormatPreference"
        const val HOME_PAGE_DATA_PREF="homePageDataPreference"
        const val KEY_HOME_PAGE_DATA = "homePageData"


        const val KEY_ALL_NOTIFICATION = "allNotifications"
        const val KEY_OFFERS_NOTIFICATION = "offersNotifications"
        const val KEY_ORDERS_NOTIFICATION = "ordersNotifications"
        const val KEY_ABANDONED_CART_NOTIFICATION = "abandonedCartNotifications"
        const val KEY_RECENTLY_VIEWED_PRODUCTS = "recentlyViewedProducts"
        const val KEY_SEARCH_HISTORY = "searchHistory"

        const val KEY_WEBSITE_ID = "websiteId"
        const val KEY_WEBSITE_LABEL = "websiteLabel"
        const val KEY_STORE_ID = "storeId"
        const val KEY_WEBSITE_DATA_COUNT = "websiteDataCount"
        const val KEY_STORE_CODE = "storeCodes"
        const val KEY_STORE_LABEL = "storeLabel"
        const val KEY_CURRENCY_CODE = "currencyCode"
        const val KEY_CURRENCY_CODE_SIZE = "currencyCodeSize"
        const val KEY_STORE_DATA_SIZE = "storeDataSize"
        const val KEY_CURRENCY_LABEL = "currencyLabel"
        const val KEY_IS_WISHLIST_ENABLED = "isWishListEnabled"
        const val KEY_FCM_TOKEN = "fcmToken"
        const val KEY_CATALOG_VIEW_TYPE = "catalogViewType"
        const val KEY_IS_AR_SUPPORTED = "isArSupported"
        const val KEY_CATEGORY_DATA = "categoryData"
        const val KEY_SHOW_SPLASH = "showSplash"
        const val KEY_LOGGED_IN = "loggedIn"
        const val KEY_QUOTE_ID = "quoteId"
        const val KEY_CART_COUNT = "cartCount"
        const val KEY_CUSTOMER_TOKEN = "customerToken"
        const val KEY_CUSTOMER_ID = "customerId"
        const val KEY_CUSTOMER_NAME = "customerName"
        const val KEY_CUSTOMER_EMAIL = "customerEmail"
        const val KEY_CUSTOMER_IMAGE_URL = "customerImageUrl"
        const val KEY_CUSTOMER_IMAGE_DOMINANT_COLOR = "customerImageDominantColor"
        const val KEY_CUSTOMER_BANNER_URL = "customerBannerUrl"
        const val KEY_CUSTOMER_BANNER_DOMINANT_COLOR = "customerBannerDominantColor"
        const val KEY_CUSTOMER_CACHED_NEW_ADDRESS = "customerCachedNewAddress"

        const val KEY_PRICE_PATTERN = "pattern"
        const val KEY_PRICE_PRECISION = "precision"

        /*Fingerprint*/
        private const val FINGERPRINT_PREF = "fingerprintPreference"
        private const val KEY_CUSTOMER_FINGER_USER_NAME = "fingerUsername"
        private const val KEY_CUSTOMER_FINGER_PASSWORD = "fingerPassword"
        const val KEY_API_KEY = "apiKey"

        fun getSharedPreference(context: Context, preferenceFile: String): SharedPreferences {
            return context.getSharedPreferences(preferenceFile, MODE_PRIVATE)
        }

        fun getSharedPreferenceEditor(context: Context, preferenceFile: String): SharedPreferences.Editor {
            return context.getSharedPreferences(preferenceFile, MODE_PRIVATE).edit()
        }


        /* Settings Related functions */

        fun getNotificationsEnabled(context: Context): Boolean {
            return getSharedPreference(context, SETTINGS_PREF).getBoolean(KEY_ALL_NOTIFICATION, true)
        }

        fun setNotificationsEnabled(context: Context, notificationEnabled: Boolean) {
            getSharedPreferenceEditor(context, SETTINGS_PREF).putBoolean(KEY_ALL_NOTIFICATION, notificationEnabled).apply()
        }

        fun getOfferNotificationsEnabled(context: Context): Boolean {
            return getSharedPreference(context, SETTINGS_PREF).getBoolean(KEY_OFFERS_NOTIFICATION, true)
        }

        fun setOfferNotificationsEnabled(context: Context, offerNotificationEnabled: Boolean) {
            getSharedPreferenceEditor(context, SETTINGS_PREF).putBoolean(KEY_OFFERS_NOTIFICATION, offerNotificationEnabled).apply()
        }

        fun getOrderNotificationsEnabled(context: Context): Boolean {
            return getSharedPreference(context, SETTINGS_PREF).getBoolean(KEY_ORDERS_NOTIFICATION, true)
        }

        fun setOrderNotificationsEnabled(context: Context, orderNotificationEnabled: Boolean) {
            getSharedPreferenceEditor(context, SETTINGS_PREF).putBoolean(KEY_ORDERS_NOTIFICATION, orderNotificationEnabled).apply()
        }

        fun getAbandonedCartNotificationsEnabled(context: Context): Boolean {
            return getSharedPreference(context, SETTINGS_PREF).getBoolean(KEY_ABANDONED_CART_NOTIFICATION, true)
        }

        fun setAbandonedCartNotificationsEnabled(context: Context, abandonedCartNotificationEnabled: Boolean) {
            getSharedPreferenceEditor(context, SETTINGS_PREF).putBoolean(KEY_ABANDONED_CART_NOTIFICATION, abandonedCartNotificationEnabled).apply()
        }

        fun getRecentlyViewedProductsEnabled(context: Context): Boolean {
            return getSharedPreference(context, SETTINGS_PREF).getBoolean(KEY_RECENTLY_VIEWED_PRODUCTS, true)
        }

        fun setRecentlyViewedProductsEnabled(context: Context, recentlyViewedProductsEnabled: Boolean) {
            getSharedPreferenceEditor(context, SETTINGS_PREF).putBoolean(KEY_RECENTLY_VIEWED_PRODUCTS, recentlyViewedProductsEnabled).apply()
        }

        fun getSearchHistoryEnabled(context: Context): Boolean {
            return getSharedPreference(context, SETTINGS_PREF).getBoolean(KEY_SEARCH_HISTORY, true)
        }

        fun setSearchHistoryEnabled(context: Context, searchHistoryEnabled: Boolean) {
            getSharedPreferenceEditor(context, SETTINGS_PREF).putBoolean(KEY_SEARCH_HISTORY, searchHistoryEnabled).apply()
        }


        /* Configuration Related functions */

        @JvmStatic
        fun getWebsiteId(context: Context): String {
            return getSharedPreference(context, CONFIGURATION_PREF).getString(KEY_WEBSITE_ID, DEFAULT_WEBSITE_ID)?:DEFAULT_WEBSITE_ID
        }

        fun setWebsiteId(context: Context, websiteId: String) {
            getSharedPreferenceEditor(context, CONFIGURATION_PREF).putString(KEY_WEBSITE_ID, websiteId).apply()
        }

        @JvmStatic
        fun getWebsiteLabel(context: Context): String {
            return getSharedPreference(context, CONFIGURATION_PREF).getString(KEY_WEBSITE_LABEL, "")?:""
        }

        fun setWebsiteLabel(context: Context, websiteLabel: String) {
            getSharedPreferenceEditor(context, CONFIGURATION_PREF).putString(KEY_WEBSITE_LABEL, websiteLabel).apply()
        }

        @JvmStatic
        fun getStoreId(context: Context): String {
            return getSharedPreference(context, CONFIGURATION_PREF).getString(KEY_STORE_ID, DEFAULT_STORE_ID)?:DEFAULT_STORE_ID
        }

        fun setStoreId(context: Context, storeId: String) {
            getSharedPreferenceEditor(context, CONFIGURATION_PREF).putString(KEY_STORE_ID, storeId).apply()
        }

 @JvmStatic
        fun getWebsiteDataCount(context: Context): Int {
            return getSharedPreference(context, CONFIGURATION_PREF).getInt(KEY_WEBSITE_DATA_COUNT, 0)?:0
        }

        fun setWebsiteDataCount(context: Context, websiteDataCount: Int) {
            getSharedPreferenceEditor(context, CONFIGURATION_PREF).putInt(KEY_WEBSITE_DATA_COUNT, websiteDataCount).apply()
        }

        @JvmStatic
        fun getStoreCode(context: Context): String {
            return getSharedPreference(context, CONFIGURATION_PREF).getString(KEY_STORE_CODE, DEFAULT_STORE_CODE)?:DEFAULT_STORE_CODE
        }

        fun setStoreCode(context: Context, languageCode: String) {
            getSharedPreferenceEditor(context, CONFIGURATION_PREF).putString(KEY_STORE_CODE, languageCode).apply()
        }

        fun getStoreLabel(context: Context): String {
            return getSharedPreference(context, CONFIGURATION_PREF).getString(KEY_STORE_LABEL, "")?:""
        }

        fun setStoreLabel(context: Context, languageLabel: String) {
            getSharedPreferenceEditor(context, CONFIGURATION_PREF).putString(KEY_STORE_LABEL, languageLabel).apply()
        }

        @JvmStatic
        fun getCurrencyCode(context: Context): String {
            return getSharedPreference(context, CONFIGURATION_PREF).getString(KEY_CURRENCY_CODE, DEFAULT_CURRENCY_CODE)?:DEFAULT_CURRENCY_CODE
        }

        fun setCurrencyCode(context: Context, currencyCode: String) {
            getSharedPreferenceEditor(context, CONFIGURATION_PREF).putString(KEY_CURRENCY_CODE, currencyCode).apply()
        }

        @JvmStatic
        fun getCurrencyCodeSize(context: Context): Int {
            return getSharedPreference(context, CONFIGURATION_PREF).getInt(KEY_CURRENCY_CODE_SIZE, 0)
        }

        fun setCurrencyCodeSize(context: Context, currencyCode: Int) {
            getSharedPreferenceEditor(context, CONFIGURATION_PREF).putInt(KEY_CURRENCY_CODE_SIZE, currencyCode).apply()
        }

        @JvmStatic
        fun getStoreDataSize(context: Context): Int {
            return getSharedPreference(context, CONFIGURATION_PREF).getInt(KEY_STORE_DATA_SIZE, 0)
        }

        fun setStoreDataSize(context: Context, currencyCode: Int) {
            getSharedPreferenceEditor(context, CONFIGURATION_PREF).putInt(KEY_STORE_DATA_SIZE, currencyCode).apply()
        }
        fun getCurrencyLabel(context: Context): String {
            return getSharedPreference(context, CONFIGURATION_PREF).getString(KEY_CURRENCY_LABEL, "")?:""
        }

        fun setCurrencyLabel(context: Context, currencyLabel: String) {
            getSharedPreferenceEditor(context, CONFIGURATION_PREF).putString(KEY_CURRENCY_LABEL, currencyLabel).apply()
        }

        @JvmStatic
        fun isWishlistEnabled(context: Context): Boolean {
            return getSharedPreference(context, CONFIGURATION_PREF).getBoolean(KEY_IS_WISHLIST_ENABLED, false)
        }

        fun setIsWishlistEnabled(context: Context, isWishlistEnabled: Boolean) {
            getSharedPreferenceEditor(context, CONFIGURATION_PREF).putBoolean(KEY_IS_WISHLIST_ENABLED, isWishlistEnabled).apply()
        }

        fun getFcmToken(context: Context): String? {
            return getSharedPreference(context, CONFIGURATION_PREF).getString(KEY_FCM_TOKEN, null)
        }

        fun setFcmToken(context: Context, fcmToken: String?) {
            getSharedPreferenceEditor(context, CONFIGURATION_PREF).putString(KEY_FCM_TOKEN, fcmToken).apply()
        }

        @JvmStatic
        fun getViewType(context: Context): Int {
            return getSharedPreference(context, CONFIGURATION_PREF).getInt(KEY_CATALOG_VIEW_TYPE, VIEW_TYPE_GRID)
        }

        fun setViewType(context: Context, viewType: Int) {
            getSharedPreferenceEditor(context, CONFIGURATION_PREF).putInt(KEY_CATALOG_VIEW_TYPE, viewType).apply()
        }

        @JvmStatic
        fun getIsArSupported(context: Context): Boolean {
            return getSharedPreference(context, CONFIGURATION_PREF).getBoolean(KEY_IS_AR_SUPPORTED, false)
        }

        fun setIsArSupported(context: Context, isArSupported: Boolean) {
            getSharedPreferenceEditor(context, CONFIGURATION_PREF).putBoolean(KEY_IS_AR_SUPPORTED, isArSupported).apply()
        }

        @JvmStatic
        fun getCategoryData(context: Context): ArrayList<Category> {
            val categoryData = getSharedPreference(context, CONFIGURATION_PREF).getString(KEY_CATEGORY_DATA, "")
            if (categoryData.isNullOrBlank()) {
                return ArrayList()
            } else {
                return Gson().fromJson(categoryData, object : TypeToken<ArrayList<Category>>() {}.type)
            }
        }

        fun setCategoryData(context: Context, categories: ArrayList<Category>) {
            getSharedPreferenceEditor(context, CONFIGURATION_PREF).putString(KEY_CATEGORY_DATA, Gson().toJson(categories)).apply()
        }

        fun showSplash(context: Context): Boolean {
            return getSharedPreference(context, CONFIGURATION_PREF).getBoolean(KEY_SHOW_SPLASH, true)
        }

        fun setShowSplash(context: Context, showSplash: Boolean) {
            getSharedPreferenceEditor(context, CONFIGURATION_PREF).putBoolean(KEY_SHOW_SPLASH, showSplash).apply()
        }


        /* Customer Related functions */

        fun getQuoteId(context: Context): Int {
            return getSharedPreference(context, CUSTOMER_PREF).getInt(KEY_QUOTE_ID, 0)
        }

        fun setQuoteId(context: Context, quoteId: Int) {
            getSharedPreferenceEditor(context, CUSTOMER_PREF).putInt(KEY_QUOTE_ID, quoteId).apply()
        }

        fun getCartCount(context: Context): Int {
            return getSharedPreference(context, CUSTOMER_PREF).getInt(KEY_CART_COUNT, 0)
        }

        fun setCartCount(context: Context, cartCount: Int) {
            getSharedPreferenceEditor(context, CUSTOMER_PREF).putInt(KEY_CART_COUNT, cartCount).apply()
        }

        @JvmStatic
        fun isLoggedIn(context: Context): Boolean {
            return getSharedPreference(context, CUSTOMER_PREF).getBoolean(KEY_LOGGED_IN, false)
        }


        fun getCustomerToken(context: Context): String {
            return getSharedPreference(context, CUSTOMER_PREF).getString(KEY_CUSTOMER_TOKEN, "")?:""
        }

        fun getCustomerId(context: Context): String {
            return getSharedPreference(context, CUSTOMER_PREF).getString(KEY_CUSTOMER_ID, "")?:""
        }

        @JvmStatic
        fun getCustomerName(context: Context): String {
            return getSharedPreference(context, CUSTOMER_PREF).getString(KEY_CUSTOMER_NAME, "")?:""
        }

        @JvmStatic
        fun getCustomerEmail(context: Context): String {
            return getSharedPreference(context, CUSTOMER_PREF).getString(KEY_CUSTOMER_EMAIL, "")?:""
        }

        @JvmStatic
        fun getCustomerImageUrl(context: Context): String {
            return getSharedPreference(context, CUSTOMER_PREF).getString(KEY_CUSTOMER_IMAGE_URL, "")?:""
        }

        fun setCustomerImageUrl(context: Context, imageUrl: String) {
            getSharedPreferenceEditor(context, CUSTOMER_PREF).putString(KEY_CUSTOMER_IMAGE_URL, imageUrl).apply()
        }

        @JvmStatic
        fun getCustomerImageDominantColor(context: Context): String {
            return getSharedPreference(context, CUSTOMER_PREF).getString(KEY_CUSTOMER_IMAGE_DOMINANT_COLOR, "#03DAC6")?:"#03DAC6"
        }

        @JvmStatic
        fun getCustomerBannerUrl(context: Context): String {
            return getSharedPreference(context, CUSTOMER_PREF).getString(KEY_CUSTOMER_BANNER_URL, "")?:""
        }

        fun setCustomerBannerUrl(context: Context, imageUrl: String) {
            getSharedPreferenceEditor(context, CUSTOMER_PREF).putString(KEY_CUSTOMER_BANNER_URL, imageUrl).apply()
        }

        @JvmStatic
        fun getCustomerBannerDominantColor(context: Context): String {
            return getSharedPreference(context, CUSTOMER_PREF).getString(KEY_CUSTOMER_BANNER_DOMINANT_COLOR, "#400099")?:"#400099"
        }

        fun getCustomerCachedNewAddress(context: Context): AddressDetailsData {
            if (getSharedPreference(context, CUSTOMER_PREF).getString(KEY_CUSTOMER_CACHED_NEW_ADDRESS, "").isNullOrBlank()) {
                return AddressDetailsData()
            } else {
                return Gson().fromJson(getSharedPreference(context, CUSTOMER_PREF).getString(KEY_CUSTOMER_CACHED_NEW_ADDRESS, ""), AddressDetailsData::class.java)
            }
        }

        fun setCustomerCachedNewAddress(context: Context, newAddress: AddressDetailsData) {
            getSharedPreferenceEditor(context, CUSTOMER_PREF).putString(KEY_CUSTOMER_CACHED_NEW_ADDRESS, Gson().toJson(newAddress)).apply()
        }

        /*Price Format*/

        fun getPattern(context: Context): String {
            return getSharedPreference(context, PRICE_FORMAT_PREF).getString(KEY_PRICE_PATTERN, "")?:""
        }

        fun getPrecision(context: Context): Int {
            return getSharedPreference(context, PRICE_FORMAT_PREF).getInt(KEY_PRICE_PRECISION, 2)
        }

        /*Fingerprint*/

        fun getCustomerFingerUserName(context: Context): String {
            return getSharedPreference(context, FINGERPRINT_PREF).getString(KEY_CUSTOMER_FINGER_USER_NAME, "")?:""
        }

        fun setCustomerFingerUserName(context: Context, username: String) {
            getSharedPreferenceEditor(context, FINGERPRINT_PREF).putString(KEY_CUSTOMER_FINGER_USER_NAME, username).apply()
        }

        fun getCustomerFingerPassword(context: Context): String {
            return getSharedPreference(context, FINGERPRINT_PREF).getString(KEY_CUSTOMER_FINGER_PASSWORD, "")?:""
        }

        fun setCustomerFingerPassword(context: Context, password: String) {
            getSharedPreferenceEditor(context, FINGERPRINT_PREF).putString(KEY_CUSTOMER_FINGER_PASSWORD, password).apply()
        }


        fun setApiKey(context: Context, apiKey: String) {
            getSharedPreferenceEditor(context, CUSTOMER_PREF).putString(KEY_API_KEY, apiKey).commit()
        }

        fun getApiKey(context: Context): String {
            return getSharedPreference(context, CUSTOMER_PREF).getString(KEY_API_KEY, "")?:""
        }

        /*  Home page data */

        fun getHomePageData(context: Context): String? {
            return getSharedPreference(context, HOME_PAGE_DATA_PREF).getString(KEY_HOME_PAGE_DATA,"")
        }

        fun setHomePageData(context: Context, data: String) {
            getSharedPreferenceEditor(context, HOME_PAGE_DATA_PREF).putString(KEY_HOME_PAGE_DATA, data).apply()
        }

    }
}