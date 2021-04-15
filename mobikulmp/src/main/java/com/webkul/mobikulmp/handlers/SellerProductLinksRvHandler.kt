package com.webkul.mobikulmp.handlers


import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.theartofdev.edmodo.cropper.CropImage
import com.webkul.mobikul.helpers.AlertDialogHelper
import com.webkul.mobikul.helpers.ConstantsHelper
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.activities.SellerAddProductActivity
import com.webkul.mobikulmp.adapters.SellerProductLinksRvAdapter

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
class SellerProductLinksRvHandler(private val mContext: SellerAddProductActivity) {

    fun onClickShowHide(position: Int) {
        (mContext.mContentViewBinding.linksRv.adapter as SellerProductLinksRvAdapter).getItemsList()[position].isExpended = (!(mContext.mContentViewBinding.linksRv.adapter as SellerProductLinksRvAdapter).getItemsList()[position].isExpended)
    }

    fun onClickDeleteItem(position: Int) {
        AlertDialogHelper.showNewCustomDialog(
                mContext,
                mContext.getString(R.string.warning_are_you_sure),
                mContext.getString(R.string.delete_warning),
                false,
                mContext.getString(R.string.yes),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    (mContext.mContentViewBinding.linksRv.adapter as SellerProductLinksRvAdapter).getItemsList().removeAt(position)
                    mContext.mContentViewBinding.linksRv.adapter?.notifyDataSetChanged()
                    dialogInterface.dismiss()
                }
                , mContext.getString(R.string.close)
                , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        })
    }


    fun onClickAddFile(position: Int, type: String) {
        mContext.mRvItemPosition = position
        mContext.mUploadType = type
        intentToPickFile()
    }

    private fun intentToPickFile() {
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
}