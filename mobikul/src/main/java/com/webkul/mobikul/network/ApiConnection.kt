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

package com.webkul.mobikul.network

import android.content.Context
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikul.helpers.ApplicationConstants.DEFAULT_OS
import com.webkul.mobikul.helpers.ApplicationConstants.LOG_PARAMS
import com.webkul.mobikul.helpers.ApplicationConstants.LOG_RESPONSE
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.models.ImageUploadResponseData
import com.webkul.mobikul.models.ReOrderModel
import com.webkul.mobikul.models.auction.GetAuctionBidListResponseData
import com.webkul.mobikul.models.auction.GetAutoBidListResponseData
import com.webkul.mobikul.models.catalog.*
import com.webkul.mobikul.models.checkout.*
import com.webkul.mobikul.models.extra.*
import com.webkul.mobikul.models.homepage.Category
import com.webkul.mobikul.models.homepage.HomePageDataModel
import com.webkul.mobikul.models.homepage.PromotionBanner
import com.webkul.mobikul.models.product.ProductDetailsPageModel
import com.webkul.mobikul.models.product.ProductRatingFormDataModel
import com.webkul.mobikul.models.product.ReviewListData
import com.webkul.mobikul.models.user.*
import com.webkul.mobikul.wallet.models.wallet.*
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject

class ApiConnection {

    companion object {

        /*Catalog*/
        fun getHomePageData(context: Context, eTag: String, isFromUrl: Boolean, url: String): Observable<HomePageDataModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getHomePageData(
                    eTag
                    , AppSharedPref.getWebsiteId(context)
                    , AppSharedPref.getStoreId(context)
                    , AppSharedPref.getQuoteId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , AppSharedPref.getCurrencyCode(context)
                    , Utils.screenWidth
                    , Utils.screenDensity
                    , if (isFromUrl) 1 else 0
                    , url)
        }

        /*OfferBanner*/
        fun getOfferData(context: Context): Observable<PromotionBanner> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getPromotionData()
        }

        fun getTopSellingProducts(context: Context, pageNumber: Int): Observable<HomePageDataModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getTopSellingProducts(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getWebsiteId(context)
                    , AppSharedPref.getQuoteId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , AppSharedPref.getCurrencyCode(context)
                    , Utils.screenWidth
                    , Utils.screenDensity
                    , pageNumber)
        }

        fun customerWebLogin(context: Context, token: String): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).customerWebLogin(
                    AppSharedPref.getCustomerToken(context),
                    token
            )
        }

        fun getSubCategoryData(context: Context, eTag: String, categoryId: String): Observable<SubCategoryResponseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getSubCategoryData(
                    eTag
                    , AppSharedPref.getStoreId(context)
                    , AppSharedPref.getQuoteId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , AppSharedPref.getCurrencyCode(context)
                    , Utils.screenWidth
                    , Utils.screenDensity
                    , categoryId)
        }

        fun getSubCategoryList(context: Context, categoryId: String?): Observable<Category> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getSubCategoryList(
                    AppSharedPref.getStoreId(context)
                    , categoryId)
        }

        fun getCatalogProductData(context: Context, eTag: String, type: String, id: String, pageNumber: Int, sortData: JSONArray, filterData: JSONArray): Observable<CatalogProductsResponseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getCatalogProductData(
                    eTag
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

        fun getBrandCatalogProductData(context: Context, eTag: String, type: String, brandId: String, pageNumber: Int, sortData: JSONArray, filterData: JSONArray): Observable<CatalogProductsResponseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getBrandCatalogProductData(
                    eTag
                    , AppSharedPref.getStoreId(context)
                    , AppSharedPref.getQuoteId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , AppSharedPref.getCurrencyCode(context)
                    , Utils.screenWidth
                    , Utils.screenDensity
                    , type
                    , brandId
                    , pageNumber
                    , sortData
                    , filterData)
        }

        fun getAdvanceSearchFormData(context: Context, eTag: String): Observable<AdvancedSearchFormModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getAdvanceSearchFormData(
                    eTag
                    , AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCurrencyCode(context)
                    , 1)
        }

        fun addToWishList(context: Context, productId: String, params: JSONObject, qty: String, files: List<MultipartBody.Part>): Observable<AddToWishListResponseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).addToWishList(
                    RequestBody.create(MediaType.parse("text/plain"), AppSharedPref.getStoreId(context))
                    , RequestBody.create(MediaType.parse("text/plain"), AppSharedPref.getCustomerToken(context))
                    , RequestBody.create(MediaType.parse("text/plain"), productId)
                    , RequestBody.create(MediaType.parse("text/plain"), params.toString())
                    , RequestBody.create(MediaType.parse("text/plain"), qty)
                    , files)
        }

        fun getProductPageData(context: Context, eTag: String, productId: String): Observable<ProductDetailsPageModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getProductPageData(
                    eTag
                    , AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , AppSharedPref.getQuoteId(context)
                    ,AppSharedPref.getWebsiteId(context)
                    , Utils.screenWidth
                    , AppSharedPref.getCurrencyCode(context)
                    , productId)
        }

        fun getProductReviewList(context: Context, eTag: String, productId: String, pageNumber: Int): Observable<ReviewListData> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getProductReviewList(
                    eTag
                    , AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , AppSharedPref.getQuoteId(context)
                    , Utils.screenWidth
                    , productId
                    , pageNumber)
        }

        fun getRatingForData(context: Context, eTag: String): Observable<ProductRatingFormDataModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getRatingForData(
                    eTag
                    , AppSharedPref.getStoreId(context))
        }

        fun addToCompare(context: Context, productId: String): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).addToCompare(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , productId)
        }

        fun getCompareList(context: Context, eTag: String): Observable<CompareListData> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getCompareList(
                    eTag
                    , AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , Utils.screenWidth
                    , AppSharedPref.getCurrencyCode(context))
        }

        fun deleteFromCompareList(context: Context, productId: String): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).deleteFromCompareList(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , productId)
        }

        /*Customer*/
        fun login(context: Context, loginFormModel: LoginFormModel): Observable<LoginResponseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).login(
                    AppSharedPref.getWebsiteId(context)
                    , AppSharedPref.getStoreId(context)
                    , AppSharedPref.getQuoteId(context)
                    , Utils.screenWidth
                    , Utils.screenDensity
                    , loginFormModel.email.trim()
                    , loginFormModel.mobile.trim()
                    , loginFormModel.password.trim()
                    , AppSharedPref.getFcmToken(context)
                    , DEFAULT_OS)
        }

        fun forgotPassword(context: Context, email: String): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).forgotPassword(
                    AppSharedPref.getWebsiteId(context)
                    , AppSharedPref.getStoreId(context)
                    , email)
        }

        fun createAccountFormData(context: Context, eTag: String): Observable<SignUpFormModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).createAccountFormData(
                    eTag
                    , AppSharedPref.getStoreId(context))
        }

        fun signUp(context: Context, signUpFormModel: SignUpFormModel): Observable<SignUpResponseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).signUp(
                    AppSharedPref.getWebsiteId(context)
                    , AppSharedPref.getStoreId(context)
                    , AppSharedPref.getQuoteId(context)
                    , Utils.screenWidth
                    , Utils.screenDensity
                    , signUpFormModel.prefix?.trim()
                    , signUpFormModel.firstName?.trim()
                    , signUpFormModel.middleName?.trim()
                    , signUpFormModel.lastName?.trim()
                    , signUpFormModel.suffix?.trim()
                    , signUpFormModel.dob?.trim()
                    , signUpFormModel.taxVat?.trim()
                    , signUpFormModel.gender
                    , signUpFormModel.emailAddr?.trim()
                    , signUpFormModel.password?.trim()
                    , signUpFormModel.pictureURL?.trim()
                    , signUpFormModel.isSocial
                    , signUpFormModel.mobile?.trim()
                    , signUpFormModel.nifNumber?.trim()
                    , AppSharedPref.getFcmToken(context)
                    , signUpFormModel.shopURL
                    , if (signUpFormModel.signUpAsSeller) 1 else 0
                    , DEFAULT_OS
                    , signUpFormModel.orderId)
        }

        fun getOrderList(context: Context, eTag: String, pageNumber: Int, forDashboard: Boolean): Observable<OrderListResponseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getOrderList(
                    eTag
                    , AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , AppSharedPref.getCurrencyCode(context)
                    , pageNumber
                    , if (forDashboard) forDashboard.toString() else null)
        }

        fun getDownloadableProductsList(context: Context, eTag: String, pageNumber: Int): Observable<DownloadableProductsListResponseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getDownloadableProductsList(
                    eTag
                    , AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , pageNumber)
        }

        fun getWishList(context: Context, eTag: String, pageNumber: Int): Observable<WishListResponseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getWishList(
                    eTag
                    , AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , AppSharedPref.getCurrencyCode(context)
                    , pageNumber)
        }

        fun getReviewsList(context: Context, eTag: String, pageNumber: Int, forDashboard: Boolean): Observable<ReviewListResponseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getReviewsList(
                    eTag
                    , AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , pageNumber
                    , Utils.screenWidth
                    , if (forDashboard) "true" else null)
        }

        fun getAddressBookData(context: Context, eTag: String, forDashboard: Boolean): Observable<AddressBookResponseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getAddressBookData(
                    eTag
                    , AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , if (forDashboard) "true" else null)
        }

        fun getAccountInfo(context: Context, eTag: String): Observable<AccountInfoResponseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getAccountInfo(
                    eTag
                    , AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context))
        }

        fun saveAccountInfo(context: Context, accountInfoResponseModel: AccountInfoResponseModel): Observable<SaveAccountInfoResponseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).saveAccountInfo(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , accountInfoResponseModel.prefixValue
                    , accountInfoResponseModel.firstName
                    , accountInfoResponseModel.middleName
                    , accountInfoResponseModel.lastName
                    , accountInfoResponseModel.suffixValue
                    , accountInfoResponseModel.dobValue
                    , accountInfoResponseModel.taxValue
                    , accountInfoResponseModel.genderValue
                    , if (accountInfoResponseModel.isChangesEmailChecked) 1 else 0
                    , accountInfoResponseModel.email
                    , if (accountInfoResponseModel.isChangesPasswordChecked) 1 else 0
                    , accountInfoResponseModel.currentPassword
                    , accountInfoResponseModel.newPassword
                    , accountInfoResponseModel.confirmNewPassword
                    , accountInfoResponseModel.mobile)
        }

        fun reorder(context: Context, incrementId: String?): Observable<ReOrderModel> {

            if (AppSharedPref.isLoggedIn(context)) {
                return ApiClient.getClient()!!.create(ApiDetails::class.java).reorder(
                        AppSharedPref.getStoreId(context)
                        , AppSharedPref.getCustomerToken(context)
                        , incrementId)
            } else {
                return ApiClient.getClient()!!.create(ApiDetails::class.java).reorderGuest(
                        AppSharedPref.getStoreId(context)
                        , incrementId)
            }


        }

        fun getOrderDetails(context: Context, eTag: String, incrementId: String): Observable<OrderDetailsModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getOrderDetails(
                    eTag
                    , AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , AppSharedPref.getCurrencyCode(context)
                    , incrementId)
        }

        fun downloadProduct(context: Context, eTag: String, hash: String): Observable<DownloadProductsResponseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).downloadProduct(
                    eTag
                    , AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , hash)
        }

        fun getReviewDetails(context: Context, eTag: String, reviewId: String): Observable<ReviewDetailsResponseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getReviewDetails(
                    eTag
                    , AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , Utils.screenWidth
                    , reviewId)
        }

        fun removeFromWishList(context: Context, itemId: String): Observable<ReviewDetailsResponseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).removeFromWishList(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , itemId)
        }

        fun wishListToCart(context: Context, productId: String, itemId: String, qty: String): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).wishListToCart(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , productId
                    , itemId
                    , qty)
        }

        fun getAddressFormData(context: Context, eTag: String, addressId: String): Observable<AddressFormResponseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getAddressFormData(
                    eTag
                    , AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , addressId)
        }

        fun saveAddress(context: Context, addressId: String, addressData: String): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).saveAddress(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , addressId
                    , addressData)
        }

        fun saveProductNotFoundData(context: Context,multipartFileBody: MultipartBody.Part, title: RequestBody, email: RequestBody,subject: RequestBody, message: RequestBody): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).saveProductNotFoundData(
                    multipartFileBody,
                     title
                    , email, subject, message)
        }

        fun addAllToCart(context: Context, qty: JSONObject): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).addAllToCart(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , qty)
        }

        fun shareWishList(context: Context, emails: String, message: String): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).shareWishList(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , emails
                    , message)
        }

        fun uploadProfileImage(context: Context, profileImageMultipartBody: MultipartBody.Part): Observable<ImageUploadResponseData> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).uploadProfileImage(
                    RequestBody.create(MediaType.parse("text/plain"), AppSharedPref.getStoreId(context))
                    , RequestBody.create(MediaType.parse("text/plain"), AppSharedPref.getCustomerToken(context))
                    , RequestBody.create(MediaType.parse("text/plain"), Utils.screenWidth.toString())
                    , RequestBody.create(MediaType.parse("text/plain"), Utils.screenDensity.toString())
                    , profileImageMultipartBody)
        }

        fun uploadBannerImage(context: Context, bannerImageMultipartBody: MultipartBody.Part): Observable<ImageUploadResponseData> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).uploadBannerImage(
                    RequestBody.create(MediaType.parse("text/plain"), AppSharedPref.getStoreId(context))
                    , RequestBody.create(MediaType.parse("text/plain"), AppSharedPref.getCustomerToken(context))
                    , RequestBody.create(MediaType.parse("text/plain"), Utils.screenWidth.toString())
                    , RequestBody.create(MediaType.parse("text/plain"), Utils.screenDensity.toString())
                    , bannerImageMultipartBody)
        }

        fun updateWishList(context: Context, itemData: JSONArray): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).updateWishList(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , itemData)
        }

        fun saveReview(context: Context, id: String, title: String, detail: String, nickname: String, ratingObj: JSONObject): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).saveReview(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , AppSharedPref.getQuoteId(context)
                    , id
                    , title
                    , detail
                    , nickname
                    , ratingObj)
        }

        fun deleteAddress(context: Context, id: String): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).deleteAddress(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , id)
        }

        fun getInvoiceDetailsData(context: Context, eTag: String, invoiceId: String): Observable<InvoiceDetailsData> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getInvoiceDetailsData(
                    eTag
                    , AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , invoiceId)
        }

        fun getShipmentDetailsData(context: Context, eTag: String, shipmentId: String): Observable<ShipmentDetailsData> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getShipmentDetailsData(
                    eTag
                    , AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , shipmentId)
        }

        fun checkCustomerByEmail(context: Context, email: String): Observable<CheckCustomerByEmailResponseData> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).checkCustomerByEmail(
                    AppSharedPref.getStoreId(context)
                    , email)
        }

        /* Extra */

        fun logout(context: Context): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).logout(
                    AppSharedPref.getCustomerToken(context)
                    , AppSharedPref.getFcmToken(context))
        }

        fun getSearchSuggestions(context: Context, searchQuery: String): Observable<SearchSuggestionResponse> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getSearchSuggestions(
                    AppSharedPref.getStoreId(context)
                    , searchQuery)
        }

        fun getSearchTermsList(context: Context, eTag: String): Observable<SearchTermsResponseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getSearchTermsList(
                    eTag
                    , AppSharedPref.getStoreId(context))
        }

        fun getCMSPageData(context: Context, eTag: String, id: String?): Observable<CMSPageDataModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getCMSPageData(
                    eTag
                    , id)
        }

        fun getNotificationsList(context: Context, eTag: String): Observable<NotificationListResponseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getNotificationsList(
                    eTag
                    , AppSharedPref.getStoreId(context)
                    , Utils.screenWidth
                    , Utils.screenDensity)
        }

        fun getOtherNotificationData(context: Context, eTag: String, notificationId: String): Observable<OtherNotificationResponseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getOtherNotificationData(
                    eTag
                    , notificationId)
        }

        fun uploadTokenData(context: Context, token: String?): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).uploadTokenData(
                    token
                    , AppSharedPref.getCustomerToken(context)
                    , DEFAULT_OS)
        }

        /* Other */

        fun postContactUs(context: Context, name: String, email: String, telephone: String, comment: String): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).postContactUs(
                    AppSharedPref.getStoreId(context)
                    , name
                    , email
                    , telephone
                    , comment)
        }

        fun addPriceAlert(context: Context, productId: String): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).addPriceAlert(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , productId)
        }

        fun addStockAlert(context: Context, productId: String): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).addStockAlert(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , productId)
        }

        fun getGuestOrderDetails(context: Context, type: String, incrementId: String, lastName: String, email: String, zipCode: String): Observable<GuestOrderDetailsResponseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getGuestOrderDetails(
                    AppSharedPref.getStoreId(context)
                    , type
                    , incrementId
                    , lastName
                    , email
                    , zipCode)
        }

        /* Checkout */

        fun addToCart(context: Context, productId: String, qty: String, params: JSONObject, files: ArrayList<MultipartBody.Part>?, relatedProducts: JSONArray): Observable<AddToCartResponseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).addToCart(
                    RequestBody.create(MediaType.parse("text/plain"), AppSharedPref.getStoreId(context))
                    , RequestBody.create(MediaType.parse("text/plain"), AppSharedPref.getCustomerToken(context))
                    , AppSharedPref.getQuoteId(context)
                    , RequestBody.create(MediaType.parse("text/plain"), productId)
                    , RequestBody.create(MediaType.parse("text/plain"), qty)
                    , RequestBody.create(MediaType.parse("text/plain"), params.toString())
                    , files
                    , RequestBody.create(MediaType.parse("text/plain"), relatedProducts.toString()))
        }

        fun updateProduct(context: Context, productId: String, qty: String, params: JSONObject, files: ArrayList<MultipartBody.Part>?, relatedProducts: JSONArray, itemId: String): Observable<AddToCartResponseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).updateProduct(
                    RequestBody.create(MediaType.parse("text/plain"), AppSharedPref.getStoreId(context))
                    , RequestBody.create(MediaType.parse("text/plain"), AppSharedPref.getCustomerToken(context))
                    , AppSharedPref.getQuoteId(context)
                    , RequestBody.create(MediaType.parse("text/plain"), productId)
                    , RequestBody.create(MediaType.parse("text/plain"), qty)
                    , RequestBody.create(MediaType.parse("text/plain"), params.toString())
                    , files
                    , RequestBody.create(MediaType.parse("text/plain"), relatedProducts.toString())
                    , RequestBody.create(MediaType.parse("text/plain"), itemId))
        }

        fun getCartDetails(context: Context, eTag: String): Observable<CartDetailsResponseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getCartDetails(
                    eTag
                    , AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , AppSharedPref.getQuoteId(context)
                    , Utils.screenWidth
                    , AppSharedPref.getCurrencyCode(context))
        }

        fun moveToWishList(context: Context, itemId: String): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).moveToWishList(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , itemId)
        }

        fun removeFromCart(context: Context, itemId: String): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).removeFromCart(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , AppSharedPref.getQuoteId(context)
                    , itemId)
        }

        fun applyOrRemoveCoupon(context: Context, couponCode: String, removeCoupon: Boolean): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).applyOrRemoveCoupon(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , AppSharedPref.getQuoteId(context)
                    , couponCode
                    , if (removeCoupon) 1 else 0)
        }

        fun updateCart(context: Context, itemIds: JSONArray, itemQtys: JSONArray): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).updateCart(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , AppSharedPref.getQuoteId(context)
                    , itemIds
                    , itemQtys)
        }

        fun emptyCart(context: Context): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).emptyCart(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , AppSharedPref.getQuoteId(context))
        }

        fun getCheckoutAddressInfo(context: Context): Observable<CheckoutAddressInfoResponseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getCheckoutAddressInfo(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , AppSharedPref.getQuoteId(context))
        }

        fun getShippingMethods(context: Context, shippingData: JSONObject): Observable<ShippingMethodsModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getShippingMethods(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , AppSharedPref.getQuoteId(context)
                    , AppSharedPref.getCurrencyCode(context)
                    , if (AppSharedPref.isLoggedIn(context)) "customer" else "guest"
                    , shippingData)
        }

        fun getReviewsAndPaymentsData(context: Context, shippingMethod: String?): Observable<ReviewsAndPaymentsResponseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getReviewsAndPaymentsData(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , AppSharedPref.getQuoteId(context)
                    , AppSharedPref.getCurrencyCode(context)
                    , Utils.screenWidth
                    , if (AppSharedPref.isLoggedIn(context)) "customer" else "guest"
                    , shippingMethod)
        }

        fun placeOrder(context: Context, paymentMethod: String, paymentData: JSONObject,  walletDiscount: JSONObject): Observable<SaveOrderResponseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).placeOrder(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , AppSharedPref.getQuoteId(context)
                    , paymentMethod
                    , paymentData
                    , if (AppSharedPref.isLoggedIn(context)) "customer" else "guest"
                    , AppSharedPref.getFcmToken(context)
                    , DEFAULT_OS
                    , DEFAULT_OS
                    , walletDiscount)
        }

        fun getDeliveryBoyLocation(context: Context, deliveryboyId: String): Observable<GetDeliveryBoyLocationResponseData> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getDeliveryBoyLocation(deliveryboyId)
        }

        fun saveDeliveryboyReview(context: Context, deliveryboyId: String?, customerId: String?, nickName: String, title: String, comment: String, rating: Int): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).saveDeliveryboyReview(
                    AppSharedPref.getStoreId(context),
                    AppSharedPref.getCustomerToken(context),
                    AppSharedPref.getCustomerEmail(context),
                    deliveryboyId,
                    customerId,
                    nickName,
                    title,
                    comment,
                    rating)
        }

        fun updateTokenOnCloud(context: Context, accountType: String, sellerId: String?): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).updateTokenOnCloud(
                    AppSharedPref.getCustomerId(context),
                    AppSharedPref.getCustomerName(context),
                    AppSharedPref.getCustomerImageUrl(context),
                    AppSharedPref.getFcmToken(context)!!,
                    accountType,
                    "android",
                    sellerId
            )

        }

        fun deleteTokenFromCloud(context: Context, accountType: String): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).deleteTokenFromCloud(
                    AppSharedPref.getCustomerId(context),
                    AppSharedPref.getFcmToken(context) ?: "",
                    accountType, "android"
            )
        }

        fun getRefundDetailsData(context: Context, eTag: String, refundId: String): Observable<RefundDetailsData> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getRefundDetailsData(
                    eTag
                    , AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , refundId)
        }



        /* Wallet */

        fun getManageWalletData(context: Context, eTag: String): Observable<ManageWalletAmountResponseData> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getManageWalletData(
                    eTag
                    , AppSharedPref.getStoreId(context)
                    , AppSharedPref.getQuoteId(context)
                    , AppSharedPref.getCurrencyCode(context)
                    , AppSharedPref.getWebsiteId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , Utils.screenDensity)
        }

        fun addMoney(context: Context, productId: String, price: String): Observable<AddToCartResponseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).addMoney(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , "1"
                    , productId
                    , price
                    , AppSharedPref.getCurrencyCode(context))
        }

        fun getWalletAccountDetails(context: Context, eTag: String): Observable<AccountDetailsResponseData> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getWalletAccountDetails(
                    eTag
                    , AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context))
        }

        fun saveWalletAccountInfo(context: Context, accountDetailsResponseData: AccountDetailsResponseData): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).saveWalletAccountInfo(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , accountDetailsResponseData.holderName
                    , accountDetailsResponseData.bankName
                    , accountDetailsResponseData.bankCode
                    , accountDetailsResponseData.additionalInformation)
        }

        fun deleteWalletAccountInfo(context: Context, id: String): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).deleteWalletAccountInfo(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , id)
        }

        fun getWalletTransferData(context: Context): Observable<TransferWalletAmountResponseData> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getWalletTransferData(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , Utils.screenDensity
                    , AppSharedPref.getCurrencyCode(context))
        }

        fun sendCode(context: Context,/* senderId: String,*/ receiverId: String, amount: String, walletNote: String): Observable<SendCodeResponseData> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).sendCode(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    /*  , senderId*/
                    , receiverId
                    , amount
                    , walletNote)
        }

        fun sendAmount(context: Context, /*senderId: String,*/recieverId: String, amount: String, code: String, walletNote: String, codeHash: String?): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).sendAmount(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getQuoteId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , AppSharedPref.getWebsiteId(context)
                    , recieverId
                    , amount
                    , code
                    , walletNote
                    , codeHash
            )
        }

        fun transferToBank(context: Context, amount: String, bankDetails: String, walletNote: String): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).transferToBank(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , amount
                    , bankDetails
                    , walletNote)
        }

        fun viewWalletTransaction(context: Context, transactionId: String): Observable<ViewTransactionResponseData> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).viewWalletTransaction(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , AppSharedPref.getCurrencyCode(context)
                    , transactionId)
        }

        fun addPayee(context: Context, nickname: String, email: String): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).addPayee(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , nickname
                    , email)
        }

        fun updatePayee(context: Context, id: String, nickname: String): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).updatePayee(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , id
                    , nickname)
        }

        fun deletePayee(context: Context, id: String): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).deletePayee(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , id)
        }

        fun applyWalletPaymentAmount(context: Context, wallet: String, grandTotal: String): Observable<ApplyPaymentAmountResponseData> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).applyWalletPaymentAmount(
                    AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCustomerToken(context)
                    , wallet
                    , grandTotal
                    , AppSharedPref.getCurrencyCode(context)
                    , AppSharedPref.getQuoteId(context)
                    , Utils.screenWidth
                    , AppSharedPref.getWebsiteId(context))
        }



        //auction
        fun getAuctionBidList(context: Context, pageNumber: Int): Observable<GetAuctionBidListResponseData> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getAuctionBidList(
                     AppSharedPref.getQuoteId(context)
                    , AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCurrencyCode(context)
                    , AppSharedPref.getCustomerToken(context)
                    , AppSharedPref.getWebsiteId(context)
                    , Utils.screenWidth
                    , pageNumber)
        }

        fun getAutoBidList(context: Context, pageNumber: Int): Observable<GetAutoBidListResponseData> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).getAutoBidList(
                     AppSharedPref.getQuoteId(context)
                    , AppSharedPref.getStoreId(context)
                    , AppSharedPref.getCurrencyCode(context)
                    , AppSharedPref.getCustomerToken(context)
                    , AppSharedPref.getWebsiteId(context)
                    , Utils.screenWidth
                    , pageNumber)
        }

        fun addBid(storeId: String, customerToken: String?, pro_name: String?, entity_id: String?, product_id: String?, bidding_amount: String?, auto_auction_opt: Int, auto_bid_allowed: String?, stop_auction_time_stamp: String?): Observable<BaseModel> {
            return ApiClient.getClient()!!.create(ApiDetails::class.java).addBid(
                    LOG_PARAMS
                    , LOG_RESPONSE
                    , storeId
                    , customerToken
                    , pro_name
                    , entity_id
                    , product_id
                    , bidding_amount
                    , auto_auction_opt
                    , auto_bid_allowed
                    , stop_auction_time_stamp
            )
        }

    }
}