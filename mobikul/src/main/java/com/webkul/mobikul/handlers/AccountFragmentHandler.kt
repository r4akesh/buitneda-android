/*
 * Webkul Software.
 *
 * Kotlin
 *
 * @author Webkul <support@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2019 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

package com.webkul.mobikul.handlers

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.theartofdev.edmodo.cropper.CropImage
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.AccountInfoActivity
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.activities.DashboardActivity
import com.webkul.mobikul.activities.HomeActivity
import com.webkul.mobikul.fragments.AccountDetailsFragment
import com.webkul.mobikul.fragments.FullImageDialogFragment
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.models.user.AddressDetailsData
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.material_search_view.*


class AccountFragmentHandler(private val mContext: AccountDetailsFragment) {

    fun onClickBannerImage() {
       /*if (AppSharedPref.isLoggedIn(mContext.context!!)) {
            mContext.mIsUpdatingProfilePic = false
            selectImage()
        } else {
            mContext.startActivityForResult((mContext.application as MobikulApplication).getLoginAndSignUpActivity(mContext), ConstantsHelper.RC_LOGIN)
        }*/

        if (AppSharedPref.isLoggedIn(mContext.context!!)) {
            mContext.mIsUpdatingProfilePic = false
            selectImage()
        } else {
            mContext.startActivityForResult((mContext.context as MobikulApplication).getLoginAndSignUpActivity(mContext.context!!), ConstantsHelper.RC_LOGIN)
        }
    }

    fun onClickProfileImage() {
        if (AppSharedPref.getCustomerImageUrl(mContext.context!!).isNotBlank())
            FullImageDialogFragment.newInstance(AppSharedPref.getCustomerImageUrl(mContext.context!!)).show((mContext as BaseActivity).supportFragmentManager, FullImageDialogFragment::class.java.simpleName)
    }

    fun onClickProfileImageEdit() {
        if (AppSharedPref.isLoggedIn(mContext.context!!)) {
            mContext.mIsUpdatingProfilePic = true
            selectImage()
        }
    }

    fun selectImage() {
        if (ContextCompat.checkSelfPermission(mContext.context!!, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mContext.context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mContext.context!!, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            CropImage.startPickImageActivity(mContext.requireActivity(),mContext)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                mContext.requestPermissions(permissions, ConstantsHelper.RC_PICK_IMAGE)
            }
        }
    }

    fun onClickEditInfo() {
        mContext.startActivityForResult(Intent(mContext.context, AccountInfoActivity::class.java), ConstantsHelper.RC_LOGIN)
    }

    fun onClickLogout() {
//        (mContext.context as HomeActivity).mContentViewBinding.drawerLayout.closeDrawers()
        val context=mContext.activity

        AlertDialogHelper.showNewCustomDialog(
                mContext.context as BaseActivity,
                mContext.getString(R.string.log_out),
                mContext.getString(R.string.logout_warning),
                false,
                mContext.getString(R.string.log_out),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                    deleteTokenFromCloud(context)

                    ApiConnection.logout(context!!)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(object : ApiCustomCallback<BaseModel>(context!!, true) {
                                override fun onNext(logoutResponse: BaseModel) {
                                }

                                override fun onError(e: Throwable) {
                                }
                            })
                    val customerSharedPrefEditor = AppSharedPref.getSharedPreferenceEditor(context, AppSharedPref.CUSTOMER_PREF)
                    customerSharedPrefEditor.clear()
                    customerSharedPrefEditor.apply()
                    customerSharedPrefEditor.commit()
                    AppPreference.logout(context)
                    AppSharedPref.setCustomerCachedNewAddress(context, AddressDetailsData())
                    ToastHelper.showToast(context as BaseActivity, context.getString(R.string.logout_message))
                    val intent = Intent(context, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                }
                , mContext.getString(R.string.cancel)
                , DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.dismiss()
        })
    }
    private fun deleteTokenFromCloud(context: FragmentActivity?) {
        ApiConnection.deleteTokenFromCloud(context!!, "seller")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<BaseModel>(context, true) {
                    override fun onNext(baseModel: BaseModel) {
                    }

                    override fun onError(e: Throwable) {
                    }
                })
        ApiConnection.deleteTokenFromCloud(context, "customer")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<BaseModel>(context, true) {
                    override fun onNext(baseModel: BaseModel) {
                    }

                    override fun onError(e: Throwable) {
                    }
                })

    }

}