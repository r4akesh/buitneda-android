package com.webkul.mobikul.fragments

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
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
import com.webkul.mobikul.view_model.ViewModel
import io.github.inflationx.calligraphy3.CalligraphyTypefaceSpan
import io.github.inflationx.calligraphy3.TypefaceUtils
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.product_carousel_first_layout.view.*
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
    private var viewModel: ViewModel? = null
    private var hashIdentifier = ""
//    private var currentCache = 0

    companion object {
        var instanceOf: Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instanceOf++
        Log.d(TAG, "onCreate: $instanceOf")
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
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)
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
        initSwipeRefresh()
        initHomePageData()
        initSupportActionBar()
    }

    private fun initSupportActionBar() {
        (activity as AppCompatActivity).setSupportActionBar(mContentViewBinding.toolbar)
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
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_whatsapp)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(@NonNull item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setPackage("com.whatsapp")
                intent.data =
                    Uri.parse("https://api.whatsapp.com/send?phone=${context!!.getString(R.string.whats_app_number)}")
                if (context!!.packageManager.resolveActivity(intent, 0) != null) {
                    startActivity(intent)
                } else {
                    ToastHelper.showToast(
                        context!!,
                        context!!.getString(R.string.please_install_whatsapp)
                    )
                    val url = "https://play.google.com/store/search?q=whatsapp&c=apps"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
//                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initSwipeRefresh() {
        mContentViewBinding.swipeRefreshLayout.setDistanceToTriggerSync(300)
        mContentViewBinding.swipeRefreshLayout.setOnRefreshListener {
            if (NetworkHelper.isNetworkAvailable(context!!)) {
//                callApiBackground()
                Thread {
                    // do background stuff here
//                    currentCache++
//
//                    if (currentCache > 3) {
//                        currentCache = 1
//                    }
//                    Log.d(TAG, "initSwipeRefresh: $currentCache")
                    loadDataFromDB()
                }.start()

            } else {
                mContentViewBinding.swipeRefreshLayout.isRefreshing = false
                ToastHelper.showToast(context!!, getString(R.string.you_are_offline))
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
            activity?.runOnUiThread {
                (
                        onSuccessfulResponse(HomeDataSingleton.getInstance().mHomePageDataModel!!)
                        )
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
                    (context as BaseActivity).mCompositeDisposable.add(disposable)
                }

                override fun onComplete() {

                }
            })
    }

    private fun updateAnimationCheckAndProceed(response: String) {
        (context as HomeActivity).runOnUiThread {
            if (response != "") {
                (context as HomeActivity).mHomePageDataModel =
                    BaseActivity.mGson.fromJson(response, HomePageDataModel::class.java)
                Log.d(TAG, "updateAnimationCheckAndProceed: $response")

                onSuccessfulResponse((context as HomeActivity).mHomePageDataModel)
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
            context!!,
            BaseActivity.mDataBaseHandler.getETagFromDatabase(hashIdentifier),
            false,
            ""
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiCustomCallback<HomePageDataModel>(context!!, false) {
                override fun onNext(homePageDataModel: HomePageDataModel) {
                    super.onNext(homePageDataModel, hashIdentifier)
                    mContentViewBinding.swipeRefreshLayout.isRefreshing = false
                    if (homePageDataModel.success) {
                        (context as HomeActivity).mHomePageDataModel = homePageDataModel
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
        if (context == null) {
            return
        }
//        Log.d(TAG, "callApi: refreshCount $refreshCount")
//        val hash = returnHash(if ("$refreshCount" == "0") "" else "$refreshCount")
        val hash = returnHash("")
        Log.d(TAG, "callApi: hash $hash")
        ApiConnection.getHomePageData(
            context!!,
            BaseActivity.mDataBaseHandler.getETagFromDatabase(hash),
            false,
            ""
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiCustomCallback<HomePageDataModel>(context!!, false) {
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
            "homePageData" + AppSharedPref.getWebsiteId(context!!) + AppSharedPref.getStoreId(
                context!!
            ) + AppSharedPref.getCustomerToken(context!!) + AppSharedPref.getQuoteId(context!!) + AppSharedPref.getCurrencyCode(
                context!!
            ) + refreshCount
        )

        Log.d(
            TAG,
            "cacheData: " + "homePageData" + ":" + AppSharedPref.getWebsiteId(context!!) + ":" + AppSharedPref.getStoreId(
                context!!
            ) + ":" + AppSharedPref.getCustomerToken(context!!) + ":" + AppSharedPref.getQuoteId(
                context!!
            ) + ":" + AppSharedPref.getCurrencyCode(
                context!!
            ) + ":" + refreshCount
        )
    }

    private fun returnHash(refreshCount: String): String {
        Log.d(
            TAG,
            "cacheData: API " + "homePageData" + ":" + AppSharedPref.getWebsiteId(context!!) + ":" + AppSharedPref.getStoreId(
                context!!
            ) + ":" + AppSharedPref.getCustomerToken(context!!) + ":" + AppSharedPref.getQuoteId(
                context!!
            ) + ":" + AppSharedPref.getCurrencyCode(
                context!!
            ) + ":" + refreshCount
        )
        return Utils.getMd5String(
            "homePageData" + AppSharedPref.getWebsiteId(context!!) + AppSharedPref.getStoreId(
                context!!
            ) + AppSharedPref.getCustomerToken(context!!) + AppSharedPref.getQuoteId(context!!) + AppSharedPref.getCurrencyCode(
                context!!
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
            mHomePageDataModel =
                BaseActivity.mGson.fromJson(response, HomePageDataModel::class.java)
            initLayout()
        }
    }


    private fun onSuccessfulResponse(homePageDataModel: HomePageDataModel) {
        Log.d(TAG, "onSuccessfulResponse: ")
        mContentViewBinding.loading = false
        mHomePageDataModel = homePageDataModel
        if (homePageDataModel != null && context != null) {

            setAppSharedPrefConfigDetails()
            initLayout()
            (activity as HomeActivity).updateCartCount(AppSharedPref.getCartCount(context!!))
//            AppSharedPref.setCartCount(context!!,mContentViewBinding.data!!.cartCount)
            if ((activity as HomeActivity).mTopSellingHomePageDataModel == null)
                TopCategoriesListRv()
            else
                onSuccessfulTopSellingResponse((activity as HomeActivity).mTopSellingHomePageDataModel!!)
        }
    }


    private fun onSuccessfulResponse(homePageDataModel: HomePageDataModel, status: Boolean) {


        mContentViewBinding.loading = false
        mHomePageDataModel = homePageDataModel
        if (homePageDataModel != null && context != null) {
//            (context as HomeActivity).mHomePageDataModel = homePageDataModel
            /* activity?.runOnUiThread({
                 setAppSharedPrefConfigDetails()
             })

             activity?.runOnUiThread({
                 initLayout()
             })

             activity?.runOnUiThread({
                 (activity as HomeActivity).updateCartCount(AppSharedPref.getCartCount(context!!))
             })*/

            activity?.runOnUiThread {
                setAppSharedPrefConfigDetails()
                initLayout()
                (activity as HomeActivity).updateCartCount(AppSharedPref.getCartCount(context!!))
            }


//            AppSharedPref.setCartCount(context!!,mContentViewBinding.data!!.cartCount)
            if ((activity as HomeActivity).mTopSellingHomePageDataModel == null)
                TopCategoriesListRv()
            else
                onSuccessfulTopSellingResponse((activity as HomeActivity).mTopSellingHomePageDataModel!!)
        }
    }

    private fun setAppSharedPrefConfigDetails() {
        AppSharedPref.setIsWishlistEnabled(context!!, mHomePageDataModel.wishlistEnable)

        val customerDataSharedPref =
            AppSharedPref.getSharedPreferenceEditor(context!!, AppSharedPref.CUSTOMER_PREF)
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

        AppSharedPref.setCategoryData(context!!, mHomePageDataModel.categories!!)
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
                        Utils.logoutAndGoToHome(context!!)
                    }, "", null
                )
            }
            else -> {
                ToastHelper.showToast(context!!, response.message)
            }
        }
    }


    private fun onErrorResponse(error: Throwable) {
        if ((!NetworkHelper.isNetworkAvailable(context!!) || (error is HttpException && error.code() == 304)) && mContentViewBinding.data != null) {
            // Do Nothing as the data is already loaded
        } else {
            AlertDialogHelper.showNewCustomDialog(
                activity as BaseActivity,
                getString(R.string.error),
                NetworkHelper.getErrorMessage(context, error),
                false,
                getString(R.string.try_again),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    mContentViewBinding.swipeRefreshLayout.isRefreshing = true
                    callApi()
                },
                getString(R.string.dismiss),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
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
                            addProductCarousel(carousel, "flashDeals")
                        } else {
                            addProductCarousel(carousel, "${carousel.type}-${carousel.id}")
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
                        addImageCarousel(carousel, "image")
                    }
                    "banner" -> {
//top banner with search

                        setupOfferBannerRv(carousel, "banner")
                    }
                    "category" -> {
                        setupFeaturesCategoriesOtherRv(carousel, "category")
                    }
                    "brandlist" -> {
//brandlist
                        setupFeaturesCategoriesRv(carousel, "brandlist")
                    }
                    "bigbannerfirst" -> {
                        setUpBigBanner(carousel, "bigbannerfirst")
                    }
                    "bigbannersecond" -> {
                        setUpBigBanner(carousel, "bigbannersecond")
                    }
                    "auctionproductlist" -> {
//                        carousel.label = "\uD83D\uDD25" + carousel.label
                        carousel.titleIconId = R.drawable.ic_law
                        addAuctionProduct(carousel, "auctionproductlist")
                    }
                    "singlebanner" -> {
                        setUpSingleBanner(carousel, "singlebanner")
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
            context!!,
            LinearLayoutManager.HORIZONTAL,
            false
        )//GridLayoutManager(context!!,4)

        productCarouselFirstLayoutBinding.productsCarouselRv.adapter =
            ProductFlashDealRvAdapter(context!!, carousel.productList!!)
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
        var layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
        if (carousel.productList!!.size >= 3) {
            layoutManager.scrollToPositionWithOffset(
                1,
                Resources.getSystem().displayMetrics.widthPixels / 4
            )
        }
//        productCarouselFirstLayoutBinding.productsCarouselRv.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)//GridLayoutManager(context!!,4)
        productCarouselFirstLayoutBinding.productsCarouselRv.layoutManager =
            layoutManager //GridLayoutManager(context!!,4)

        productCarouselFirstLayoutBinding.productsCarouselRv.adapter =
            ProductCarouselHorizontalRvAdapter(context!!, carousel.productList!!, eventName)
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
                DataBindingUtil.inflate<ProductCarouselFirstLayoutBinding>(
                    layoutInflater,
                    R.layout.product_carousel_first_layout,
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
                    LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
                layoutManager.scrollToPositionWithOffset(
                    1,
                    Resources.getSystem().displayMetrics.widthPixels / 4
                )
                productCarouselFirstLayoutBinding.productsCarouselRv.layoutManager = layoutManager
            }
            if (carousel.productList!!.size > 4)
                productCarouselFirstLayoutBinding.productsCarouselRv.layoutManager =
                    GridLayoutManager(context!!, 4)
            productCarouselFirstLayoutBinding.productsCarouselRv.adapter =
                ProductCarouselHorizontalRvAdapter(context!!, carousel.productList!!, eventName)
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
        imageCarouselLayoutBinding.carouselBannerViewPager.adapter =
            HomePageBannerVpAdapter(this, carousel.banners!!, eventName)
        imageCarouselLayoutBinding.carouselBannerViewPager.offscreenPageLimit = 2
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
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
        }

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
            categoryCarouselLayoutBinding.data = carousel
            categoryCarouselLayoutBinding.themeType = mHomePageDataModel.themeType
            categoryCarouselLayoutBinding.featuredCategoriesRv.addItemDecoration(
                HorizontalMarginItemDecoration(
                    resources.getDimension(R.dimen.spacing_generic1).toInt()
                )
            )

            categoryCarouselLayoutBinding.featuredCategoriesRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
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
                        categoryCarouselLayoutBinding.featuredCategoriesRv.layoutManager =
                            GridLayoutManager(
                                context,
                                if (tmp.size >= 5) 2 else 1,
                                GridLayoutManager.HORIZONTAL,
                                false
                            )
                        FeaturedCategoriesOtherRvAdapter(
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
                    LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
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
            categoryCarouselLayoutBinding.bigBanner1Rv.layoutManager = GridLayoutManager(context, 2)
            categoryCarouselLayoutBinding.bigBanner1Rv.adapter =
                BigBannerRvAdapter(context!!, carousel.productList!!, eventName)
//            productCarouselFirstLayoutBinding.productsCarouselRv.adapter = ProductCarouselHorizontalRvAdapter(context!!, carousel.productList!!)
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


            categoryCarouselLayoutBinding.bigBanner1Rv.layoutManager = GridLayoutManager(context, 2)

//            categoryCarouselLayoutBinding.bigBanner1Rv.adapter = BigBannerRvAdapter(this, carousel.brandlist!!, ConstantsHelper.VIEW_TYPE_GRID)


            mContentViewBinding.carouselsLayout.addView(categoryCarouselLayoutBinding.root)
        }
    }
*/

    private fun TopCategoriesListRv() {
        try {
            callTopApi()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        /* mContentViewBinding.topProductsRv.adapter = mContentViewBinding.data!!.topSellingProducts?.let { TopSellingProductRvAdapter(this, it) }
         mContentViewBinding.topProductsRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
             override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                 super.onScrollStateChanged(recyclerView, newState)
                 val lastCompletelyVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                 if (!mContentViewBinding.loading!! && mContentViewBinding.data!!.topSellingProducts!!.size < mContentViewBinding.data!!.topSellingProducts!!.size
                         && lastCompletelyVisibleItemPosition > mContentViewBinding.data!!.topSellingProducts!!.size - 4) {
                     callTopApi()
                 }
             }
         })*/
    }

    private fun callTopApi() {
        isLoadingTopSellingProducts = true
//        mContentViewBinding.progressBar = true
        ApiConnection.getTopSellingProducts(context!!, mPageNumber)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiCustomCallback<HomePageDataModel>(context!!, false) {
                override fun onNext(homePageDataModel: HomePageDataModel) {
                    super.onNext(homePageDataModel)
//                        mContentViewBinding.progressBar = false

                    if (homePageDataModel.success) {
                        (activity as HomeActivity).mTopSellingHomePageDataModel = homePageDataModel
                        onSuccessfulTopSellingResponse(homePageDataModel)
//                            onSuccessfulResponse(homePageDataModel)
                    } else {
                        mContentViewBinding.progressBar = false
//                            onFailureResponse(homePageDataModel)
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
        if (homePageDataModel != null && context != null) {
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
                GridLayoutManager(context, 2)
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
            val bannerCarouselLayoutBinding = DataBindingUtil.inflate<BannerCarouselLayoutBinding>(
                layoutInflater,
                R.layout.banner_carousel_layout,
                mContentViewBinding.carouselsLayout,
                false
            )
            bannerCarouselLayoutBinding.data = carousel
            bannerCarouselLayoutBinding.themeType = mHomePageDataModel.themeType

            if (mHomePageDataModel.themeType == 1) {

                bannerCarouselLayoutBinding.carouselBannerViewPager.adapter =
                    HomePageTopBannerVpAdapter(this, carousel.banners, eventName)
                if (AppSharedPref.getStoreCode(context!!) == "ar")
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
            mContentViewBinding.bellNotificationBadge.visibility = View.GONE
            mContentViewBinding.bellNotificationBadge.text = "0"
        } else {
            mContentViewBinding.bellNotificationBadge.visibility = View.VISIBLE
            mContentViewBinding.bellNotificationBadge.text = count.toString()
        }
    }

    fun gotToTop() {
        mContentViewBinding.mainScroller.fullScroll(View.FOCUS_UP)

    }

}