package com.webkul.mobikulmp.activities

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.theartofdev.edmodo.cropper.CropImage
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.helpers.AlertDialogHelper
import com.webkul.mobikul.helpers.ConstantsHelper
import com.webkul.mobikul.helpers.NetworkHelper
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.ActivitySellerProfileEditBinding
import com.webkul.mobikulmp.handlers.SellerProfileEditActivityHandler
import com.webkul.mobikulmp.helpers.RichTextEditorHelper
import com.webkul.mobikulmp.models.seller.SellerProfileFormResponseData
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.util.*

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
class SellerProfileEditActivity : BaseActivity() {

    lateinit var mContentViewBinding: ActivitySellerProfileEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_seller_profile_edit)
        initSupportActionBar()
        callApi()
    }

    private fun initSupportActionBar() {
        supportActionBar?.title = getString(R.string.seller_profile)
    }

    private fun callApi() {
        mContentViewBinding.loading = true
        MpApiConnection.getSellerProfileFormData(this@SellerProfileEditActivity)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<SellerProfileFormResponseData>(this@SellerProfileEditActivity, true) {
                    override fun onNext(t: SellerProfileFormResponseData) {
                        super.onNext(t)
                        mContentViewBinding.loading = false
                        if (t.success) {
                            startInitialization(t)
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

    override fun onFailureResponse(response: Any) {
        AlertDialogHelper.showNewCustomDialog(
                this,
                getString(com.webkul.mobikul.R.string.error),
                (response as SellerProfileFormResponseData).message,
                false,
                getString(com.webkul.mobikul.R.string.ok),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                }
                , ""
                , null)
    }


    private fun onErrorResponse(error: Throwable) {
        mContentViewBinding.loading = false
        if ((!NetworkHelper.isNetworkAvailable(this) || (error is HttpException && error.code() == 304))) {

        } else {
            AlertDialogHelper.showNewCustomDialog(
                    this,
                    getString(com.webkul.mobikul.R.string.oops),
                    NetworkHelper.getErrorMessage(this, error),
                    false,
                    getString(com.webkul.mobikul.R.string.try_again),
                    DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        mContentViewBinding.loading = true
                        callApi()
                    }
                    , getString(com.webkul.mobikul.R.string.close)
                    , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                finish()
            })
        }
    }

    private fun startInitialization(mSellerEditProfileResponseData: SellerProfileFormResponseData) {
        mContentViewBinding.data = mSellerEditProfileResponseData
        mContentViewBinding.handler = SellerProfileEditActivityHandler(this)
        setupCountrySpinner(mSellerEditProfileResponseData)
        mContentViewBinding.contactNoEt.setOnClickListener({

        })
        /*Setting up the Rich TextView*/
        val companyDescriptionEditor = RichTextEditorHelper()
        companyDescriptionEditor.setOnChangeTextEditor(object : RichTextEditorHelper.OnChangeTextEditor {
            override fun onChangeTextEditor(query: String) {
//                Log.d("Tag", "RichTextEditorHelper====>" + query)
                mContentViewBinding.data?.companyDescription = query
            }

        })
        companyDescriptionEditor.setUpRichTextView(this, mContentViewBinding.richLayout.editor, mContentViewBinding.data?.companyDescription
                ?: "")
    }

    private fun setupCountrySpinner(mSellerEditProfileResponseData: SellerProfileFormResponseData) {
        val countryNames = ArrayList<String>()
        for (noOfCountries in 0 until (mSellerEditProfileResponseData.countryList?.size ?: 0)) {
            if (mSellerEditProfileResponseData.countryList?.get(noOfCountries)?.label!!.trim().isEmpty()) {
                countryNames.add(getString(R.string.select_country))
            } else {
                mSellerEditProfileResponseData.countryList?.get(noOfCountries)?.label?.let { countryNames.add(it) }
            }
        }
        mContentViewBinding.countrySp.adapter = ArrayAdapter(this@SellerProfileEditActivity, R.layout.custom_spinner_item, countryNames)

        mContentViewBinding.countrySp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                mSellerEditProfileResponseData.country = mSellerEditProfileResponseData.countryList?.get(position)?.value
                mContentViewBinding.flagImageUrl = mSellerEditProfileResponseData.flagImageUrl + "/" + mSellerEditProfileResponseData.country + ".png"
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        mContentViewBinding.countrySp.setSelection(mSellerEditProfileResponseData.selectedCountryPositionFromAddressData)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
                mContentViewBinding.handler?.cropImage(data)
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                mContentViewBinding.handler?.updateData(data)
            }
        }
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

}