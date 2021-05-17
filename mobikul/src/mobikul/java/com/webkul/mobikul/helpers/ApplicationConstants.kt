package com.webkul.mobikul.helpers

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

object ApplicationConstants {

    /* Server Configurations */


    //    const val BASE_URL = "http://161.97.110.25"
//    const val BASE_URL = "http://18.158.155.18"
    const val BASE_URL = "https://buitanda.com" //Live instance
//    const val BASE_URL = "https://buitanda.ezxdemo.com" //demo url
    const val API_USER_NAME = "mobikul"
    const val API_PASSWORD = "#uU=T{t{m~Tv2PxG"


    const val DEMO_USERNAME = ""
    const val DEMO_PASSWORD = ""

    /* FCM Topic */
    val DEFAULT_FCM_TOPICS = arrayOf("buitanda_android")

    /* ALLOWED PAYMENT METHODS */
    private const val PAYMENT_CODE_COD = "cashondelivery"
    private const val PAYMENT_CODE_BANK_TRANSFER = "banktransfer"
    private const val PAYMENT_CODE_CHECK_MONEY_ORDER = "checkmo"
    const val PAYMENT_MP_WALLET = "walletsystem"

    val AVAILABLE_PAYMENT_METHOD = arrayOf(PAYMENT_CODE_COD, PAYMENT_CODE_BANK_TRANSFER, PAYMENT_CODE_CHECK_MONEY_ORDER, PAYMENT_MP_WALLET)

    /* Font Path */
    const val CALLIGRAPHY_FONT_PATH_SEMI_BOLD = "fonts/Montserrat-SemiBold.ttf"
    const val CALLIGRAPHY_FONT_PATH_REGULAR = "fonts/Montserrat-Regular.ttf"

    /* Features Constants */
    const val ENABLE_WEBSITE = true
    const val ENABLE_STORES = true
    const val ENABLE_CURRENCIES = true
    const val ENABLE_COMPARE_PRODUCTS = true
    const val ENABLE_WISHLIST = true
    const val ENABLE_CMS_PAGES = true
    const val ENABLE_OFFLINE_MODE = true
    const val ENABLE_SHIMMER_EFFECT = false
    const val ENABLE_HOME_BANNER_AUTO_SCROLLING = true
    const val ENABLE_AR_CORE = true
    const val ENABLE_IMAGE_ZOOMING = true

    const val ENABLE_SPLASH_ANIMATION = false
    const val ENABLE_VERSION_CHECKER = true
    const val ENABLE_FIREBASE_ANALYTICS = false

    var DEFAULT_PAGE_COUNT = 1

    /* Configuration Constants */
    const val DEFAULT_WEBSITE_ID = "1"
    const val DEFAULT_STORE_ID = "19"
    const val DEFAULT_STORE_CODE = "pt"
    const val DEFAULT_CURRENCY_CODE = ""

    const val LOG_PARAMS = 0
    const val LOG_RESPONSE = 0

    /* Miscellaneous Constants */
    const val DEFAULT_TIME_TO_SWITCH_BANNER_IN_MILLIS = 5 * 1000
    const val DEFAULT_OS = "android"
    const val DEFAULT_NUMBER_OF_RECENTLY_VIEWED_PRODUCTS = 15
    const val DEFAULT_TIME_FOR_ABANDONED_CART_NOTIFICATION = 2 * 3600000 // 2 hours
    const val BACKSTACK_SUFFIX = "backstack"

    /* Configurations for Testing */
    const val ENABLE_KEYBOARD_OBSERVER = true
}