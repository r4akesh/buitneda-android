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

import com.webkul.mobikul.helpers.ConstantsHelper
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_ADD_BID
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_AUCTION_BID_LIST
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_AUTO_BID_LIST
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CATALOG_ADD_TO_COMPARE
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CATALOG_ADD_TO_WISH_LIST
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CATALOG_ADVANCED_SEARCH_FORM_DATA
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CATALOG_BRAND_PRODUCT_LIST
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CATALOG_CATEGORY_LIST
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CATALOG_CATEGORY_PRODUCT_LIST
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CATALOG_COMPARE_LIST
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CATALOG_HOME_PAGE_DATA
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CATALOG_PRODUCT_PAGE_DATA
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CATALOG_PRODUCT_REVIEW_LIST
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CATALOG_PROMOTION_DATA
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CATALOG_RATING_FORM_DATA
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CATALOG_REMOVE_FROM_COMPARE_LIST
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CATALOG_SUB_CATEGORY
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CHECKOUT_ADDRESS_INFO
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CHECKOUT_ADD_TO_CART
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CHECKOUT_APPLY_COUPON
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CHECKOUT_CART_DETAILS
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CHECKOUT_EMPTY_CART
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CHECKOUT_PLACE_ORDER
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CHECKOUT_REMOVE_CART_ITEM
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CHECKOUT_REVIEWS_AND_PAYMENTS
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CHECKOUT_SHIPPING_METHODS
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CHECKOUT_UPDATE_CART
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CHECKOUT_UPDATE_ITEM_OPTION
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CHECKOUT_WISH_LIST_FROM_CART
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CONTACT_POST
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_ADDRESS_FORM_DATA
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_ADD_ALL_TO_CART
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_CHECK_CUSTOMER_BY_EMAIL
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_CREATE_ACCOUNT
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_CREATE_ACCOUNT_FORM_DATA
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_DELETE_ADDRESS
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_DOWNLOAD_PRODUCT
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_FORGOT_PASSWORD
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_GET_ACCOUNT_INFO_DATA
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_GET_ADDRESS_BOOK_DATA
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_GUEST_REORDER
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_LOGIN
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_MY_DOWNLOADS_LIST
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_ORDER_DETAIL
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_ORDER_LIST
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_PRODUCT_NOT_FOUND
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_REMOVE_FROM_WISH_LIST
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_REORDER
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_REVIEW_DETAIL
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_REVIEW_LIST
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_SAVE_ACCOUNT_INFO
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_SAVE_ADDRESS
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_SAVE_REVIEW
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_SHARE_WISH_LIST
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_UPDATE_WISH_LIST
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_UPLOAD_BANNER_IMAGE
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_UPLOAD_PROFILE_PIC
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_VIEW_INVOICE
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_VIEW_REFUND
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_VIEW_SHIPMENT
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_WEB_LOGIN
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_WISHLIST_TO_CART
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_CUSTOMER_WISH_LIST
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_DELIVERY_BOY_DELETE_TOKEN_FROM_CLOUD
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_DELIVERY_BOY_GET_LOCATION
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_DELIVERY_BOY_UPDATE_TOKEN_ON_CLOUD
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_DELIVERY_TOP_PRODUCT_LIST
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_EXTRAS_CMS_PAGES_DATA
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_EXTRAS_LOGOUT
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_EXTRAS_NOTIFICATION_LIST
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_EXTRAS_OTHER_NOTIFICATION_DATA
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_EXTRAS_SAVE_ANDROID_TOKEN
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_EXTRAS_SEARCH_SUGGESTION
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_EXTRAS_SEARCH_TERMS_LIST
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_PRODUCT_ALERT_PRICE
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_PRODUCT_ALERT_STOCK
import com.webkul.mobikul.helpers.ConstantsHelper.MOBIKUL_SALES_GUEST_VIEW
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
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.http.*

interface ApiDetails {

    /*
    * Catalog
    */

    @GET(MOBIKUL_CATALOG_HOME_PAGE_DATA)
    fun getHomePageData(@Query("eTag") eTag: String,
                        @Query("websiteId") websiteId: String,
                        @Query("storeId") storeId: String,
                        @Query("quoteId") quoteId: Int,
                        @Query("customerToken") customerToken: String,
                        @Query("currency") currency: String,
                        @Query("width") width: Int,
                        @Query("mFactor") mFactor: Float,
                        @Query("isFromUrl") isFromUrl: Int,
                        @Query("url") url: String):
            Observable<HomePageDataModel>

    @GET(MOBIKUL_CATALOG_PROMOTION_DATA)
    fun getPromotionData():
            Observable<PromotionBanner>


    @GET(MOBIKUL_DELIVERY_TOP_PRODUCT_LIST)
    fun getTopSellingProducts(@Query("storeId") storeId: String,
                              @Query("websiteId") websiteId: String,
                              @Query("quoteId") quoteId: Int,
                              @Query("customerToken") customerToken: String,
                              @Query("currency") currency: String,
                              @Query("width") width: Int,
                              @Query("mFactor") mFactor: Float,
                              @Query("pageNumber") pageNumber: Int):
            Observable<HomePageDataModel>

   /* @GET(MOBIKUL_DELIVERY_TOP_PRODUCT_LIST)
    fun getTopSellingProducts(@Query("storeId") storeId: String,
                              @Query("websiteId") websiteId: String,
                              @Query("quoteId") quoteId: Int,
                              @Query("customerToken") customerToken: String,
                              @Query("currency") currency: String,
                              @Query("width") width: Int,
                              @Query("mFactor") mFactor: Float,
                              @Query("pageNumber") pageNumber: Int):
            Deferred<HomePageDataModel>*/





    @GET(MOBIKUL_CUSTOMER_WEB_LOGIN)
    fun customerWebLogin(
            @Query("customerToken") customerToken: String,
            @Query("token") token: String
    ): Observable<BaseModel>

    @GET(MOBIKUL_CATALOG_SUB_CATEGORY)
    fun getSubCategoryData(@Query("eTag") eTag: String,
                           @Query("storeId") storeId: String,
                           @Query("quoteId") quoteId: Int,
                           @Query("customerToken") customerToken: String,
                           @Query("currency") currency: String,
                           @Query("width") width: Int,
                           @Query("mFactor") mFactor: Float,
                           @Query("categoryId") categoryId: String):
            Observable<SubCategoryResponseModel>

    @GET(MOBIKUL_CATALOG_CATEGORY_LIST)
    fun getSubCategoryList(
            @Query("storeId") storeId: String,
            @Query("categoryId") categoryId: String?):
            Observable<Category>

    @GET(MOBIKUL_CATALOG_CATEGORY_PRODUCT_LIST)
    fun getCatalogProductData(@Query("eTag") eTag: String,
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
                              @Query("filterData") filterData: JSONArray):
            Observable<CatalogProductsResponseModel>

    @GET(MOBIKUL_CATALOG_BRAND_PRODUCT_LIST)
    fun getBrandCatalogProductData(@Query("eTag") eTag: String,
                                   @Query("storeId") storeId: String,
                                   @Query("quoteId") quoteId: Int,
                                   @Query("customerToken") customerToken: String,
                                   @Query("currency") currency: String,
                                   @Query("width") width: Int,
                                   @Query("mFactor") mFactor: Float,
                                   @Query("type") type: String,
                                   @Query("brandId") brandId: String,
                                   @Query("pageNumber") pageNumber: Int,
                                   @Query("sortData") sortData: JSONArray,
                                   @Query("filterData") filterData: JSONArray):
            Observable<CatalogProductsResponseModel>

    @GET(MOBIKUL_CATALOG_ADVANCED_SEARCH_FORM_DATA)
    fun getAdvanceSearchFormData(@Query("eTag") eTag: String,
                                 @Query("storeId") storeId: String,
                                 @Query("currency") currency: String,
                                 @Query("customCollection") customCollection: Int):
            Observable<AdvancedSearchFormModel>

    @Multipart
    @POST(MOBIKUL_CATALOG_ADD_TO_WISH_LIST)
    fun addToWishList(@Part("storeId") storeId: RequestBody,
                      @Part("customerToken") customerToken: RequestBody,
                      @Part("productId") productId: RequestBody,
                      @Part("params") params: RequestBody,
                      @Part("qty") qty: RequestBody,
                      @Part files: List<MultipartBody.Part>):
            Observable<AddToWishListResponseModel>

    @GET(MOBIKUL_CATALOG_PRODUCT_PAGE_DATA)
    fun getProductPageData(@Query("eTag") eTag: String,
                           @Query("storeId") storeId: String,
                           @Query("customerToken") customerToken: String,
                           @Query("quoteId") quoteId: Int,
                           @Query("websiteId") websiteId: String,
                           @Query("width") width: Int,
                           @Query("currency") currency: String,
                           @Query("productId") productId: String):
            Observable<ProductDetailsPageModel>

    @GET(MOBIKUL_CATALOG_PRODUCT_REVIEW_LIST)
    fun getProductReviewList(@Query("eTag") eTag: String,
                             @Query("storeId") storeId: String,
                             @Query("customerToken") customerToken: String,
                             @Query("quoteId") quoteId: Int,
                             @Query("width") width: Int,
                             @Query("productId") productId: String,
                             @Query("pageNumber") pageNumber: Int):
            Observable<ReviewListData>

    @GET(MOBIKUL_CATALOG_RATING_FORM_DATA)
    fun getRatingForData(@Query("eTag") eTag: String,
                         @Query("storeId") storeId: String):
            Observable<ProductRatingFormDataModel>

    @FormUrlEncoded
    @POST(MOBIKUL_CATALOG_ADD_TO_COMPARE)
    fun addToCompare(@Field("storeId") eTag: String,
                     @Field("customerToken") storeId: String,
                     @Field("productId") customerToken: String):
            Observable<BaseModel>

    @GET(MOBIKUL_CATALOG_COMPARE_LIST)
    fun getCompareList(@Query("eTag") eTag: String,
                       @Query("storeId") storeId: String,
                       @Query("customerToken") customerToken: String,
                       @Query("width") width: Int,
                       @Query("currency") currency: String):
            Observable<CompareListData>

    @DELETE(MOBIKUL_CATALOG_REMOVE_FROM_COMPARE_LIST)
    fun deleteFromCompareList(@Query("storeId") storeId: String,
                              @Query("customerToken") customerToken: String,
                              @Query("productId") productId: String):
            Observable<BaseModel>

    /*
     * Customer
     */

    @FormUrlEncoded
    @POST(MOBIKUL_CUSTOMER_LOGIN)
    fun login(@Field("websiteId") websiteId: String,
              @Field("storeId") storeId: String,
              @Field("quoteId") quoteId: Int,
              @Field("width") width: Int,
              @Field("mFactor") mFactor: Float,
              @Field("username") username: String?,
              @Field("mobile") mobile: String?,
              @Field("password") password: String,
              @Field("token") token: String?,
              @Field("os") os: String):
            Observable<LoginResponseModel>

    @FormUrlEncoded
    @POST(MOBIKUL_CUSTOMER_FORGOT_PASSWORD)
    fun forgotPassword(@Field("websiteId") websiteId: String,
                       @Field("storeId") storeId: String,
                       @Field("email") email: String):
            Observable<BaseModel>

    @GET(MOBIKUL_CUSTOMER_CREATE_ACCOUNT_FORM_DATA)
    fun createAccountFormData(@Query("eTag") eTag: String,
                              @Query("storeId") storeId: String):
            Observable<SignUpFormModel>

    @FormUrlEncoded
    @POST(MOBIKUL_CUSTOMER_CREATE_ACCOUNT)
    fun signUp(@Field("websiteId") websiteId: String,
               @Field("storeId") storeId: String,
               @Field("quoteId") quoteId: Int,
               @Field("width") width: Int,
               @Field("mFactor") mFactor: Float,
               @Field("prefix") prefix: String?,
               @Field("firstName") firstName: String?,
               @Field("middleName") middleName: String?,
               @Field("lastName") lastName: String?,
               @Field("suffix") suffix: String?,
               @Field("dob") dob: String?,
               @Field("taxvat") taxvat: String?,
               @Field("gender") gender: Int,
               @Field("email") email: String?,
               @Field("password") password: String?,
               @Field("pictureURL") pictureURL: String?,
               @Field("isSocial") isSocial: Int,
               @Field("mobile") mobile: String?,
               @Field("token") token: String?,
               @Field("shopUrl") shopURL: String?,
               @Field("becomeSeller") becomeSeller: Int?,
               @Field("os") os: String,
               @Field("orderId") orderId: String?
    ):
            Observable<SignUpResponseModel>

    @GET(MOBIKUL_CUSTOMER_ORDER_LIST)
    fun getOrderList(@Query("eTag") eTag: String,
                     @Query("storeId") storeId: String,
                     @Query("customerToken") customerToken: String,
                     @Query("currency") currency: String,
                     @Query("pageNumber") pageNumber: Int,
                     @Query("forDashboard") forDashboard: String?):
            Observable<OrderListResponseModel>

    @GET(MOBIKUL_CUSTOMER_MY_DOWNLOADS_LIST)
    fun getDownloadableProductsList(@Query("eTag") eTag: String,
                                    @Query("storeId") storeId: String,
                                    @Query("customerToken") customerToken: String,
                                    @Query("pageNumber") pageNumber: Int):
            Observable<DownloadableProductsListResponseModel>

    @GET(MOBIKUL_CUSTOMER_WISH_LIST)
    fun getWishList(@Query("eTag") eTag: String,
                    @Query("storeId") storeId: String,
                    @Query("customerToken") customerToken: String,
                    @Query("currency") currency: String,
                    @Query("pageNumber") pageNumber: Int):
            Observable<WishListResponseModel>

    @GET(MOBIKUL_CUSTOMER_REVIEW_LIST)
    fun getReviewsList(@Query("eTag") eTag: String,
                       @Query("storeId") storeId: String,
                       @Query("customerToken") customerToken: String,
                       @Query("pageNumber") pageNumber: Int,
                       @Query("width") width: Int,
                       @Query("forDashboard") forDashboard: String?):
            Observable<ReviewListResponseModel>

    @GET(MOBIKUL_CUSTOMER_GET_ADDRESS_BOOK_DATA)
    fun getAddressBookData(@Query("eTag") eTag: String,
                           @Query("storeId") storeId: String,
                           @Query("customerToken") customerToken: String,
                           @Query("forDashboard") forDashboard: String?):
            Observable<AddressBookResponseModel>

    @GET(MOBIKUL_CUSTOMER_GET_ACCOUNT_INFO_DATA)
    fun getAccountInfo(@Query("eTag") eTag: String,
                       @Query("storeId") storeId: String,
                       @Query("customerToken") customerToken: String):
            Observable<AccountInfoResponseModel>

    @FormUrlEncoded
    @POST(MOBIKUL_CUSTOMER_SAVE_ACCOUNT_INFO)
    fun saveAccountInfo(@Field("storeId") storeId: String,
                        @Field("customerToken") customerToken: String,
                        @Field("prefix") prefix: String,
                        @Field("firstName") firstName: String,
                        @Field("middleName") middleName: String,
                        @Field("lastName") lastName: String,
                        @Field("suffix") suffix: String,
                        @Field("dob") dob: String,
                        @Field("taxvat") taxvat: String,
                        @Field("gender") gender: Int,
                        @Field("doChangeEmail") doChangeEmail: Int,
                        @Field("email") email: String,
                        @Field("doChangePassword") doChangePassword: Int,
                        @Field("currentPassword") currentPassword: String,
                        @Field("newPassword") newPassword: String,
                        @Field("confirmPassword") confirmPassword: String,
                        @Field("mobile") mobile: String):
            Observable<SaveAccountInfoResponseModel>

    @FormUrlEncoded
    @POST(MOBIKUL_CUSTOMER_REORDER)
    fun reorder(@Field("storeId") storeId: String,
                @Field("customerToken") customerToken: String,
                @Field("incrementId") incrementId: String?):
            Observable<ReOrderModel>

    @FormUrlEncoded
    @POST(MOBIKUL_CUSTOMER_GUEST_REORDER)
    fun reorderGuest(@Field("storeId") storeId: String,
                     @Field("incrementId") incrementId: String?):
            Observable<ReOrderModel>

    @GET(MOBIKUL_CUSTOMER_ORDER_DETAIL)
    fun getOrderDetails(@Query("eTag") eTag: String,
                        @Query("storeId") storeId: String,
                        @Query("customerToken") customerToken: String,
                        @Query("currency") currency: String,
                        @Query("incrementId") incrementId: String):
            Observable<OrderDetailsModel>

    @GET(MOBIKUL_CUSTOMER_DOWNLOAD_PRODUCT)
    fun downloadProduct(@Query("eTag") eTag: String,
                        @Query("storeId") storeId: String,
                        @Query("customerToken") customerToken: String,
                        @Query("hash") hash: String):
            Observable<DownloadProductsResponseModel>

    @GET(MOBIKUL_CUSTOMER_REVIEW_DETAIL)
    fun getReviewDetails(@Query("eTag") eTag: String,
                         @Query("storeId") storeId: String,
                         @Query("customerToken") customerToken: String,
                         @Query("width") width: Int,
                         @Query("reviewId") reviewId: String):
            Observable<ReviewDetailsResponseModel>

    @DELETE(MOBIKUL_CUSTOMER_REMOVE_FROM_WISH_LIST)
    fun removeFromWishList(@Query("storeId") storeId: String,
                           @Query("customerToken") customerToken: String,
                           @Query("itemId") itemId: String):
            Observable<ReviewDetailsResponseModel>

    @FormUrlEncoded
    @POST(MOBIKUL_CUSTOMER_WISHLIST_TO_CART)
    fun wishListToCart(@Field("storeId") storeId: String,
                       @Field("customerToken") customerToken: String,
                       @Field("id") productId: String,
                       @Field("itemId") itemId: String,
                       @Field("qty") qty: String):
            Observable<BaseModel>

    @GET(MOBIKUL_CUSTOMER_ADDRESS_FORM_DATA)
    fun getAddressFormData(@Query("eTag") eTag: String,
                           @Query("storeId") storeId: String,
                           @Query("customerToken") customerToken: String,
                           @Query("addressId") addressId: String):
            Observable<AddressFormResponseModel>



    @Multipart
    @POST(MOBIKUL_CUSTOMER_PRODUCT_NOT_FOUND)
    fun saveProductNotFoundData(@Part file: MultipartBody.Part,
                                @Part("title") title: RequestBody,
                                @Part("email") email: RequestBody,
                                @Part("subject") subject: RequestBody,
                                @Part("message") message: RequestBody,
    ):
            Observable<BaseModel>


    @FormUrlEncoded
    @POST(MOBIKUL_CUSTOMER_SAVE_ADDRESS)
    fun saveAddress(@Field("storeId") storeId: String,
                    @Field("customerToken") customerToken: String,
                    @Field("addressId") addressId: String,
                    @Field("addressData") addressData: String):
            Observable<BaseModel>

    @FormUrlEncoded
    @POST(MOBIKUL_CUSTOMER_ADD_ALL_TO_CART)
    fun addAllToCart(@Field("storeId") storeId: String,
                     @Field("customerToken") customerToken: String,
                     @Field("itemData") qty: JSONObject):
            Observable<BaseModel>

    @FormUrlEncoded
    @POST(MOBIKUL_CUSTOMER_SHARE_WISH_LIST)
    fun shareWishList(@Field("storeId") storeId: String,
                      @Field("customerToken") customerToken: String,
                      @Field("emails") emails: String,
                      @Field("message") message: String):
            Observable<BaseModel>

    @Multipart
    @POST(MOBIKUL_CUSTOMER_UPLOAD_PROFILE_PIC)
    fun uploadProfileImage(@Part("storeId") storeId: RequestBody,
                           @Part("customerToken") customerToken: RequestBody,
                           @Part("width") width: RequestBody,
                           @Part("mFactor") mFactor: RequestBody,
                           @Part file: MultipartBody.Part):
            Observable<ImageUploadResponseData>

    @Multipart
    @POST(MOBIKUL_CUSTOMER_UPLOAD_BANNER_IMAGE)
    fun uploadBannerImage(@Part("storeId") storeId: RequestBody,
                          @Part("customerToken") customerToken: RequestBody,
                          @Part("width") width: RequestBody,
                          @Part("mFactor") mFactor: RequestBody,
                          @Part file: MultipartBody.Part):
            Observable<ImageUploadResponseData>

    @FormUrlEncoded
    @POST(MOBIKUL_CUSTOMER_UPDATE_WISH_LIST)
    fun updateWishList(@Field("storeId") storeId: String,
                       @Field("customerToken") customerToken: String,
                       @Field("itemData") itemData: JSONArray):
            Observable<BaseModel>

    @FormUrlEncoded
    @POST(MOBIKUL_CUSTOMER_SAVE_REVIEW)
    fun saveReview(@Field("storeId") storeId: String,
                   @Field("customerToken") customerToken: String,
                   @Field("quotId") quotId: Int,
                   @Field("productId") productId: String,
                   @Field("title") title: String,
                   @Field("detail") detail: String,
                   @Field("nickname") nickname: String,
                   @Field("ratings") ratings: JSONObject):
            Observable<BaseModel>

    @DELETE(MOBIKUL_CUSTOMER_DELETE_ADDRESS)
    fun deleteAddress(@Query("storeId") storeId: String,
                      @Query("customerToken") customerToken: String,
                      @Query("addressId") addressId: String):
            Observable<BaseModel>

    @GET(MOBIKUL_CUSTOMER_VIEW_INVOICE)
    fun getInvoiceDetailsData(@Query("eTag") eTag: String,
                              @Query("storeId") storeId: String,
                              @Query("customerToken") customerToken: String,
                              @Query("invoiceId") invoiceId: String):
            Observable<InvoiceDetailsData>

    @GET(MOBIKUL_CUSTOMER_VIEW_SHIPMENT)
    fun getShipmentDetailsData(@Query("eTag") eTag: String,
                               @Query("storeId") storeId: String,
                               @Query("customerToken") customerToken: String,
                               @Query("shipmentId") shipmentId: String):
            Observable<ShipmentDetailsData>

    @GET(MOBIKUL_CUSTOMER_CHECK_CUSTOMER_BY_EMAIL)
    fun checkCustomerByEmail(@Query("storeId") storeId: String,
                             @Query("email") email: String):
            Observable<CheckCustomerByEmailResponseData>


    /*
     * Extra
     */

    @FormUrlEncoded
    @POST(MOBIKUL_EXTRAS_LOGOUT)
    fun logout(@Field("customerToken") customerToken: String,
               @Field("token") token: String?):
            Observable<BaseModel>

    @GET(MOBIKUL_EXTRAS_SEARCH_SUGGESTION)
    fun getSearchSuggestions(@Query("storeId") storeId: String,
                             @Query("searchQuery") customerToken: String):
            Observable<SearchSuggestionResponse>

    @GET(MOBIKUL_EXTRAS_SEARCH_TERMS_LIST)
    fun getSearchTermsList(@Query("eTag") eTag: String,
                           @Query("storeId") storeId: String):
            Observable<SearchTermsResponseModel>

    @GET(MOBIKUL_EXTRAS_CMS_PAGES_DATA)
    fun getCMSPageData(@Query("eTag") eTag: String,
                       @Query("id") id: String?):
            Observable<CMSPageDataModel>

    @GET(MOBIKUL_EXTRAS_NOTIFICATION_LIST)
    fun getNotificationsList(@Query("eTag") eTag: String,
                             @Query("storeId") storeId: String,
                             @Query("width") width: Int,
                             @Query("mFactor") mFactor: Float):
            Observable<NotificationListResponseModel>

    @GET(MOBIKUL_EXTRAS_OTHER_NOTIFICATION_DATA)
    fun getOtherNotificationData(@Query("eTag") eTag: String,
                                 @Query("notificationId") notificationId: String):
            Observable<OtherNotificationResponseModel>

    @FormUrlEncoded
    @POST(MOBIKUL_EXTRAS_SAVE_ANDROID_TOKEN)
    fun uploadTokenData(@Field("token") token: String?,
                        @Field("customerToken") customerToken: String,
                        @Field("os") os: String):
            Observable<BaseModel>


    /*
     * Other
     */

    @FormUrlEncoded
    @POST(MOBIKUL_CONTACT_POST)
    fun postContactUs(@Field("storeId") storeId: String,
                      @Field("name") name: String,
                      @Field("email") email: String,
                      @Field("telephone") telephone: String,
                      @Field("comment") comment: String?):
            Observable<BaseModel>

    @FormUrlEncoded
    @POST(MOBIKUL_PRODUCT_ALERT_PRICE)
    fun addPriceAlert(@Field("storeId") storeId: String,
                      @Field("customerToken") customerToken: String,
                      @Field("productId") productId: String?):
            Observable<BaseModel>

    @FormUrlEncoded
    @POST(MOBIKUL_PRODUCT_ALERT_STOCK)
    fun addStockAlert(@Field("storeId") storeId: String,
                      @Field("customerToken") customerToken: String,
                      @Field("productId") productId: String?):
            Observable<BaseModel>

    @FormUrlEncoded
    @POST(MOBIKUL_SALES_GUEST_VIEW)
    fun getGuestOrderDetails(@Field("storeId") storeId: String,
                             @Field("type") type: String,
                             @Field("incrementId") incrementId: String,
                             @Field("lastName") lastName: String,
                             @Field("email") email: String,
                             @Field("zipCode") zipCode: String?):
            Observable<GuestOrderDetailsResponseModel>


    /*
     * Checkout
     */

    @Multipart
    @POST(MOBIKUL_CHECKOUT_ADD_TO_CART)
    fun addToCart(@Part("storeId") storeId: RequestBody,
                  @Part("customerToken") customerToken: RequestBody,
                  @Part("quoteId") quoteId: Int,
                  @Part("productId") productId: RequestBody,
                  @Part("qty") qty: RequestBody,
                  @Part("params") params: RequestBody,
                  @Part file: List<MultipartBody.Part>?,
                  @Part("relatedProducts") relatedProducts: RequestBody):
            Observable<AddToCartResponseModel>

    @Multipart
    @POST(MOBIKUL_CHECKOUT_UPDATE_ITEM_OPTION)
    fun updateProduct(@Part("storeId") storeId: RequestBody,
                      @Part("customerToken") customerToken: RequestBody,
                      @Part("quoteId") quoteId: Int,
                      @Part("productId") productId: RequestBody,
                      @Part("qty") qty: RequestBody,
                      @Part("params") params: RequestBody,
                      @Part file: List<MultipartBody.Part>?,
                      @Part("relatedProducts") relatedProducts: RequestBody,
                      @Part("itemId") itemId: RequestBody):
            Observable<AddToCartResponseModel>

    @GET(MOBIKUL_CHECKOUT_CART_DETAILS)
    fun getCartDetails(@Query("eTag") eTag: String,
                       @Query("storeId") storeId: String,
                       @Query("customerToken") customerToken: String,
                       @Query("quoteId") quoteId: Int,
                       @Query("width") width: Int,
                       @Query("currency") currency: String):
            Observable<CartDetailsResponseModel>

    @FormUrlEncoded
    @POST(MOBIKUL_CHECKOUT_WISH_LIST_FROM_CART)
    fun moveToWishList(@Field("storeId") storeId: String,
                       @Field("customerToken") customerToken: String,
                       @Field("itemId") itemId: String):
            Observable<BaseModel>

    @FormUrlEncoded
    @POST(MOBIKUL_CHECKOUT_REMOVE_CART_ITEM)
    fun removeFromCart(@Field("storeId") storeId: String,
                       @Field("customerToken") customerToken: String,
                       @Field("quoteId") quoteId: Int,
                       @Field("itemId") itemId: String):
            Observable<BaseModel>

    @FormUrlEncoded
    @POST(MOBIKUL_CHECKOUT_APPLY_COUPON)
    fun applyOrRemoveCoupon(@Field("storeId") storeId: String,
                            @Field("customerToken") customerToken: String,
                            @Field("quoteId") quoteId: Int,
                            @Field("couponCode") couponCode: String,
                            @Field("removeCoupon") removeCoupon: Int):
            Observable<BaseModel>

    @FormUrlEncoded
    @POST(MOBIKUL_CHECKOUT_UPDATE_CART)
    fun updateCart(@Field("storeId") storeId: String,
                   @Field("customerToken") customerToken: String,
                   @Field("quoteId") quoteId: Int,
                   @Field("itemIds") itemIds: JSONArray,
                   @Field("itemQtys") itemQtys: JSONArray):
            Observable<BaseModel>

    @FormUrlEncoded
    @POST(MOBIKUL_CHECKOUT_EMPTY_CART)
    fun emptyCart(@Field("storeId") storeId: String,
                  @Field("customerToken") customerToken: String,
                  @Field("quoteId") quoteId: Int):
            Observable<BaseModel>

    @GET(MOBIKUL_CHECKOUT_ADDRESS_INFO)
    fun getCheckoutAddressInfo(@Query("storeId") storeId: String,
                               @Query("customerToken") customerToken: String,
                               @Query("quoteId") quoteId: Int):
            Observable<CheckoutAddressInfoResponseModel>

    @GET(MOBIKUL_CHECKOUT_SHIPPING_METHODS)
    fun getShippingMethods(@Query("storeId") storeId: String,
                           @Query("customerToken") customerToken: String,
                           @Query("quoteId") quoteId: Int,
                           @Query("currency") currency: String,
                           @Query("method") method: String,
                           @Query("shippingData") shippingData: JSONObject):
            Observable<ShippingMethodsModel>

    @FormUrlEncoded
    @POST(MOBIKUL_CHECKOUT_REVIEWS_AND_PAYMENTS)
    fun getReviewsAndPaymentsData(@Field("storeId") storeId: String,
                                  @Field("customerToken") customerToken: String,
                                  @Field("quoteId") quoteId: Int,
                                  @Field("currency") currency: String,
                                  @Field("width") width: Int,
                                  @Field("method") method: String,
                                  @Field("shippingMethod") shippingMethod: String?):
            Observable<ReviewsAndPaymentsResponseModel>

    @FormUrlEncoded
    @POST(MOBIKUL_CHECKOUT_PLACE_ORDER)
    fun placeOrder(@Field("storeId") storeId: String,
                   @Field("customerToken") customerToken: String,
                   @Field("quoteId") quoteId: Int,
                   @Field("paymentMethod") paymentMethod: String,
                   @Field("billingData") paymentData: JSONObject,
                   @Field("method") method: String,
                   @Field("token") token: String?,
                   @Field("purchasePoint") purchasePoint: String?,
                   @Field("os") os: String,
                   @Field("walletDiscount") walletDiscount: JSONObject):
            Observable<SaveOrderResponseModel>


    /* Delivery Boy */
    @GET(MOBIKUL_DELIVERY_BOY_GET_LOCATION)
    fun getDeliveryBoyLocation(@Query("deliveryboyId") deliveryboyId: String):
            Observable<GetDeliveryBoyLocationResponseData>


    @FormUrlEncoded
    @POST(ConstantsHelper.MOBIKUL_DELIVERY_BOY_ADD_REVIEW)
    fun saveDeliveryboyReview(
            @Field("storeId") storeId: String,
            @Field("customerToken") customerToken: String,
            @Field("customerEmail") customerEmail: String,
            @Field("deliveryboyId") deliveryboyId: String?,
            @Field("customerId") customerId: String?,
            @Field("nickName") nickName: String,
            @Field("title") title: String,
            @Field("comment") comment: String,
            @Field("rating") rating: Int): Observable<BaseModel>

    @FormUrlEncoded
    @POST(MOBIKUL_DELIVERY_BOY_UPDATE_TOKEN_ON_CLOUD)
    fun updateTokenOnCloud(
            @Field("userId") userId: String,
            @Field("name") username: String,
            @Field("avatar") avatar: String,
            @Field("token") token: String,
            @Field("accountType") accountType: String,
            @Field("os") os: String,
            @Field("sellerId") sellerId: String?
    ): Observable<BaseModel>

    @FormUrlEncoded
    @POST(MOBIKUL_DELIVERY_BOY_DELETE_TOKEN_FROM_CLOUD)
    fun deleteTokenFromCloud(
            @Field("userId") userId: String,
            @Field("token") token: String,
            @Field("accountType") accountType: String,
            @Field("os") os: String
    ): Observable<BaseModel>


    @GET(MOBIKUL_CUSTOMER_VIEW_REFUND)
    fun getRefundDetailsData(@Query("eTag") eTag: String,
                             @Query("storeId") storeId: String,
                             @Query("customerToken") customerToken: String,
                             @Query("creditMemoId") invoiceId: String):
            Observable<RefundDetailsData>


    /* Wallet */

    @FormUrlEncoded
    @POST(ConstantsHelper.MOBIKUL_WALLET_MANAGE)
    fun getManageWalletData(@Field("eTag") eTag: String,
                            @Field("storeId") storeId: String,
                            @Field("quoteId") quoteId: Int,
                            @Field("currency") currency: String,
                            @Field("websiteId") websiteId: String,
                            @Field("customerToken") customerToken: String,
                            @Field("mFactor") mFactor: Float):
            Observable<ManageWalletAmountResponseData>

    @FormUrlEncoded
    @POST(ConstantsHelper.MOBIKUL_WALLET_ADD)
    fun addMoney(@Field("storeId") storeId: String,
                 @Field("customerToken") customerToken: String,
                 @Field("qty") qty: String,
                 @Field("productId") producId: String,
                 @Field("price") price: String,
                 @Field("currency") currency: String):
            Observable<AddToCartResponseModel>

    @FormUrlEncoded
    @POST(ConstantsHelper.MOBIKUL_WALLET_ACCOUNT_DETAILS)
    fun getWalletAccountDetails(@Field("eTag") eTag: String,
                                @Field("storeId") storeId: String,
                                @Field("customerToken") customerToken: String):
            Observable<AccountDetailsResponseData>

    @FormUrlEncoded
    @POST(ConstantsHelper.MOBIKUL_WALLET_SAVE_ACCOUNT_INFO)
    fun saveWalletAccountInfo(@Field("storeId") storeId: String,
                              @Field("customerToken") customerToken: String,
                              @Field("holdername") holdername: String,
                              @Field("bankname") bankname: String,
                              @Field("bankcode") bankcode: String,
                              @Field("addtional") addtional: String):
            Observable<BaseModel>

    @FormUrlEncoded
    @POST(ConstantsHelper.MOBIKUL_WALLET_DELETE_ACCOUNT_INFO)
    fun deleteWalletAccountInfo(@Field("storeId") storeId: String,
                                @Field("customerToken") customerToken: String,
                                @Field("id") id: String):
            Observable<BaseModel>

    @FormUrlEncoded
    @POST(ConstantsHelper.MOBIKUL_WALLET_TRANSFER)
    fun getWalletTransferData(@Field("storeId") storeId: String,
                              @Field("customerToken") customerToken: String,
                              @Field("mFactor") mFactor: Float,
                              @Field("currency") currency: String):
            Observable<TransferWalletAmountResponseData>

    @FormUrlEncoded
    @POST(ConstantsHelper.MOBIKUL_WALLET_SEND_CODE)
    fun sendCode(@Field("storeId") storeId: String,
                 @Field("customerToken") customerToken: String,
            /*        @Field("senderId") senderId: String,*/
                 @Field("receiverId") receiverId: String,
                 @Field("amount") amount: String,
                 @Field("walletNote") walletNote: String):
            Observable<SendCodeResponseData>

    @FormUrlEncoded
    @POST(ConstantsHelper.MOBIKUL_WALLET_SEND_AMOUNT)
    fun sendAmount(
            @Field("storeId") storeId: String,
            @Field("quoteId") quoteId: Int,
            @Field("customerToken") customerToken: String,
            @Field("websiteId") websiteId: String,
            @Field("receiverId") receiverId: String,
            @Field("amount") amount: String,
            @Field("code") code: String,
            @Field("walletNote") walletNote: String,
            @Field("codeHash") codeHash: String?
    ): Observable<BaseModel>

    @FormUrlEncoded
    @POST(ConstantsHelper.MOBIKUL_WALLET_TRANSFER_BANK)
    fun transferToBank(@Field("storeId") storeId: String,
                       @Field("customerToken") customerToken: String,
                       @Field("amount") amount: String,
                       @Field("bankDetails") bankDetails: String,
                       @Field("walletNote") walletNote: String):
            Observable<BaseModel>

    @FormUrlEncoded
    @POST(ConstantsHelper.MOBIKUL_WALLET_VIEW_TRANSACTION)
    fun viewWalletTransaction(@Field("storeId") storeId: String,
                              @Field("customerToken") customerToken: String,
                              @Field("currency") currency: String,
                              @Field("transactionId") transactionId: String):
            Observable<ViewTransactionResponseData>


    @FormUrlEncoded
    @POST(ConstantsHelper.MOBIKUL_WALLET_ADD_PAYEE)
    fun addPayee(
            @Field("storeId") storeId: String,
            @Field("customerToken") customerToken: String,
            @Field("nickName") nickName: String,
            @Field("customerEmail") customerEmail: String,
    ):
            Observable<BaseModel>

    @FormUrlEncoded
    @POST(ConstantsHelper.MOBIKUL_WALLET_UPDATE_PAYEE)
    fun updatePayee(@Field("storeId") storeId: String,
                    @Field("customerToken") customerToken: String,
                    @Field("id") id: String,
                    @Field("nickName") nickName: String):
            Observable<BaseModel>

    @FormUrlEncoded
    @POST(ConstantsHelper.MOBIKUL_WALLET_DELETE_PAYEE)
    fun deletePayee(@Field("storeId") storeId: String,
                    @Field("customerToken") customerToken: String,
                    @Field("id") id: String):
            Observable<BaseModel>

    @FormUrlEncoded
    @POST(ConstantsHelper.MOBIKUL_WALLET_APPLY_PAYMENT_AMOUNT)
    fun applyWalletPaymentAmount(@Field("storeId") storeId: String,
                                 @Field("customerToken") customerToken: String,
                                 @Field("wallet") wallet: String,
                                 @Field("grandtotal") grandtotal: String,
                                 @Field("currency") currency: String,
                                 @Field("quoteId") quoteId: Int,
                                 @Field("width") width: Int,
                                 @Field("websiteId") websiteId: String):
            Observable<ApplyPaymentAmountResponseData>


    //auction

    @GET(MOBIKUL_AUCTION_BID_LIST)
    fun getAuctionBidList(@Query("quoteId") quoteId: Int,
                          @Query("storeId") storeId: String,
                          @Query("currency") currency: String,
                          @Query("customerToken") customerToken: String,
                          @Query("websiteId") websiteId: String,
                          @Query("width") width: Int,
                          @Query("pageNumber") pageNumber: Int):
            Observable<GetAuctionBidListResponseData>

    @GET(MOBIKUL_AUTO_BID_LIST)
    fun getAutoBidList(@Query("quoteId") quoteId: Int,
                       @Query("storeId") storeId: String,
                       @Query("currency") currency: String,
                       @Query("customerToken") customerToken: String,
                       @Query("websiteId") websiteId: String,
                       @Query("width") width: Int,
                       @Query("pageNumber") pageNumber: Int):
            Observable<GetAutoBidListResponseData>

    @FormUrlEncoded
    @POST(MOBIKUL_ADD_BID)
    fun addBid(
            @Field("logParams") logParams: Int, @Field("logResponse") logResponse: Int, @Field("storeId") storeId: String, @Field("customerToken") customerToken: String?, @Field("proName") pro_name: String?, @Field("entityId") entity_id: String?, @Field("productId") product_id: String?, @Field("biddingAmount") bidding_amount: String?, @Field("autoAuctionOpt") auto_auction_opt: Int, @Field("autoBidAllowed") auto_bid_allowed: String?, @Field("stopAuctionTimeStamp") stop_auction_time_stamp: String?
    ): Observable<BaseModel>

}