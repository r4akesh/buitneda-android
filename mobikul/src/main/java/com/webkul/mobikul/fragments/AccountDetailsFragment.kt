package com.webkul.mobikul.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.activities.HomeActivity
import com.webkul.mobikul.adapters.AccountFragmentRvAdapter
import com.webkul.mobikul.adapters.NavDrawerAccountRvAdapter
import com.webkul.mobikul.adapters.NavDrawerPreferencesRvAdapter
import com.webkul.mobikul.adapters.PreferencesRvAdapter
import com.webkul.mobikul.databinding.ActivityDashboardBinding
import com.webkul.mobikul.databinding.FragmentAccountDetailsBinding
import com.webkul.mobikul.handlers.AccountFragmentHandler
import com.webkul.mobikul.handlers.DashboardActivityHandler
import com.webkul.mobikul.handlers.HomeActivityHandler
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.models.ImageUploadResponseData
import com.webkul.mobikul.models.user.AccountRvModel
import com.webkul.mobikul.models.user.PreferencesRvModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File

open class AccountDetailsFragment : BaseFragment() {

    lateinit var mContentViewBinding: FragmentAccountDetailsBinding
    var mIsUpdatingProfilePic: Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_details, container, false)
        startInitialization()
//        HomeActivity.mContentViewBinding.handler= HomeActivityHandler(this)
        return mContentViewBinding.root
    }

    private fun startInitialization() {
//        updateProfilePic()
        updateBannerImage()
        updateNameAndEmail()
        setupAccountRv()
        setupPreferencesRv()
        mContentViewBinding.handler = AccountFragmentHandler(this)
    }

    open fun setupAccountRv() {
        if (AppSharedPref.isLoggedIn(context!!)) {
            val accountRvData: ArrayList<AccountRvModel> = ArrayList()
            accountRvData.add(AccountRvModel(AccountRvModel.ORDERS, getString(R.string.orders), R.drawable.orders2))
            accountRvData.add(AccountRvModel(AccountRvModel.SUGGESTION, "Suggestion", R.drawable.orders2))
            if (ApplicationConstants.ENABLE_WISHLIST && AppSharedPref.isWishlistEnabled(context!!))
                accountRvData.add(AccountRvModel(AccountRvModel.WISH_LIST, getString(R.string.wishlist), R.drawable.wish2))
//            accountRvData.add(AccountRvModel(AccountRvModel.DOWNLOADABLE_PRODUCTS, getString(R.string.downloadable_products), R.drawable.download1))
            accountRvData.add(AccountRvModel(AccountRvModel.PRODUCT_REVIEWS, getString(R.string.product_reviews), R.drawable.product_review1))
            accountRvData.add(AccountRvModel(AccountRvModel.ADDRESS_BOOK, getString(R.string.address_book), R.drawable.adress2))
            accountRvData.add(AccountRvModel(AccountRvModel.ACCOUNT_INFORMATION, getString(R.string.account_information), R.drawable.info2))
            accountRvData.add(AccountRvModel(AccountRvModel.WALLET, getString(R.string.wallet), R.drawable.ic_user_account_wallet2))
            accountRvData.add(AccountRvModel(AccountRvModel.WHATS_APP, getString(R.string.whats_app), R.drawable.ic_user_whats_app))


//            accountRvData.add(AccountRvModel(AccountRvModel.DASHBOARD, getString(R.string.dashboard), R.drawable.ic_user_dashboard))

//            accountRvData.add(AccountRvModel(AccountRvModel.QR_CODE_LOGIN, getString(R.string.web_login), R.drawable.ic_vector_qr_code))
            mContentViewBinding.navDrawerAccountRv.layoutManager = GridLayoutManager(context, 4)
            mContentViewBinding.navDrawerAccountRv.adapter = AccountFragmentRvAdapter(this, accountRvData)
            mContentViewBinding.navDrawerAccountRv.isNestedScrollingEnabled = false
        } else {
            val accountRvData: ArrayList<AccountRvModel> = ArrayList()
            accountRvData.add(AccountRvModel(AccountRvModel.LOG_IN, getString(R.string.sign_in), R.drawable.ic_user_dashboard))
            mContentViewBinding.navDrawerAccountRv.layoutManager = GridLayoutManager(context, 4)
            mContentViewBinding.navDrawerAccountRv.adapter = AccountFragmentRvAdapter(this, accountRvData)
            mContentViewBinding.navDrawerAccountRv.isNestedScrollingEnabled = false


        }
    }

    fun loginAccount() {

        val accountRvData: ArrayList<AccountRvModel> = ArrayList()
        accountRvData.add(AccountRvModel(AccountRvModel.LOG_IN, getString(R.string.sign_in), R.drawable.ic_user_dashboard))
        mContentViewBinding.navDrawerAccountRv.layoutManager = GridLayoutManager(context, 4)
        mContentViewBinding.navDrawerAccountRv.adapter = AccountFragmentRvAdapter(this, accountRvData)
        mContentViewBinding.navDrawerAccountRv.isNestedScrollingEnabled = false


    }

    private fun setupPreferencesRv() {
        val preferencesRvModel: ArrayList<PreferencesRvModel> = ArrayList()
        preferencesRvModel.add(PreferencesRvModel(PreferencesRvModel.SETTINGS, getString(R.string.settings), R.drawable.setting2))

//        if (ApplicationConstants.ENABLE_WEBSITE && !(context as HomeActivity).mHomePageDataModel.websiteData.isNullOrEmpty() && (context as HomeActivity).mHomePageDataModel.websiteData?.size ?: 0 > 1)

        if (ApplicationConstants.ENABLE_WEBSITE && AppSharedPref.getWebsiteDataCount(context!!) > 1)
            preferencesRvModel.add(PreferencesRvModel(PreferencesRvModel.WEBSITE, String.format(getString(R.string.website_x), AppSharedPref.getWebsiteLabel(context!!)), R.drawable.info2))

//        if (ApplicationConstants.ENABLE_STORES && !(context as HomeActivity).mHomePageDataModel.storeData.isNullOrEmpty() && !((context as HomeActivity).mHomePageDataModel.storeData?.size  == 1 && (context as HomeActivity).mHomePageDataModel.storeData?.get(0)?.stores?.size  == 1))
        if (ApplicationConstants.ENABLE_STORES && AppSharedPref.getStoreDataSize(context!!) >= 0)
            preferencesRvModel.add(PreferencesRvModel(PreferencesRvModel.LANGUAGE, String.format(getString(R.string.language), AppSharedPref.getStoreLabel(context!!)), R.drawable.language2))
//        if (ApplicationConstants.ENABLE_CURRENCIES && !(context as HomeActivity).mHomePageDataModel.allowedCurrencies.isNullOrEmpty() && (context as HomeActivity).mHomePageDataModel.allowedCurrencies?.size ?: 0 > 1)
        if (ApplicationConstants.ENABLE_CURRENCIES && AppSharedPref.getCurrencyCodeSize(context!!) > 1)
            preferencesRvModel.add(PreferencesRvModel(PreferencesRvModel.CURRENCY, String.format(getString(R.string.currency_x), AppSharedPref.getCurrencyLabel(context!!)), R.drawable.info2))

//        preferencesRvModel.add(PreferencesRvModel(PreferencesRvModel.COMPARE, getString(R.string.compare_products), R.drawable.compare1))
        preferencesRvModel.add(PreferencesRvModel(PreferencesRvModel.CONTACT, getString(R.string.contact_us), R.drawable.contact_us2))
        preferencesRvModel.add(PreferencesRvModel(PreferencesRvModel.ASK, getString(R.string.ask_questions), R.drawable.ask2))

        mContentViewBinding.navDrawerPreferencesRv.layoutManager = GridLayoutManager(context, 4)
        mContentViewBinding.navDrawerPreferencesRv.adapter = PreferencesRvAdapter(this, preferencesRvModel)
        mContentViewBinding.navDrawerPreferencesRv.isNestedScrollingEnabled = false
    }


/*
    private fun updateProfilePic() {
        try {
            if (!AppSharedPref.getCustomerImageUrl(context!!).isBlank()) {
                Glide.with(this).load(AppSharedPref.getCustomerImageUrl(context!!)).apply(RequestOptions.circleCropTransform()).into(mContentViewBinding.profileImage)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
*/

    private fun updateBannerImage() {
        try {
            if (!AppSharedPref.getCustomerBannerUrl(context!!).isBlank()) {
                ImageHelper.load(mContentViewBinding.bannerImage, AppSharedPref.getCustomerBannerUrl(context!!), AppSharedPref.getCustomerBannerDominantColor(context!!))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateNameAndEmail() {
        mContentViewBinding.customerNameTv.text = AppSharedPref.getCustomerName(context!!)
        mContentViewBinding.customerEmailTv.text = AppSharedPref.getCustomerEmail(context!!)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
                CropImage.activity(CropImage.getPickImageResultUri(context!!, data))
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(if (mIsUpdatingProfilePic) 1 else 3, if (mIsUpdatingProfilePic) 1 else 2)
                        .start(context!!, this)
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                uploadPic(CropImage.getActivityResult(data).uri)
            }
        }
    }

    private fun uploadPic(data: Uri) {
        try {

            val bitmap = BitmapFactory.decodeFile(data.path)
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos)

            val fileBody = RequestBody.create(MediaType.parse("image/*"), bos.toByteArray())
            val multipartFileBody = MultipartBody.Part.createFormData("files", File(data.path).name, fileBody)


            uploadBanner(multipartFileBody)

        } catch (e: Exception) {
            ToastHelper.showToast(context!!, getString(R.string.something_went_wrong))
            e.printStackTrace()
        }
    }

    private fun uploadBanner(multipartFileBody: MultipartBody.Part) {
        mContentViewBinding.loadingBanner = true
        ApiConnection.uploadBannerImage(context!!, multipartFileBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<ImageUploadResponseData>(context!!, true) {
                    override fun onNext(imageUploadResponseData: ImageUploadResponseData) {
                        super.onNext(imageUploadResponseData)
                        mContentViewBinding.loadingBanner = false
                        if (imageUploadResponseData.success) {
                            AppSharedPref.setCustomerBannerUrl(context, imageUploadResponseData.url)
                            updateBannerImage()
                        } else {
                            ToastHelper.showToast(context, imageUploadResponseData.message)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mContentViewBinding.loadingBanner = false
                        ToastHelper.showToast(context, getString(R.string.something_went_wrong))
                    }
                })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var allPermissionsGranted = true
        for (eachGrantResult in grantResults) {
            if (eachGrantResult != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false
            }
        }
        if (allPermissionsGranted) {
            if (requestCode == ConstantsHelper.RC_PICK_IMAGE) {
                mContentViewBinding.handler!!.selectImage()
            }
        }
    }
}
