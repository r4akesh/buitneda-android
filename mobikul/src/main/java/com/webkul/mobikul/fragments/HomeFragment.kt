package com.webkul.mobikul.fragments

//import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_HOME_PAGE_DATA
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.os.*
import android.text.SpannableString
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.webkul.mobikul.R
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.activities.HomeActivity
import com.webkul.mobikul.adapters.*
import com.webkul.mobikul.databinding.*
import com.webkul.mobikul.handlers.BigBannerHandler
import com.webkul.mobikul.handlers.FeaturedCategoriesRvHandler
import com.webkul.mobikul.handlers.HomePageBannerVpHandler
import com.webkul.mobikul.handlers.HomePageProductCarouselHandler
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.models.SortOrder
import com.webkul.mobikul.models.homepage.Carousel
import com.webkul.mobikul.models.homepage.HomePageDataModel
import com.webkul.mobikul.models.product.AnalysisModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikul.view_model.CommonViewModel
import com.webkul.mobikul.wallet.activities.ManageWalletAmountActivity
import io.github.inflationx.calligraphy3.CalligraphyTypefaceSpan
import io.github.inflationx.calligraphy3.TypefaceUtils
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.product_carousel_first_layout.view.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule


class HomeFragment : Fragment() {
    private var isLoadingTopSellingProducts: Boolean = false
    lateinit var mContentViewBinding: FragmentHomeBinding
    private var mBannerSwitcherTimerList: ArrayList<Timer> = ArrayList()
    var mHomePageDataModel: HomePageDataModel = HomePageDataModel()
    var mPageNumber: Int = 1
    var mResendTimer: CountDownTimer? = null
    private var commonViewModel: CommonViewModel? = null
    private var hashIdentifier = ""
    lateinit var bannerCarouselLayoutBinding:BannerCarouselLayoutBinding
    var mContext:Context? = null
//    private var currentCache = 0


//    private var mContext: Context? = null
    private lateinit var appCompatActivity: AppCompatActivity

    companion object {
        var instanceOf: Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instanceOf++
        Log.d(TAG, "onCreate: $instanceOf")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context
    }
    private val TAG = "HomeFragment:::"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mContentViewBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
//        HomeActivity.mContentViewBinding.bottomAppCl.visibility=View.VISIBLE
//        HomeActivity.mContentViewBinding.bottomNavView.menu.findItem(R.id.bottom_home).isChecked = true
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel::class.java)
        setHasOptionsMenu(true)
        startInitialization()
        mContentViewBinding.loading = true
        return mContentViewBinding.root
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        Handler().postDelayed({
//
//        },10)
//    }

    private fun startInitialization() {
        bannerCarouselLayoutBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.banner_carousel_layout,
            mContentViewBinding.carouselsLayout,
            false
        )
        initSwipeRefresh()
        initHomePageData()
        initSupportActionBar()


        mContentViewBinding.whatsAppHomeBtn.setOnClickListener {
            openWhatsApp()
        }
    }

    private fun initSupportActionBar() {
        //  (activity as AppCompatActivity).setSupportActionBar(mContentViewBinding.toolbar!!)
        var title = SpannableString(getString(R.string.activity_title_home))
        title.setSpan(
            CalligraphyTypefaceSpan(
                TypefaceUtils.load(
                    (activity as AppCompatActivity).assets,
                    ApplicationConstants.CALLIGRAPHY_FONT_PATH_SEMI_BOLD
                )
            ), 0, title.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        (activity as AppCompatActivity).supportActionBar?.title = title
        /* val drawable = resources.getDrawable(R.drawable.whatsapp)
         val bitmap = (drawable as BitmapDrawable).bitmap
         val newdrawable: Drawable =
             BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, 25, 25, true))*/
        /* (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_whatsapp)
         (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
         (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)*/
       /* mContentViewBinding.toolbar.whatAppBtn.setOnClickListener {
            openWhatsApp()
        }*/

        mContentViewBinding.toolbar.walletBtn.setOnClickListener {
            mContext?.let { mContext ->
                if (AppSharedPref.isLoggedIn(mContext)) {
                    startActivity(Intent(mContext, ManageWalletAmountActivity::class.java))
                } else {
                    mContext.startActivity(
                        (mContext.applicationContext as MobikulApplication).getLoginAndSignUpActivity(
                            mContext
                        )
                    )
                }

            }
            // startActivity(Intent(context, WalletActivity::class.java))
        }

        mContentViewBinding.toolbar.searchEditorBtn.setOnClickListener {
            (activity as HomeActivity).openMaterialSearchView()
        }

        mContentViewBinding.toolbar.notificationBtn.setOnClickListener {
            (activity as HomeActivity).openNotificationFragment()
        }
    }


    override fun onOptionsItemSelected(@NonNull item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {

//                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun appInstalledOrNot(uri: String): Boolean {
        val pm: PackageManager = requireActivity().packageManager
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return false
    }

    private fun openWhatsApp() {
        val isAppInstalled = appInstalledOrNot("com.whatsapp.w4b")
        val intent = Intent(Intent.ACTION_VIEW)
        if (isAppInstalled) {
            intent.setPackage("com.whatsapp.w4b")
            intent.data =
                Uri.parse("https://api.whatsapp.com/send?phone=${mContext!!.getString(R.string.whats_app_number)}")
            if (mContext!!.packageManager.resolveActivity(intent, 0) != null) {
                startActivity(intent)
            } else {
                ToastHelper.showToast(
                    mContext!!,
                    mContext!!.getString(R.string.please_install_whatsapp)
                )
                val url = "https://play.google.com/store/search?q=whatsapp&c=apps"
                val whatsBusinessIntent = Intent(Intent.ACTION_VIEW)
                whatsBusinessIntent.data = Uri.parse(url)
                startActivity(whatsBusinessIntent)
            }
        } else {
            intent.setPackage("com.whatsapp")
            intent.data =
                Uri.parse("https://api.whatsapp.com/send?phone=${mContext!!.getString(R.string.whats_app_number)}")
            if (mContext!!.packageManager.resolveActivity(intent, 0) != null) {
                startActivity(intent)
            } else {
                ToastHelper.showToast(
                    mContext!!,
                    mContext!!.getString(R.string.please_install_whatsapp)
                )
                val url = "https://play.google.com/store/search?q=whatsapp&c=apps"
                val whatsAppIntent = Intent(Intent.ACTION_VIEW)
                whatsAppIntent.data = Uri.parse(url)
                startActivity(whatsAppIntent)
            }
        }
    }

    private fun initSwipeRefresh() {
        mContentViewBinding.swipeRefreshLayout.setDistanceToTriggerSync(300)
        mContentViewBinding.swipeRefreshLayout.setOnRefreshListener {
            if (NetworkHelper.isNetworkAvailable(mContext!!)) {
//                callApiBackground()
                lifecycleScope.launch{
                    loadDataFromDB()
                }

            } else {
                mContentViewBinding.swipeRefreshLayout.isRefreshing = false
                ToastHelper.showToast(mContext!!, getString(R.string.you_are_offline))
            }
        }
    }

    fun initHomePageData() {
        /* if (arguments?.containsKey(BundleKeysHelper.BUNDLE_KEY_HOME_PAGE_DATA)!! && arguments?.getParcelable<HomePageDataModel>(
                 BUNDLE_KEY_HOME_PAGE_DATA
             ) != null
         ) {
            *//* (context as HomeActivity).mHomePageDataModel =
                arguments?.getParcelable<HomePageDataModel>(BUNDLE_KEY_HOME_PAGE_DATA)!!*//*

            activity?.runOnUiThread {(
                onSuccessfulResponse(HomeDataSingleton.getInstance().mHomePageDataModel!!)
                )}


//            Handler().postDelayed(Runnable {
//
//            },3000)

        } else {
            callApi()

        }*/

        if (HomeDataSingleton.getInstance().mHomePageDataModel != null) {
            /*activity?.runOnUiThread {
                (onSuccessfulResponse(HomeDataSingleton.getInstance().mHomePageDataModel!!))
            }*/

            lifecycleScope.launch{
                (onSuccessfulResponse(HomeDataSingleton.getInstance().mHomePageDataModel!!))
            }

        } else {
            callApi()

        }


        callApiBackground()
        checkAndLoadLocalData()
    }


    private fun loadDataFromDB() {
//        createHash("$currentCache")
        createHash("")
        BaseActivity.mDataBaseHandler.getResponseFromDatabaseOnThread(hashIdentifier)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Observer<String> {
                override fun onNext(data: String) {
                    Log.d(TAG, "onNext: $data")
                    mContentViewBinding.swipeRefreshLayout.isRefreshing = false
                    Handler(Looper.getMainLooper()).postDelayed({
                        updateAnimationCheckAndProceed(data)
                    }, 300)

                }

                override fun onError(e: Throwable) {
                    Log.d(TAG, "onError: ")
                    e.printStackTrace()
                }

                override fun onSubscribe(disposable: Disposable) {
                    (mContext as BaseActivity).mCompositeDisposable.add(disposable)
                }

                override fun onComplete() {

                }
            })
    }

    private fun updateAnimationCheckAndProceed(response: String) {
        (mContext as HomeActivity).runOnUiThread {
            if (response != "") {
                (mContext as HomeActivity).mHomePageDataModel =
                    BaseActivity.mGson.fromJson(response, HomePageDataModel::class.java)
                Log.d(TAG, "updateAnimationCheckAndProceed: $response")
                onSuccessfulResponse((mContext as HomeActivity).mHomePageDataModel)
            }

        }
    }

    private fun callApi() {
        mContentViewBinding.swipeRefreshLayout.isRefreshing = true
        mContentViewBinding.loading = true
        createHash("")
        Log.d(
            TAG,
            "callApi: " + BaseActivity.mDataBaseHandler.getETagFromDatabase(hashIdentifier)
        )
        ApiConnection.getHomePageData(
            mContext!!,
            BaseActivity.mDataBaseHandler.getETagFromDatabase(hashIdentifier),
            false,
            ""
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiCustomCallback<HomePageDataModel>(mContext!!, false) {
                override fun onNext(homePageDataModel: HomePageDataModel) {
                    super.onNext(homePageDataModel, hashIdentifier)
                    mContentViewBinding.swipeRefreshLayout.isRefreshing = false
                    if (homePageDataModel.success) {
                        (mContext as HomeActivity).mHomePageDataModel = homePageDataModel
                        onSuccessfulResponse(homePageDataModel)
                    } else {
                        onFailureResponse(homePageDataModel)
                    }
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    mContentViewBinding.swipeRefreshLayout.isRefreshing = false
                    onErrorResponse(e)
                }
            })
    }

    private fun callApiBackground() {
        if (mContext == null) {
            return
        }
//        Log.d(TAG, "callApi: refreshCount $refreshCount")
//        val hash = returnHash(if ("$refreshCount" == "0") "" else "$refreshCount")
        val hash = returnHash("")
        Log.d(TAG, "callApi: hash $hash")
        ApiConnection.getHomePageData(
            mContext!!,
            BaseActivity.mDataBaseHandler.getETagFromDatabase(hash),
            false,
            ""
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiCustomCallback<HomePageDataModel>(mContext!!, false) {
                override fun onNext(homePageDataModel: HomePageDataModel) {
                    super.onNext(homePageDataModel, hash)
//                    if (homePageDataModel.success && refreshCount != 2) {
//                        callApi(refreshCount + 1)
//                    }
                    Timer("backgroundHomeAPI", false).schedule(3000) {
                        callApiBackground()
                    }

                }

            })
    }

    private fun createHash(refreshCount: String) {
        hashIdentifier = Utils.getMd5String(
            "homePageData" + AppSharedPref.getWebsiteId(mContext!!) + AppSharedPref.getStoreId(
                mContext!!
            ) + AppSharedPref.getCustomerToken(mContext!!) + AppSharedPref.getQuoteId(mContext!!) + AppSharedPref.getCurrencyCode(
                mContext!!
            ) + refreshCount
        )

        Log.d(
            TAG,
            "cacheData: " + "homePageData" + ":" + AppSharedPref.getWebsiteId(mContext!!) + ":" + AppSharedPref.getStoreId(
                mContext!!
            ) + ":" + AppSharedPref.getCustomerToken(mContext!!) + ":" + AppSharedPref.getQuoteId(
                mContext!!
            ) + ":" + AppSharedPref.getCurrencyCode(
                mContext!!
            ) + ":" + refreshCount
        )
    }

    private fun returnHash(refreshCount: String): String {
        Log.d(
            TAG,
            "cacheData: API " + "homePageData" + ":" + AppSharedPref.getWebsiteId(mContext!!) + ":" + AppSharedPref.getStoreId(
                mContext!!
            ) + ":" + AppSharedPref.getCustomerToken(mContext!!) + ":" + AppSharedPref.getQuoteId(
                mContext!!
            ) + ":" + AppSharedPref.getCurrencyCode(
                mContext!!
            ) + ":" + refreshCount
        )
        return Utils.getMd5String(
            "homePageData" + AppSharedPref.getWebsiteId(mContext!!) + AppSharedPref.getStoreId(
                mContext!!
            ) + AppSharedPref.getCustomerToken(mContext!!) + AppSharedPref.getQuoteId(mContext!!) + AppSharedPref.getCurrencyCode(
                mContext!!
            ) + refreshCount
        )


    }

    fun initIntent() {
        (activity as HomeActivity).setupFragment(2)
    }

    private fun checkAndLoadLocalData() {
        val response =
            BaseActivity.mDataBaseHandler.getResponseFromDatabase(hashIdentifier)
        if (response.isNotBlank()) {
            mHomePageDataModel = BaseActivity.mGson.fromJson(response, HomePageDataModel::class.java)
            initLayout()
        }
    }


    private fun onSuccessfulResponse(homePageDataModel: HomePageDataModel?) {
        Log.d(TAG, "onSuccessfulResponse: ")
        mContentViewBinding.loading = false
        mHomePageDataModel = homePageDataModel!!
        if (mContext != null) {

            setAppSharedPrefConfigDetails()
            initLayout()
            (activity as HomeActivity).updateCartCount(AppSharedPref.getCartCount(mContext!!))
//            AppSharedPref.setCartCount(context!!,mContentViewBinding.data!!.cartCount)
            //onSuccessfulTopSellingResponse(homePageDataModel)
            callTopApi()
            //TopCategoriesListRv()
        }
    }




    private fun setAppSharedPrefConfigDetails() {
        AppSharedPref.setIsWishlistEnabled(mContext!!, mHomePageDataModel.wishlistEnable)

        val customerDataSharedPref =
            AppSharedPref.getSharedPreferenceEditor(mContext!!, AppSharedPref.CUSTOMER_PREF)
        customerDataSharedPref.putString(
            AppSharedPref.KEY_CUSTOMER_NAME,
            mHomePageDataModel.customerName
        )
        customerDataSharedPref.putString(
            AppSharedPref.KEY_CUSTOMER_EMAIL,
            mHomePageDataModel.customerEmail
        )
        try {
            customerDataSharedPref.putBoolean("isSeller", mHomePageDataModel.isSeller)
            customerDataSharedPref.putBoolean("isPendingSeller", mHomePageDataModel.isPending)
            customerDataSharedPref.putBoolean("isAdmin", mHomePageDataModel.isAdmin)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        customerDataSharedPref.apply()

        AppSharedPref.setCategoryData(mContext!!, mHomePageDataModel.categories!!)
    }

    fun onFailureResponse(response: Any) {
        when ((response as BaseModel).otherError) {
            ConstantsHelper.CUSTOMER_NOT_EXIST -> {
                AlertDialogHelper.showNewCustomDialog(
                    activity as BaseActivity,
                    getString(R.string.error),
                    response.message,
                    false,
                    getString(R.string.ok),
                    { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        Utils.logoutAndGoToHome(mContext!!)
                    }, "", null
                )
            }
            else -> {
                ToastHelper.showToast(mContext!!, response.message)
            }
        }
    }


    private fun onErrorResponse(error: Throwable) {

        if ((!NetworkHelper.isNetworkAvailable(mContext!!) || (error is HttpException && error.code() == 304)) && mContentViewBinding.data != null) {
            // Do Nothing as the data is already loaded
        } else {
            AlertDialogHelper.showNewCustomDialog(
                mContext as BaseActivity,
                getString(R.string.error),
                NetworkHelper.getErrorMessage(mContext, error),
                false,
                getString(R.string.try_again),
                { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    mContentViewBinding.swipeRefreshLayout.isRefreshing = true
                    callApi()
                },
                getString(R.string.dismiss),
                { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                })
        }
    }

    private fun initLayout() {
        mPageNumber = 1
        mContentViewBinding.mainScroller.visibility = View.VISIBLE
        mContentViewBinding.data = mHomePageDataModel
//        HomeActivity.mContentViewBinding.handler = HomeActivityHandler(this)

//        mContentViewBinding.handler = HomeActivityHandler(this)
//        (activity as HomeActivity).updateCartCount(mContentViewBinding.data!!.cartCount)

        /* Init Nav Drawer Start */
//        setupNavDrawerStart()

        /* Canceling All the Timers*/
        cancelAndPurgeAllTimers()

//        setupCategoriesRv()
        /* Init Carousel Layout */
        activity?.runOnUiThread {
            setupCarouselsLayout()
        }


        /* Init Recently Viewed Carousel Layout */
//        setupRecentlyViewedCarouselsLayout()

        /* Check to Open Cart from Notification */
//        checkForCartOpening()
    }

    private fun cancelAndPurgeAllTimers() {
        if (mBannerSwitcherTimerList.isNotEmpty()) {
            for (timer in mBannerSwitcherTimerList) {
                timer.cancel()
                timer.purge()
            }
            mBannerSwitcherTimerList = ArrayList()
        }
    }

    private fun setupCategoriesRv() {
        if (mHomePageDataModel.categories!!.isNotEmpty()) {

//                 mContentViewBinding.categoriesRv.addItemDecoration(HorizontalMarginItemDecoration(resources.getDimension(R.dimen.spacing_mini).toInt()))
//            mContentViewBinding.categoriesRv.adapter = HomePageCategoriesRvAdapter(this, mHomePageDataModel.categories)
//            mContentViewBinding.categoriesRv.layoutManager = LinearLayoutManager(context!!, RecyclerView.HORIZONTAL, false)
        }
    }


    private fun setupCarouselsLayout() {
        mContentViewBinding.carouselsLayout.removeAllViews()
        val bannerImage = Carousel()
        bannerImage.id = "bannerimage"
        bannerImage.type = "banner"
        bannerImage.banners = mHomePageDataModel.bannerImages
        mHomePageDataModel.carousel?.add(bannerImage)

//top banner with search
        activity?.runOnUiThread {
            setupOfferBannerRv(bannerImage, "bannerimage")
        }


/* val category = Carousel()
category.id = "brandlist"
category.type = "category"
// category.featuredCategories = mHomePageDataModel.featuredCategories
category.brandlist = mHomePageDataModel.brandlist
mHomePageDataModel.carousel?.add(category)
setupFeaturesCategoriesRv(category)*/
        val sortedOrder = getSortedCarouselsData(mHomePageDataModel.sort_order)
        if (sortedOrder.isNotEmpty()) {
            val categoryStatus1 =
                mHomePageDataModel.carousel?.singleOrNull { it.id.equals("bannerimage") }
            if (categoryStatus1 == null) {
                val bannerImage = Carousel()
                bannerImage.id = "bannerimage"
                bannerImage.type = "banner"
                bannerImage.banners = mHomePageDataModel.bannerImages
// if (mHomePageDataModel.carousel===bannerImage) {
                mHomePageDataModel.carousel?.add(bannerImage)
            }

// }
            val categoryStatus =
                mHomePageDataModel.carousel?.singleOrNull { it.id.equals("brandlist") }
            if (categoryStatus == null) {
                val category = Carousel()
                category.id = "featuredcategories"
                category.type = "category"
                category.featuredCategories = mHomePageDataModel.featuredCategories
// if (!mHomePageDataModel.carousel?.contains(category)!!) {
                mHomePageDataModel.carousel?.add(category)
            }

// }
            val categoryStatus2 =
                mHomePageDataModel.carousel?.singleOrNull { it.id.equals("brandlist") }
            if (categoryStatus2 == null) {
                val category3 = Carousel()
                category3.id = "brandlist"
                category3.type = "brandlist"
                category3.brandlist = mHomePageDataModel.brandlist
// if (!mHomePageDataModel.carousel?.contains(category3)!!) {
                mHomePageDataModel.carousel?.add(category3)
            }

// }


// setupFeaturesCategoriesRv(category3)

// val category1 = Carousel()
// category.id = "featuredcategories"
// category.type = "category"
// category.featuredCategories = mHomePageDataModel.featuredCategories
// mHomePageDataModel.carousel?.add(category1)

            sortedOrder.forEachIndexed { index, sortorder ->
                val carousel: Carousel? =
                    mHomePageDataModel.carousel?.singleOrNull { it.id == sortorder.layout_id }

                when (carousel?.type) {
                    "product" -> {

                        if (carousel.id == "flashDeals") {
                            carousel.label = "\uD83D\uDD25" + carousel.label
//                            addFlashDealProduct(carousel)
                            addProductCarousel(carousel, "FlashDeals")
                        } else {
                            var eventType = "Product"

                            val idInInt: Int? = carousel.id?.toIntOrNull()
                            idInInt?.let {
                                eventType = "${carousel.label}"
                            } ?: run {
                                eventType = "${carousel.id}"
                            }



                            addProductCarousel(carousel, eventType)
//                            carousel.type?.let {
//                                Log.d(TAG, "logHomeEvent: asdasdasdd${it}")
//                                addProductCarousel(carousel, it)
//                            } ?: run {
//                                // If id is null.
//                                addProductCarousel(carousel, "unknown")
//                            }
                        }

                    }
                    "image" -> {
                        var eventType = "Product"

                        val idInInt: Int? = carousel.id?.toIntOrNull()
                        idInInt?.let {
                            eventType = "${carousel.label}"
                        } ?: run {
                            eventType = "${carousel.id}"
                        }

                        addImageCarousel(carousel, eventType)
                    }
                    "banner" -> {
//top banner with search

                        setupOfferBannerRv(carousel, "Banner")
                    }
                    "category" -> {
                        setupFeaturesCategoriesOtherRv(carousel, "Category")
                    }
                    "brandlist" -> {
//brandlist
                        setupFeaturesCategoriesRv(carousel, "BrandTable")
                    }
                    "bigbannerfirst" -> {
                        setUpBigBanner(carousel, "BigBannerFirst")
                    }
                    "bigbannersecond" -> {
                        setUpBigBanner(carousel, "BigBannerSecond")
                    }
                    "auctionproductlist" -> {
//                        carousel.label = "\uD83D\uDD25" + carousel.label
                        carousel.titleIconId = R.drawable.ic_law
                        addAuctionProduct(carousel, "AuctionProductList")
                    }
                    "singlebanner" -> {
                        setUpSingleBanner(carousel, "SingleBanner")
                    }
                }
            }
        } else {
            val bannerImage = Carousel()
            bannerImage.id = "bannerimage"
            bannerImage.type = "banner"
            bannerImage.banners = mHomePageDataModel.bannerImages
// setupOfferBannerRv(bannerImage)

            val category = Carousel()
            category.id = "featuredcategories"
            category.type = "category"
// category.featuredCategories = mHomePageDataModel.featuredCategories
            category.brandlist = mHomePageDataModel.brandlist


            val auctionproductlist = Carousel()
            auctionproductlist.id = "auctionproductlist"
            auctionproductlist.type = "product"
//            auctionproductlist.label = "\uD83D\uDD25" + auctionproductlist.label

            auctionproductlist.titleIconId = R.drawable.ic_law
            auctionproductlist.auctionproductlist = mHomePageDataModel.auctionProductList
            mHomePageDataModel.carousel?.add(auctionproductlist)

            if (!mHomePageDataModel.carousel.isNullOrEmpty()) {
                mHomePageDataModel.carousel?.forEachIndexed { index, carousel ->
// Handler().postDelayed({

                    when (carousel.type) {
                        "product" -> {
                            addProductCarousel(carousel, "product")
                        }
                        "image" -> {
                            addImageCarousel(carousel, "image")
                        }
                    }
// }, (index * 200).toLong())
                }
            }
        }
    }

    fun getSortedCarouselsData(sortOrder: ArrayList<SortOrder>?): List<SortOrder> {
        val sortedOrder = ArrayList<SortOrder>()
        sortOrder?.forEach { indexJ ->
            if (indexJ.positionArray?.size == 1) {
                sortedOrder.add(indexJ)
            } else {
                indexJ.positionArray?.forEach {
                    it
                    val sortData = SortOrder()
                    sortData.position = it
                    sortData.layout_id = indexJ.layout_id
                    sortData.layout_id = indexJ.layout_id
                    sortData.type = indexJ.type
                    sortedOrder.add(sortData)
                }
            }
        }
        return sortedOrder.sortedWith(compareBy { it.position!!.replace(",", "").toInt() })
    }

    private fun addProductCarousel(carousel: Carousel, eventName: String) {
        activity?.runOnUiThread {
            loadCarouselFirstLayout(carousel, eventName)
        }
    }

    private fun addFlashDealProduct(carousel: Carousel) {
        val productCarouselFirstLayoutBinding =
            DataBindingUtil.inflate<ProductCarouselFirstLayoutBinding>(
                layoutInflater,
                R.layout.product_carousel_first_layout,
                mContentViewBinding.carouselsLayout,
                false
            )
        productCarouselFirstLayoutBinding.data = carousel
        Log.d("asasas", carousel.label.toString())
        productCarouselFirstLayoutBinding.handler =
            HomePageProductCarouselHandler(activity as BaseActivity)
        if (carousel.timerTimeStamp != 0)
            startTimer(carousel.timerTimeStamp * 1000)
        productCarouselFirstLayoutBinding.productsCarouselRv.layoutManager = LinearLayoutManager(
            mContext!!,
            LinearLayoutManager.HORIZONTAL,
            false
        )//GridLayoutManager(context!!,4)

        productCarouselFirstLayoutBinding.productsCarouselRv.adapter =
            ProductFlashDealRvAdapter(mContext!!, carousel.productList!!)
//        productCarouselFirstLayoutBinding.productsCarouselRv.addItemDecoration(HorizontalMarginItemDecoration(resources.getDimension(R.dimen.spacing_tiny).toInt()))
        productCarouselFirstLayoutBinding.productsCarouselRv.isNestedScrollingEnabled = false
        mContentViewBinding.carouselsLayout.addView(productCarouselFirstLayoutBinding.root)
    }


    private fun loadCarouselFirstLayout(carousel: Carousel, eventName: String) {
        val productCarouselFirstLayoutBinding =
            DataBindingUtil.inflate<ProductCarouselFirstLayoutBinding>(
                layoutInflater,
                R.layout.product_carousel_first_layout,
                mContentViewBinding.carouselsLayout,
                false
            )
        productCarouselFirstLayoutBinding.data = carousel
        productCarouselFirstLayoutBinding.handler =
            HomePageProductCarouselHandler(activity as BaseActivity)
        if (carousel.timerTimeStamp != 0)
            startTimer(carousel.timerTimeStamp * 1000)
        var layoutManager = LinearLayoutManager(mContext!!, LinearLayoutManager.HORIZONTAL, false)
        if (carousel.productList!!.size >= 3) {
            layoutManager.scrollToPositionWithOffset(
                1,
                Resources.getSystem().displayMetrics.widthPixels / 4
            )
        }
//        productCarouselFirstLayoutBinding.productsCarouselRv.layoutManager = LinearLayoutManager(mContext!!, LinearLayoutManager.HORIZONTAL, false)//GridLayoutManager(mContext!!,4)
        productCarouselFirstLayoutBinding.productsCarouselRv.layoutManager =
            layoutManager //GridLayoutManager(mContext!!,4)

        productCarouselFirstLayoutBinding.productsCarouselRv.adapter =
            ProductCarouselHorizontalRvAdapter(mContext!!, carousel.productList!!, eventName)
        productCarouselFirstLayoutBinding.productsCarouselRv.addItemDecoration(
            HorizontalMarginItemDecoration(resources.getDimension(R.dimen.spacing_tiny).toInt())
        )
        productCarouselFirstLayoutBinding.productsCarouselRv.isNestedScrollingEnabled = false
        mContentViewBinding.carouselsLayout.addView(productCarouselFirstLayoutBinding.root)
    }

    private fun startTimer(finishMinutes: Int) {
        if (mResendTimer != null) {
            mResendTimer?.cancel()
        }
        mResendTimer = object : CountDownTimer((finishMinutes).toLong(), 1000) {
            override fun onTick(l: Long) {
                val days = l / (24 * 60 * 60 * 1000) % 60
                val hours = l / (60 * 1000 * 60) % 60
                val minutes = l / (60 * 1000) % 60
                val seconds = l / 1000 % 60


                if (days > 0) {
                    mContentViewBinding.carouselsLayout.day_tv.visibility = View.VISIBLE
                    mContentViewBinding.carouselsLayout.day_tv.text =
                        String.format("%02d", days) + " d"
                } else {
                    mContentViewBinding.carouselsLayout.day_tv.visibility = View.GONE

                }
                if (hours > 0) {
                    mContentViewBinding.carouselsLayout.hour_tv.visibility = View.VISIBLE
                    mContentViewBinding.carouselsLayout.hour_tv.text =
                        String.format("%02d", hours) + " h"
                } else {
                    mContentViewBinding.carouselsLayout.hour_tv.visibility = View.GONE
                }
                if (minutes > 0) {
                    mContentViewBinding.carouselsLayout.minute_tv.visibility = View.VISIBLE
                    mContentViewBinding.carouselsLayout.minute_tv.text =
                        String.format("%02d", minutes) + " m"
                } else {
                    mContentViewBinding.carouselsLayout.minute_tv.visibility = View.GONE

                }
                if (seconds > 0) {
                    mContentViewBinding.carouselsLayout.seconds_tv.visibility = View.VISIBLE
                    mContentViewBinding.carouselsLayout.seconds_tv.text =
                        String.format("%02d", seconds) + " s"
                } else {
                    mContentViewBinding.carouselsLayout.seconds_tv.visibility = View.GONE

                }
            }

            override fun onFinish() {
//                mContentViewBinding.carouselsLayout.day_tv.setText("")//.setResend("")
                cancel()
            }
        }.start()
    }


    private fun addAuctionProduct(carousel: Carousel, eventName: String) {
        if (carousel.productList!!.isNotEmpty()) {
            val productCarouselFirstLayoutBinding =
                DataBindingUtil.inflate<AuctionProductCarouselLayoutBinding>(
                    layoutInflater,
                    R.layout.auction_product_carousel_layout,
                    mContentViewBinding.carouselsLayout,
                    false
                )
            carousel.titleIconId?.let {
                productCarouselFirstLayoutBinding.carouselLabel.setCompoundDrawablesWithIntrinsicBounds(
                    it, 0, 0, 0
                )
            }
            productCarouselFirstLayoutBinding.data = carousel
            productCarouselFirstLayoutBinding.handler =
                HomePageProductCarouselHandler(activity as BaseActivity)
            if (carousel.productList!!.size >= 3) {
                var layoutManager =
                    LinearLayoutManager(mContext!!, LinearLayoutManager.HORIZONTAL, false)
                layoutManager.scrollToPositionWithOffset(
                    1,
                    Resources.getSystem().displayMetrics.widthPixels / 4
                )
                productCarouselFirstLayoutBinding.productsCarouselRv.layoutManager = layoutManager
            }
            if (carousel.productList!!.size > 4)
                productCarouselFirstLayoutBinding.productsCarouselRv.layoutManager =
                    GridLayoutManager(mContext!!, 4)
            productCarouselFirstLayoutBinding.productsCarouselRv.adapter =
                ProductAuctionHorizontalRvAdapter(mContext!!, carousel.productList!!, eventName)
            productCarouselFirstLayoutBinding.productsCarouselRv.addItemDecoration(
                HorizontalMarginItemDecoration(resources.getDimension(R.dimen.spacing_tiny).toInt())
            )
            productCarouselFirstLayoutBinding.productsCarouselRv.isNestedScrollingEnabled = false
            mContentViewBinding.carouselsLayout.addView(productCarouselFirstLayoutBinding.root)
        }
    }

    private fun addImageCarousel(carousel: Carousel, eventName: String) {
        val imageCarouselLayoutBinding = DataBindingUtil.inflate<ImageCarouselLayoutBinding>(
            layoutInflater,
            R.layout.image_carousel_layout,
            mContentViewBinding.carouselsLayout,
            false
        )
        imageCarouselLayoutBinding.data = carousel
        carousel.banners!!.forEach {
            if(it.promotion_type=="yes"){
                imageCarouselLayoutBinding.carouselBannerViewPager.layoutManager = GridLayoutManager(mContext,2)
                imageCarouselLayoutBinding.carouselBannerViewPager.addItemDecoration(GridSpacingItemDecoration(2,1,false))
                imageCarouselLayoutBinding.carouselBannerViewPager.adapter = HomePageVerticalBannerAdapter(mContext!!,this, carousel.banners!!, eventName,ConstantsHelper.VIEW_TYPE_GRID)
            }else{
                imageCarouselLayoutBinding.carouselBannerViewPager.layoutManager = LinearLayoutManager(mContext)
                imageCarouselLayoutBinding.carouselBannerViewPager.adapter = HomePageVerticalBannerAdapter(mContext!!,this, carousel.banners!!, eventName,ConstantsHelper.VIEW_TYPE_LIST)
            }
        }

        //imageCarouselLayoutBinding.carouselBannerViewPager.offscreenPageLimit = 2
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
            imageCarouselLayoutBinding.carouselBannerViewPager.pageMargin =
                resources.displayMetrics.widthPixels / -16
        if (AppSharedPref.getStoreCode(context!!) == "ar")
            imageCarouselLayoutBinding.carouselBannerViewPager.rotationY = 180f
        if (ApplicationConstants.ENABLE_HOME_BANNER_AUTO_SCROLLING) {
            try {
                val imageCarouselSwitcherTimer = Timer()
                imageCarouselSwitcherTimer.scheduleAtFixedRate(
                    BannerSwitchTimerTask(
                        imageCarouselLayoutBinding.carouselBannerViewPager,
                        carousel.banners!!.size
                    ),
                    ((mBannerSwitcherTimerList.size % 3) * 1000).toLong(),
                    ApplicationConstants.DEFAULT_TIME_TO_SWITCH_BANNER_IN_MILLIS.toLong()
                )
                mBannerSwitcherTimerList.add(imageCarouselSwitcherTimer)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }*/

        mContentViewBinding.carouselsLayout.addView(imageCarouselLayoutBinding.root)
    }

    private fun setupFeaturesCategoriesRv(carousel: Carousel, eventName: String) {
        if (carousel.brandlist != null) {
            val categoryCarouselLayoutBinding =
                DataBindingUtil.inflate<CategoryCarouselLayoutBinding>(
                    layoutInflater,
                    R.layout.category_carousel_layout,
                    mContentViewBinding.carouselsLayout,
                    false
                )
            categoryCarouselLayoutBinding.carouselLabel.visibility = View.VISIBLE
            categoryCarouselLayoutBinding.carouselLabel.text = mContext!!.resources.getString(R.string.more_brands)
            categoryCarouselLayoutBinding.data = carousel
            categoryCarouselLayoutBinding.themeType = mHomePageDataModel.themeType
            categoryCarouselLayoutBinding.featuredCategoriesRv.addItemDecoration(
                HorizontalMarginItemDecoration(
                    resources.getDimension(R.dimen.spacing_generic1).toInt()
                )
            )

            categoryCarouselLayoutBinding.featuredCategoriesRv.layoutManager =
                LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
            categoryCarouselLayoutBinding.featuredCategoriesRv.adapter =
                BrandListRvAdapter(
                    this,
                    carousel.brandlist!!,
                    ConstantsHelper.VIEW_TYPE_GRID,
                    eventName
                )

//            categoryCarouselLayoutBinding.featuredCategoriesRv.adapter = FeaturedCategoriesRvAdapter(this, carousel.featuredCategories!!, ConstantsHelper.VIEW_TYPE_LIST)
//            categoryCarouselLayoutBinding.featuredCategoriesRv.adapter = BrandListRvAdapter(this, carousel.brandlist!!, ConstantsHelper.VIEW_TYPE_LIST)
//            categoryCarouselLayoutBinding.featuredCategoriesRv.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

            mContentViewBinding.carouselsLayout.addView(categoryCarouselLayoutBinding.root)
        }
    }

    private fun setupFeaturesCategoriesOtherRv(carousel: Carousel, eventName: String) {
        if (!carousel.featuredCategories.isNullOrEmpty()) {
            val categoryCarouselLayoutBinding =
                DataBindingUtil.inflate<CategoryCarouselLayoutBinding>(
                    layoutInflater,
                    R.layout.category_carousel_layout,
                    mContentViewBinding.carouselsLayout,
                    false
                )
            categoryCarouselLayoutBinding.data = carousel
            categoryCarouselLayoutBinding.carouselLabel.visibility = View.VISIBLE
            categoryCarouselLayoutBinding.carouselLabel.text = getString(R.string.hot_categories)
            categoryCarouselLayoutBinding.themeType = mHomePageDataModel.themeType
            categoryCarouselLayoutBinding.featuredCategoriesRv.addItemDecoration(
                HorizontalMarginItemDecoration(
                    resources.getDimension(R.dimen.spacing_generic).toInt()
                )
            )

            if (mHomePageDataModel.themeType == 1) {
                categoryCarouselLayoutBinding.featuredCategoriesRv.adapter =
                    carousel.featuredCategories?.let {
                        // TODO: 24/05/21 FIX ME
                        val tmp = it//.take(3) as ArrayList
                        /*categoryCarouselLayoutBinding.featuredCategoriesRv.layoutManager =
                            LinearLayoutManager(
                                context,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )*/
                        GridLayoutManager(mContext,4,LinearLayoutManager.HORIZONTAL,false)
                        HomeFeaturedCategoriesRvAdapter(
                            this,
                            tmp,
                            ConstantsHelper.VIEW_TYPE_GRID,
                            eventName
                        )
                    }
            } else {
                categoryCarouselLayoutBinding.featuredCategoriesRv.adapter =
                    carousel.featuredCategories?.let {
                        FeaturedCategoriesOtherRvAdapter(
                            this,
                            it,
                            ConstantsHelper.VIEW_TYPE_LIST,
                            eventName
                        )
                    }
                categoryCarouselLayoutBinding.featuredCategoriesRv.layoutManager =
                    LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
            }
            mContentViewBinding.carouselsLayout.addView(categoryCarouselLayoutBinding.root)
        }
    }


    private fun setUpBigBanner(carousel: Carousel, eventName: String) {
        if (carousel.id != null) {
            val categoryCarouselLayoutBinding = DataBindingUtil.inflate<ItemBigBanner1Binding>(
                layoutInflater,
                R.layout.item_big_banner1,
                mContentViewBinding.carouselsLayout,
                false
            )
            categoryCarouselLayoutBinding.data = carousel
            categoryCarouselLayoutBinding.handler = BigBannerHandler(this)
            categoryCarouselLayoutBinding.bigBanner1Rv.layoutManager = GridLayoutManager(mContext, 2)
            categoryCarouselLayoutBinding.bigBanner1Rv.adapter =
                BigBannerRvAdapter(mContext!!, carousel.productList!!, eventName)
//            productCarouselFirstLayoutBinding.productsCarouselRv.adapter = ProductCarouselHorizontalRvAdapter(mContext!!, carousel.productList!!)
            mContentViewBinding.carouselsLayout.addView(categoryCarouselLayoutBinding.root)
        }
    }

    fun setUpSingleBanner(carousel: Carousel, eventName: String) {
        val bannerCarouselLayoutBinding =
            DataBindingUtil.inflate<SingleBannerCarouselLayoutBinding>(
                layoutInflater,
                R.layout.single_banner_carousel_layout,
                mContentViewBinding.carouselsLayout,
                false
            )
        bannerCarouselLayoutBinding.data = carousel
        bannerCarouselLayoutBinding?.analysisData = AnalysisModel(eventName, carousel.categoryId)
        bannerCarouselLayoutBinding.handler = HomePageBannerVpHandler(this)
        mContentViewBinding.carouselsLayout.addView(bannerCarouselLayoutBinding.root)


    }
/*
    private fun setUpBigBanner2(carousel: Carousel) {
        if (carousel.id != null) {


            val categoryCarouselLayoutBinding = DataBindingUtil.inflate<ItemBigBanner1Binding>(layoutInflater, R.layout.item_big_banner1, mContentViewBinding.carouselsLayout, false)
            categoryCarouselLayoutBinding.data = carousel


            categoryCarouselLayoutBinding.bigBanner1Rv.layoutManager = GridLayoutManager(mContext, 2)

//            categoryCarouselLayoutBinding.bigBanner1Rv.adapter = BigBannerRvAdapter(this, carousel.brandlist!!, ConstantsHelper.VIEW_TYPE_GRID)


            mContentViewBinding.carouselsLayout.addView(categoryCarouselLayoutBinding.root)
        }
    }
*/


    private fun callTopApi() {
        isLoadingTopSellingProducts = true
//        mContentViewBinding.progressBar = true
        ApiConnection.getTopSellingProducts(mContext!!, mPageNumber)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiCustomCallback<HomePageDataModel?>(mContext!!, false) {
                override fun onNext(homePageDataModel: HomePageDataModel?) {
                    super.onNext(homePageDataModel)
//                        mContentViewBinding.progressBar = false
                    if(homePageDataModel!=null){
                        if (homePageDataModel.success) {
                            onSuccessfulTopSellingResponse(homePageDataModel)
//                            onSuccessfulResponse(homePageDataModel)
                        } else {
                            mContentViewBinding.progressBar = false
//                            onFailureResponse(homePageDataModel)
                        }
                    }

                    isLoadingTopSellingProducts = false
                }

                override fun onError(e: Throwable) {
                    super.onError(e)

                    mContentViewBinding.progressBar = false
                    mContentViewBinding.loading = false
                    onErrorResponse(e)
                    isLoadingTopSellingProducts = false
                }
            })


    }

    var count = 0
    private fun onSuccessfulTopSellingResponse(homePageDataModel: HomePageDataModel) {
        if (homePageDataModel != null && mContext != null && isAdded) {
            val categoryCarouselLayoutBinding = DataBindingUtil.inflate<ItemHomeTopProductBinding>(
                layoutInflater,
                R.layout.item_home_top_product,
                mContentViewBinding.carouselsLayout,
                false
            )
            categoryCarouselLayoutBinding.data = homePageDataModel
            mContentViewBinding.progressBar = false
            categoryCarouselLayoutBinding.handler = FeaturedCategoriesRvHandler(this)
            categoryCarouselLayoutBinding.topProductsRv.layoutManager =
                GridLayoutManager(mContext, 2)
            categoryCarouselLayoutBinding.topProductsRv.adapter =
                TopSellingProductRvAdapter(this, homePageDataModel.topSellingProducts)
            mContentViewBinding.carouselsLayout.addView(categoryCarouselLayoutBinding.root)

            if (mPageNumber > 1 || categoryCarouselLayoutBinding.data!!.topSellingProducts!!.size < 0) {
                categoryCarouselLayoutBinding.topSellingTv.visibility = View.GONE
            }
//            categoryCarouselLayoutBinding.topProductsRv.adapter = mContentViewBinding.data!!.topSellingProducts?.let { TopSellingProductRvAdapter(this, it) }

            categoryCarouselLayoutBinding.topProductsRv.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (!isLoadingTopSellingProducts) { //&& mPageNumber <= 2
                        val lastCompletelyVisibleItemPosition =
                            (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                        count += categoryCarouselLayoutBinding.data!!.topSellingProducts!!.size
                        if (count < homePageDataModel.totalCount
                            && lastCompletelyVisibleItemPosition > categoryCarouselLayoutBinding.data!!.topSellingProducts!!.size - 4
                        ) {
                            mContentViewBinding.progressBar = true
                            ++mPageNumber
                            callTopApi()

                        }
                    }

                }
            })
        }
    }

    private fun setupOfferBannerRv(carousel: Carousel, eventName: String) {
        if (carousel.banners!!.isNotEmpty()) {
            bannerCarouselLayoutBinding.data = carousel
            bannerCarouselLayoutBinding.themeType = mHomePageDataModel.themeType

            if (mHomePageDataModel.themeType == 1) {
               /* bannerCarouselLayoutBinding.carouselBannerViewPager.pageMargin = -50
                bannerCarouselLayoutBinding.carouselBannerViewPager.isHorizontalFadingEdgeEnabled =
                    true
                bannerCarouselLayoutBinding.carouselBannerViewPager.setFadingEdgeLength(30)*/
                bannerCarouselLayoutBinding.carouselBannerViewPager.adapter = HomePageTopBannerVpAdapter(this, carousel.banners, eventName)
                if (AppSharedPref.getStoreCode(mContext!!) == "ar")
                    bannerCarouselLayoutBinding.carouselBannerViewPager.rotationY = 180f

                if (ApplicationConstants.ENABLE_HOME_BANNER_AUTO_SCROLLING) {
                    try {
                        val imageCarouselSwitcherTimer = Timer()
                        imageCarouselSwitcherTimer.scheduleAtFixedRate(
                            BannerSwitchTimerTask(
                                bannerCarouselLayoutBinding.carouselBannerViewPager,
                                mHomePageDataModel.bannerImages!!.size
                            ),
                            ((mBannerSwitcherTimerList.size % 3) * 1000).toLong(),
                            ApplicationConstants.DEFAULT_TIME_TO_SWITCH_BANNER_IN_MILLIS.toLong()
                        )
                        mBannerSwitcherTimerList.add(imageCarouselSwitcherTimer)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                bannerCarouselLayoutBinding.bannerDotsTabLayout.setupWithViewPager(
                    bannerCarouselLayoutBinding.carouselBannerViewPager,
                    true
                )

            } else {
                if (bannerCarouselLayoutBinding.offerBannersRv.adapter == null) {
                    bannerCarouselLayoutBinding.offerBannersRv.isNestedScrollingEnabled = false
                }
                bannerCarouselLayoutBinding.offerBannersRv.adapter =
                    OfferBannersRvAdapter(this, carousel.banners!!, eventName)
            }
            mContentViewBinding.carouselsLayout.addView(bannerCarouselLayoutBinding.root)

        }
    }


    private inner class BannerSwitchTimerTask(
        private val mViewPager: ViewPager,
        private val mBannerSize: Int
    ) : TimerTask() {

        var firstTime = true

        override fun run() {
            try {
                activity?.runOnUiThread {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                ConstantsHelper.RC_LOGIN -> {
                    callApi()
                }
            }
        }
    }

    fun showBadge(count: Int) {
        if (count == 0) {
            mContentViewBinding.toolbar.bellNotificationBadge.visibility = View.GONE
            mContentViewBinding.toolbar.bellNotificationBadge.text = "0"
        } else {
            mContentViewBinding.toolbar.bellNotificationBadge.visibility = View.VISIBLE
            mContentViewBinding.toolbar.bellNotificationBadge.text = count.toString()
        }
    }

    fun gotToTop() {
        if (mContentViewBinding != null) {
            mContentViewBinding.mainScroller.fullScroll(View.FOCUS_UP)
        }


    }

}