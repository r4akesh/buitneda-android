package com.webkul.mobikulmp.activities


import android.content.ContentResolver
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MotionEvent
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_PRODUCT_ID
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.adapters.ProductImagesRvAdapter
import com.webkul.mobikulmp.adapters.SelectedCategoriesRvAdapter
import com.webkul.mobikulmp.adapters.SellerProductLinksRvAdapter
import com.webkul.mobikulmp.adapters.SellerProductSamplesRvAdapter
import com.webkul.mobikulmp.databinding.ActivitySellerAddProductBinding
import com.webkul.mobikulmp.fragments.EditAddProductImageFragment
import com.webkul.mobikulmp.fragments.ProductCategorySelectFragment
import com.webkul.mobikulmp.fragments.SellerSelectProductsFragment
import com.webkul.mobikulmp.handlers.SellerAddProductActivityHandler
import com.webkul.mobikulmp.helpers.RichTextEditorHelper
import com.webkul.mobikulmp.models.seller.*
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.File
import java.util.*

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

class SellerAddProductActivity : BaseActivity(), ProductCategorySelectFragment.OnDetachInterface, EditAddProductImageFragment.OnDetachInterface, SellerSelectProductsFragment.OnDetachInterface {

    lateinit var mContentViewBinding: ActivitySellerAddProductBinding
    var mSelectedCategoriesAdapter: SelectedCategoriesRvAdapter? = null
    var mProductId: String? = null
    var mRvItemPosition: Int = 0
    var mUploadType: String? = null
    var mSellerAddProductResponseData: SellerAddProductResponseData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_seller_add_product)
        initSupportActionBar()
        callApi()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    private fun initSupportActionBar() {
        if (intent.hasExtra(BUNDLE_KEY_PRODUCT_ID)) {
            mProductId = intent.getStringExtra(BUNDLE_KEY_PRODUCT_ID)
            mContentViewBinding.productId = (intent.getStringExtra(BUNDLE_KEY_PRODUCT_ID))?.toInt()
            supportActionBar?.title = getString(R.string.edit_product)
        } else {
            supportActionBar?.title = getString(R.string.add_product)
        }
    }

    fun callApi() {
        mContentViewBinding.loading = true
        MpApiConnection.getProductFormData(this@SellerAddProductActivity, mProductId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<SellerAddProductResponseData>(this@SellerAddProductActivity, true) {
                    override fun onNext(t: SellerAddProductResponseData) {
                        super.onNext(t)
                        mContentViewBinding.loading = false
                        if (t.success) {
                            if (t.allowedTypes != null && t.allowedTypes!!.isNotEmpty()) {
                                mSellerAddProductResponseData = t
                                mContentViewBinding.data = mSellerAddProductResponseData
                                mContentViewBinding.handler = SellerAddProductActivityHandler(this@SellerAddProductActivity)
                                startInitialization()
                            } else {
                                ToastHelper.showToast(this@SellerAddProductActivity, getString(R.string.product_type_not_found))
                            }
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

        super.onFailureResponse(response)
        when ((response as BaseModel).otherError) {
            ConstantsHelper.CUSTOMER_NOT_EXIST -> {
                // Do nothing as it will be handled from the super.
            }
            else -> {
                AlertDialogHelper.showNewCustomDialog(
                        this,
                        getString(com.webkul.mobikul.R.string.error),
                        response.message,
                        false,
                        getString(com.webkul.mobikul.R.string.ok),
                        DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                            dialogInterface.dismiss()
                        }
                        , ""
                        , null)
            }
        }

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
            })
        }
    }

    private fun onSkuErrorResponse(error: Throwable) {
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
                        setupSkuChangeListener()
                    }
                    , getString(com.webkul.mobikul.R.string.close)
                    , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            })
        }
    }

    private fun startInitialization() {
        setupSelectedProductCategories()
       // setupAttributeSetSpinner()
       // setupProductTypeSpinner()
        setupSkuChangeListener()
       // setupStockAvailabilitySpinner()
       // setupVisibilitySpinner()
        //setupTaxClassSpinner()
        updateBaseImage()
        setupProductImages()
       // setupDownloadableData()
       // setupFeatureSp()
        setupRichTextBox()
        if (mProductId == null) {
            mSellerAddProductResponseData?.productData?.setProductHasWeight(true)
        }
    }

    private fun setupRichTextBox() {
        /*Setting up the Rich TextView*/
        val productDescriptionEditor = RichTextEditorHelper()
        productDescriptionEditor.setOnChangeTextEditor(object : RichTextEditorHelper.OnChangeTextEditor {
            override fun onChangeTextEditor(query: String) {
                mContentViewBinding.data?.productData?.description = query
            }
        })
        mContentViewBinding.richLayoutDiscription.editor.id=View.generateViewId()
        productDescriptionEditor.setUpRichTextView(this, mContentViewBinding.richLayoutDiscription.editor, mContentViewBinding.data?.productData?.description
                ?: "")


        val productShortDescriptionEditor = RichTextEditorHelper()
        productShortDescriptionEditor.setOnChangeTextEditor(object : RichTextEditorHelper.OnChangeTextEditor {
            override fun onChangeTextEditor(query: String) {
                mContentViewBinding.data?.productData?.shortDescription = query
            }
        })

        mContentViewBinding.richLayoutShortDescription.editor.id=View.generateViewId()
        productShortDescriptionEditor.setUpRichTextView(this, mContentViewBinding.richLayoutShortDescription.editor, mContentViewBinding.data?.productData?.shortDescription
                ?: "")

    }

   /* private fun setupFeatureSp() {
        mContentViewBinding.mobikulFeatureSp.adapter = ArrayAdapter(this, R.layout.custom_spinner_item, resources.getStringArray(R.array.mobikul_feature))
    }*/

   /* private fun setupAttributeSetSpinner() {
        val attributeNames = ArrayList<String>()
        for (noOfCountries in 0 until mSellerAddProductResponseData!!.allowedAttributes!!.size) {
            attributeNames.add(mSellerAddProductResponseData!!.allowedAttributes!![noOfCountries].label!!)
        }
        mContentViewBinding.attributeSetSp.adapter = ArrayAdapter(this, R.layout.custom_spinner_item, attributeNames)
        mContentViewBinding.attributeSetSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                mSellerAddProductResponseData!!.productData!!.attributeSetId = mSellerAddProductResponseData!!.allowedAttributes!![position].value
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        mContentViewBinding.attributeSetSp.setSelection(mSellerAddProductResponseData!!.selectedAttributeSet)

        mContentViewBinding.attributeSetSp.setOnTouchListener { v, event ->
            if (event?.action == MotionEvent.ACTION_UP) {
                Utils.hideKeyboard(this@SellerAddProductActivity)
            }
            false
        }
    }*/

   /* private fun setupProductTypeSpinner() {
        val productTypeNames = ArrayList<String>()
        for (noOfType in 0 until mSellerAddProductResponseData!!.allowedTypes!!.size) {
            productTypeNames.add(mSellerAddProductResponseData!!.allowedTypes!![noOfType].label!!)
        }
        mContentViewBinding.productTypeSp.adapter = ArrayAdapter(this, R.layout.custom_spinner_item, productTypeNames)
        mContentViewBinding.productTypeSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                mSellerAddProductResponseData!!.productData!!.type = mSellerAddProductResponseData!!.allowedTypes!![position].value
                when (mSellerAddProductResponseData!!.productData!!.type) {
                    "simple" -> {
                        mSellerAddProductResponseData!!.productData!!.isDownloadableProduct = false
                        mContentViewBinding.weightSwitchBtn.isChecked = true
                    }
                    "downloadable" -> mSellerAddProductResponseData!!.productData!!.isDownloadableProduct = true
                    "virtual" -> {
                        mSellerAddProductResponseData!!.productData!!.isDownloadableProduct = false
                        mContentViewBinding.weightSwitchBtn.isChecked = false
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        mContentViewBinding.productTypeSp.setSelection(mSellerAddProductResponseData!!.selectedType)

        mContentViewBinding.productTypeSp.setOnTouchListener { v, event ->
            if (event?.action == MotionEvent.ACTION_UP) {
                Utils.hideKeyboard(this@SellerAddProductActivity)
            }
            false
        }
    }*/


    private fun setupSkuChangeListener() {
        mContentViewBinding.skuEt.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (!(v as TextInputEditText).text!!.toString().trim { it <= ' ' }.isEmpty()) {
                    mContentViewBinding.loading = true
                    MpApiConnection.checkSku(this@SellerAddProductActivity, v.text!!.toString())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(object : ApiCustomCallback<CheckSkuResponseData>(this@SellerAddProductActivity, true) {
                                override fun onNext(t: CheckSkuResponseData) {
                                    super.onNext(t)
                                    mContentViewBinding.loading = false
                                    if (t.success) {
                                        ToastHelper.showToast(this@SellerAddProductActivity, t.message, Toast.LENGTH_LONG)
                                        if (!t.isAvailable) {
                                            mContentViewBinding.skuEt.setText("")
                                        }
                                    } else {
                                        ToastHelper.showToast(this@SellerAddProductActivity, t.message)
                                    }
                                }

                                override fun onError(e: Throwable) {
                                    super.onError(e)
                                    onSkuErrorResponse(e)
                                }
                            })
                }
            }
        }
    }

   /* private fun setupStockAvailabilitySpinner() {
        val stockAvailabilityNames = ArrayList<String>()
        for (noOfStockItems in 0 until mSellerAddProductResponseData!!.inventoryAvailabilityOptions!!.size) {
            stockAvailabilityNames.add(mSellerAddProductResponseData!!.inventoryAvailabilityOptions!![noOfStockItems].label!!)
        }
        mContentViewBinding.stockAvailabilitySp.adapter = ArrayAdapter(this, R.layout.custom_spinner_item, stockAvailabilityNames)
        mContentViewBinding.stockAvailabilitySp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                mSellerAddProductResponseData!!.productData!!.isIsInStock = mSellerAddProductResponseData!!.inventoryAvailabilityOptions!![position].value.equals("1")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        mContentViewBinding.stockAvailabilitySp.setSelection(if (mProductId == null || mSellerAddProductResponseData!!.productData!!.isIsInStock) 0 else 1)


        mContentViewBinding.stockAvailabilitySp.setOnTouchListener { v, event ->
            if (event?.action == MotionEvent.ACTION_UP) {
                Utils.hideKeyboard(this@SellerAddProductActivity)
            }
            false
        }
    }*/

  /*  private fun setupVisibilitySpinner() {
        val visibilityOptions = ArrayList<String>()
        visibilityOptions.add(getString(R.string.please_select_visibility))
        for (noOfVisibilityItems in 0 until mSellerAddProductResponseData!!.visibilityOptions!!.size) {
            visibilityOptions.add(mSellerAddProductResponseData!!.visibilityOptions!![noOfVisibilityItems].label!!)
        }
        mContentViewBinding.visibilitySp.adapter = ArrayAdapter(this, R.layout.custom_spinner_item, visibilityOptions)
        mContentViewBinding.visibilitySp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (position == 0) {
                    mSellerAddProductResponseData!!.productData!!.visibility = ""
                } else {
                    mSellerAddProductResponseData!!.productData!!.visibility = mSellerAddProductResponseData!!.visibilityOptions!![position - 1].value
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        //mContentViewBinding.visibilitySp.setSelection(mSellerAddProductResponseData!!.selectedVisibility)

       *//* mContentViewBinding.visibilitySp.setOnTouchListener { v, event ->
            if (event?.action == MotionEvent.ACTION_UP) {
                Utils.hideKeyboard(this@SellerAddProductActivity)
            }
            false
        }*//*
    }*/

   /* private fun setupTaxClassSpinner() {
        val taxClassNames = ArrayList<String>()
        taxClassNames.add(getString(R.string.none))
        for (noOfTaxClasses in 0 until mSellerAddProductResponseData!!.taxOptions!!.size) {
            taxClassNames.add(mSellerAddProductResponseData!!.taxOptions!![noOfTaxClasses].label!!)
        }
        mContentViewBinding.taxClassSp.adapter = ArrayAdapter(this, R.layout.custom_spinner_item, taxClassNames)
        mContentViewBinding.taxClassSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (position == 0) {
                    mSellerAddProductResponseData!!.productData!!.taxClassId = "0"
                } else {
                    mSellerAddProductResponseData!!.productData!!.taxClassId = mSellerAddProductResponseData!!.taxOptions!![position - 1].value
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        mContentViewBinding.taxClassSp.setSelection(mSellerAddProductResponseData!!.selectedTaxClass)

        mContentViewBinding.taxClassSp.setOnTouchListener { v, event ->
            if (event?.action == MotionEvent.ACTION_UP) {
                Utils.hideKeyboard(this@SellerAddProductActivity)
            }
            false
        }
    }*/

   /* private fun setupDownloadableData() {
        if ((mSellerAddProductResponseData!!.productData?.linkData != null && mSellerAddProductResponseData!!.productData?.linkData!!.isNotEmpty()) || (mSellerAddProductResponseData!!.productData?.sampleData != null && mSellerAddProductResponseData!!.productData?.sampleData!!.isNotEmpty())) {
            mSellerAddProductResponseData!!.productData!!.isDownloadableProduct = true
        }
        if (mSellerAddProductResponseData!!.productData?.linkData == null) {
            mSellerAddProductResponseData!!.productData!!.linkData = ArrayList()
            mSellerAddProductResponseData!!.productData!!.linkData!!.add(LinkDatum())
        }
        //Set Downloadable product view
        mContentViewBinding.linksRv.adapter = SellerProductLinksRvAdapter(this, mSellerAddProductResponseData!!.productData!!.linkData!!)
        mContentViewBinding.linksRv.isNestedScrollingEnabled = false
//        ItemTouchHelper(mRvItemTouchCallback).attachToRecyclerView(mContentViewBinding.linksRv)
        if (mSellerAddProductResponseData!!.productData?.sampleData == null) {
            mSellerAddProductResponseData!!.productData?.sampleData = ArrayList()
            mSellerAddProductResponseData!!.productData?.sampleData!!.add(SampleDatum())
        }

        mContentViewBinding.samplesRv.adapter = SellerProductSamplesRvAdapter(this, mSellerAddProductResponseData!!.productData!!.sampleData!!)
        mContentViewBinding.samplesRv.isNestedScrollingEnabled = false
//        ItemTouchHelper(mRvItemTouchCallback).attachToRecyclerView(mContentViewBinding.samplesRv)
    }*/

    fun setupProductImages() {
        val adapter = mContentViewBinding.productImageRv.adapter
        if (adapter == null) {
            mContentViewBinding.productImageRv.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
            val mediaGallery: ArrayList<MediaGallery> = ArrayList()
            mediaGallery.addAll(mSellerAddProductResponseData!!.productData!!.mediaGallery)
            mContentViewBinding.productImageRv.adapter = ProductImagesRvAdapter(this, mediaGallery)
        } else {
            val adapter = mContentViewBinding.productImageRv.adapter as ProductImagesRvAdapter
            val mediaGallery: ArrayList<MediaGallery> = ArrayList()
            mediaGallery.addAll(mSellerAddProductResponseData!!.productData!!.mediaGallery)
            adapter.addImages(mediaGallery)
        }
    }

    private fun updateBaseImage() {
        var baseImageId: String? = ""
        for (roleIndex in 0 until mSellerAddProductResponseData!!.imageRole!!.size) {
            if (mSellerAddProductResponseData!!.imageRole!![roleIndex].value == "baseImage") {
                baseImageId = mSellerAddProductResponseData!!.imageRole!![roleIndex].id
                break
            }
        }
        if (baseImageId != null && mSellerAddProductResponseData!!.productData != null) {
            for (imageIndex in 0 until mSellerAddProductResponseData!!.productData!!.mediaGallery.size) {
                if (mSellerAddProductResponseData!!.productData!!.mediaGallery[imageIndex].id.equals(baseImageId)) {
                    mSellerAddProductResponseData!!.productData!!.mediaGallery[imageIndex].baseImagePosition = 1
                    break
                } else {
                    mSellerAddProductResponseData!!.productData!!.mediaGallery[imageIndex].baseImagePosition = 0

                }
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        var allPermissionsGranted = true
        for (eachGrantResult in grantResults) {
            if (eachGrantResult != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false
            }
        }
        if (allPermissionsGranted) {
            if (requestCode == ConstantsHelper.RC_PICK_IMAGE) {
                if (mUploadType == null) {
                    mContentViewBinding.handler!!.onClickAddImage()
                } else {
                    CropImage.startPickImageActivity(this)
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (mUploadType == null) {
                mContentViewBinding.handler!!.onActivityResult(requestCode, resultCode, data)
            } else {
                if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
                    CropImage.activity(CropImage.getPickImageResultUri(this, data))
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(this)
                } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    uploadDownloadableFile(CropImage.getActivityResult(data).uri)
                }
            }
        }
    }


    private fun uploadDownloadableFile(data: Uri) {
        try {
            mContentViewBinding.loading = true

            val file = File(PathUtil.getPath(this, data))

            val fileBody = RequestBody.create(MediaType.parse(getMimeType(data)!!), file)
            val multipartFileBody: MultipartBody.Part
            multipartFileBody = when (mUploadType) {
                "links" -> MultipartBody.Part.createFormData("links", file.name, fileBody)
                "link_samples" -> MultipartBody.Part.createFormData("link_samples", file.name, fileBody)
                else -> MultipartBody.Part.createFormData("samples", file.name, fileBody)
            }

            MpApiConnection.uploadDownloadableFile(this@SellerAddProductActivity, mUploadType!!, multipartFileBody)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : ApiCustomCallback<FileData>(this@SellerAddProductActivity, true) {
                        override fun onNext(fileData: FileData) {
                            super.onNext(fileData)
                            mContentViewBinding.loading = false
                            if (fileData.success) {
                                ToastHelper.showToast(this@SellerAddProductActivity, fileData.message, Toast.LENGTH_LONG)
                                when (mUploadType) {
                                    "links" -> {
                                        mSellerAddProductResponseData!!.productData!!.linkData!![mRvItemPosition].fileSave = fileData
                                        mSellerAddProductResponseData!!.productData!!.linkData!![mRvItemPosition].fileSave!!.status = "new"
                                    }
                                    "link_samples" -> {
                                        mSellerAddProductResponseData!!.productData!!.linkData!![mRvItemPosition].sampleFileSave = fileData
                                        mSellerAddProductResponseData!!.productData!!.linkData!![mRvItemPosition].sampleFileSave!!.status = "new"
                                    }
                                    "samples" -> {
                                        mSellerAddProductResponseData!!.productData!!.sampleData!![mRvItemPosition].fileSave = fileData
                                        mSellerAddProductResponseData!!.productData!!.sampleData!![mRvItemPosition].fileSave!!.status = "new"
                                    }
                                }
                                mUploadType = null
                              //  mContentViewBinding.linksRv.adapter!!.notifyDataSetChanged()
                              //  mContentViewBinding.samplesRv.adapter!!.notifyDataSetChanged()
                            } else {
                                ToastHelper.showToast(this@SellerAddProductActivity, fileData.message)
                            }
                        }

                        override fun onError(e: Throwable) {
                            super.onError(e)
                            mContentViewBinding.loading = false
                            mUploadType = null
                        }
                    })


        } catch (e: Exception) {
            mContentViewBinding.loading = false
            mUploadType = null
            ToastHelper.showToast(this, getString(R.string.error_please_try_again), Toast.LENGTH_SHORT)
            e.printStackTrace()
        }

    }

    private fun getMimeType(uri: Uri): String? {
        var mimeType: String?
        if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            val cr = applicationContext.contentResolver
            mimeType = cr.getType(uri)
        } else {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase())
        }
        return mimeType
    }

    /*Selected Product Categories*/
    lateinit var selectedCategoryName: ArrayList<String>
    private var mChildLevel: Int = 0
    lateinit var mCategoryList: ArrayList<CustomCategoryData>
    lateinit var mCategoryChildList: ArrayList<CustomCategoryData>

    private val selectedCategoryIds: ArrayList<String>
        get() {
            val selectedCategoryIds = ArrayList<String>()
            selectedCategoryName = ArrayList()
            for (noOfCategories in mCategoryList.indices) {
                val customCategories = mCategoryList[noOfCategories]
                if (customCategories.isChecked) {
                    selectedCategoryIds.add(customCategories.id.toString())
                    selectedCategoryName.add(customCategories.name!!.trim { it <= ' ' })
                }
                if (customCategories.childrenData!!.size != 0) {
                    for (noOfSubcategory in 0 until customCategories.childrenData!!.size) {
                        if (customCategories.childrenData!![noOfSubcategory].isChecked) {
                            selectedCategoryIds.add(customCategories.childrenData!![noOfSubcategory].id.toString())
                            selectedCategoryName.add(customCategories.childrenData!![noOfSubcategory].name!!.trim { it <= ' ' })
                        }
                    }
                }
            }
            return selectedCategoryIds
        }


    override fun onSelectCategories(categoryList: ArrayList<CustomCategoryData>) {
        mCategoryList = categoryList
        mSellerAddProductResponseData?.productData?.categoryIds = selectedCategoryIds
        if (mSelectedCategoriesAdapter == null) {
            mSelectedCategoriesAdapter = SelectedCategoriesRvAdapter(this@SellerAddProductActivity, selectedCategoryIds, selectedCategoryName)
            mContentViewBinding.categoryRv.layoutManager = LinearLayoutManager(this@SellerAddProductActivity)
            mContentViewBinding.categoryRv.adapter = mSelectedCategoriesAdapter
        } else {
            mSelectedCategoriesAdapter!!.setData(selectedCategoryIds, selectedCategoryName)
        }

    }

    private fun setupSelectedProductCategories() {
        mSellerAddProductResponseData?.categories?.let { mSellerAddProductResponseData?.categories!!.categoriesData?.add(it) }
        checkCategoryAvailability()
        createCategoryViewData()
        if (selectedCategoryName.size > 0) {
            mSelectedCategoriesAdapter = SelectedCategoriesRvAdapter(this@SellerAddProductActivity, mSellerAddProductResponseData?.productData?.categoryIds!!, selectedCategoryName)
            mContentViewBinding.categoryRv.layoutManager = LinearLayoutManager(this@SellerAddProductActivity)
            mContentViewBinding.categoryRv.adapter = mSelectedCategoriesAdapter
        }
    }

    private fun checkCategoryAvailability() {
        //  This method checks whether the categories are available or not because when particular categories are assigned to seller then assignedCategories data is used.
        if (mSellerAddProductResponseData!!.categories?.children == null && mSellerAddProductResponseData!!.assignedCategories?.isNotEmpty() == true) {
            mSellerAddProductResponseData!!.categories!!.categoriesData = ArrayList()
            for (mainCategory in mSellerAddProductResponseData!!.assignedCategories!!.indices) {
                val customCategories = CategoriesData()
                customCategories.categoryId = mSellerAddProductResponseData!!.assignedCategories?.get(mainCategory)?.value?.let { Integer.parseInt(it) }?:0
                customCategories.name = mSellerAddProductResponseData!!.assignedCategories?.get(mainCategory)?.label
                customCategories.children = ArrayList()
                mSellerAddProductResponseData!!.categories?.categoriesData!!.add(customCategories)
            }
        }

    }

    private fun createCategoryViewData() {
        selectedCategoryName = ArrayList()
        mCategoryList = ArrayList()
        if (mSellerAddProductResponseData!!.categories?.categoriesData != null) {
            for (mainCategory in 0 until mSellerAddProductResponseData!!.categories!!.categoriesData!!.size) {
                mCategoryChildList = ArrayList()
                val customCategories = CustomCategoryData()
                customCategories.id = mSellerAddProductResponseData!!.categories!!.categoriesData!![mainCategory].categoryId
                customCategories.name = mSellerAddProductResponseData!!.categories!!.categoriesData!![mainCategory].name
                if (mSellerAddProductResponseData?.productData?.categoryIds != null && mSellerAddProductResponseData?.productData?.categoryIds!!.contains(customCategories.id.toString())) {
                    customCategories.isChecked = true
                    mSellerAddProductResponseData!!.categories!!.categoriesData!![mainCategory].name?.let { selectedCategoryName.add(it) }
                }

                if (mSellerAddProductResponseData!!.categories?.categoriesData?.get(mainCategory)?.children?.isNotEmpty() == true) {
                    mChildLevel = 1
                    addChildrenData(mSellerAddProductResponseData!!.categories!!.categoriesData!![mainCategory].children!!)
                }
                if(mSellerAddProductResponseData!!.categories?.children == null){
                    customCategories.childrenData = mCategoryChildList
                    mCategoryList.add(customCategories)
                }else{
                    mCategoryList.addAll(mCategoryChildList)
                }
            }
        }
    }

    private fun addChildrenData(childrenData: List<SubCategory>) {
        for (childCategory in childrenData.indices) {
            val customChildren = CustomCategoryData()
            customChildren.id = childrenData[childCategory].categoryId
            customChildren.name = childrenData[childCategory].name!!
            customChildren.level = mChildLevel * 3
            if (mSellerAddProductResponseData?.productData?.categoryIds != null && mSellerAddProductResponseData?.productData?.categoryIds!!.contains(customChildren.id.toString())) {
                customChildren.isChecked = true
                selectedCategoryName.add(childrenData[childCategory].name!!)
            }
            mCategoryChildList.add(customChildren)
            if (childrenData[childCategory].children!!.isNotEmpty()) {
                mChildLevel++
                addChildrenData(childrenData[childCategory].children!!)
            }
        }
        mChildLevel--
    }

    override fun onFragmentDetached() {
        initSupportActionBar()
    }

    override fun onEditAddProductImage(imageRole: List<ImageRole>, mediaGallery: MediaGallery) {
        mSellerAddProductResponseData!!.productData!!.mediaGallery[mediaGallery.position - 1] = mediaGallery
        updateBaseImage()
        setupProductImages()
    }
}