package com.webkul.mobikulmp.handlers

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_ID
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.SellerAddProductActivity
import com.webkul.mobikulmp.adapters.ProductImagesRvAdapter
import com.webkul.mobikulmp.fragments.EditAddProductImageFragment
import com.webkul.mobikulmp.fragments.ProductCategorySelectFragment
import com.webkul.mobikulmp.fragments.SellerSelectProductsFragment
import com.webkul.mobikulmp.models.seller.*
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.fragments
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 16/6/19
 */
class SellerAddProductActivityHandler(private val mContext: SellerAddProductActivity) {
    private lateinit var mFileUri: Uri

    fun onClickOpenCalender(v: View) {
        val dobCalendar = Calendar.getInstance()
        val date = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            dobCalendar.set(Calendar.YEAR, year)
            dobCalendar.set(Calendar.MONTH, month)
            dobCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val dob = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            (v as EditText).setText(dob.format(dobCalendar.time))

            if (v.id == R.id.special_price_from_et) {
                mContext.mContentViewBinding.specialPriceToEt.setText("")
            }
        }

        val dpd = DatePickerDialog(mContext, R.style.AlertDialogTheme, date, dobCalendar.get(Calendar.YEAR), dobCalendar.get(Calendar.MONTH), dobCalendar.get(Calendar.DAY_OF_MONTH))
        dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, mContext.getString(R.string.ok), dpd)
        dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, mContext.getString(R.string.cancel), dpd)

        if (v.id == R.id.special_price_from_et) {
            mContext.mContentViewBinding.specialPriceToEt.isEnabled = true
            mContext.mContentViewBinding.specialPriceToEt.setText("")
        } else if (v.id == R.id.special_price_to_et) {
            var fromDate: Long = 0
            try {
                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val dateFrom = formatter.parse(mContext.mContentViewBinding.specialPriceFromEt.text.toString()) as Date
                fromDate = dateFrom.time
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            dpd.datePicker.minDate = fromDate
        }
        dpd.show()
    }


    fun onClickAddImage() {
        mContext.mUploadType = null
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

    fun onClickDeleteImage(mediaGallery: MediaGallery) {
        val adapter = mContext.mContentViewBinding.productImageRv.adapter as ProductImagesRvAdapter
        mContext.mSellerAddProductResponseData!!.productData!!.mediaGallery[mediaGallery.adapterPosition].removed = 1
//        adapter.notifyDataSetChanged()
        adapter.removeImageItem(mediaGallery)
    }


    fun onClickEditProductImage(v: View, viewNumber: Int) {
        val fragmentManager = mContext.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val editAddProductImageFragment = EditAddProductImageFragment.newInstance(mContext.mSellerAddProductResponseData!!.productData!!.mediaGallery[viewNumber - 1], mContext.mSellerAddProductResponseData!!.imageRole as ArrayList<ImageRole>?)
        editAddProductImageFragment.setOnDetachInterface(mContext)

        fragmentTransaction.add(android.R.id.content, editAddProductImageFragment, EditAddProductImageFragment::class.java.simpleName)
        fragmentTransaction.addToBackStack(EditAddProductImageFragment::class.java.simpleName)
        fragmentTransaction.commit()
        mContext.supportActionBar?.title = mContext.getString(R.string.edit_image_info)

    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
            CropImage.activity(CropImage.getPickImageResultUri(mContext, data))
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(mContext)
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            mFileUri = CropImage.getActivityResult(data).uri
            uploadPic(CropImage.getActivityResult(data).uri)
        }
    }

    private fun uploadPic(data: Uri) {
        try {
            mContext.mContentViewBinding.loading = true
            val bitmap = BitmapFactory.decodeFile(data.path)
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos)
            val fileBody = RequestBody.create(MediaType.parse("image/*"), bos.toByteArray())
            val multipartFileBody = MultipartBody.Part.createFormData("image", File(data.path).name, fileBody)
            val width = RequestBody.create(MediaType.parse("text/plain"), Utils.screenWidth.toString())
            val mFactor = RequestBody.create(MediaType.parse("text/plain"), mContext.resources.displayMetrics.density.toString())

            MpApiConnection.uploadImage(mContext, multipartFileBody, width, mFactor)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : ApiCustomCallback<UploadPicResponseData>(mContext, true) {
                        override fun onNext(response: UploadPicResponseData) {
                            super.onNext(response)
                            mContext.mContentViewBinding.loading = false

                            ToastHelper.showToast(mContext, response.message, Toast.LENGTH_LONG)
                            if (response.success) {
                                val mediaGallery = MediaGallery()
                                mediaGallery.mediaType = response.type
                                mediaGallery.file = response.file
                                mediaGallery.url = response.url
                                val randomId = Utils.generateRandomId()
                                mediaGallery.id = randomId
                                if (mContext.mSellerAddProductResponseData!!.productData!!.mediaGallery.isEmpty()) {
                                    for (roleIndex in 0 until mContext.mSellerAddProductResponseData!!.imageRole!!.size) {
                                        mContext.mSellerAddProductResponseData!!.imageRole!![roleIndex].id = randomId
                                    }
                                    mediaGallery.baseImagePosition = 1
                                }
                                mediaGallery.position = mContext.mSellerAddProductResponseData!!.productData!!.mediaGallery.size + 1
                                mContext.mSellerAddProductResponseData!!.productData!!.mediaGallery.add(mediaGallery)
                                mContext.setupProductImages()
                            }

                        }

                        override fun onError(e: Throwable) {
                            super.onError(e)
                            onErrorResponse(e)
                        }

                    })


        } catch (e: Exception) {
            mContext.mContentViewBinding.loading = false
            ToastHelper.showToast(mContext, mContext.getString(R.string.error_please_try_again), Toast.LENGTH_SHORT)
            e.printStackTrace()
        }

    }


    fun onClickSelectFromProductCollection(collectionType: String) {
        Utils.hideKeyboard(mContext)
        val supportFragmentManager = mContext.supportFragmentManager
        val sellerSelectProductsDialogFragment = SellerSelectProductsFragment.newInstance(collectionType)
        sellerSelectProductsDialogFragment.setOnDetachInterface(mContext)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        transaction.add(R.id.main_container, sellerSelectProductsDialogFragment, SellerSelectProductsFragment::class.java.simpleName)
        transaction.addToBackStack(SellerSelectProductsFragment::class.java.simpleName)
        transaction.commit()
    }

    fun onClickAddLinkBtn() {
        if (mContext.mSellerAddProductResponseData?.productData?.linkData == null) {
            mContext.mSellerAddProductResponseData!!.productData!!.linkData = ArrayList()
        }
        mContext.mSellerAddProductResponseData!!.productData!!.linkData!!.add(LinkDatum())
        mContext.mContentViewBinding.linksRv.adapter?.notifyDataSetChanged()
    }


    fun onClickAddSampleBtn() {
        if (mContext.mSellerAddProductResponseData?.productData?.sampleData == null) {
            mContext.mSellerAddProductResponseData?.productData?.sampleData = ArrayList()
        }
        mContext.mSellerAddProductResponseData?.productData?.sampleData!!.add(SampleDatum())
        mContext.mContentViewBinding.samplesRv.adapter?.notifyDataSetChanged()
    }

    fun onClickSaveBtn(addProductData: AddProductData?) {
        if (addProductData!!.isFormValidated(mContext)) {
            addProductData.status = if (mContext.mContentViewBinding.statusRadioBtnYes.isChecked) 1 else 2

            Utils.hideKeyboard(mContext)
            mContext.mContentViewBinding.loading = true
            MpApiConnection.saveProduct(mContext, mContext.mProductId, addProductData, mContext.mSellerAddProductResponseData!!.imagesData)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : ApiCustomCallback<SaveProductResponseData>(mContext, true) {
                        override fun onNext(t: SaveProductResponseData) {
                            super.onNext(t)
                            mContext.mContentViewBinding.loading = false
                            if (t.success) {
                                if (mContext.intent.hasExtra(BUNDLE_KEY_PRODUCT_ID)) {
                                    mContext.setResult(RESULT_OK, Intent())
                                }
                                ToastHelper.showToast(mContext, t.message, Toast.LENGTH_LONG)
                                mContext.mProductId = t.productId
                                mContext.mContentViewBinding.handler = null
                                mContext.supportActionBar?.title = mContext.getString(R.string.edit_product)
                                mContext.callApi()


                            } else {
                                onFailureResponse(t)
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
                mContext.getString(com.webkul.mobikul.R.string.error),
                (response as SaveProductResponseData).message,
                false,
                mContext.getString(com.webkul.mobikul.R.string.ok),
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
                    mContext.getString(com.webkul.mobikul.R.string.try_again),
                    DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        mContext.mContentViewBinding.loading = true
                        onClickSaveBtn(mContext.mContentViewBinding.data!!.productData)
                    }
                    , mContext.getString(R.string.close)
                    , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            })
        }
    }


    fun onClickSelectCategory() {
        val productCategorySelectFragment = ProductCategorySelectFragment.newInstance(mContext.mCategoryList)
        val transaction = mContext.supportFragmentManager.beginTransaction()
        productCategorySelectFragment.setOnDetachInterface(mContext)
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        transaction.add(R.id.main_container, productCategorySelectFragment, ProductCategorySelectFragment::class.java.simpleName)
        transaction.addToBackStack(ProductCategorySelectFragment::class.java.simpleName)
        transaction.commit()
        mContext.supportActionBar?.title = mContext.getString(R.string.select_category)

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

    fun onClickProductCategoryBtn() {
        if (mContext.mContentViewBinding.productCategoryInformation.visibility == View.VISIBLE) {
            mContext.mContentViewBinding.productCategoryInformation.visibility = View.GONE
            mContext.mContentViewBinding.productCategoryHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mContext.mContentViewBinding.productCategoryInformation.visibility = View.VISIBLE
            mContext.mContentViewBinding.productCategoryHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mContext.mContentViewBinding.scrollView.top + mContext.mContentViewBinding.productCategoryHeading.top
                mContext.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }

    fun onClickProductPurchaseLimitBtn() {
        if (mContext.mContentViewBinding.productPurchaseLimitInformation.visibility == View.VISIBLE) {
            mContext.mContentViewBinding.productPurchaseLimitInformation.visibility = View.GONE
            mContext.mContentViewBinding.productPurchaseLimitHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mContext.mContentViewBinding.productPurchaseLimitInformation.visibility = View.VISIBLE
            mContext.mContentViewBinding.productPurchaseLimitHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mContext.mContentViewBinding.scrollView.top + mContext.mContentViewBinding.productPurchaseLimitHeading.top
                mContext.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }

    fun onClickStockBtn() {
        if (mContext.mContentViewBinding.stockInformation.visibility == View.VISIBLE) {
            mContext.mContentViewBinding.stockInformation.visibility = View.GONE
            mContext.mContentViewBinding.stockHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mContext.mContentViewBinding.stockInformation.visibility = View.VISIBLE
            mContext.mContentViewBinding.stockHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mContext.mContentViewBinding.scrollView.top + mContext.mContentViewBinding.stockHeading.top
                mContext.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }

    fun onClickWeightBtn() {
        if (mContext.mContentViewBinding.weightInformation.visibility == View.VISIBLE) {
            mContext.mContentViewBinding.weightInformation.visibility = View.GONE
            mContext.mContentViewBinding.weightHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mContext.mContentViewBinding.weightInformation.visibility = View.VISIBLE
            mContext.mContentViewBinding.weightHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mContext.mContentViewBinding.scrollView.top + mContext.mContentViewBinding.weightHeading.top
                mContext.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }

    fun onClickImagesBtn() {
        if (mContext.mContentViewBinding.imagesInformation.visibility == View.VISIBLE) {
            mContext.mContentViewBinding.imagesInformation.visibility = View.GONE
            mContext.mContentViewBinding.imagesHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mContext.mContentViewBinding.imagesInformation.visibility = View.VISIBLE
            mContext.mContentViewBinding.imagesHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mContext.mContentViewBinding.scrollView.top + mContext.mContentViewBinding.imagesHeading.top
                mContext.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }

    fun onClickDownloadableBtn() {
        if (mContext.mContentViewBinding.downloadableInformation.visibility == View.VISIBLE) {
            mContext.mContentViewBinding.downloadableInformation.visibility = View.GONE
            mContext.mContentViewBinding.downloadableHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mContext.mContentViewBinding.downloadableInformation.visibility = View.VISIBLE
            mContext.mContentViewBinding.downloadableHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mContext.mContentViewBinding.scrollView.top + mContext.mContentViewBinding.downloadableHeading.top
                mContext.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }

    fun onSeoBtn() {
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

    fun onClickRelatedProductsBtn() {
        if (mContext.mContentViewBinding.relatedProductsInformation.visibility == View.VISIBLE) {
            mContext.mContentViewBinding.relatedProductsInformation.visibility = View.GONE
            mContext.mContentViewBinding.relatedProductsHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mContext.mContentViewBinding.relatedProductsInformation.visibility = View.VISIBLE
            mContext.mContentViewBinding.relatedProductsHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mContext.mContentViewBinding.scrollView.top + mContext.mContentViewBinding.relatedProductsHeading.top
                mContext.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }

    fun onClickUpSellProductsBtn() {
        if (mContext.mContentViewBinding.upsellProductsInformation.visibility == View.VISIBLE) {
            mContext.mContentViewBinding.upsellProductsInformation.visibility = View.GONE
            mContext.mContentViewBinding.upsellProductsHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mContext.mContentViewBinding.upsellProductsInformation.visibility = View.VISIBLE
            mContext.mContentViewBinding.upsellProductsHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mContext.mContentViewBinding.scrollView.top + mContext.mContentViewBinding.upsellProductsHeading.top
                mContext.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }

    fun onClickCrossSellProductsBtn() {
        if (mContext.mContentViewBinding.crossSellProductsInformation.visibility == View.VISIBLE) {
            mContext.mContentViewBinding.crossSellProductsInformation.visibility = View.GONE
            mContext.mContentViewBinding.crossSellProductsHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_down_arrow_grey_wrapper), null)
        } else {
            mContext.mContentViewBinding.crossSellProductsInformation.visibility = View.VISIBLE
            mContext.mContentViewBinding.crossSellProductsHeading.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_up_arrow_grey_wrapper), null)
            Handler().postDelayed({
                val scrollTo = mContext.mContentViewBinding.scrollView.top + mContext.mContentViewBinding.crossSellProductsHeading.top
                mContext.mContentViewBinding.scrollView.smoothScrollTo(0, scrollTo)
            }, 200)
        }
    }
}