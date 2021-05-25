package com.webkul.mobikul.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.databinding.DataBindingUtil
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.ActivityProductNotFoundBinding
import com.webkul.mobikul.handlers.ProductNotFoundHandler

class ProductNotFound : BaseActivity() {

    lateinit var mContentViewBinding: ActivityProductNotFoundBinding
    lateinit var mFileUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_product_not_found)
        startInitialization()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }


    private fun startInitialization() {
        initSupportActionBar()
//        callApi()
//        initSwipeRefresh()

    }

    private fun initSupportActionBar() {
        supportActionBar?.title = getString(R.string.product_not_found_title)
        mContentViewBinding.handler = ProductNotFoundHandler(this)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                CropImage.activity(CropImage.getPickImageResultUri(this, data))
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this)
            }


        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                mFileUri = CropImage.getActivityResult(data).uri
                mContentViewBinding.productImage.setImageURI(mFileUri)
                mContentViewBinding.productImage.visibility = View.VISIBLE
            }
//            uploadPic(CropImage.getActivityResult(data).uri)
        }
    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK) {
//            //Image Uri will not be null for RESULT_OK
//            val fileUri = data?.data
//            mContentViewBinding.productImage.setImageURI(fileUri)
//            mContentViewBinding.productImage.visibility = View.VISIBLE
////            imgProfile.setImageURI(fileUri)
//
//            //You can get File object from intent
//            val file: File = ImagePicker.getFile(data)!!
//
//            //You can also get File Path from intent
//            val filePath:String = ImagePicker.getFilePath(data)!!
//        } else if (resultCode == ImagePicker.RESULT_ERROR) {
//            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
//        }
//    }


}