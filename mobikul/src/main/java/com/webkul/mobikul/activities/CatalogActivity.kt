/*
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

package com.webkul.mobikul.activities

import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.AbsListView
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenu
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.webkul.arcore.activities.ArActivity
import com.webkul.arcore.activities.CameraWithImageActivity
import com.webkul.mobikul.R
import com.webkul.mobikul.adapters.CatalogAuctionProductsRvAdapter
import com.webkul.mobikul.adapters.CatalogProductsRvAdapter
import com.webkul.mobikul.adapters.CategoryBannerVpAdapter
import com.webkul.mobikul.adapters.CriteriaDataAdapter

import com.webkul.mobikul.databinding.ActivityCatalogBinding
import com.webkul.mobikul.fragments.EmptyFragment
import com.webkul.mobikul.handlers.CatalogActivityHandler
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_ID
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_TITLE
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_CATALOG_TYPE
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_FROM_NOTIFICATION
import com.webkul.mobikul.helpers.ConstantsHelper.RC_AR
import com.webkul.mobikul.helpers.ConstantsHelper.VIEW_TYPE_GRID
import com.webkul.mobikul.interfaces.OnMenuSelectListener
import com.webkul.mobikul.models.catalog.CatalogProductsResponseModel
import com.webkul.mobikul.models.homepage.BannerImage
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.github.inflationx.calligraphy3.CalligraphyTypefaceSpan
import io.github.inflationx.calligraphy3.TypefaceUtils
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONArray
import retrofit2.HttpException
import java.util.*

open class CatalogActivity : BaseActivity() {
    lateinit var mContentViewBinding: ActivityCatalogBinding
    var mFromNotification: Boolean = false
    var mPageNumber = 1
    var mCatalogType = ""
    var mCatalogName = ""
    var mCatalogId = ""
    var mSortingInputJson = JSONArray()
    var mFilterInputJson = JSONArray()
    var mSellerAttributesIdOptionCodeMap: HashMap<String, String> = HashMap()
    var mSelectedProduct: Int = 0
    var clickedPageNumber:Int = 0
    var intentFilter:IntentFilter? = null
    private lateinit var categoryAdapter:CatalogProductsRvAdapter

    companion object{
        const val BROADCAST_DEFAULT_ALBUM_CHANGED = "BROADCAST_DEFAULT_ALBUM_CHANGED"
        const val BROADCAST_DEFAULT_UPDATE_CART_BADGE = "BROADCAST_DEFAULT_UPDATE_CART_BADGE"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_catalog)
        LocalBroadcastManager.getInstance(this).registerReceiver(
            updateCartBadgeReceiver,
            IntentFilter(BROADCAST_DEFAULT_UPDATE_CART_BADGE)
        )
        startInitialization(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        removeEmptyLayout()
        startInitialization(intent!!)
    }

    open fun startInitialization(intent: Intent) {
        intentFilter = IntentFilter("com.journaldev.broadcastreceiver.SOME_ACTION")
        if (intent.hasExtra(BUNDLE_KEY_CATALOG_TYPE) && intent.hasExtra(BUNDLE_KEY_CATALOG_TITLE) && intent.hasExtra(
                BUNDLE_KEY_CATALOG_ID
            )
        ) {
            mFromNotification = intent.hasExtra(BUNDLE_KEY_FROM_NOTIFICATION)
            mCatalogType = intent.getStringExtra(BUNDLE_KEY_CATALOG_TYPE)!!

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mCatalogName = Html.fromHtml(
                    intent.getStringExtra(BUNDLE_KEY_CATALOG_TITLE),
                    Html.FROM_HTML_MODE_LEGACY
                ).toString()

            } else {
                mCatalogName =
                    Html.fromHtml(intent.getStringExtra(BUNDLE_KEY_CATALOG_TITLE)).toString()
            }
            mCatalogId = intent.getStringExtra(BUNDLE_KEY_CATALOG_ID)!!
        } else {
            ToastHelper.showToast(this, getString(R.string.something_went_wrong))
        }
        initSupportActionBar()
        mPageNumber = 1


        callApi(false,0)
        checkAndLoadLocalData()
    }

    private fun inflateBadge() {
        val bottomNavigationMenuView = mContentViewBinding.bottomNavView.getChildAt(0) as BottomNavigationMenuView
        val v = bottomNavigationMenuView.getChildAt(3)
        val itemView = v as BottomNavigationItemView
        mBadge = LayoutInflater.from(this).inflate(R.layout.notification_badge, itemView, true)
    }

    fun initSupportActionBar() {


        mContentViewBinding.toolbar.backBtn.setOnClickListener {
            finish()
        }

        mContentViewBinding.toolbar.searchEditorBtn.setOnClickListener {
            openMaterialSearchView()
        }

        mContentViewBinding.bottomNavView.menu.getItem(2).isChecked = true

        mContentViewBinding.bottomNavView.setOnNavigationItemSelectedListener { menuItem ->
            updateCartBadge()
            updateCartCount(AppSharedPref.getCartCount(this@CatalogActivity))
            when (menuItem.itemId) {
                R.id.bottom_category -> {
                    val intent = Intent()
                    intent.action = "bottom.menu.action"
                    intent.putExtra("menuName", "category")
                    intent.putExtra("menuIndex", 0)
                    sendBroadcast(intent)
                    finish()
                }
                R.id.bottom_auction -> {
                    val intent = Intent()
                    intent.action = "bottom.menu.action"
                    intent.putExtra("menuName", "auction")
                    intent.putExtra("menuIndex", 1)
                    sendBroadcast(intent)
                    finish()
                }
                R.id.bottom_home -> {
                    val intent = Intent()
                    intent.action = "bottom.menu.action"
                    intent.putExtra("menuName", "Home")
                    intent.putExtra("menuIndex", 2)
                    sendBroadcast(intent)
                    finish()
                }
                R.id.bottom_cart -> {
                    val intent = Intent()
                    intent.action = "bottom.menu.action"
                    intent.putExtra("menuName", "cart")
                    intent.putExtra("menuIndex", 3)
                    sendBroadcast(intent)
                    finish()
                }
                R.id.bottom_profile -> {
                    val intent = Intent()
                    intent.action = "bottom.menu.action"
                    intent.putExtra("menuName", "profile")
                    intent.putExtra("menuIndex", 4)
                    sendBroadcast(intent)
                    finish()
                }
            }

            true
        }

        /* mContentViewBinding.productsRv.addOnScrollListener(object:RecyclerView.OnScrollListener(){
             override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                 super.onScrollStateChanged(recyclerView, newState)
                 if(newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    mContentViewBinding.appBarLayout.visibility = View.GONE
                 } else {

                     // Do something
                 }
             }
         })*/
        updateCartBadge()
        inflateBadge()
        updateCartCount(AppSharedPref.getCartCount(this))
    }

    open fun callApi(isClicked:Boolean,position:Int) {
        mContentViewBinding.loading = true
        if(isClicked){
            mHashIdentifier = Utils.getMd5String(
                "getCatalogProductData" + AppSharedPref.getStoreId(this) + AppSharedPref.getQuoteId(this) + AppSharedPref.getCustomerToken(
                    this
                ) + mCatalogType + mCatalogId + clickedPageNumber + mSortingInputJson + mFilterInputJson
            )
            ApiConnection.getCatalogProductData(
                this,
                mDataBaseHandler.getETagFromDatabase(mHashIdentifier),
                mCatalogType,
                mCatalogId,
                clickedPageNumber,
                mSortingInputJson,
                mFilterInputJson
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<CatalogProductsResponseModel>(this, false) {
                    override fun onNext(catalogProductsResponseModel: CatalogProductsResponseModel) {
                        super.onNext(catalogProductsResponseModel)
                        mContentViewBinding.loading = false
                        if (catalogProductsResponseModel.success) {

                            onSuccessfulResponse(catalogProductsResponseModel,isClicked, position)
                        } else {
                            onFailureResponse(catalogProductsResponseModel)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mContentViewBinding.loading = false
                        onErrorResponse(e)
                    }
                })
        }else{
            mHashIdentifier = Utils.getMd5String(
                "getCatalogProductData" + AppSharedPref.getStoreId(this) + AppSharedPref.getQuoteId(this) + AppSharedPref.getCustomerToken(
                    this
                ) + mCatalogType + mCatalogId + mPageNumber + mSortingInputJson + mFilterInputJson
            )
            ApiConnection.getCatalogProductData(
                this,
                mDataBaseHandler.getETagFromDatabase(mHashIdentifier),
                mCatalogType,
                mCatalogId,
                mPageNumber++,
                mSortingInputJson,
                mFilterInputJson
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<CatalogProductsResponseModel>(this, false) {
                    override fun onNext(catalogProductsResponseModel: CatalogProductsResponseModel) {
                        super.onNext(catalogProductsResponseModel)
                        mContentViewBinding.loading = false
                        if (catalogProductsResponseModel.success) {
                            onSuccessfulResponse(catalogProductsResponseModel,isClicked, position)
                        } else {
                            onFailureResponse(catalogProductsResponseModel)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mContentViewBinding.loading = false
                        onErrorResponse(e)
                    }
                })
        }
    }

    fun checkAndLoadLocalData() {
        mDataBaseHandler.getResponseFromDatabaseOnThread(mHashIdentifier)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Observer<String> {
                override fun onNext(response: String) {
                    if (response.isNotBlank()) {
                        val catalogResponse =
                            mGson.fromJson(response, CatalogProductsResponseModel::class.java)
                        if (catalogResponse.productList.isNotEmpty())
                            onSuccessfulResponse(catalogResponse,false,0)
                    }
                }

                override fun onError(e: Throwable) {
                }

                override fun onSubscribe(disposable: Disposable) {
                    mCompositeDisposable.add(disposable)
                }

                override fun onComplete() {

                }
            })
    }

    @SuppressLint("NotifyDataSetChanged")
    fun onSuccessfulResponse(catalogProductsResponseModel: CatalogProductsResponseModel, isClicked: Boolean, position: Int) {
        if (mPageNumber == 2) {
            mContentViewBinding.data = catalogProductsResponseModel
            mContentViewBinding.handler = CatalogActivityHandler(this)

            if (mContentViewBinding.data!!.productList.isEmpty()) {
                if (mFilterInputJson.length() == 0)
                    mContentViewBinding.productsOptionsLayout.visibility = View.GONE
                addEmptyLayout()
            } else {
                removeEmptyLayout()
              //  setupBannersViewPager(catalogProductsResponseModel.bannerImage)
              //  setupCriteriaDataRv()
                setupProductsRv(isClicked,position)
            }
        } else {
            if(isClicked){
                mContentViewBinding.data!!.productList.addAll(catalogProductsResponseModel.productList)
                mContentViewBinding.productsRv.adapter?.notifyItemRangeChanged(
                    mContentViewBinding.data!!.productList.size - catalogProductsResponseModel.productList.size,
                    mContentViewBinding.data!!.productList.size
                )
                mContentViewBinding.productsRv.scrollToPosition(position)

            }else{
                mContentViewBinding.data!!.productList.addAll(catalogProductsResponseModel.productList)
                mContentViewBinding.productsRv.adapter?.notifyItemRangeChanged(
                    mContentViewBinding.data!!.productList.size - catalogProductsResponseModel.productList.size,
                    mContentViewBinding.data!!.productList.size
                )

            }

           /* if(isClicked){
                //categoryAdapter.notifyItemChanged(position)
                categoryAdapter.notifyDataSetChanged()
            }*/

        }
    }

    private fun setupCriteriaDataRv() {
       /* mContentViewBinding.criteriaDataRv.adapter =
            CriteriaDataAdapter(this, mContentViewBinding.data!!.criteriaData)*/
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupProductsRv(isClicked: Boolean, position: Int) {
        if (mContentViewBinding.productsRv.adapter == null) {
            if (AppSharedPref.getViewType(this) == VIEW_TYPE_GRID) {
                mContentViewBinding.productsRv.layoutManager = GridLayoutManager(this, 2)
            } else {
                mContentViewBinding.productsRv.layoutManager = LinearLayoutManager(this)
            }
            mContentViewBinding.productsRv.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val lastCompletelyVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    if (!mContentViewBinding.loading!! && mContentViewBinding.data!!.productList.size < mContentViewBinding.data!!.totalCount
                        && lastCompletelyVisibleItemPosition > mContentViewBinding.data!!.productList.size - 4
                    ) {
                        callApi(false,0)
                    }
                }
            })
        }

        if(mCatalogId=="auctionproductlist"){
            mContentViewBinding.productsRv.adapter = CatalogAuctionProductsRvAdapter(this, mContentViewBinding.data!!.productList)
        }else{
            mContentViewBinding.productsRv.adapter = CatalogProductsRvAdapter(this, mContentViewBinding.data!!.productList)
        }


    }

    private fun setupBannersViewPager(bannerImage: ArrayList<BannerImage>) {
      /*  if (mContentViewBinding.categoryBannerViewPager.adapter == null) {
            mContentViewBinding.categorySliderDotsTabLayout.setupWithViewPager(
                mContentViewBinding.categoryBannerViewPager,
                true
            )
            val imageCarouselSwitcherTimer = Timer()
            imageCarouselSwitcherTimer.scheduleAtFixedRate(
                BannerSwitchTimerTask(
                    mContentViewBinding.categoryBannerViewPager,
                    bannerImage.size
                ),
                1000.toLong(),
                ApplicationConstants.DEFAULT_TIME_TO_SWITCH_BANNER_IN_MILLIS.toLong()
            )
        }
        mContentViewBinding.categoryBannerViewPager.adapter =
            CategoryBannerVpAdapter(this, bannerImage)
        mContentViewBinding.categoryBannerViewPager.offscreenPageLimit = 2*/
    }


    private fun onFailureResponse(catalogProductsResponseModel: CatalogProductsResponseModel) {
        AlertDialogHelper.showNewCustomDialog(
            this,
            getString(R.string.error),
            catalogProductsResponseModel.message,
            false,
            getString(R.string.try_again),
            DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                mPageNumber--
                callApi(false,0)
            },
            getString(R.string.dismiss),
            DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                finish()
            })
    }

    open fun onErrorResponse(error: Throwable) {
        val response = mDataBaseHandler.getResponseFromDatabase(mHashIdentifier)
        if ((!NetworkHelper.isNetworkAvailable(this) || (error is HttpException && error.code() == 304)) && response.isNotBlank()) {
            onSuccessfulResponse(mGson.fromJson(response, CatalogProductsResponseModel::class.java),false,0)
        } else {
            AlertDialogHelper.showNewCustomDialog(
                this,
                getString(R.string.error),
                NetworkHelper.getErrorMessage(this, error),
                false,
                getString(R.string.try_again),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    mPageNumber--
                    callApi(false,0)
                },
                getString(R.string.dismiss),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    finish()
                })
        }
    }

    private fun addEmptyLayout() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(
            R.id.product_list_container,
            EmptyFragment.newInstance(
                "empty_cart.json",
                getString(R.string.empty_product_catalog),
                getString(R.string.try_different_category_or_search_keyword_maybe),
                false,
                "search"
            ),
            EmptyFragment::class.java.simpleName
        )
        fragmentTransaction.commitAllowingStateLoss()
    }

    private fun removeEmptyLayout() {
        val emptyFragment =
            supportFragmentManager.findFragmentByTag(EmptyFragment::class.java.simpleName)
        if (emptyFragment != null) {
            supportFragmentManager.beginTransaction().remove(emptyFragment)
                .commitAllowingStateLoss()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var allPermissionsGranted = true
        for (eachGrantResult in grantResults) {
            if (eachGrantResult != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false
            }
        }
        if (allPermissionsGranted) {
            if (requestCode == RC_AR) {
                startArActivity()
            }
        }
    }

    fun startArActivity() {
        val intent: Intent =
            if (mContentViewBinding.data!!.productList[mSelectedProduct].arType == "3D") {
                Intent(this, ArActivity::class.java)
            } else {
                Intent(this, CameraWithImageActivity::class.java)
            }
        intent.putExtra(
            BundleKeysHelper.BUNDLE_KEY_PRODUCT_NAME,
            mContentViewBinding.data!!.productList[mSelectedProduct].name
        )
        intent.putExtra(
            BundleKeysHelper.BUNDLE_KEY_AR_MODEL_URL,
            mContentViewBinding.data!!.productList[mSelectedProduct].arModelUrl
        )
        startActivity(intent)
    }

    override fun onBackPressed() {
        if (mFromNotification) {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        } else {
            super.onBackPressed()
        }
    }

    private inner class BannerSwitchTimerTask(
        private val mViewPager: ViewPager,
        private val mBannerSize: Int
    ) : TimerTask() {

        var firstTime = true

        override fun run() {
            try {
                runOnUiThread {
                    if (mViewPager.currentItem == mBannerSize - 1) {
                        mViewPager.currentItem = 0
                    } else {
                        if (firstTime) {
                            mViewPager.currentItem = mViewPager.currentItem
                            firstTime = false
                        } else {
                            mViewPager.currentItem = mViewPager.currentItem + 1
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



    val updateCartBadgeReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            when (intent?.action) {
                BROADCAST_DEFAULT_UPDATE_CART_BADGE -> {
                    updateCartBadge()
                    updateCartCount(AppSharedPref.getCartCount(this@CatalogActivity))
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateBadge()
    }


}


