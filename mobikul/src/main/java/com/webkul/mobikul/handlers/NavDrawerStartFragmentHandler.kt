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
import com.webkul.mobikul.activities.*
import com.webkul.mobikul.fragments.NavDrawerStartFragment
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.models.user.AddressDetailsData
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
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

class NavDrawerStartFragmentHandler(private val mFragmentContext: NavDrawerStartFragment) {

    fun onClickBannerImage() {
        if (AppSharedPref.isLoggedIn(mFragmentContext.context!!)) {
            mFragmentContext.activity?.startActivityForResult(Intent(mFragmentContext.context, DashboardActivity::class.java), ConstantsHelper.RC_LOGIN)
//            (mFragmentContext.context as HomeActivity).mContentViewBinding.drawerLayout.closeDrawers()
        } else {
            mFragmentContext.activity?.startActivityForResult((mFragmentContext.activity!!.application as MobikulApplication).getLoginAndSignUpActivity(mFragmentContext.context!!), ConstantsHelper.RC_LOGIN)
//            (mFragmentContext.context as HomeActivity).mContentViewBinding.drawerLayout.closeDrawers()
        }
    }

    fun onClickProfileImage() {
        if (AppSharedPref.isLoggedIn(mFragmentContext.context!!)) {
            mFragmentContext.mIsUpdatingProfilePic = true
            selectImage()
        }
    }

    fun selectImage() {
        if (ContextCompat.checkSelfPermission(mFragmentContext.context!!, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mFragmentContext.context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mFragmentContext.context!!, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            CropImage.startPickImageActivity(mFragmentContext.context!!, mFragmentContext)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                mFragmentContext.requestPermissions(permissions, ConstantsHelper.RC_PICK_IMAGE)
            }
        }
    }

    fun onClickCompareProducts() {
        mFragmentContext.context?.startActivity(Intent(mFragmentContext.context, CompareProductsActivity::class.java))
//        (mFragmentContext.context as HomeActivity).mContentViewBinding.drawerLayout.closeDrawers()
    }

    fun onClickContactUs() {
        mFragmentContext.context?.startActivity(Intent(mFragmentContext.context, ContactUsActivity::class.java))
//        (mFragmentContext.context as HomeActivity).mContentViewBinding.drawerLayout.closeDrawers()
    }

    fun onClickOrdersAndReturns() {
        mFragmentContext.context?.startActivity(Intent(mFragmentContext.context, OrdersAndReturnsActivity::class.java))
//        (mFragmentContext.context as HomeActivity).mContentViewBinding.drawerLayout.closeDrawers()
    }

    fun onClickLogout() {
//        (mFragmentContext.context as HomeActivity).mContentViewBinding.drawerLayout.closeDrawers()
        val context=mFragmentContext.activity

        AlertDialogHelper.showNewCustomDialog(
                mFragmentContext.context as BaseActivity,
                mFragmentContext.getString(R.string.log_out),
                mFragmentContext.getString(R.string.logout_warning),
                false,
                mFragmentContext.getString(R.string.log_out),
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
                    AppSharedPref.setCustomerCachedNewAddress(context, AddressDetailsData())
                    ToastHelper.showToast(context as BaseActivity, context.getString(R.string.logout_message))
                    val intent = Intent(context, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                }
                , mFragmentContext.getString(R.string.cancel)
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