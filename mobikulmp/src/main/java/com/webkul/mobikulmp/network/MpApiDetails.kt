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

package com.webkul.mobikulmp.network

import com.webkul.mobikul.helpers.ConstantsHelper
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.models.catalog.CatalogProductsResponseModel
import com.webkul.mobikulmp.deliveryboy.models.GetDeliveryBoyListResponseData
import com.webkul.mobikulmp.helpers.MpConstantsHelper
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_AUCTION_BID_DETAILS
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_AUCTION_FORM_DATA
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_AUCTION_LIST
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_AUCTION_MASS_DISABLE
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_AUCTION_PRODUCT_LIST_FOR_AUCTION
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_AUCTION_SAVE
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_BECOME_PARTNER
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_CANCEL_ORDER
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_CHAT_NOTIFY_ADMIN
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_CHAT_SELLER_LIST
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_CONTACT_ADMIN
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_CONTACT_SELLER
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_CREATE_CREDIT_MEMO
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_CREATE_INVOICE
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_CREATE_SHIPMENT
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_CREDIT_MEMO_DETAILS
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_CREDIT_MEMO_FORM_DATA
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_CUSTOMER_LIST
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_DELIVERY_BOY_ASSIGN_ORDER
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_INVOICE_DETAILS
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_MARKETPLACE_LANDING_PAGE_DATA
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_PDF_HEADER_FORM_DATA
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_PRODUCT_CHECK_SKU
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_PRODUCT_DOWNLOADABLE_FILE_UPLOAD
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_PRODUCT_NEW_FORM_DATA
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_PRODUCT_SAVE_ATTRIBUTE
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_PRODUCT_SAVE_PRODUCT
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_SAVE_PDF_HEADER
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_SAVE_PRODUCT_REPORT
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_SAVE_SELLER_REPORT
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_SELLER_COLLECTION
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_SELLER_DASHBOARD_DATA
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_SELLER_DELETE_PRODUCT
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_SELLER_LIST
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_SELLER_MASS_DELETE_PRODUCT
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_SELLER_ORDER_DETAILS_DATA
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_SELLER_ORDER_LIST_DATA
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_SELLER_PRODUCTS_LIST
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_SELLER_PROFILE_DATA
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_SELLER_PROFILE_DELETE_IMAGE
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_SELLER_PROFILE_FORM_DATA
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_SELLER_REVIEW_LIST
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_SELLER_SAVE_PROFILE
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_SELLER_SAVE_REVIEW
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_SELLER_TRANSACTIONS_LIST
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_SELLER_TRANSACTIONS_MAKE_WITHDRAWAL
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_SELLER_VIEW_TRANSACTION
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_SEND_CREDIT_MEMO_MAIL
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_SEND_INVOICE_MAIL
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_SEND_ORDER_MAIL
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_SEND_TRACKING_INFO
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_SHIPMENT_DETAILS
import com.webkul.mobikulmp.model.auction.AuctionFormData
import com.webkul.mobikulmp.models.BecomePartnerResponseData
import com.webkul.mobikulmp.models.CustomerListResponse
import com.webkul.mobikulmp.models.SellerListResponseData
import com.webkul.mobikulmp.models.auction.AuctionBidDetailsData
import com.webkul.mobikulmp.models.auction.GetAuctionListData
import com.webkul.mobikulmp.models.auction.GetProductListForAuctionData
import com.webkul.mobikulmp.models.chat.ChatSellerListResponseData
import com.webkul.mobikulmp.models.landingpage.MarketplaceLandingPageData
import com.webkul.mobikulmp.models.pdfheader.PdfHeaderInfoDataResponse
import com.webkul.mobikulmp.models.seller.*
import com.webkul.mobikulmp.models.sellerinfo.ProductDetailsPageModelExtended
import com.webkul.mobikulmp.models.sellermaplocation.MapResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface MpApiDetails {

    /*Marketplace Landing Page*/
    @GET(MOBIKUL_MARKETPLACE_LANDING_PAGE_DATA)
    fun getLandingPageData(
            @Query("storeId") storeId: String,
            @Query("currency") currency: String,
            @Query("width") width: Int,
            @Query("mFactor") density: Float?): Observable<MarketplaceLandingPageData>

    @GET(ConstantsHelper.MOBIKUL_CATALOG_PRODUCT_PAGE_DATA)
    fun getProductPageData(@Query("eTag") eTag: String,
                           @Query("storeId") storeId: String,
                           @Query("customerToken") customerToken: String,
                           @Query("quoteId") quoteId: Int,
                           @Query("websiteId") websiteId: String,
                           @Query("width") width: Int,
                           @Query("currency") currency: String,
                           @Query("productId") productId: String):
            Observable<ProductDetailsPageModelExtended>


    @GET(MOBIKUL_SELLER_LIST)
    fun getSellerList(
            @Query("eTag") eTag: String,
            @Query("storeId") storeId: String,
            @Query("searchQuery") searchQuery: String,
            @Query("width") width: Int,
            @Query("mFactor") density: Float?): Observable<SellerListResponseData>


    @GET(MOBIKUL_SELLER_COLLECTION)
    fun getSellerCatalogProductData(@Query("eTag") eTag: String,
                                    @Query("sellerId") sellerId: Int,
                                    @Query("storeId") storeId: String,
                                    @Query("quoteId") quoteId: Int,
                                    @Query("customerToken") customerToken: String,
                                    @Query("currency") currency: String,
                                    @Query("width") width: Int,
                                    @Query("mFactor") mFactor: Float,
                                    @Query("type") type: String,
                                    @Query("id") id: String,
                                    @Query("pageNumber") pageNumber: Int,
                                    @Query("sortData") sortData: JSONArray,
                                    @Query("filterData") filterData: JSONArray): Observable<CatalogProductsResponseModel>


    @FormUrlEncoded
    @POST(MOBIKUL_CONTACT_ADMIN)
    fun askQuestionsToAdmin(
            @Field("storeId") storeId: String,
            @Field("customerToken") customerToken: String,
            @Field("subject") subject: String,
            @Field("query") query: String): Observable<BaseModel>

    @FormUrlEncoded
    @POST(MOBIKUL_BECOME_PARTNER)
    fun sendBecomePartnerRequest(
            @Field("storeId") storeId: String,
            @Field("customerToken") customerToken: String,
            @Field("shopUrl") shopUrl: String): Observable<BecomePartnerResponseData>

    @FormUrlEncoded
    @POST(MOBIKUL_PRODUCT_SAVE_ATTRIBUTE)
    fun saveAttribute(
            @Field("storeId") storeId: String,
            @Field("attributeCode") attributeCode: String,
            @Field("attributeLabel") attributeLabel: String,
            @Field("attributeType") attributeType: String,
            @Field("attributeOption") attributeOption: JSONArray,
            @Field("isRequired") isRequired: Boolean): Observable<BaseModel>

    /*Edit Seller Profile*/
    @GET(MOBIKUL_SELLER_PROFILE_FORM_DATA)
    fun getSellerProfileFormData(
            @Query("storeId") storeId: String,
            @Query("customerToken") customerToken: String): Observable<SellerProfileFormResponseData>

    @FormUrlEncoded
    @POST(MOBIKUL_SELLER_PROFILE_DELETE_IMAGE)
    fun deleteSellerImage(
            @Field("storeId") storeId: String,
            @Field("customerToken") customerToken: String,
            @Field("entity") entity: String): Observable<BaseModel>

    @Multipart
    @POST(MOBIKUL_SELLER_SAVE_PROFILE)
    fun saveSellerProfileData(
            @Part("storeId") storeId: RequestBody,
            @Part("customerToken") customerToken: RequestBody,
            @Part("twActive") twActive: Int,
            @Part("twitterId") twitterId: RequestBody,
            @Part("fbActive") fbActive: Int,
            @Part("facebookId") facebookId: RequestBody,
            @Part("instagramActive") instagramActive: Int,
            @Part("instagramId") instagramId: RequestBody,
            @Part("gplusActive") gplusActive: Int,
            @Part("gplusId") gplusId: RequestBody,
            @Part("youtubeActive") youtubeActive: Int,
            @Part("youtubeId") youtubeId: RequestBody,
            @Part("vimeoActive") vimeoActive: Int,
            @Part("vimeoId") vimeoId: RequestBody,
            @Part("pinterestActive") pinterestActive: Int,
            @Part("pinterestId") pinterestId: RequestBody,
            @Part("contactNumber") contactNumber: RequestBody,
            @Part("taxvat") taxvat: RequestBody,
            @Part("backgroundColor") backgroundColor: RequestBody,
            @Part("shopTitle") shopTitle: RequestBody,
            @Part companyBanner: MultipartBody.Part?,
            @Part logo: MultipartBody.Part?,
            @Part("companyLocality") companyLocality: RequestBody,
            @Part("companyDescription") companyDescription: RequestBody,
            @Part("returnPolicy") returnPolicy: RequestBody,
            @Part("shippingPolicy") shippingPolicy: RequestBody,
            @Part("privacyPolicy") privacyPolicy: RequestBody,
            @Part("country") country: RequestBody,
            @Part("metaKeyword") metaKeyword: RequestBody,
            @Part("metaDescription") metaDescription: RequestBody,
            @Part("paymentDetails") paymentDetails: RequestBody): Observable<BaseModel>

    /*Seller Profile*/
    @GET(MOBIKUL_SELLER_PROFILE_DATA)
    fun getSellerProfileData(
            @Query("eTag") eTag: String,
            @Query("storeId") storeId: String,
            @Query("customerToken") customerToken: String,
            @Query("sellerId") sellerId: Int,
            @Query("currency") currency: String,
            @Query("width") width: Int,
            @Query("mFactor") density: Float?
    ): Observable<SellerProfileData>

    @FormUrlEncoded
    @POST(MOBIKUL_CONTACT_SELLER)
    fun contactSeller(
            @Field("storeId") storeId: String,
            @Field("customerToken") customerToken: String,
            @Field("sellerId") sellerId: Int,
            @Field("productId") productId: String?,
            @Field("name") customerName: String,
            @Field("email") customerEmail: String,
            @Field("subject") subject: String,
            @Field("query") query: String): Observable<BaseModel>

    @FormUrlEncoded
    @POST(MOBIKUL_SELLER_REVIEW_LIST)
    fun getSellerReviews(
            @Field("eTag") eTag: String,
            @Field("storeId") storeId: String,
            @Field("sellerId") sellerId: Int,
            @Field("width") width: Int,
            @Field("pageNumber") pageNumber: Int): Observable<SellerReviewListData>

    @FormUrlEncoded
    @POST(MOBIKUL_SELLER_SAVE_REVIEW)
    fun saveReview(
            @Field("storeId") storeId: String,
            @Field("sellerId") sellerId: Int,
            @Field("customerToken") customerToken: String,
            @Field("customerEmail") customerEmail: String,
            @Field("nickName") nickName: String,
            @Field("summary") summary: String,
            @Field("description") details: String,
            @Field("priceRating") price: Int,
            @Field("valueRating") value: Int,
            @Field("qualityRating") quality: Int,
            @Field("shopUrl") shopUrl: String): Observable<BaseModel>


    @GET(MOBIKUL_CREDIT_MEMO_FORM_DATA)
    fun getCreditMemoFormData(
            @Query("eTag") eTag: String,
            @Query("storeId") storeId: String,
            @Query("customerToken") customerToken: String,
            @Query("incrementId") incrementId: String): Observable<CreditMemoFormResponseData>


    @FormUrlEncoded
    @POST(MOBIKUL_CREATE_CREDIT_MEMO)
    fun createCreditMemo(
            @Field("storeId") storeId: String,
            @Field("customerToken") customerToken: String,
            @Field("incrementId") incrementId: String,
            @Field("item") item: JSONObject,
            @Field("comment") comment: String,
            @Field("shippingAmount") shippingAmount: String,
            @Field("adjustmentPositive") adjustmentPositive: String,
            @Field("adjustmentNegative") adjustmentNegative: String,
            @Field("doOffline") doOffline: Int,
            @Field("commentCustomerNotify") commentCustomerNotify: Int,
            @Field("isVisibleOnFront") isVisibleOnFront: Int,
            @Field("sendEmail") sendEmail: Int,
            @Field("invoiceId") invoiceId: String): Observable<BaseModel>


    /*Add Edit Product*/
    @GET(MOBIKUL_PRODUCT_NEW_FORM_DATA)
    fun getProductFormData(
            @Query("storeId") storeId: String,
            @Query("customerToken") customerToken: String,
            @Query("productId") productId: String?): Observable<SellerAddProductResponseData>

    @FormUrlEncoded
    @POST(MOBIKUL_PRODUCT_CHECK_SKU)
    fun checkSku(
            @Field("storeId") storeId: String,
            @Field("customerToken") customerToken: String,
            @Field("sku") sku: String): Observable<CheckSkuResponseData>

    @Multipart
    @POST
    fun uploadImage(
            @Url url: String,
            @Part file: MultipartBody.Part,
            @Part("width") width: RequestBody,
            @Part("mFactor") density: RequestBody): Observable<UploadPicResponseData>

    @Multipart
    @POST(MOBIKUL_PRODUCT_DOWNLOADABLE_FILE_UPLOAD)
    fun uploadDownloadableFile(
            @Part("storeId") storeId: RequestBody,
            @Part("customerToken") customerToken: RequestBody,
            @Part("type") type: RequestBody,
            @Part file: MultipartBody.Part): Observable<FileData>

    @FormUrlEncoded
    @POST(MOBIKUL_PRODUCT_SAVE_PRODUCT)
    fun saveProduct(
            @Field("storeId") storeId: String,
            @Field("customerToken") customerToken: String,
            @Field("productId") productId: String?,
            @Field("type") type: String?,
            @Field("status") status: Int,
            @Field("isFeaturedProduct") isFeaturedProduct: Int,
            @Field("attributeSetId") attributeSetId: String?,
            @Field("websiteIds") websiteIds: JSONArray?,
            @Field("categoryIds") categoryIds: JSONArray?,
            @Field("name") name: String?,
            @Field("description") description: String?,
            @Field("shortDescription") shortDescription: String?,
            @Field("sku") sku: String?,
            @Field("price") price: String?,
            @Field("specialPrice") specialPrice: String?,
            @Field("specialFromDate") specialFromDate: String?,
            @Field("specialToDate") specialToDate: String?,
            @Field("qty") qty: String?,
            @Field("isInStock") isInStock: Int?,
            @Field("visibility") visibility: String?,
            @Field("taxClassId") taxClassId: String?,
            @Field("productHasWeight") productHasWeight: Int,
            @Field("weight") weight: String?,
            @Field("metaTitle") metaTitle: String?,
            @Field("metaKeyword") metaKeyword: String?,
            @Field("metaDescription") metaDescription: String?,
            @Field("mpProductCartLimit") mpProductCartLimit: String?,
            @Field("mediaGallery") mediaGallery: JSONArray?,
            @Field("crossSell") crossSell: JSONArray?,
            @Field("upsell") upsell: JSONArray?,
            @Field("related") related: JSONArray?,
            @Field("isDownloadable") isDownloadable: String?,
            @Field("linksTitle") linksTitle: String?,
            @Field("purchasedSeparately") purchasedSeparately: String?,
            @Field("linksData") linksData: JSONArray?,
            @Field("samplesTitle") samplesTitle: String?,
            @Field("samplesData") samplesData: JSONArray?,
            @Field("deleteImageId") deleteImageId: String?,
            @FieldMap imagesData: Map<String, String>?): Observable<SaveProductResponseData>


    @GET
    fun getRelatedProductData(
            @Url url: String,
            @Query("storeId") storeId: String,
            @Query("customerToken") customerToken: String,
            @Query("productId") productId: String?,
            @Query("pageNumber") pageNumber: Int,
            @Query("sortData") sortData: JSONArray?,
            @Query("filterData") filterData: JSONArray?): Observable<AddProductFieldProductCollectionData>


    /*Seller Products*/
    @GET(MOBIKUL_SELLER_PRODUCTS_LIST)
    fun getSellerProductsListData(
            @Query("storeId") storeId: String,
            @Query("customerToken") customerToken: String,
            @Query("currency") currency: String,
            @Query("pageNumber") pageNumber: Int,
            @Query("dateTo") dateTo: String,
            @Query("dateFrom") dateFrom: String,
            @Query("productName") productName: String,
            @Query("productStatus") productStatus: String): Observable<SellerProductListResponseData>

    @FormUrlEncoded
    @POST(MOBIKUL_SELLER_DELETE_PRODUCT)
    fun deleteProduct(
            @Field("storeId") storeId: String,
            @Field("customerToken") customerToken: String,
            @Field("productId") productId: Int): Observable<BaseModel>

    /*Seller Transaction*/
    @GET(MOBIKUL_SELLER_TRANSACTIONS_LIST)
    fun getSellerTransactionsListData(
            @Query("eTag") eTag: String,
            @Query("storeId") storeId: String,
            @Query("customerToken") customerToken: String,
            @Query("pageNumber") pageNumber: Int,
            @Query("dateTo") dateTo: String,
            @Query("dateFrom") dateFrom: String,
            @Query("transactionId") transactionId: String): Observable<SellerTransactionListResponseData>

    @GET(MOBIKUL_SELLER_TRANSACTIONS_MAKE_WITHDRAWAL)
    fun makeWidthdrawal(
            @Query("customerToken") customerToken: String,
            @Query("storeId") storeId: String,
            @Query("is_requested") isRequested: Int): Observable<BaseModel>

    /*Seller Transaction*/
    @GET(MOBIKUL_SELLER_VIEW_TRANSACTION)
    fun getSellerTransactionsDetailsData(
            @Query("eTag") eTag: String,
            @Query("storeId") storeId: String,
            @Query("customerToken") customerToken: String,
            @Query("transactionId") transactionId: String): Observable<ViewTransactionResponseData>

    /*Seller Orders*/
    @GET(MOBIKUL_SELLER_ORDER_LIST_DATA)
    fun getSellerOrdersListData(
            @Query("eTag") eTag: String,
            @Query("storeId") storeId: String,
            @Query("customerToken") customerToken: String,
            @Query("pageNumber") pageNumber: Int,
            @Query("status") status: String?,
            @Query("dateTo") dateTo: String,
            @Query("dateFrom") dateFrom: String,
            @Query("incrementId") incrementId: String): Observable<SellerOrderListData>

    /* Seller Orders Details */
    @GET(MOBIKUL_SELLER_ORDER_DETAILS_DATA)
    fun getSellerOrdersDetailsData(
            @Query("eTag") eTag: String,
            @Query("storeId") storeId: String,
            @Query("customerToken") customerToken: String,
            @Query("currency") currency: String,
            @Query("incrementId") incrementId: String): Observable<SellerOrderDetailsModel>

    @FormUrlEncoded
    @POST(MOBIKUL_SEND_ORDER_MAIL)
    fun sendOrderMailData(
            @Field("storeId") storeId: String,
            @Field("customerToken") customerToken: String,
            @Field("incrementId") incrementId: String): Observable<BaseModel>

    @FormUrlEncoded
    @POST(MOBIKUL_CANCEL_ORDER)
    fun cancelOrder(
            @Field("storeId") storeId: String,
            @Field("customerToken") customerToken: String,
            @Field("incrementId") incrementId: String): Observable<BaseModel>

    @FormUrlEncoded
    @POST(MOBIKUL_CREATE_INVOICE)
    fun createInvoice(
            @Field("storeId") storeId: String,
            @Field("customerToken") customerToken: String,
            @Field("incrementId") incrementId: String): Observable<BaseModel>

    @FormUrlEncoded
    @POST(MOBIKUL_CREATE_SHIPMENT)
    fun createShipment(
            @Field("storeId") storeId: String,
            @Field("customerToken") customerToken: String,
            @Field("incrementId") incrementId: String,
            @Field("carrier") carrier: String,
            @Field("trackingId") trackingId: String): Observable<BaseModel>

    @GET(MOBIKUL_INVOICE_DETAILS)
    fun getSellerInvoiceDetails(
            @Query("eTag") eTag: String,
            @Query("storeId") storeId: String,
            @Query("customerToken") customerToken: String,
            @Query("invoiceId") invoiceId: String,
            @Query("incrementId") incrementId: String): Observable<SellerInvoiceDetailsData>

    @FormUrlEncoded
    @POST(MOBIKUL_SEND_INVOICE_MAIL)
    fun sendSellerInvoiceMail(
            @Field("storeId") storeId: String,
            @Field("customerToken") customerToken: String,
            @Field("invoiceId") invoiceId: String,
            @Field("incrementId") incrementId: String): Observable<BaseModel>

    @GET(MOBIKUL_SHIPMENT_DETAILS)
    fun getSellerShipmentDetails(
            @Query("eTag") eTag: String,
            @Query("storeId") storeId: String,
            @Query("customerToken") customerToken: String,
            @Query("shipmentId") shipmentId: String,
            @Query("incrementId") incrementId: String): Observable<SellerShipmentDetailsData>

    @FormUrlEncoded
    @POST(MOBIKUL_SEND_TRACKING_INFO)
    fun sendTrackingInfo(
            @Field("customerToken") customerToken: String?
            , @Field("incrementId") incrementId: String?
            , @Field("shipmentId") shipmentId: String?
            , @Field("storeId") storeId: String): Observable<BaseModel>

    @GET(MOBIKUL_CREDIT_MEMO_DETAILS)
    fun getSellerCreditMemoDetails(
            @Query("eTag") eTag: String,
            @Query("storeId") storeId: String,
            @Query("customerToken") customerToken: String,
            @Query("creditmemoId") creditmemoId: String,
            @Query("incrementId") incrementId: String): Observable<CreditMemoDetailsResponseData>

    @FormUrlEncoded
    @POST(MOBIKUL_SEND_CREDIT_MEMO_MAIL)
    fun sendCreditMemoMail(
            @Field("storeId") storeId: String,
            @Field("customerToken") customerToken: String,
            @Field("creditmemoId") creditmemoId: String,
            @Field("incrementId") incrementId: String): Observable<CreditMemoDetailsResponseData>

    @FormUrlEncoded
    @POST(MOBIKUL_SELLER_MASS_DELETE_PRODUCT)
    fun massDeleteProduct(
            @Field("storeId") storeId: String,
            @Field("customerToken") customerToken: String,
            @Field("productIds") productIds: JSONArray): Observable<BaseModel>


    /*Print PDF Header Info*/
    @FormUrlEncoded
    @POST(MOBIKUL_PDF_HEADER_FORM_DATA)
    fun getPdfHeaderFormData(
            @Field("storeId") storeId: String,
            @Field("customerToken") customerToken: String): Observable<PdfHeaderInfoDataResponse>

    @FormUrlEncoded
    @POST(MOBIKUL_SAVE_PDF_HEADER)
    fun savePdfHeader(
            @Field("storeId") storeId: String,
            @Field("customerToken") customerToken: String,
            @Field("pdfHeader") pdfHeader: String): Observable<BaseModel>

    /*Seller/ Admin Chat*/
    @FormUrlEncoded
    @POST(MOBIKUL_CHAT_SELLER_LIST)
    fun getChatSellerList(
            @Field("websiteId") websiteId: String,
            @Field("storeId") storeId: String,
            @Field("customerToken") customerToken: String,
            @Field("mFactor") density: Float?,
            @Field("width") width: Int): Observable<ChatSellerListResponseData>

    @FormUrlEncoded
    @POST(MOBIKUL_CHAT_NOTIFY_ADMIN)
    fun chatNotifyAdmin(
            @Field("storeId") storeId: String,
            @Field("websiteId") websiteId: String,
            @Field("message") message: String,
            @Field("customerToken") customerToken: String,
            @Field("sellerName") sellerName: String
    ): Observable<BaseModel>

    /*Seller Dashboard*/

    @GET(MOBIKUL_SELLER_DASHBOARD_DATA)
    fun getSellerDashboardData(
            @Query("customerToken") customerToken: String,
            @Query("storeId") storeId: String,
            @Query("width") width: Int,
            @Query("mFactor") mFactor: Float?): Observable<SellerDashboardData>

    /*Customer List*/

    @GET(MOBIKUL_CUSTOMER_LIST)
    fun getCustomerList(
            @Query("eTag") eTag: String,
            @Query("customerToken") customerToken: String,
            @Query("storeId") storeId: String,
            @Query("email") email: String,
            @Query("gender") gender: Int?,
            @Query("name") name: String,
            @Query("billing_full") billing_full: String,
            @Query("billing_telephone") contact: String,
            @Query("currency") currency: String,
            @Query("pageNumber") pageNumber: Int?
    ): Observable<CustomerListResponse>


    @GET
    fun getGoogleMapResponse(@Url url: String): Call<MapResponse>


    @FormUrlEncoded
    @POST(MOBIKUL_SAVE_SELLER_REPORT)
    fun saveSellerReport(
            @Field("storeId") storeId: String,
            @Field("sellerId") sellerId: String,
            @Field("otherReason") otherReason: String,
            @Field("isOtherReason") isOtherReason: String,
            @Field("reasonId") reasonId: String?,
            @Field("customerEmail") customerEmail: String,
            @Field("customerName") customerName: String,
            @Field("customerToken") customerToken: String
    ): Observable<BaseModel>


    @FormUrlEncoded
    @POST(MOBIKUL_SAVE_PRODUCT_REPORT)
    fun saveProductReport(
            @Field("storeId") storeId: String,
            @Field("productId") sellerId: String,
            @Field("otherReason") otherReason: String,
            @Field("isOtherReason") isOtherReason: String,
            @Field("reasonId") reasonId: String?,
            @Field("customerEmail") customerEmail: String,
            @Field("customerName") customerName: String,
            @Field("customerToken") customerToken: String
    ): Observable<BaseModel>


    // #Delivery boy

    @GET(MpConstantsHelper.MOBIKUL_DELIVERY_BOY_LIST)
    fun getDeliveryBoyList(
            @Query("storeId") storeId: String?,
            @Query("adminCustomerEmail") deliveryboyId: String?
            , @Query("mFactor") mFactor: Float
            , @Query("pageNumber") pageNumber: Int
            , @Query("purpose") purpose: String?
            , @Query("sellerId") sellerId: String?
            , @Query("isSeller") isSeller: Int?
            , @Query("userId") userId: String?
    ): Observable<GetDeliveryBoyListResponseData>

    @FormUrlEncoded
    @POST(MOBIKUL_DELIVERY_BOY_ASSIGN_ORDER)
    fun assignOrder(
            @Field("storeId") storeId: String?
            , @Field("adminCustomerEmail") adminCustomerEmail: String?
            , @Field("incrementId") incrementId: String?
            , @Field("deliveryboyId") deliveryboyId: String?
            , @Field("sellerId") sellerId: String?
    ): Observable<BaseModel>

/* Auction ---------*/

    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------
        AUCTION API's
     ------------------------------------------------------------------------------------------------------------------------------------------------------------*/
    @FormUrlEncoded
    @POST(MOBIKUL_AUCTION_PRODUCT_LIST_FOR_AUCTION)
    fun getProductListForAuction(
            @Field("storeId") storeId: Int
            , @Field("customerToken") customerToken: String?
            , @Field("pageNumber") pageNumber: Int
            , @Field("productName") productName: String?
    ): Observable<GetProductListForAuctionData>

    @FormUrlEncoded
    @POST(MOBIKUL_AUCTION_LIST)
    fun getAuctionList(
            @Field("storeId") storeId: Int
            , @Field("customerToken") customerToken: String?
            , @Field("pageNumber") pageNumber: Int
            , @Field("productName") productName: String?
    ): Observable<GetAuctionListData>

    @FormUrlEncoded
    @POST(MOBIKUL_AUCTION_FORM_DATA)
    fun getAuctionFormData(
            @Field("customerToken") customerToken: String?
            , @Field("productId") productId: String?
            , @Field("auctionId") auctionId: String?
    ): Observable<AuctionFormData>

    @FormUrlEncoded
    @POST(MOBIKUL_AUCTION_MASS_DISABLE)
    fun massDisableAuction(
            @Field("customerToken") customerToken: String?
            , @Field("auctionIds") auctionIds: JSONArray?
    ): Observable<BaseModel>

    @FormUrlEncoded
    @POST(MOBIKUL_AUCTION_BID_DETAILS)
    fun getAuctionBitDetails(
            @Field("customerToken") customerToken: String?
            , @Field("auctionId") auctionId: String?
    ): Observable<AuctionBidDetailsData>

    @FormUrlEncoded
    @POST(MOBIKUL_AUCTION_SAVE)
    fun saveAuction(
            @Field("storeId") storeId: Int
            , @Field("customerToken") customerToken: String?
            , @Field("days") days: String?
            , @Field("minQty") minQty: String?
            , @Field("maxQty") maxQty: String?
            , @Field("timezone") timezone: String?
            , @Field("productId") productId: String?
            , @Field("auctionId") auctionId: String?
            , @Field("minAmount") minAmount: String?
            , @Field("increment") increment: JSONObject?
            , @Field("auctionType") auctionType: String?
            , @Field("reservePrice") reservePrice: String?
            , @Field("incrementOpt") incrementOpt: Int
            , @Field("startingPrice") startingPrice: String?
            , @Field("autoAuctionOpt") autoAuctionOpt: Int
            , @Field("stopAuctionTime") stopAuctionTime: String?
            , @Field("startAuctionTime") startAuctionTime: String?
    ): Observable<BaseModel>


/* / Auction ---------*/
}