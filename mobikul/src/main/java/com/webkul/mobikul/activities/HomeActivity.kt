package com.webkul.mobikul.activities

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.webkul.mobikul.BuildConfig
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.ActivityHomeBinding
import com.webkul.mobikul.fragments.AccountDetailsFragment
import com.webkul.mobikul.fragments.CartBottomSheetFragment
import com.webkul.mobikul.fragments.CategoryPageFragment
import com.webkul.mobikul.fragments.HomeFragment
import com.webkul.mobikul.handlers.HomeActivityHandler
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_BOTTOM_NAV_INDEX
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_HOME_PAGE_DATA
import com.webkul.mobikul.models.homepage.HomePageDataModel
import com.webkul.mobikul.models.homepage.PromotionBanner
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseActivity() {

    private var mPromotionBanner: PromotionBanner? = null

    companion object {
        lateinit var mContentViewBinding: ActivityHomeBinding
    }

    open var homeFragment: HomeFragment? = null
    open var categoryFragment: CategoryPageFragment? = null
    open var cartBottomfragment:CartBottomSheetFragment? = null
    open var accountDetailsFragment:AccountDetailsFragment? = null
    open var mHomePageDataModel: HomePageDataModel = HomePageDataModel()
    var mTopSellingHomePageDataModel: HomePageDataModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        startInitialization()
        updateCartBadge()
        updateCartCount(AppSharedPref.getCartCount(this))

        callApi()

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
    }

    open fun initIntent() {
        if (intent.getParcelableExtra<HomePageDataModel>(BUNDLE_KEY_HOME_PAGE_DATA) != null)
            mHomePageDataModel =
                intent.getParcelableExtra<HomePageDataModel>(BUNDLE_KEY_HOME_PAGE_DATA)!!
        setupFragment(intent.getIntExtra(BUNDLE_KEY_BOTTOM_NAV_INDEX, 2))
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
                    R.id.bottom_auction -> startActivityForResult(
                        Intent(
                            this,
                            AuctionFragmentActivity::class.java
                        ), 1010
                    )// setupFragment(1)
                    R.id.bottom_home -> setupFragment(2)
                    R.id.bottom_cart -> setupFragment(3)
                    R.id.bottom_profile -> setupFragment(4)
                }
            }
            true
        }

    open fun setupFragment(index: Int) {
        var fragment: Fragment? = null
        updateBadge()
        updateCartCount(AppSharedPref.getCartCount(this))
        when (index) {
            0 -> {
                if (categoryFragment != null){
                    fragment = categoryFragment

                    for (frag in supportFragmentManager.fragments){
                        supportFragmentManager.beginTransaction().hide(frag).commit()
                    }

                    supportFragmentManager.beginTransaction().show(fragment!!)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .setPrimaryNavigationFragment(fragment)
                        .setReorderingAllowed(true)
                        .commit()
                }else{
                    categoryFragment = CategoryPageFragment(mHomePageDataModel.categories)
                    fragment = categoryFragment
                    supportFragmentManager.beginTransaction()
                        .add(R.id.main_frame, fragment!!, "Category")
                        .addToBackStack(CategoryPageFragment::class.java.simpleName)
                        .commit()
                }

            }

            2 -> {
                if (homeFragment != null) {
                    fragment = homeFragment

                    for (frag in supportFragmentManager.fragments){
                        supportFragmentManager.beginTransaction().hide(frag).commit()
                    }

                    supportFragmentManager.beginTransaction().show(fragment!!)
//                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                        .setPrimaryNavigationFragment(fragment)
//                        .setReorderingAllowed(true)
                        .commit()
                } else {
                    homeFragment = HomeFragment()
                    fragment = homeFragment
                    val bundle = Bundle()
                    bundle.putParcelable(
                        BUNDLE_KEY_HOME_PAGE_DATA,
                        intent.getParcelableExtra(BUNDLE_KEY_HOME_PAGE_DATA)
                    )
                    fragment?.arguments = bundle
                    supportFragmentManager.beginTransaction()
                        .add(R.id.main_frame, fragment!!, "HomeFragment")
                        .addToBackStack(HomeFragment::class.java.javaClass.simpleName)
                        .commit()
                }

                mContentViewBinding.bottomNavView.menu.findItem(R.id.bottom_home).isChecked = true
            }
            3 -> {

                if (cartBottomfragment != null){

                    for (frag in supportFragmentManager.fragments){
                        supportFragmentManager.beginTransaction().hide(frag).commit()
                    }

//                    categoryFragment?.let {categoryFragmentSafe->
//                        supportFragmentManager.beginTransaction().hide(categoryFragmentSafe).commit()
//                        println("HomeActivity:: categoryFragmentSafe")
//                    } ?: run {
//                    }

                    fragment = cartBottomfragment
                    supportFragmentManager.beginTransaction().show(fragment!!)
//                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                        .setPrimaryNavigationFragment(fragment)
//                        .setReorderingAllowed(true)
                        .commit()
                }else{
                    mContentViewBinding.bottomAppCl.visibility = View.GONE
                    cartBottomfragment = CartBottomSheetFragment()
                    fragment = cartBottomfragment
                    supportFragmentManager.beginTransaction().add(R.id.main_frame, fragment!!, "Cart")
                        .addToBackStack(CartBottomSheetFragment::class.java.javaClass.simpleName)
                        .commit()
                }


            }
            4 -> {


                if (accountDetailsFragment != null){

                    categoryFragment?.let {categoryFragmentSafe->
                        supportFragmentManager.beginTransaction().hide(categoryFragmentSafe).commit()
                        println("HomeActivity:: categoryFragmentSafe")
                    } ?: run {

                    }

                    fragment = accountDetailsFragment
                    supportFragmentManager.beginTransaction().show(fragment!!)
//                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                        .setPrimaryNavigationFragment(fragment)
//                        .setReorderingAllowed(true)
                        .commit()
                }else{
                    accountDetailsFragment = (applicationContext as MobikulApplication).gettAccounntDetailsFragment()
                    fragment = accountDetailsFragment
                    supportFragmentManager.beginTransaction()
                        .add(R.id.main_frame, fragment!!, "Account")
                        .addToBackStack(AccountDetailsFragment::class.java.javaClass.simpleName)
                        .commit()
                }



            }
        }

    }

    private var mBackPressedTime: Long = 0

    private fun inflateBadge() {
        val bottomNavigationMenuView =
            mContentViewBinding.bottomNavView.getChildAt(0) as BottomNavigationMenuView
        val v = bottomNavigationMenuView.getChildAt(3)
        val itemView = v as BottomNavigationItemView
        mBadge = LayoutInflater.from(this).inflate(R.layout.notification_badge, itemView, true)
    }

    open override fun onBackPressed() {
        if (mMaterialSearchView.isOpen()) {
            mMaterialSearchView.closeSearch()
        } else {
//            if (bottom_nav_view.selectedItemId == R.id.bottom_home)
//                finishMainActivity()
//            else {
            if (supportFragmentManager.backStackEntryCount > 0)
                if (supportFragmentManager.backStackEntryCount == 1) {
                    finishMainActivity()
                } else {
                    mContentViewBinding.bottomAppCl.visibility = View.VISIBLE
                    if (supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount) != null && supportFragmentManager.fragments.get(
                            supportFragmentManager.backStackEntryCount
                        ).toString().contains("CartBottomSheetFragment")
                        && supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount - 1) != null && supportFragmentManager.fragments.get(
                            supportFragmentManager.backStackEntryCount - 1
                        ).toString().contains("AccountDetailsFragment")
                    ) {
                        mContentViewBinding.bottomNavView.menu.findItem(R.id.bottom_profile).isChecked =
                            true
                        supportFragmentManager.popBackStackImmediate()
                    } else if (supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount) != null && supportFragmentManager.fragments.get(
                            supportFragmentManager.backStackEntryCount
                        ).toString().contains("CartBottomSheetFragment")
                        && supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount - 1) != null && supportFragmentManager.fragments.get(
                            supportFragmentManager.backStackEntryCount - 1
                        ).toString().contains("CategoryPageFragment")
                    ) {
                        mContentViewBinding.bottomNavView.menu.findItem(R.id.bottom_category).isChecked =
                            true
                        supportFragmentManager.popBackStackImmediate()
                    } else if (supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount) != null && supportFragmentManager.fragments.get(
                            supportFragmentManager.backStackEntryCount
                        ).toString().contains("CartBottomSheetFragment")
                        && (supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount - 1) != null && supportFragmentManager.fragments.get(
                            supportFragmentManager.backStackEntryCount - 1
                        ).toString().contains("HomeFragment")
                                || supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount - 1)
                            .toString().contains("SupportRequestManagerFragment"))
                    ) {
                        mContentViewBinding.bottomNavView.menu.findItem(R.id.bottom_home).isChecked =
                            true
                        supportFragmentManager.popBackStackImmediate()
                    } else {
                        finishMainActivity()
//                        supportFragmentManager.popBackStackImmediate()
                    }
                }
            else {
                super.onBackPressed()
            }

//            }
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

    fun onLatestVersionResponse(result: String?) {
        try {
            if (java.lang.Double.parseDouble(BuildConfig.VERSION_NAME) < java.lang.Double.parseDouble(
                    result
                )
            ) {
                AlertDialogHelper.showNewCustomDialog(
                    this,
                    getString(R.string.update_alert_title),
                    getString(R.string.new_version_available),
                    false,
                    getString(R.string.update),
                    DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        startActivityForResult(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                            ), ConstantsHelper.RC_UPDATE_APP_FROM_PLAY_STORE
                        )
                    },
                    getString(R.string.later),
                    DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                    })
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1010) {
            if (supportFragmentManager.backStackEntryCount > 0)
                if (supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount) != null && supportFragmentManager.fragments.get(
                        supportFragmentManager.backStackEntryCount
                    ).toString().contains("AccountDetailsFragment")
                ) {
                    mContentViewBinding.bottomNavView.menu.findItem(R.id.bottom_profile).isChecked =
                        true
                } else if (supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount) != null && supportFragmentManager.fragments.get(
                        supportFragmentManager.backStackEntryCount
                    ).toString().contains("CategoryPageFragment")
                ) {
                    mContentViewBinding.bottomNavView.menu.findItem(R.id.bottom_category).isChecked =
                        true
                } else if (supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount) != null && (supportFragmentManager.fragments.get(
                        supportFragmentManager.backStackEntryCount
                    ).toString().contains("HomeFragment")
                            || supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount)
                        .toString().contains("SupportRequestManagerFragment"))
                ) {
                    mContentViewBinding.bottomNavView.menu.findItem(R.id.bottom_home).isChecked =
                        true
                } else {
                }
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

        /*  val builder: Dialog = Dialog(this)
          val inflater: LayoutInflater = this.layoutInflater
          val vg = inflater.inflate(R.layout.myphoto_layout, null) as ViewGroup
          val image: AppCompatImageView = vg.findViewById<View>(R.id.banner_image) as AppCompatImageView
          Picasso.with(this).load(promotionBanner.image).into(image)
          image.setOnClickListener(object : View.OnClickListener {
              override fun onClick(view: View?) {
                  // Do some work here
                  if(promotionBanner.type?.equals("0")!!){
                      val navigate = Intent(this@HomeActivity,ProductDetailsActivity::class.java)
                      navigate.putExtra(BundleKeysHelper.BUNDLE_KEY_PRODUCT_DOMINANT_COLOR, "")
                      navigate.putExtra(BundleKeysHelper.BUNDLE_KEY_PRODUCT_NAME, promotionBanner.title)
                      navigate.putExtra(BundleKeysHelper.BUNDLE_KEY_PRODUCT_ID, promotionBanner.category_product_id)
                      startActivity(navigate)
                  } else{
                      val navigate = Intent(this@HomeActivity,CatalogActivity::class.java)
                      navigate.putExtra(BundleKeysHelper.BUNDLE_KEY_CATALOG_TYPE, BundleKeysHelper.BUNDLE_KEY_CATALOG_TYPE_CATEGORY)
                      navigate.putExtra(BundleKeysHelper.BUNDLE_KEY_CATALOG_TITLE, promotionBanner.title)
                      navigate.putExtra(BundleKeysHelper.BUNDLE_KEY_CATALOG_ID, promotionBanner.category_product_id)
                      startActivity(navigate)

                  }



                  builder.dismiss()
              }
          })
          builder.setContentView(vg)
          builder.show()*/
    }

}
