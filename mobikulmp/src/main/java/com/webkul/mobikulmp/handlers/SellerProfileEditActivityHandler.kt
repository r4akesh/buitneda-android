package com.webkul.mobikulmp.handlers

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.provider.OpenableColumns
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.SellerProfileEditActivity
import com.webkul.mobikulmp.helpers.MpImageHelper
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.File

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.models.seller
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 1/6/19
 */

class SellerProfileEditActivityHandler(private val mContext: SellerProfileEditActivity) {
    private var mImageType: String? = null
    private var mSelectedFileURI: Uri? = null
    private var mCompanyBannerFile: File? = null
    private var mCompanyLogoFile: File? = null


    fun onClickSaveProfileBtn() {
        mContext.mContentViewBinding.loading = true
        Utils.hideKeyboard(mContext)
        var companyBannerMultipartFileBody: MultipartBody.Part? = null
        var companyLogoMultipartFileBody: MultipartBody.Part? = null
        if (mCompanyBannerFile != null) {
            val companyBannerRequestBody = RequestBody.create(MediaType.parse("image/*"), mCompanyBannerFile!!)
            companyBannerMultipartFileBody = MultipartBody.Part.createFormData("banner", mCompanyBannerFile!!.name, companyBannerRequestBody)
        }
        if (mCompanyLogoFile != null) {
            val companyLogoRequestBody = RequestBody.create(MediaType.parse("image/*"), mCompanyLogoFile!!)
            companyLogoMultipartFileBody = MultipartBody.Part.createFormData("logo", mCompanyLogoFile!!.name, companyLogoRequestBody)
        }
        mContext.mContentViewBinding.data?.let {
            MpApiConnection.saveSellerProfileData(mContext, it, companyBannerMultipartFileBody, companyLogoMultipartFileBody)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : ApiCustomCallback<BaseModel>(mContext, true) {
                        override fun onNext(baseModel: BaseModel) {
                            super.onNext(baseModel)
                            mContext.mContentViewBinding.loading = false
                            if (baseModel.success) {
                                ToastHelper.showToast(mContext, baseModel.message, Toast.LENGTH_LONG)
                                mCompanyBannerFile = null
                                mCompanyLogoFile = null
                            } else {
                                onFailureResponse(baseModel)
                            }
                        }

                        override fun onError(e: Throwable) {
                            super.onError(e)
                            onErrorResponse(e)
                        }
                    })
        }
    }

    fun onFailureResponse(response: Any) {
        AlertDialogHelper.showNewCustomDialog(
                mContext,
                mContext.getString(R.string.error),
                (response as BaseModel).message,
                false,
                mContext.getString(R.string.ok),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                }
                , ""
                , null)
    }

    private fun onErrorResponse(error: Throwable) {
        mContext.mContentViewBinding.loading = false

        if ((!NetworkHelper.isNetworkAvailable(mContext) || (error is HttpException && error.code() == 304))) {

        } else {
            AlertDialogHelper.showNewCustomDialog(
                    mContext,
                    mContext.getString(R.string.oops),
                    NetworkHelper.getErrorMessage(mContext, error),
                    false,
                    mContext.getString(R.string.try_again),
                    DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        mContext.mContentViewBinding.loading = true
                        onClickSaveProfileBtn()
                    }
                    , mContext.getString(R.string.close)
                    , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            })
        }

    }

    fun onClickDeleteBtn(imageType: String) {
        mImageType = imageType

        AlertDialogHelper.showNewCustomDialog(
                mContext,
                mContext.getString(R.string.delete),
                (if (mImageType.equals("banner")) mContext.getString(R.string.are_you_sure_you_want_to_delete_this_banner) else mContext.getString(R.string.are_you_sure_you_want_to_delete_this_logo)),
                false,
                mContext.getString(R.string.ok),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    mContext.mContentViewBinding.loading = true

                    MpApiConnection.deleteSellerImage(mContext, imageType)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(object : ApiCustomCallback<BaseModel>(mContext, true) {
                                override fun onNext(baseModel: BaseModel) {
                                    super.onNext(baseModel)
                                    mContext.mContentViewBinding.loading = false
                                    if (baseModel.success) {
                                        when (mImageType) {
                                            "banner" -> mContext.mContentViewBinding.data?.bannerImage = ""
                                            "logo" -> mContext.mContentViewBinding.data?.profileImage = ""
                                        }
                                    } else {
                                        onFailureDeleteSellerImageResponse(baseModel)
                                    }
                                }

                                override fun onError(e: Throwable) {
                                    super.onError(e)
                                    onErrorDeleteSellerImageResponse(e)
                                }
                            })
                },
                mContext.getString(R.string.cancel),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                })

    }

    private fun onErrorDeleteSellerImageResponse(error: Throwable) {
        mContext.mContentViewBinding.loading = false

        if ((!NetworkHelper.isNetworkAvailable(mContext) || (error is HttpException && error.code() == 304))) {

        } else {
            AlertDialogHelper.showNewCustomDialog(
                    mContext,
                    mContext.getString(R.string.oops),
                    NetworkHelper.getErrorMessage(mContext, error),
                    false,
                    mContext.getString(R.string.try_again),
                    DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        mContext.mContentViewBinding.loading = true
                        onClickDeleteBtn(mImageType!!)
                    }
                    , mContext.getString(R.string.close)
                    , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            })
        }
    }

    fun onFailureDeleteSellerImageResponse(response: Any) {
        AlertDialogHelper.showNewCustomDialog(
                mContext,
                mContext.getString(R.string.error),
                (response as BaseModel).message,
                false,
                mContext.getString(R.string.ok),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                }
                , ""
                , null)
    }

    fun onClickPickColorBtn(color: String?) {
        var color = color
        if (color == null) {
            color = "#FFFFFF"
        }
        Utils.hideKeyboard(mContext)
        ColorPickerDialogBuilder
                .with(mContext, R.style.AlertDialogTheme)
                .setTitle(mContext.getString(R.string.choose_color))
                .initialColor(Color.parseColor(color))
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setPositiveButton(mContext.getString(R.string.ok)) { _, selectedColor, _ ->
                    var color = Integer.toHexString(selectedColor)
                    color = if (color.length == 1) "FFFFFF" else color.substring(2, color.length)
                    mContext.mContentViewBinding.data?.backgroundColor = ("#$color")
                }
                .build()
                .show()
    }

    //
//    fun onClickHintBtn(view: View, hint: String?) {
//        if (hint != null && !hint.isEmpty()) {
//            Utils.hideKeyboard(mContext)
//            val supportFragmentManager = mContext.getSupportFragmentManager()
//            val hintDialogFragment = HintDialogFragment.newInstance(hint)
//            val transaction = supportFragmentManager.beginTransaction()
//            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
//            transaction.add(R.id.main_container, hintDialogFragment, HintDialogFragment::class.java!!.getSimpleName())
//            transaction.addToBackStack(HintDialogFragment::class.java!!.getSimpleName() + BACKSTACK_SUFFIX)
//            transaction.commit()
//        }
//    }
//
    fun onClickBrowseBtn(imageType: String) {
        Utils.hideKeyboard(mContext)
        mImageType = imageType
        selectImage()
    }

    fun selectImage() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            CropImage.startPickImageActivity(mContext)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                mContext.requestPermissions(permissions, ConstantsHelper.RC_PICK_IMAGE)
            }
        }
    }

    fun cropImage(data: Intent?) {
        CropImage.activity(CropImage.getPickImageResultUri(mContext, data))
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(if (mImageType == "banner") 4 else 1, if (mImageType == "banner") 1 else 1)
                .start(mContext)
    }

    fun updateData(data: Intent?) {
        mSelectedFileURI = CropImage.getActivityResult(data).uri
        when (mImageType) {
            "banner" -> {
                mCompanyBannerFile = File(mSelectedFileURI!!.path)
                mContext.mContentViewBinding.data?.bannerImage = mSelectedFileURI!!.path //#506
                mCompanyBannerFile?.let {
                    MpImageHelper.loadFromFile(mContext.mContentViewBinding.bannerIv, it)
                }
            }
            "logo" -> {
                mCompanyLogoFile = File(mSelectedFileURI!!.path)
                mContext.mContentViewBinding.data?.profileImage = mSelectedFileURI!!.path //#506
                mCompanyLogoFile?.let {
                    MpImageHelper.loadFromFile(mContext.mContentViewBinding.logoIv, it)
                }
            }
        }
    }

    fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = mContext.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor!!.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

    fun onClickBasicDetailsBtn() {
        if (mContext.mContentViewBinding.basicInformation.visibility == View.VISIBLE) {
            mContext.mContentViewBinding.basicInformation.visibility = View.GONE
            mContext.mContentViewBinding.basicInformationHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mContext.mContentViewBinding.basicInformation.visibility = View.VISIBLE
            mContext.mContentViewBinding.basicInformationHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mContext.mContentViewBinding.scrollView.top + mContext.mContentViewBinding.basicInformationHeading.top
                mContext.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }

    fun onClickImageAndThemeBtn() {
        if (mContext.mContentViewBinding.imageAndThemeInformation.visibility == View.VISIBLE) {
            mContext.mContentViewBinding.imageAndThemeInformation.visibility = View.GONE
            mContext.mContentViewBinding.imageAndThemeHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mContext.mContentViewBinding.imageAndThemeInformation.visibility = View.VISIBLE
            mContext.mContentViewBinding.imageAndThemeHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mContext.mContentViewBinding.scrollView.top + mContext.mContentViewBinding.imageAndThemeHeading.top
                mContext.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }

    fun onClickSocialProfilesBtn() {
        if (mContext.mContentViewBinding.socialProfilesInformation.visibility == View.VISIBLE) {
            mContext.mContentViewBinding.socialProfilesInformation.visibility = View.GONE
            mContext.mContentViewBinding.socialProfilesHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mContext.mContentViewBinding.socialProfilesInformation.visibility = View.VISIBLE
            mContext.mContentViewBinding.socialProfilesHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mContext.mContentViewBinding.scrollView.top + mContext.mContentViewBinding.socialProfilesHeading.top
                mContext.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }

    fun onClickSeoBtn() {
        if (mContext.mContentViewBinding.seoInformation.visibility == View.VISIBLE) {
            mContext.mContentViewBinding.seoInformation.visibility = View.GONE
            mContext.mContentViewBinding.seoHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mContext.mContentViewBinding.seoInformation.visibility = View.VISIBLE
            mContext.mContentViewBinding.seoHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mContext.mContentViewBinding.scrollView.top + mContext.mContentViewBinding.seoHeading.top
                mContext.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }

    fun onClickPaymentInfoBtn() {
        if (mContext.mContentViewBinding.paymentInfoInformation.visibility == View.VISIBLE) {
            mContext.mContentViewBinding.paymentInfoInformation.visibility = View.GONE
            mContext.mContentViewBinding.paymentInfoHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mContext.mContentViewBinding.paymentInfoInformation.visibility = View.VISIBLE
            mContext.mContentViewBinding.paymentInfoHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mContext.mContentViewBinding.scrollView.top + mContext.mContentViewBinding.paymentInfoHeading.top
                mContext.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }

    fun onClickPoliciesBtn() {
        if (mContext.mContentViewBinding.policiesInformation.visibility == View.VISIBLE) {
            mContext.mContentViewBinding.policiesInformation.visibility = View.GONE
            mContext.mContentViewBinding.policiesHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mContext.mContentViewBinding.policiesInformation.visibility = View.VISIBLE
            mContext.mContentViewBinding.policiesHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mContext.mContentViewBinding.scrollView.top + mContext.mContentViewBinding.policiesHeading.top
                mContext.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }

}