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

object ConstantsHelper {

    /************************************************************
     *
     * API constants
     *
     *************************************************************/

    /* Catalog */
    const val MOBIKUL_CATALOG_HOME_PAGE_DATA = "mobikulhttp/catalog/homepagedata"
    const val MOBIKUL_CATALOG_PROMOTION_DATA = "rest/V1/promotion-banner"
    const val MOBIKUL_CATALOG_SUB_CATEGORY = "mobikulhttp/catalog/categoryPageData"
    const val MOBIKUL_CATALOG_CATEGORY_LIST = "mobikulhttp/catalog/getCategoryList"
    const val MOBIKUL_CATALOG_CATEGORY_PRODUCT_LIST = "mobikulhttp/catalog/productCollection"
    const val MOBIKUL_CATALOG_BRAND_PRODUCT_LIST = "mobikulhttp/catalog/brandproducts"
    const val MOBIKUL_CATALOG_ADVANCED_SEARCH_FORM_DATA = "mobikulhttp/catalog/advancedSearchFormData"
    const val MOBIKUL_CATALOG_ADD_TO_WISH_LIST = "mobikulhttp/catalog/addtoWishlist"
    const val MOBIKUL_CATALOG_PRODUCT_PAGE_DATA = "mobikulhttp/catalog/productPageData"
    const val MOBIKUL_CATALOG_PRODUCT_REVIEW_LIST = "mobikulhttp/catalog/reviewsandratings"
    const val MOBIKUL_CATALOG_RATING_FORM_DATA = "mobikulhttp/catalog/ratingformdata"
    const val MOBIKUL_CATALOG_ADD_TO_COMPARE = "mobikulhttp/catalog/addtocompare"
    const val MOBIKUL_CATALOG_COMPARE_LIST = "mobikulhttp/catalog/comparelist"
    const val MOBIKUL_CATALOG_REMOVE_FROM_COMPARE_LIST = "mobikulhttp/catalog/removefromcompare"

    /* Customer */
    const val MOBIKUL_CUSTOMER_LOGIN = "mobikulhttp/customer/logIn"
    const val MOBIKUL_CUSTOMER_FORGOT_PASSWORD = "mobikulhttp/customer/forgotpassword"
    const val MOBIKUL_CUSTOMER_CREATE_ACCOUNT_FORM_DATA = "mobikulhttp/customer/createAccountFormData"
    const val MOBIKUL_CUSTOMER_CREATE_ACCOUNT = "mobikulhttp/customer/createAccount"
    const val MOBIKUL_CUSTOMER_ORDER_LIST = "mobikulhttp/customer/orderList"
    const val MOBIKUL_CUSTOMER_MY_DOWNLOADS_LIST = "mobikulhttp/customer/myDownloadsList"
    const val MOBIKUL_CUSTOMER_WISH_LIST = "mobikulhttp/customer/wishList"
    const val MOBIKUL_CUSTOMER_REVIEW_LIST = "mobikulhttp/customer/reviewList"
    const val MOBIKUL_CUSTOMER_GET_ADDRESS_BOOK_DATA = "mobikulhttp/customer/addressBookData"
    const val MOBIKUL_CUSTOMER_GET_ACCOUNT_INFO_DATA = "mobikulhttp/customer/accountinfoData"
    const val MOBIKUL_CUSTOMER_SAVE_ACCOUNT_INFO = "mobikulhttp/customer/saveAccountInfo"
    const val MOBIKUL_CUSTOMER_REORDER = "mobikulhttp/customer/reOrder"
    const val MOBIKUL_CUSTOMER_GUEST_REORDER = "mobikulhttp/sales/guestreorder"
    const val MOBIKUL_CUSTOMER_ORDER_DETAIL = "mobikulhttp/customer/orderDetails"
    const val MOBIKUL_CUSTOMER_DOWNLOAD_PRODUCT = "mobikulhttp/customer/downloadProduct"
    const val MOBIKUL_CUSTOMER_REVIEW_DETAIL = "mobikulhttp/customer/reviewDetails"
    const val MOBIKUL_CUSTOMER_REMOVE_FROM_WISH_LIST = "mobikulhttp/customer/removefromWishlist"
    const val MOBIKUL_CUSTOMER_WISHLIST_TO_CART = "mobikulhttp/customer/wishlisttoCart"
    const val MOBIKUL_CUSTOMER_ADDRESS_FORM_DATA = "mobikulhttp/customer/addressformData"
    const val MOBIKUL_CUSTOMER_PRODUCT_NOT_FOUND = "/rest/V1/suggestion-product"
    const val MOBIKUL_CUSTOMER_SAVE_ADDRESS = "mobikulhttp/customer/saveAddress"
    const val MOBIKUL_CUSTOMER_ADD_ALL_TO_CART = "mobikulhttp/sales/alltocart"
    const val MOBIKUL_CUSTOMER_SHARE_WISH_LIST = "mobikulhttp/sales/sharewishlist"
    const val MOBIKUL_CUSTOMER_UPLOAD_PROFILE_PIC = "mobikulhttp/index/uploadprofilepic"
    const val MOBIKUL_CUSTOMER_UPLOAD_BANNER_IMAGE = "mobikulhttp/index/uploadbannerpic"
    const val MOBIKUL_CUSTOMER_UPDATE_WISH_LIST = "mobikulhttp/customer/updateWishlist"
    const val MOBIKUL_CUSTOMER_SAVE_REVIEW = "mobikulhttp/customer/saveReview"
    const val MOBIKUL_CUSTOMER_DELETE_ADDRESS = "mobikulhttp/customer/deleteAddress"
    const val MOBIKUL_CUSTOMER_VIEW_INVOICE = "mobikulhttp/customer/invoiceview"
    const val MOBIKUL_CUSTOMER_VIEW_REFUND = "mobikulhttp/customer/creditview"
    const val MOBIKUL_CUSTOMER_VIEW_SHIPMENT = "mobikulhttp/customer/shipmentview"
    const val MOBIKUL_CUSTOMER_CHECK_CUSTOMER_BY_EMAIL = "mobikulhttp/customer/checkCustomerByEmail"
    const val MOBIKUL_CUSTOMER_WEB_LOGIN = "mobikul/qrcodelogin/savecustomertoken"

    /* Extra */
    const val MOBIKUL_EXTRAS_LOGOUT = "mobikulhttp/extra/logOut"
    const val MOBIKUL_EXTRAS_SEARCH_SUGGESTION = "mobikulhttp/extra/searchSuggestion"
    const val MOBIKUL_EXTRAS_SEARCH_TERMS_LIST = "mobikulhttp/extra/searchTermList"
    const val MOBIKUL_EXTRAS_CMS_PAGES_DATA = "mobikulhttp/extra/cmsData"
    const val MOBIKUL_EXTRAS_NOTIFICATION_LIST = "mobikulhttp/extra/notificationList"
    const val MOBIKUL_EXTRAS_OTHER_NOTIFICATION_DATA = "mobikulhttp/extra/otherNotificationData"
    const val MOBIKUL_EXTRAS_SAVE_ANDROID_TOKEN = "mobikulhttp/extra/registerDevice"

    /*Other*/
    const val MOBIKUL_CONTACT_POST = "mobikulhttp/contact/post"
    const val MOBIKUL_PRODUCT_ALERT_PRICE = "mobikulhttp/productalert/price"
    const val MOBIKUL_PRODUCT_ALERT_STOCK = "mobikulhttp/productalert/stock"
    const val MOBIKUL_SALES_GUEST_VIEW = "mobikulhttp/sales/guestview"
    const val MOBIKUL_AUTO_RELATED_PRODUCT = "rest/V1/automatic-related-products"

    /* Checkout */
    const val MOBIKUL_CHECKOUT_ADD_TO_CART = "mobikulhttp/checkout/addtoCart"
    const val MOBIKUL_CHECKOUT_UPDATE_ITEM_OPTION = "mobikulhttp/checkout/updateitemoptions"
    const val MOBIKUL_CHECKOUT_CART_DETAILS = "mobikulhttp/checkout/cartDetails"
    const val MOBIKUL_CHECKOUT_WISH_LIST_FROM_CART = "mobikulhttp/checkout/wishlistfromCart"
    const val MOBIKUL_CHECKOUT_REMOVE_CART_ITEM = "mobikulhttp/checkout/removeCartItem"
    const val MOBIKUL_CHECKOUT_APPLY_COUPON = "mobikulhttp/checkout/applyCoupon"
    const val MOBIKUL_CHECKOUT_UPDATE_CART = "mobikulhttp/checkout/updateCart"
    const val MOBIKUL_CHECKOUT_EMPTY_CART = "mobikulhttp/checkout/emptyCart"
    const val MOBIKUL_CHECKOUT_ADDRESS_INFO = "mobikulhttp/checkout/checkoutaddress"
    const val MOBIKUL_CHECKOUT_SHIPPING_METHODS = "mobikulhttp/checkout/shippingmethods"
    const val MOBIKUL_CHECKOUT_REVIEWS_AND_PAYMENTS = "mobikulhttp/checkout/reviewandpayment"
    const val MOBIKUL_CHECKOUT_PLACE_ORDER = "mobikulhttp/checkout/placeorder"

    /* Delivery Boy */
    const val MOBIKUL_DELIVERY_BOY_GET_LOCATION = "expressdelivery/api/GetLocation"
    const val MOBIKUL_DELIVERY_BOY_UPDATE_TOKEN_ON_CLOUD = "https://us-central1-mobikul-magento2-app.cloudfunctions.net/updateTokenToDataBase"
    const val MOBIKUL_DELIVERY_BOY_DELETE_TOKEN_FROM_CLOUD = "https://us-central1-mobikul-magento2-app.cloudfunctions.net/deleteTokenFromDataBase"
    const val MOBIKUL_DELIVERY_BOY_ADD_REVIEW = "expressdelivery/api_customer/addreview"
    const val MOBIKUL_DELIVERY_TOP_PRODUCT_LIST = "mobikulhttp/catalog/topsellingproducts"


    /* Wallet */
    const val MOBIKUL_WALLET_MANAGE = "mobikulwallet/index/index"
    const val MOBIKUL_WALLET_ADD = "mobikulwallet/index/add"
    const val MOBIKUL_WALLET_ACCOUNT_DETAILS = "rest/V1/customer/accontdetails"
    const val MOBIKUL_WALLET_SAVE_ACCOUNT_INFO = "rest/V1/customer/save-account-information"
    const val MOBIKUL_WALLET_DELETE_ACCOUNT_INFO = "rest/V1/customer/request-delete"
    const val MOBIKUL_WALLET_TRANSFER = "mobikulwallet/index/transfer"
    const val MOBIKUL_WALLET_SEND_CODE = "mobikulwallet/index/sendcode"
    const val MOBIKUL_WALLET_SEND_AMOUNT = "mobikulwallet/index/sendamount"
    const val MOBIKUL_WALLET_TRANSFER_BANK = "mobikulwallet/index/bankaccount"
    const val MOBIKUL_WALLET_VIEW_TRANSACTION = "mobikulwallet/index/view"
    const val MOBIKUL_WALLET_ADD_PAYEE = "mobikulwallet/index/addpayee"
    const val MOBIKUL_WALLET_UPDATE_PAYEE = "mobikulwallet/index/updatepayee"
    const val MOBIKUL_WALLET_DELETE_PAYEE = "mobikulwallet/index/deletepayee"
    const val MOBIKUL_WALLET_APPLY_PAYMENT_AMOUNT = "mobikulwallet/index/Applypaymentamount"


    //auction
    const val MOBIKUL_AUCTION_BID_LIST = "mobikulmpauction/api/AuctionbidList"
    const val MOBIKUL_AUTO_BID_LIST = "mobikulmpauction/api/autobidList"
    const val MOBIKUL_ADD_BID = "mobikulmpauction/api/addbid"
    const val MOBIKUL_INVOICE = "rest/V1/invoiceapi/post"

    //serviceProvider Apis
    const val MOBIKUL_HOME_TOP_BANNER = "rest/V1/emizentech-serviceprovider/homepage-top-banner"
    const val MOBIKUL_HOME_SERVICE_LIST = "rest/V1/emizentech-serviceprovider/homepage-services"
    const val MOBIKUL_HOME_PAGE_BANNER_LIST = "rest/V1/emizentech-serviceprovider/homepage-banner"
    const val MOBIKUL_SERVICE_PROVIDER_LIST = "rest/V1/emizentech-serviceprovider/serviceprovider/"
    const val MOBIKUL_SERVICE_PROVIDER_INFO = "rest/V1/emizentech-serviceprovider/serviceproviderbyid"
    const val MOBIKUL_SERVICE_PROVIDER_REVIEW_LIST = "rest/V1/emizentech-serviceprovider/review"
    const val MOBIKUL_SERVICE_SUBMIT_REVIEW = "rest/V1/emizentech-serviceprovider/review-submit"
    const val MOBIKUL_SERVICE_ALL_SERVICE_LIST = "rest/V1/emizentech-serviceprovider/services/"
    const val MOBIKUL_SERVICE_AREA_LIST = "rest/V1/emizentech-serviceprovider/area"
    const val MOBIKUL_SERVICE_VISITOR = "rest/V1/emizentech-serviceprovider/visitor"
    const val MOBIKUL_SEARCH_SERVICE = "rest/V1/emizentech-serviceprovider/service-search"
    const val MOBIKUL_ICON_BANNER_SERVICE = "rest/V1/emizentech-serviceprovider/icon-banner"





    /************************************************************
     *
     * Request Code Constants
     *
     ************************************************************/
    const val RC_LOGIN = 1001
    const val RC_SIGN_UP = 1002
    const val RC_WRITE_TO_EXTERNAL_STORAGE = 1003
    const val RC_PICK_IMAGE = 1004
    const val RC_VOICE = 1005
    const val RC_CAMERA_SEARCH = 1006
    const val RC_PICK_SINGLE_FILE = 1007
    const val RC_ADD_EDIT_ADDRESS = 1008
    const val RC_PAYMENT = 1009
    const val RC_GOOGLE_SIGN_IN = 1010
    const val RC_AR = 1011
    const val RC_LOCATION_PERMISSION = 1012
    const val RC_CHECK_LOCATION_SETTINGS = 1013
    const val RC_UPDATE_APP_FROM_PLAY_STORE = 1014
    const val RC_PLACE_PICKER = 1014
    const val RC_QR_LOGIN = 1015
    const val RC_OPEN_SCANNER = 1022
    const val RC_AR_MEASURE = 1016
    const val APP_UPDATE_REQUEST_CODE = 1017


    /************************************************************
     *
     * API Errors Constants
     *
     ************************************************************/
    const val CUSTOMER_NOT_EXIST = "customerNotExist"


    /************************************************************
     *
     * Order Constants
     *
     ************************************************************/
    const val ORDER_STATUS_COMPLETE = "complete"
    const val ORDER_STATUS_PENDING = "pending"
    const val ORDER_STATUS_PROCESSING = "processing"
    const val ORDER_STATUS_HOLD = "holded"
    const val ORDER_STATUS_CANCELLED = "canceled"
    const val ORDER_STATUS_NEW = "new"
    const val ORDER_STATUS_CLOSED = "closed"


    /************************************************************
     *
     * Catalog View Type
     *
     ************************************************************/
    const val VIEW_TYPE_LIST = 1
    const val VIEW_TYPE_GRID = 2


    /************************************************************
     *
     * Login Register Type
     *
     ************************************************************/
    const val OPEN_LOGIN_PAGE = 1
    const val OPEN_SIGN_UP_PAGE = 2

    const val DEFAULT_DATE_FORMAT = "MM/dd/yy"


    /****************************Event title const****************/
    const val HOME_CATEGORY_BANNER = "home_category_banner"
    const val HOME_FEATURE_CATEGORY = "home_feature_category"
    const val HOME_FEATURE_PRODUCT = "home_feature_product"
    const val HOME_PRODUCT_BANNER = "home_product_banner"
    const val HOME_PRODUCT = "home_product"

}