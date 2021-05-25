package com.webkul.mobikul.handlers

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageActivity
import com.theartofdev.edmodo.cropper.CropImageView
import com.webkul.mobikul.activities.ProductNotFound
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*


class ProductNotFoundHandler(private val mContext: ProductNotFound) {

    fun onClickImageEdit() {

//        ImagePicker.with(mContext)
//                .crop()	    			//Crop image(Optional), Check Customization for more option
//                .compress(1024)			//Final image size will be less than 1 MB(Optional)
//                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
//                .start()

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//            CropImage.startPickImageActivity(mContext)
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(mContext)


        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                mContext.requestPermissions(permissions, ConstantsHelper.RC_PICK_IMAGE)
            }
        }

    }


//    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
//            CropImage.activity(CropImage.getPickImageResultUri(mContext, data))
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .start(mContext)
//        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            mFileUri = CropImage.getActivityResult(data).uri
//            mContext.mContentViewBinding.productImage.setImageURI(mFileUri)
//            mContext.mContentViewBinding.productImage.visibility = View.VISIBLE
////            uploadPic(CropImage.getActivityResult(data).uri)
//        }
//    }


    fun onClickSend() {
        try {
            mContext.mContentViewBinding.loading = true
            val bitmap = BitmapFactory.decodeFile(mContext.mFileUri.path)
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos)
            val fileBody = RequestBody.create(MediaType.parse("image/*"), bos.toByteArray())
            val multipartFileBody = MultipartBody.Part.createFormData("image", File(mContext.mFileUri.path).name, fileBody)
            val name = RequestBody.create(MediaType.parse("text/plain"), mContext.mContentViewBinding.name.text.toString().trim())
            val email = RequestBody.create(MediaType.parse("text/plain"), mContext.mContentViewBinding.email.text.toString().trim())
            val subject = RequestBody.create(MediaType.parse("text/plain"), "Product Not Found" /*mContext.mContentViewBinding.subject.text.toString().trim()*/)
            val message = RequestBody.create(MediaType.parse("text/plain"), mContext.mContentViewBinding.msg.text.toString().trim())

            ApiConnection.saveProductNotFoundData(mContext, multipartFileBody, name, email, subject, message)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : ApiCustomCallback<BaseModel>(mContext, true) {
                        override fun onNext(saveAddressResponseModel: BaseModel) {
                            super.onNext(saveAddressResponseModel)
                            mContext.mContentViewBinding.loading = false
                            if (saveAddressResponseModel.success) {
//                            onSuccessfulResponse(saveAddressResponseModel)
                            } else {
//                            onFailureResponse(saveAddressResponseModel)
                            }
                        }

                        override fun onError(e: Throwable) {
                            super.onError(e)
                            mContext.mContentViewBinding.loading = false
//                        onErrorResponse(e, addressFormResponseModel)
                        }
                    })
        } catch (e: Exception) {
            mContext.mContentViewBinding.loading = false
            ToastHelper.showToast(mContext, "Try Again", Toast.LENGTH_SHORT)
            e.printStackTrace()
        }
    }


}