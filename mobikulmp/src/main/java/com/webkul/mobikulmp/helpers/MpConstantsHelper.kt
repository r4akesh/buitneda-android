package com.webkul.mobikulmp.helpers

/**
 *
 * Webkul Software.
 *
 * @category Mobikul
 * @package com.webkul.mobikulmp.network
 * @author Webkul
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 20/5/19
 *
 */

object MpConstantsHelper {

    /************************************************************
     *
     * API constants
     *
     *************************************************************/
    const val RC_CREATE_CREDIT_MEMO = 1015

    const val MOBIKUL_BECOME_PARTNER = "mobikulmphttp/marketplace/BecomeSeller"
    const val MOBIKUL_PRODUCT_SAVE_ATTRIBUTE = "mobikulmphttp/product/saveattribute"

    /*Marketplace Landing Page*/
    const val MOBIKUL_MARKETPLACE_LANDING_PAGE_DATA = "mobikulmphttp/marketplace/landingPageData"
    const val MOBIKUL_SELLER_LIST = "mobikulmphttp/marketplace/sellerList"
    const val MOBIKUL_SELLER_COLLECTION = "mobikulmphttp/marketplace/sellerCollection"


    /* AskQuestionsToAdmin */
    const val MOBIKUL_CONTACT_ADMIN = "mobikulmphttp/marketplace/askQuestionToAdmin"

    /*Edit Seller Profile*/
    const val MOBIKUL_SELLER_PROFILE_FORM_DATA = "mobikulmphttp/marketplace/profileFormData"
    const val MOBIKUL_SELLER_PROFILE_DELETE_IMAGE = "mobikulmphttp/marketplace/deleteSellerImage"
    const val MOBIKUL_SELLER_SAVE_PROFILE = "mobikulmphttp/marketplace/SaveProfile"
    const val MOBIKUL_CREDIT_MEMO_FORM_DATA = "mobikulmphttp/marketplace/creditMemoFormData"
    const val MOBIKUL_CREATE_CREDIT_MEMO = "mobikulmphttp/marketplace/createcreditmemo"

    /*Edit Seller Profile*/
    const val MOBIKUL_SELLER_PROFILE_DATA = "mobikulmphttp/marketplace/SellerProfile/"
    const val MOBIKUL_CONTACT_SELLER = "mobikulmphttp/marketplace/contactSeller/"
    const val MOBIKUL_SELLER_REVIEW_LIST = "mobikulmphttp/marketplace/SellerReviews/"
    const val MOBIKUL_SELLER_SAVE_REVIEW = "mobikulmphttp/marketplace/saveReview/"

    /*Add Edit Product*/
    const val MOBIKUL_PRODUCT_NEW_FORM_DATA = "mobikulmphttp/product/newformdata"
    const val MOBIKUL_PRODUCT_CHECK_SKU = "mobikulmphttp/product/checkSku"
    const val MOBIKUL_PRODUCT_DOWNLOADABLE_FILE_UPLOAD = "mobikulmphttp/product/DownloadableFileUpload"
    const val MOBIKUL_PRODUCT_SAVE_PRODUCT = "mobikulmphttp/product/SaveProduct"
    const val UPLOAD_PIC_CONTROLLER = "/mobikulmphttp/product/UploadProductImage/sellerId/"
    const val MOBIKUL_PRODUCT_RELATED_PRODUCT = "/mobikulmphttp/product/relatedProductData"
    const val MOBIKUL_PRODUCT_UPSELL_PRODUCT = "/mobikulmphttp/product/upsellProductData"
    const val MOBIKUL_PRODUCT_CROSS_SELL_PRODUCT = "/mobikulmphttp/product/crosssellProductData"

    /*Seller Products*/
    const val MOBIKUL_SELLER_PRODUCTS_LIST = "mobikulmphttp/marketplace/productlist"
    const val MOBIKUL_SELLER_MASS_DELETE_PRODUCT = "mobikulmphttp/marketplace/productMassDelete"
    const val MOBIKUL_SELLER_DELETE_PRODUCT = "mobikulmphttp/marketplace/productDelete"
    const val MOBIKUL_SELLER_VIEW_TRANSACTION = "mobikulmphttp/marketplace/viewTransaction"
    const val MOBIKUL_SELLER_ORDER_LIST_DATA = "mobikulmphttp/marketplace/orderList"
    const val MOBIKUL_SELLER_ORDER_DETAILS_DATA = "mobikulmphttp/marketplace/viewOrder"
    const val MOBIKUL_SEND_ORDER_MAIL = "mobikulmphttp/marketplace/sendorderemail"
    const val MOBIKUL_CANCEL_ORDER = "mobikulmphttp/marketplace/cancelOrder"
    const val MOBIKUL_CREATE_INVOICE = "mobikulmphttp/marketplace/createInvoice"
    const val MOBIKUL_CREATE_SHIPMENT = "mobikulmphttp/marketplace/createShipment"
    const val MOBIKUL_INVOICE_DETAILS = "mobikulmphttp/marketplace/viewinvoice"
    const val MOBIKUL_SEND_INVOICE_MAIL = "mobikulmphttp/marketplace/sendinvoice"
    const val MOBIKUL_SHIPMENT_DETAILS = "mobikulmphttp/marketplace/viewshipment"
    const val MOBIKUL_SEND_TRACKING_INFO = "mobikulmphttp/marketplace/sendTrackinginfo"
    const val MOBIKUL_CREDIT_MEMO_DETAILS = "mobikulmphttp/marketplace/viewCreditMemo"
    const val MOBIKUL_SEND_CREDIT_MEMO_MAIL = "mobikulmphttp/marketplace/SendCreditmemo"

    /*Seller Transaction*/
    const val MOBIKUL_SELLER_TRANSACTIONS_LIST = "mobikulmphttp/marketplace/transactionList"
    const val MOBIKUL_SELLER_TRANSACTIONS_MAKE_WITHDRAWAL = "mobikulmphttp/marketplace/withdrawalRequest"
    const val MOBIKUL_DOWNLOAD_TRANSACTION_LIST = "/mobikulmphttp/marketplace/downloadTransactionList"

    /*Print PDF Header Info*/
    const val MOBIKUL_PDF_HEADER_FORM_DATA = "mobikulmphttp/marketplace/pdfHeaderFormData"
    const val MOBIKUL_SAVE_PDF_HEADER = "mobikulmphttp/marketplace/savePdfHeader"

    /*Seller/ Admin Chat*/
    const val MOBIKUL_CHAT_SELLER_LIST = "mobikulmphttp/chat/sellerList"
    const val MOBIKUL_CHAT_NOTIFY_ADMIN = "mobikulmphttp/chat/notifyAdmin"

    /*Seller Dashboard*/
    const val MOBIKUL_SELLER_DASHBOARD_DATA = "mobikulmphttp/marketplace/Dashboard"

    /*Customer List*/
    const val MOBIKUL_CUSTOMER_LIST = "mobikulmphttp/seller/customerlist"

    /*Report*/
    const val MOBIKUL_SAVE_SELLER_REPORT = "mobikulmphttp/marketplace/savesellerreport"
    const val MOBIKUL_SAVE_PRODUCT_REPORT = "mobikulmphttp/marketplace/saveproductreport"

    //#Delivery boy
    const val MOBIKUL_DELIVERY_BOY_LIST = "expressdelivery/api_admin/getdeliveryboylist"
    const val MOBIKUL_DELIVERY_BOY_ASSIGN_ORDER = "expressdelivery/api_admin/assignorder"

    /*AUCTION*/
    const  val MOBIKUL_AUCTION_PRODUCT_LIST_FOR_AUCTION = "mobikulmpauction/api/productlistforauction"
    const val MOBIKUL_AUCTION_LIST = "mobikulmpauction/api/auctionlist"
    const val MOBIKUL_AUCTION_FORM_DATA = "mobikulmpauction/api/auctionformdata"
    const val MOBIKUL_AUCTION_MASS_DISABLE = "mobikulmpauction/api/massdisable"
    const val MOBIKUL_AUCTION_BID_DETAILS = "mobikulmpauction/api/auctionbiddetails"
    const val MOBIKUL_AUCTION_SAVE = "mobikulmpauction/api/saveauction"

    /************************************************************
     *
     * Request Code Constants
     *
     ************************************************************/
    const val RC_EDIT_SELLER_PRODUCT = 1011
}
