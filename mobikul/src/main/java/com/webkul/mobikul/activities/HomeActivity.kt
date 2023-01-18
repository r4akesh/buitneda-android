package com.webkul.mobikul.activities


//import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_HOME_PAGE_DATA
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.webkul.mobikul.BuildConfig
import com.webkul.mobikul.R
import com.webkul.mobikul.broadcast_receivers.BottomMenuReceiver
import com.webkul.mobikul.databinding.ActivityHomeBinding
import com.webkul.mobikul.firebase.*
import com.webkul.mobikul.fragments.*
import com.webkul.mobikul.handlers.HomeActivityHandler
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_BOTTOM_NAV_INDEX
import com.webkul.mobikul.interfaces.OnMenuSelectListener
import com.webkul.mobikul.models.extra.NotificationListResponseModel
import com.webkul.mobikul.models.homepage.HomePageDataModel
import com.webkul.mobikul.models.homepage.PromotionBanner
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseActivity(), OnMenuSelectListener {
    private var mPromotionBanner: PromotionBanner? = null
    private lateinit var bottomMenuReceiver: BottomMenuReceiver

    companion object {
        lateinit var mContentViewBinding: ActivityHomeBinding
        const val BROADCAST_DEFAULT_ALBUM_CHANGED = "BROADCAST_DEFAULT_ALBUM_CHANGED"
        const val BROADCAST_DEFAULT_UPDATE_CART_BADGE = "BROADCAST_DEFAULT_UPDATE_CART_BADGE"
        private const val TAG = "HomeActivity"

    }

    open var homeFragment: HomeFragment? = null
    open var categoryFragment: CategoryPageFragment? = null
    open var serviceProviderFragment:ServiceProviderFragment? = null
    open var cartBottomFragment: CartBottomSheetFragment? = null
    open var accountDetailsFragment: AccountDetailsFragment? = null
    open var mHomePageDataModel: HomePageDataModel = HomePageDataModel()
    private var lastIndex = 2
    var fragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadCastReceiver, IntentFilter(BROADCAST_DEFAULT_ALBUM_CHANGED))
        LocalBroadcastManager.getInstance(this).registerReceiver(
            updateCartBadgeReceiver,
            IntentFilter(BROADCAST_DEFAULT_UPDATE_CART_BADGE)
        )



        startInitialization()
        Handler(Looper.myLooper()!!).postDelayed({
            updateCartBadge()
            updateCartCount(AppSharedPref.getCartCount(this))
            if (!MobikulApplication.isCalledPromotionalBanner) {
                MobikulApplication.isCalledPromotionalBanner = true
                callApi()
            }

            callNotificationApi()
        }, 500.toLong())


        val notificationType = this.intent.getStringExtra("notificationType")
        notificationType?.let {
            Log.d(TAG, "notificationType: $notificationType")
            var intent: Intent? = null
            when (notificationType) {
                NOTIFICATION_TYPE_PRODUCT,
                NOTIFICATION_TYPE_PRODUCT_IN_STOCK -> {

                    intent =
                        (applicationContext as MobikulApplication).getProductDetailsActivity(
                            this
                        )
                    intent.putExtra(
                        BundleKeysHelper.BUNDLE_KEY_PRODUCT_ID,
                        this.intent.getStringExtra("productId")
                    )
                    intent.putExtra(
                        BundleKeysHelper.BUNDLE_KEY_PRODUCT_NAME,
                        this.intent.getStringExtra("productName")
                    )

                }
                NOTIFICATION_TYPE_CATEGORY -> {

                    intent = Intent(this, CatalogActivity::class.java)
                    intent.putExtra(
                        BundleKeysHelper.BUNDLE_KEY_CATALOG_TYPE,
                        BundleKeysHelper.BUNDLE_KEY_CATALOG_TYPE_CATEGORY
                    )
                    intent.putExtra(
                        BundleKeysHelper.BUNDLE_KEY_CATALOG_TITLE,
                        this.intent.getStringExtra("categoryName")
                    )
                    intent.putExtra(
                        BundleKeysHelper.BUNDLE_KEY_CATALOG_ID,
                        this.intent.getStringExtra("categoryId")
                    )

                }
                NOTIFICATION_TYPE_OTHERS -> {

                    intent = Intent(this, OtherNotificationActivity::class.java)

                }
                NOTIFICATION_TYPE_CUSTOM -> {

                    intent = Intent(this, CatalogActivity::class.java)
                    intent.putExtra(
                        BundleKeysHelper.BUNDLE_KEY_CATALOG_TYPE,
                        BundleKeysHelper.BUNDLE_KEY_CATALOG_TYPE_CUSTOM_COLLECTION
                    )


                }
                NOTIFICATION_TYPE_ORDER_PICKUP,
                NOTIFICATION_TYPE_ORDER -> {

                    intent = Intent(this, OrderDetailsActivity::class.java)
                    intent.putExtra(
                        BundleKeysHelper.BUNDLE_KEY_INCREMENT_ID,
                        this.intent.getStringExtra("incrementId")
                    )

                }
                NOTIFICATION_TYPE_CHAT -> {
                    /* var accountType: String? = null
                     if (data.containsKey("accountType"))
                         accountType = data["accountType"]
                     val notificationSenderId = data["senderId"]
                     intent = Intent(this, DeliveryChatActivity::class.java)
                     intent.putExtra(BundleKeysHelper.KEY_ACCOUNT_TYPE, accountType)
                     intent.putExtra(BundleKeysHelper.BUNDLE_KEY_USER_ID, notificationId)
                     if (notificationSenderId != "deliveryAdmin")
                         intent.putExtra(BundleKeysHelper.BUNDLE_KEY_USER_NAME, notificationTitle)*/
                }
                NOTIFICATION_TYPE_SELLER_CHAT -> {
                    /*intent =
                        (applicationContext as MobikulApplication).getChatRelatedActivity(
                            this
                        )
                    intent?.putExtra("user_name", data["name"])
                    intent?.putExtra("user_id", data["customerToken"])
                    if (data.containsKey("apiKey")) {
                        data["apiKey"].let {
                            AppSharedPref.setApiKey(
                                applicationContext,
                                it
                            )
                        }
                    }
                    if (data.containsKey("tokens")) {
                        intent?.putExtra("token", data["tokens"])
                    }*/
                }
                NOTIFICATION_TYPE_SELLER_ORDER -> {
                    intent =
                        (applicationContext as MobikulApplication).getSellerOrderDetailsActivity(
                            this
                        )
                    intent?.putExtra(
                        BundleKeysHelper.BUNDLE_KEY_INCREMENT_ID,
                        this.intent.getStringExtra("incrementId")
                    )
                }
                NOTIFICATION_TYPE_SELLER_PRODUCT_APPROVAL -> {
                    intent = Intent(this, ProductDetailsActivity::class.java)
                    intent.putExtra(
                        BundleKeysHelper.BUNDLE_KEY_PRODUCT_ID,
                        this.intent.getStringExtra("productId")
                    )
                    intent.putExtra(
                        BundleKeysHelper.BUNDLE_KEY_PRODUCT_NAME,
                        this.intent.getStringExtra("productName")
                    )
                }
                NOTIFICATION_TYPE_SELLER_APPROVAL -> {
                    /*  if (AppSharedPref.isLoggedIn(this) && AppSharedPref.getCustomerToken(
                              this
                          ) == this.intent.getStringExtra("sellerId")
                      ) {
                          intent =
                              (applicationContext as MobikulApplication).getSellerDashboardActivity(
                                  this
                              )
                      }*/
                }
            }

            if (intent != null) {
                startActivity(intent)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadCastReceiver)
        unregisterReceiver(bottomMenuReceiver)
    }

/*
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        mItemCart = menu.findItem(R.id.menu_item_cart)
        menu.findItem(R.id.menu_item_cart).isVisible = false

        return true
    }
*/


    private fun startInitialization() {

        getLatestVersionFromPlayStore()
        bottomMenuReceiver = BottomMenuReceiver()
        initIntent()
//        mContentViewBinding.handler = HomeActivityHandler(this)

        mContentViewBinding.bottomNavView.setOnNavigationItemSelectedListener(
            mOnNavigationItemSelectedListener
        )
        mContentViewBinding.handler = HomeActivityHandler(this)
        mContentViewBinding.invalidateAll()
        updateCartBadge()
        inflateBadge()
        updateBadge()
        updateCartCount(AppSharedPref.getCartCount(this))

        registerReceiver(bottomMenuReceiver,IntentFilter("bottom.menu.action"))





    }

    open fun initIntent() {
        if (HomeDataSingleton.getInstance().mHomePageDataModel != null) {
            mHomePageDataModel = HomeDataSingleton.getInstance().mHomePageDataModel!!
        }
        /* if (intent.getParcelableExtra<HomePageDataModel>(BUNDLE_KEY_HOME_PAGE_DATA) != null){
            mHomePageDataModel = intent.getParcelableExtra<HomePageDataModel>(BUNDLE_KEY_HOME_PAGE_DATA)!!
        }*/

        // mHomePageDataModel = HomeDataSingleton.mHomePageDataModel!!

        HomeDataSingleton.getInstance().mHomePageDataModel?.let {
            mHomePageDataModel = it
        }
        lastIndex = 2
        setupFragment(intent.getIntExtra(BUNDLE_KEY_BOTTOM_NAV_INDEX, 2))

        mContentViewBinding.fab.setOnClickListener {
            setupFragment(2)
        }
    }


    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            updateCartBadge()
            updateCartCount(AppSharedPref.getCartCount(this))
            val previousItem = bottom_nav_view?.selectedItemId
            val nextItem = menuItem.itemId
            if (previousItem != nextItem) {
                when (menuItem.itemId) {
                    R.id.bottom_category -> setupFragment(0)
                    R.id.bottom_auction -> setupFragment(1)
                    R.id.bottom_home -> setupFragment(2)
                    R.id.bottom_cart -> setupFragment(3)
                    R.id.bottom_profile -> setupFragment(4)
                }
            }
            true
        }

    /**update the setupFragment with older code**/
    open fun setupFragment(index: Int) {

        updateBadge()
        updateCartCount(AppSharedPref.getCartCount(this))
        when (index) {
            0 -> {
                lastIndex = 0
                navigateToFragment(CategoryPageFragment(mHomePageDataModel.categories))
            }

            1 -> {
                lastIndex = 1
                navigateToFragment(ServiceProviderFragment())
            }

            2 -> {
                lastIndex = 2
                homeFragment = HomeFragment()
                fragment = homeFragment
                val bundle = Bundle()
                fragment?.arguments = bundle
                navigateToFragment(fragment!!)
                mContentViewBinding.bottomNavView.menu.findItem(R.id.bottom_home).isChecked = true
            }
            3 -> {
                mContentViewBinding.bottomAppCl.visibility = View.GONE
                mContentViewBinding.fab.visibility = View.GONE
                cartBottomFragment = CartBottomSheetFragment()
                fragment = cartBottomFragment
                supportFragmentManager.beginTransaction().add(R.id.main_frame, fragment!!, "Cart")
                    .addToBackStack(CartBottomSheetFragment::class.java.javaClass.simpleName)
                    .commitAllowingStateLoss()



            }
            4 -> {
                lastIndex = 4
                navigateToFragment((applicationContext as MobikulApplication).gettAccounntDetailsFragment())
            }
        }

    }

    private fun navigateToFragment(fragmentToNavigate: Fragment) {
        fragment = fragmentToNavigate
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_frame, fragmentToNavigate)
        fragmentTransaction.commitAllowingStateLoss()
    }


    private var mBackPressedTime: Long = 0

    private fun inflateBadge() {
        val bottomNavigationMenuView =
            mContentViewBinding.bottomNavView.getChildAt(0) as BottomNavigationMenuView
        val v = bottomNavigationMenuView.getChildAt(3)
        val itemView = v as BottomNavigationItemView
        mBadge = LayoutInflater.from(this).inflate(R.layout.notification_badge, itemView, true)
    }

    @SuppressLint("RestrictedApi")
    open override fun onBackPressed() {
        updateBadge()
        if (mMaterialSearchView.isOpen()) {
            mMaterialSearchView.closeSearch()
        } else {
            if (supportFragmentManager.backStackEntryCount > 0)
                if (supportFragmentManager.backStackEntryCount == 1) {
                    supportFragmentManager.popBackStackImmediate()
                    mContentViewBinding.bottomAppCl.visibility = View.VISIBLE
                    mContentViewBinding.fab.visibility = View.VISIBLE
                    mContentViewBinding.bottomNavView.menu.getItem(lastIndex).isChecked = true
                //finishMainActivity()
                } else {
                    mContentViewBinding.bottomAppCl.visibility = View.VISIBLE
                    mContentViewBinding.fab.visibility = View.VISIBLE
                    mContentViewBinding.bottomNavView.menu.getItem(lastIndex).isChecked = true
                    //supportFragmentManager.popBackStackImmediate()
                    finishMainActivity()
                }
            else {
               // super.onBackPressed()
                finishMainActivity()
            }
        }
    }

    private fun finishMainActivity() {

        val time = System.currentTimeMillis()
        if (time - mBackPressedTime > 2000) {
            mBackPressedTime = time
            Toast.makeText(
                this,
                resources.getString(R.string.press_back_to_exit),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            moveTaskToBack(true)
            finish()
        }
    }


    private fun getLatestVersionFromPlayStore() {
        VersionChecker(this).execute()
    }

    @SuppressLint("NewApi")
    fun onLatestVersionResponse(result: String?) {
        try {
            val list: List<String> = listOf(*result!!.split(",").toTypedArray())
            val version = list[0]
            val updateDate = list[1] + ", " + list[2]
            val currentTimeStamp = Utils.strToTimemills(updateDate)
            val nextCurrentTimeStamp = currentTimeStamp + 86400000
            if (java.lang.Double.parseDouble(BuildConfig.VERSION_NAME) < java.lang.Double.parseDouble(
                    version
                )
            ) {

                val currentDate = Utils.millsToDate(System.currentTimeMillis())
                val nextDate = Utils.millsToDate(nextCurrentTimeStamp)
                println(currentDate == nextDate)
//                if (true) {
                if (currentDate == nextDate) {
                    AlertDialogHelper.showNewCustomDialog(
                        this,
                        getString(R.string.update_alert_title),
                        getString(R.string.new_version_available),
                        false,
                        getString(R.string.update),
                        { dialogInterface: DialogInterface, _: Int ->
                            dialogInterface.dismiss()
                            startActivityForResult(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                                ), ConstantsHelper.RC_UPDATE_APP_FROM_PLAY_STORE
                            )
                        },
                    )
                }

            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }


        if (requestCode == 1010) {
            if (supportFragmentManager.backStackEntryCount > 0) {
                mContentViewBinding.bottomNavView.menu.getItem(lastIndex).isChecked = true
            }
            /*if (supportFragmentManager.fragments[supportFragmentManager.backStackEntryCount] != null && supportFragmentManager.fragments[supportFragmentManager.backStackEntryCount].toString()
                        .contains("AccountDetailsFragment")
                ) {
                    mContentViewBinding.bottomNavView.menu.findItem(R.id.bottom_profile).isChecked =
                        true
                } else if (supportFragmentManager.fragments[supportFragmentManager.backStackEntryCount] != null && supportFragmentManager.fragments[supportFragmentManager.backStackEntryCount].toString()
                        .contains("CategoryPageFragment")
                ) {
                    mContentViewBinding.bottomNavView.menu.findItem(R.id.bottom_category).isChecked =
                        true
                } else if (supportFragmentManager.fragments[supportFragmentManager.backStackEntryCount] != null && (supportFragmentManager.fragments[supportFragmentManager.backStackEntryCount].toString()
                        .contains("HomeFragment")
                            || supportFragmentManager.fragments[supportFragmentManager.backStackEntryCount]
                        .toString().contains("SupportRequestManagerFragment"))
                ) {
                    mContentViewBinding.bottomNavView.menu.findItem(R.id.bottom_home).isChecked =
                        true
                } else {
                }*/
        }
    }


    private fun callApi() {
        ApiConnection.getOfferData(this)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiCustomCallback<PromotionBanner>(this, true) {
                override fun onNext(responseModel: PromotionBanner) {
                    super.onNext(responseModel)
                    if (responseModel.success) {

//                            mIsFreshHomePageData = true
                        onSuccessfulResponse(responseModel)
                    } else {
                        onFailureResponse(responseModel)
                    }
                }
            })
    }


    private fun onSuccessfulResponse(promotionBanner: PromotionBanner) {
        mPromotionBanner = promotionBanner
        val dialog = Dialog(this)
        if (!dialog.isShowing) {
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.home_banner_dialog_layout)
            val image: ImageView = dialog.findViewById(R.id.dialogBanner)
            val closeImage: ImageView = dialog.findViewById(R.id.closeBannerBtn)
            Glide.with(this).load(promotionBanner.image).into(image)
            image.setOnClickListener {
                if (promotionBanner.type == "1") {

                    /*FirebaseAnalyticsHelper.logHomeEvent(
                        "promotion_banner_category",
                        promotionBanner.category_product_id,
                        HOME_CATEGORY_BANNER
                    )*/

                    FirebaseAnalyticsHelper.logHomeEvent("PromotionBanner", "0", "0")

                    val navigate = Intent(this@HomeActivity, CatalogActivity::class.java)
                    navigate.putExtra(
                        BundleKeysHelper.BUNDLE_KEY_CATALOG_TYPE,
                        BundleKeysHelper.BUNDLE_KEY_CATALOG_TYPE_CATEGORY
                    )
                    navigate.putExtra(
                        BundleKeysHelper.BUNDLE_KEY_CATALOG_TITLE,
                        promotionBanner.title
                    )
                    navigate.putExtra(
                        BundleKeysHelper.BUNDLE_KEY_CATALOG_ID,
                        promotionBanner.category_product_id
                    )
                    startActivity(navigate)
                } else {

                    /*FirebaseAnalyticsHelper.logHomeEvent(
                        "promotion_banner_product",
                        promotionBanner.category_product_id,
                        HOME_PRODUCT_BANNER
                    )*/
                    FirebaseAnalyticsHelper.logHomeEvent("PromotionBanner", "0", "0")

                    val navigate = Intent(this@HomeActivity, ProductDetailsActivity::class.java)
                    navigate.putExtra(BundleKeysHelper.BUNDLE_KEY_PRODUCT_DOMINANT_COLOR, "")
                    navigate.putExtra(
                        BundleKeysHelper.BUNDLE_KEY_PRODUCT_NAME,
                        promotionBanner.title
                    )

                    navigate.putExtra(
                        BundleKeysHelper.BUNDLE_KEY_PRODUCT_ID,
                        promotionBanner.category_product_id
                    )
                    navigate.putExtra(
                        BundleKeysHelper.BUNDLE_KEY_PRODUCT_CATEGORY,
                        promotionBanner.id
                    )
                    startActivity(navigate)
                }

                dialog.dismiss()
            }


            closeImage.setOnClickListener {
                dialog.dismiss()
            }
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }

    }


    fun onNotificationSuccessfulResponse(notificationListResponseModel: NotificationListResponseModel) {
        var onNotReadSize = 0
        runOnUiThread {
            val notificationList: ArrayList<String> = mDataBaseHandler.getNotificationData()
            notificationListResponseModel.notificationList.forEach {
                it.isRead = notificationList.contains(it.id)
            }
            onNotReadSize =
                (notificationListResponseModel.notificationList.filter { !it.isRead }).count()
        }
        homeFragment!!.showBadge(onNotReadSize)

    }

    val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            when (intent?.action) {
                BROADCAST_DEFAULT_ALBUM_CHANGED -> clearAllNotification()
            }
        }
    }

    val updateCartBadgeReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            when (intent?.action) {
                BROADCAST_DEFAULT_UPDATE_CART_BADGE -> {
                    updateCartBadge()
                    updateCartCount(AppSharedPref.getCartCount(this@HomeActivity))
                }
            }
        }
    }

    private fun clearAllNotification() {
        homeFragment!!.showBadge(0)
    }

    private fun callNotificationApi() {
        println("Notification api is calling>>>>>>>>>>>>")
        val mHashIdentifier =
            Utils.getMd5String("getNotificationsList" + AppSharedPref.getStoreId(this))
        ApiConnection.getNotificationsList(
            this,
            mDataBaseHandler.getETagFromDatabase(mHashIdentifier)
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : ApiCustomCallback<NotificationListResponseModel>(this, false) {
                override fun onNext(notificationListResponseModel: NotificationListResponseModel) {
                    super.onNext(notificationListResponseModel)
                    if (notificationListResponseModel.success) {
                        if (context is HomeActivity) {
                            context.onNotificationSuccessfulResponse(
                                notificationListResponseModel
                            )
                        }

                    } else {
                        onFailureResponse(notificationListResponseModel)
                    }
                }

            })
    }

    override fun onResume() {
        super.onResume()
        updateBadge()
        BottomMenuReceiver.onMenuSelectListener = this


    }

    override fun onMenuSelected(menu: String, menuIndex: Int) {
        if (menuIndex == 2) {
            mContentViewBinding.bottomNavView.menu.getItem(2).isChecked = true
            setupFragment(menuIndex)
        } else {
            mContentViewBinding.bottomNavView.menu.getItem(menuIndex).isChecked = true
            setupFragment(menuIndex)
        }

    }


}
