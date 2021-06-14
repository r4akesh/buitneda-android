package com.webkul.mobikulmp.network

import android.content.Context
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikul.helpers.ApplicationConstants
import com.webkul.mobikul.helpers.ApplicationConstants.BASE_URL
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.models.catalog.CatalogProductsResponseModel
import com.webkul.mobikul.network.ApiClient
import com.webkul.mobikulmp.deliveryboy.models.GetDeliveryBoyListResponseData
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_PRODUCT_CROSS_SELL_PRODUCT
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_PRODUCT_RELATED_PRODUCT
import com.webkul.mobikulmp.helpers.MpConstantsHelper.MOBIKUL_PRODUCT_UPSELL_PRODUCT
import com.webkul.mobikulmp.helpers.MpConstantsHelper.UPLOAD_PIC_CONTROLLER
import com.webkul.mobikulmp.model.auction.AuctionFormData
import com.webkul.mobikulmp.models.AskQuestionsToAdminRequestData
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
import com.webkul.mobikulmp.models.sellerinfo.ReportData
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import retrofit2.Callback
import java.util.*

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
class MpApiConnection {
    companion object {

        /*Marketplace Landing Page*/
        fun getLandingPageData(context: Context): Observable<MarketplaceLandingPageData> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).getLandingPageData(
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCurrencyCode(context),
                    Utils.screenWidth,
                    context.resources.displayMetrics.density)
        }

        fun getProductPageData(context: Context, eTag: String, productId: String): Observable<ProductDetailsPageModelExtended> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).getProductPageData(
                    eTag
                    , AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , AppSharedPref.getQuoteId(context)
                    , AppSharedPref.getWebsiteId(context)
                    , Utils.screenWidth
                    , AppSharedPref.getCurrencyCode(context)
                    , productId)
        }


        fun getSellerList(context: Context, eTag: String, storeName: String): Observable<SellerListResponseData> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).getSellerList(
                    eTag,
                    AppSharedPref.getStoreId(context),
                    storeName,
                    Utils.screenWidth,
                    context.resources.displayMetrics.density)
        }

        fun getSellerCatalogProductData(context: Context, eTag: String, type: String, id: String, sellerId: Int, pageNumber: Int, sortData: JSONArray, filterData: JSONArray): Observable<CatalogProductsResponseModel> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).getSellerCatalogProductData(
                    eTag
                    , sellerId
                    , AppSharedPref.getStoreId(context)
                    , AppSharedPref.getQuoteId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , AppSharedPref.getCurrencyCode(context)
                    , Utils.screenWidth
                    , Utils.screenDensity
                    , type
                    , id
                    , pageNumber
                    , sortData
                    , filterData)
        }


        fun askQuestionToAdmin(context: Context, data: AskQuestionsToAdminRequestData): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).askQuestionsToAdmin(
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context)
                    , data.subject
                    , data.query)

        }

        fun makeSeller(context: Context, storeUrl: String): Observable<BecomePartnerResponseData> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).sendBecomePartnerRequest(
                    AppSharedPref.getStoreId(context), AppSharedPref.getCustomerToken(context), storeUrl)
        }

        fun saveAttribute(context: Context, createAttributeRequestData: CreateAttributeRequestData): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).saveAttribute(
                    AppSharedPref.getStoreId(context),
                    createAttributeRequestData.attributeCode,
                    createAttributeRequestData.attributeLabel,
                    createAttributeRequestData.catalogInputType,
                    createAttributeRequestData.attributeOptionData,
                    createAttributeRequestData.isValueRequired)
        }

        /*Edit Seller Profile*/
        fun getSellerProfileFormData(context: Context): Observable<SellerProfileFormResponseData> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).getSellerProfileFormData(
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context))
        }

        fun deleteSellerImage(context: Context, entity: String): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).deleteSellerImage(
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context),
                    entity)
        }

        fun saveSellerProfileData(context: Context, profileData: SellerProfileFormResponseData, companyBanner: MultipartBody.Part?, companyLogo: MultipartBody.Part?): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).saveSellerProfileData(
                    RequestBody.create(MediaType.parse("text/plain"), AppSharedPref.getStoreId(context)),
                    RequestBody.create(MediaType.parse("text/plain"), AppSharedPref.getCustomerToken(context)),
                    if (profileData.isTwitterActive) 1 else 0,
                    RequestBody.create(MediaType.parse("text/plain"), profileData.twitterId ?: ""),
                    if (profileData.isFacebookActive) 1 else 0,
                    RequestBody.create(MediaType.parse("text/plain"), profileData.facebookId ?: ""),
                    if (profileData.isInstagramActive) 1 else 0,
                    RequestBody.create(MediaType.parse("text/plain"), profileData.instagramId
                            ?: ""),
                    if (profileData.isGoogleplusActive) 1 else 0,
                    RequestBody.create(MediaType.parse("text/plain"), profileData.googleplusId
                            ?: ""),
                    if (profileData.isYoutubeActive) 1 else 0,
                    RequestBody.create(MediaType.parse("text/plain"), profileData.youtubeId ?: ""),
                    if (profileData.isVimeoActive) 1 else 0,
                    RequestBody.create(MediaType.parse("text/plain"), profileData.vimeoId ?: ""),
                    if (profileData.isPinterestActive) 1 else 0,
                    RequestBody.create(MediaType.parse("text/plain"), profileData.pinterestId
                            ?: ""),
                    RequestBody.create(MediaType.parse("text/plain"), profileData.contactNumber
                            ?: ""),
                    RequestBody.create(MediaType.parse("text/plain"), profileData.taxvat ?: ""),
                    RequestBody.create(MediaType.parse("text/plain"), profileData.backgroundColor
                            ?: ""),
                    RequestBody.create(MediaType.parse("text/plain"), profileData.shopTitle ?: ""),
                    companyBanner,
                    companyLogo,
                    RequestBody.create(MediaType.parse("text/plain"), profileData.companyLocality
                            ?: ""),
                    RequestBody.create(MediaType.parse("text/plain"), profileData.companyDescription
                            ?: ""),
                    RequestBody.create(MediaType.parse("text/plain"), profileData.returnPolicy
                            ?: ""),
                    RequestBody.create(MediaType.parse("text/plain"), profileData.shippingPolicy
                            ?: ""),
                    RequestBody.create(MediaType.parse("text/plain"), profileData.privacyPolicy
                            ?: ""),
                    RequestBody.create(MediaType.parse("text/plain"), profileData.country ?: ""),
                    RequestBody.create(MediaType.parse("text/plain"), profileData.metaKeyword
                            ?: ""),
                    RequestBody.create(MediaType.parse("text/plain"), profileData.metaDescription
                            ?: ""),
                    RequestBody.create(MediaType.parse("text/plain"), profileData.paymentDetails
                            ?: "")
            )
        }

        fun getCreditMemoFormData(context: Context, eTag: String, incrementId: String): Observable<CreditMemoFormResponseData> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).getCreditMemoFormData(
                    eTag,
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context),
                    incrementId)
        }

        fun createCreditMemo(context: Context, creditMemoFormResponseData: CreditMemoFormResponseData, incrementId: String, doOffline: Int): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).createCreditMemo(
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context),
                    incrementId,
                    creditMemoFormResponseData.itemsListObject,
                    creditMemoFormResponseData.creditMemoComment,
                    creditMemoFormResponseData.shippingAmount,
                    creditMemoFormResponseData.adjustmentRefund,
                    creditMemoFormResponseData.adjustmentFee,
                    doOffline,
                    if (creditMemoFormResponseData.isAppendComment) 1 else 0,
                    if (creditMemoFormResponseData.isVisibleInFrontend) 1 else 0,
                    if (creditMemoFormResponseData.isEmailCopyOfCreditMemo) 1 else 0,
                    creditMemoFormResponseData.invoiceId)
        }


        /*Seller Profile*/
        fun getSellerProfileData(context: Context, eTag: String, sellerId: Int): Observable<SellerProfileData> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).getSellerProfileData(
                    eTag,
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context),
                    sellerId,
                    AppSharedPref.getCurrencyCode(context),
                    Utils.screenWidth,
                    Utils.screenDensity)
        }

        fun contactSeller(context: Context, data: ContactSellerRequestData, sellerId: Int, productId: String?): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).contactSeller(
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context),
                    sellerId,
                    productId,
                    data.name,
                    data.email,
                    data.subject,
                    data.query)
        }

        fun getSellerReviews(context: Context, eTag: String, sellerId: Int, pageNumber: Int): Observable<SellerReviewListData> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).getSellerReviews(
                    eTag,
                    AppSharedPref.getStoreId(context),
                    sellerId,
                    Utils.screenWidth,
                    pageNumber)
        }

        fun saveReview(context: Context, sellerId: Int, nickName: String, summary: String, details: String, price: Int, value: Int, quantity: Int, shopUrl: String): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).saveReview(
                    AppSharedPref.getStoreId(context),
                    sellerId,
                    AppSharedPref.getCustomerToken(context),
                    AppSharedPref.getCustomerEmail(context),
                    nickName,
                    summary,
                    details,
                    price,
                    value,
                    quantity,
                    shopUrl)
        }

        /*Add/Edit Product*/
        fun getProductFormData(context: Context, productId: String?): Observable<SellerAddProductResponseData> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).getProductFormData(
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context),
                    productId)
        }

        fun checkSku(context: Context, sku: String): Observable<CheckSkuResponseData> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).checkSku(
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context),
                    sku)
        }

        fun uploadImage(context: Context, multipartFileBody: MultipartBody.Part, width: RequestBody, mFactor: RequestBody): Observable<UploadPicResponseData> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).uploadImage(
                    ApplicationConstants.BASE_URL + UPLOAD_PIC_CONTROLLER + AppSharedPref.getCustomerToken(context)
                    , multipartFileBody
                    , width
                    , mFactor
            )
        }

        fun uploadDownloadableFile(context: Context, type: String, file: MultipartBody.Part): Observable<FileData> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).uploadDownloadableFile(
                    RequestBody.create(MediaType.parse("text/plain"), AppSharedPref.getStoreId(context)),
                    RequestBody.create(MediaType.parse("text/plain"), AppSharedPref.getCustomerToken(context)),
                    RequestBody.create(MediaType.parse("text/plain"), type),
                    file)
        }

        fun saveProduct(context: Context, productId: String?, addProductData: AddProductData, imagesData: Map<String, String>): Observable<SaveProductResponseData> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).saveProduct(
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context),
                    productId,
                    "simple",
                    addProductData.status,
                    addProductData.featuredProduct,
                    addProductData.attributeSetId,
                    addProductData.websiteJSONArray,
                    addProductData.categoryIdsJSONArray,
                    addProductData.name,
                    addProductData.description,
                    addProductData.shortDescription,
                    addProductData.sku,
                    addProductData.price,
                    addProductData.specialPrice,
                    addProductData.specialFromDateForRequest,
                    addProductData.specialToDateForRequest,
                    addProductData.qty,
                    1,
                    "4",
                    "2",
                    1,
                    "1",
                    "",
                    "",
                    "",
                    addProductData.mpProductCartLimit,
                    addProductData.mediaGalleryJSONArray,
                    addProductData.crossSellJSONArray,
                    addProductData.upsellJSONArray,
                    addProductData.relatedJSONArray,
                    "0",
                    addProductData.linksTitle,
                    addProductData.purchasedSeparately,
                    addProductData.linksJsonData,
                    addProductData.samplesTitle,
                    addProductData.samplesJsonData,
                    addProductData.deleteImageId,
                    imagesData)

        }


        fun getRelatedProductData(context: Context, collectionType: String, productId: String?, pageNumber: Int, sortData: JSONArray?, filterData: JSONArray?): Observable<AddProductFieldProductCollectionData> {
            var url = ""
            when (collectionType) {
                "related" -> url = BASE_URL + MOBIKUL_PRODUCT_RELATED_PRODUCT
                "upsell" -> url = BASE_URL + MOBIKUL_PRODUCT_UPSELL_PRODUCT
                "crosssell" -> url = BASE_URL + MOBIKUL_PRODUCT_CROSS_SELL_PRODUCT
            }

            return ApiClient.getClient()!!.create(MpApiDetails::class.java).getRelatedProductData(
                    url,
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context),
                    productId,
                    pageNumber,
                    sortData,
                    filterData)
        }


        /*Seller Products*/
        fun getSellerProductsListData(context: Context, pageNumber: Int, dateTo: String, dateFrom: String, productName: String, productStatus: String): Observable<SellerProductListResponseData> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).getSellerProductsListData(
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context),
                    AppSharedPref.getCurrencyCode(context),
                    pageNumber,
                    dateTo,
                    dateFrom,
                    productName,
                    productStatus)
        }

        /*Seller Transactions*/
        fun getSellerTransactionsListData(context: Context, eTag: String, pageNumber: Int, dateTo: String, dateFrom: String, productName: String): Observable<SellerTransactionListResponseData> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).getSellerTransactionsListData(
                    eTag,
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context),
                    pageNumber,
                    dateTo,
                    dateFrom,
                    productName)
        }

        fun makeWidthdrawal(context: Context): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).makeWidthdrawal(
                    AppSharedPref.getCustomerToken(context), AppSharedPref.getStoreId(context), 1)
        }

        /*Seller Transaction Details*/
        fun getSellerTransactionsDetailsData(context: Context, eTag: String, transactionId: String): Observable<ViewTransactionResponseData> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).getSellerTransactionsDetailsData(
                    eTag,
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context),
                    transactionId)
        }

        /*Seller Orders*/
        fun getSellerOrdersListData(context: Context, eTag: String, pageNumber: Int, status: String?, dateTo: String, dateFrom: String, incrementId: String): Observable<SellerOrderListData> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).getSellerOrdersListData(
                    eTag,
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context),
                    pageNumber,
                    status,
                    dateTo,
                    dateFrom,
                    incrementId)
        }

        /*Seller Orders*/
        fun getSellerOrdersDetailsData(context: Context, eTag: String, incrementId: String): Observable<SellerOrderDetailsModel> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).getSellerOrdersDetailsData(
                    eTag,
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context),
                    AppSharedPref.getCurrencyCode(context),
                    incrementId)
        }

        fun sendOrderMailData(context: Context, incrementId: String): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).sendOrderMailData(
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context),
                    incrementId)
        }

        fun cancelOrder(context: Context, incrementId: String): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).cancelOrder(
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context),
                    incrementId)
        }

        fun createInvoice(context: Context, incrementId: String): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).createInvoice(
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context),
                    incrementId)
        }

        fun createShipment(context: Context, incrementId: String, carrier: String, trackingId: String): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).createShipment(
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context),
                    incrementId,
                    carrier,
                    trackingId)
        }

        fun getSellerInvoiceDetails(context: Context, eTag: String, invoiceId: String, incrementId: String): Observable<SellerInvoiceDetailsData> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).getSellerInvoiceDetails(
                    eTag,
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context),
                    invoiceId,
                    incrementId)
        }

        fun getSellerShipmentDetails(context: Context, eTag: String, shipmentId: String, incrementId: String): Observable<SellerShipmentDetailsData> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).getSellerShipmentDetails(
                    eTag,
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context),
                    shipmentId,
                    incrementId)
        }
        fun sendTrackingInfo(context: Context?, incrementId: String?, shipmentId: String?): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).sendTrackingInfo(
                    AppSharedPref.getCustomerToken(context!!)
                    , incrementId
                    , shipmentId
                    , AppSharedPref.getStoreId(context))
        }
        fun getSellerCreditMemoDetails(context: Context, eTag: String, creditMemoId: String, incrementId: String): Observable<CreditMemoDetailsResponseData> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).getSellerCreditMemoDetails(
                    eTag,
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context),
                    creditMemoId,
                    incrementId)
        }

        fun sendCreditMemoMail(context: Context, creditMemoId: String, incrementId: String): Observable<CreditMemoDetailsResponseData> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).sendCreditMemoMail(
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context),
                    creditMemoId,
                    incrementId)
        }

        fun sendSellerInvoiceMail(context: Context, invoiceId: String, incrementId: String): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).sendSellerInvoiceMail(
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context),
                    invoiceId,
                    incrementId)
        }

        fun deleteProduct(context: Context, productId: Int): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).deleteProduct(
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context),
                    productId)
        }

        fun massDeleteProduct(context: Context, productIds: JSONArray): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).massDeleteProduct(
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context),
                    productIds)
        }


        /*Print PDF Header Info*/
        fun getPdfHeaderFormData(context: Context): Observable<PdfHeaderInfoDataResponse> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).getPdfHeaderFormData(
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context))
        }

        fun savePdfHeader(context: Context, data: String): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).savePdfHeader(
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context),
                    data)
        }

        /*Seller/ Admin Chat*/
        fun getChatSellerList(context: Context): Observable<ChatSellerListResponseData> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).getChatSellerList(
                    ApplicationConstants.DEFAULT_WEBSITE_ID,
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context),
                    context.resources.displayMetrics.density,
                    Utils.screenWidth)
        }

        fun chatNotifyAdmin(context: Context, message: String, sellerId: String, sellerName: String): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).chatNotifyAdmin(
                    AppSharedPref.getStoreId(context),
                    ApplicationConstants.DEFAULT_WEBSITE_ID,
                    message,
                    sellerId,
                    sellerName
            )
        }

        /*Seller Dashboard*/

        fun getSellerDashboardData(context: Context): Observable<SellerDashboardData> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).getSellerDashboardData(
                    AppSharedPref.getCustomerToken(context),
                    AppSharedPref.getStoreId(context),
                    Utils.screenWidth,
                    context.resources.displayMetrics.density)
        }

        /*Customer List*/
        fun getCustomerList(context: Context, name: String, contact: String, gender: Int?, email: String, address: String, eTag: String, pageNumber: Int): Observable<CustomerListResponse> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).getCustomerList(
                    eTag,
                    AppSharedPref.getCustomerToken(context),
                    AppSharedPref.getStoreId(context),
                    email,
                    gender,
                    name,
                    address,
                    contact,
                    AppSharedPref.getCurrencyCode(context),
                    pageNumber
            )
        }

        fun saveSellerReport(context: Context,reportData:ReportData ,id:String): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).saveSellerReport(
                    AppSharedPref.getStoreId(context),
                    id,
                    reportData.flagMessage,
                    if(reportData.selectedOtherReasonMethod) "1" else "0",
                    reportData.selectedReasonMethod,
                    reportData.email,
                    reportData.name,
                    AppSharedPref.getCustomerToken(context)
                )
        }
        fun saveProductReport(context: Context,reportData:ReportData ,id:String): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).saveProductReport(
                    AppSharedPref.getStoreId(context),
                    id,
                    reportData.flagMessage,
                   if(reportData.selectedOtherReasonMethod) "1" else "0",
                    reportData.selectedReasonMethod,
                    reportData.email,
                    reportData.name,
                    AppSharedPref.getCustomerToken(context)
                )
        }

        // #Delivery boy
        fun getDeliveryBoyList(context: Context, pageNumber: Int, purpose: String, sellerId: String?): Observable<GetDeliveryBoyListResponseData> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).getDeliveryBoyList(
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerEmail(context)
                    , Utils.screenDensity
                    , pageNumber
                    , purpose
                    , sellerId
                    , 1
                    , null
            )
        }

        fun assignOrder(context: Context?, incrementId: String?, deliveryBoyId: String?,sellerId:String?): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).assignOrder(
                    AppSharedPref.getStoreId(context!!)
                    , AppSharedPref.getCustomerEmail(context)
                    , incrementId
                    , deliveryBoyId
                    , sellerId
            )
        }

        /* Auction   -------   */
        fun getProductListForAuction(context: Context?, pageNumber: Int, productName: String?): Observable<GetProductListForAuctionData> {
           return ApiClient.getClient()!!.create(MpApiDetails::class.java).getProductListForAuction(
                    AppSharedPref.getStoreId(context!!).toInt()
                    , AppSharedPref.getCustomerToken(context)
                    , pageNumber
                    , productName
            )
        }

        fun getAuctionList(context: Context?, pageNumber: Int, productName: String?): Observable<GetAuctionListData> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).getAuctionList(
                    AppSharedPref.getStoreId(context!!).toInt()
                    , AppSharedPref.getCustomerToken(context)
                    , pageNumber
                    , productName
            )
        }

        fun getAuctionFormData(context: Context?, customerToken: String?, productId: String?): Observable<AuctionFormData> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).getAuctionFormData(
                    AppSharedPref.getCustomerToken(context!!)
                    , customerToken
                    , productId
            )
        }

        fun massDisableAuction(context: Context?, auctionIds: JSONArray): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).massDisableAuction(
                    AppSharedPref.getCustomerToken(context!!)
                    , auctionIds
            )
        }

        fun getAuctionBitDetails(context: Context?, auctionId: String?): Observable<AuctionBidDetailsData> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).getAuctionBitDetails(
                    AppSharedPref.getCustomerToken(context!!)
                    , auctionId
            )
        }

        fun saveAuction(context: Context?, auctionFormData: AuctionFormData, productId: String?, auctionId: String?): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(MpApiDetails::class.java).saveAuction(
                    AppSharedPref.getStoreId(context!!).toInt()
                    , AppSharedPref.getCustomerToken(context)
                    , auctionFormData.noOfDaysTillBuy
                    , auctionFormData.minQty
                    , auctionFormData.maxQty
                    , TimeZone.getDefault().id
                    , productId
                    , auctionId
                    , auctionFormData.minAmount
                    , auctionFormData.incrementForReq
                    , auctionFormData.auctionTypeForReq
                    , auctionFormData.reservePrice
                    , if (auctionFormData.incrementAuction) 1 else 0
                    , auctionFormData.startingPrice
                    , if (auctionFormData.autoAuction) 1 else 0
                    , auctionFormData.stopTimeAdjust
                    , auctionFormData.startTimeAdjust
            )
        }

        /* /Auction   -------   */

    }
}