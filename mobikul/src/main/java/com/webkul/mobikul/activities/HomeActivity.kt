package com.webkul.mobikul.activities

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.ActivityHomeBinding
import com.webkul.mobikul.fragments.AccountDetailsFragment
import com.webkul.mobikul.fragments.CartBottomSheetFragment
import com.webkul.mobikul.fragments.CategoryPageFragment
import com.webkul.mobikul.fragments.HomeFragment
import com.webkul.mobikul.handlers.HomeActivityHandler
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_BOTTOM_NAV_INDEX
import com.webkul.mobikul.helpers.BundleKeysHelper.BUNDLE_KEY_HOME_PAGE_DATA
import com.webkul.mobikul.helpers.MobikulApplication
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.helpers.VersionChecker
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.models.homepage.HomePageDataModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*
import kotlin.concurrent.schedule


class HomeActivity : BaseActivity() {

    companion object {
        lateinit var mContentViewBinding: ActivityHomeBinding
        private const val TAG = "HomeActivity"
    }

    open var homeFragment: HomeFragment? = null
    open var mHomePageDataModel: HomePageDataModel = HomePageDataModel()
    var mTopSellingHomePageDataModel: HomePageDataModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        startInitialization()
        updateCartBadge()
        updateCartCount(AppSharedPref.getCartCount(this))

//        Timer().schedule(5000) {
//            runOnUiThread {
//
//            }
//
//        }

        getUpdateFCMCode()
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

        mContentViewBinding.bottomNavView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        mContentViewBinding.handler = HomeActivityHandler(this)
        mContentViewBinding.invalidateAll()
        updateCartBadge()
        inflateBadge()
        updateBadge()
        updateCartCount(AppSharedPref.getCartCount(this))
    }

    open fun initIntent() {
        if (intent.getParcelableExtra<HomePageDataModel>(BUNDLE_KEY_HOME_PAGE_DATA) != null)
            mHomePageDataModel = intent.getParcelableExtra<HomePageDataModel>(BUNDLE_KEY_HOME_PAGE_DATA)!!
        setupFragment(intent.getIntExtra(BUNDLE_KEY_BOTTOM_NAV_INDEX, 2))
    }


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        updateCartBadge()
        updateCartCount(AppSharedPref.getCartCount(this))
        val previousItem = bottom_nav_view?.selectedItemId
        val nextItem = menuItem.itemId
        if (previousItem != nextItem) {
            when (menuItem.itemId) {
                R.id.bottom_category -> setupFragment(0)
                R.id.bottom_auction -> startActivityForResult(Intent(this, AuctionFragmentActivity::class.java), 1010)// setupFragment(1)
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
                fragment = CategoryPageFragment(mHomePageDataModel.categories)
                supportFragmentManager.beginTransaction().add(R.id.main_frame, fragment, "Category")
                        .addToBackStack(CategoryPageFragment::class.java.simpleName)
                        .commit()
            }

            2 -> {
//                if (homeFragment != null) {
//                    fragment = homeFragment
//                    supportFragmentManager.beginTransaction().show(fragment!!)
//                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                            .setPrimaryNavigationFragment(fragment!!)
//                            .setReorderingAllowed(true)
//                            .commitNowAllowingStateLoss()
//                } else {
                homeFragment = HomeFragment()
                fragment = homeFragment
                var bundle = Bundle()
                bundle.putParcelable(BUNDLE_KEY_HOME_PAGE_DATA, intent.getParcelableExtra(BUNDLE_KEY_HOME_PAGE_DATA))
                fragment?.arguments = bundle
                supportFragmentManager.beginTransaction().add(R.id.main_frame, fragment!!, "HomeFragment")
                        .addToBackStack(HomeFragment::class.java.javaClass.simpleName)
                        .commit()
//                }

                mContentViewBinding.bottomNavView.menu.findItem(R.id.bottom_home).isChecked = true
            }
            3 -> {
                mContentViewBinding.bottomAppCl.visibility = View.GONE
                fragment = CartBottomSheetFragment()
                supportFragmentManager.beginTransaction().add(R.id.main_frame, fragment, "Cart")
                        .addToBackStack(CartBottomSheetFragment::class.java.javaClass.simpleName)
                        .commit()
            }
            4 -> {
                fragment = (applicationContext as MobikulApplication).gettAccounntDetailsFragment()
                supportFragmentManager.beginTransaction().add(R.id.main_frame, fragment, "Account")
                        .addToBackStack(AccountDetailsFragment::class.java.javaClass.simpleName)
                        .commit()
            }
        }

    }

    private var mBackPressedTime: Long = 0

    private fun inflateBadge() {
        val bottomNavigationMenuView = mContentViewBinding.bottomNavView.getChildAt(0) as BottomNavigationMenuView
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
                    if (supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount) != null && supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount).toString().contains("CartBottomSheetFragment")
                            && supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount - 1) != null && supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount - 1).toString().contains("AccountDetailsFragment")) {
                        mContentViewBinding.bottomNavView.menu.findItem(R.id.bottom_profile).isChecked = true
                        supportFragmentManager.popBackStackImmediate()
                    } else if (supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount) != null && supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount).toString().contains("CartBottomSheetFragment")
                            && supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount - 1) != null && supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount - 1).toString().contains("CategoryPageFragment")) {
                        mContentViewBinding.bottomNavView.menu.findItem(R.id.bottom_category).isChecked = true
                        supportFragmentManager.popBackStackImmediate()
                    } else if (supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount) != null && supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount).toString().contains("CartBottomSheetFragment")
                            && (supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount - 1) != null && supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount - 1).toString().contains("HomeFragment")
                                    || supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount - 1).toString().contains("SupportRequestManagerFragment"))) {
                        mContentViewBinding.bottomNavView.menu.findItem(R.id.bottom_home).isChecked = true
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
            Toast.makeText(this, resources.getString(R.string.press_back_to_exit), Toast.LENGTH_SHORT).show()
        } else {
            moveTaskToBack(true)
            finish()
        }
    }


    private fun getLatestVersionFromPlayStore() {
        VersionChecker(this).execute()
    }

    fun onLatestVersionResponse(result: String?) {
        // TODO: 02/04/21 Fix it
        /*try {
            if (java.lang.Double.parseDouble(BuildConfig.VERSION_NAME) < java.lang.Double.parseDouble(result)) {
                AlertDialogHelper.showNewCustomDialog(
                        this,
                        getString(R.string.update_alert_title),
                        getString(R.string.new_version_available),
                        false,
                        getString(R.string.update),
                        DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                            dialogInterface.dismiss()
                            startActivityForResult(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")), ConstantsHelper.RC_UPDATE_APP_FROM_PLAY_STORE)
                        }
                        , getString(R.string.later)
                        , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                })
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }*/
    }

    private fun getUpdateFCMCode() {
        val fcmToken = AppSharedPref.getFcmToken(this)

        val uniqueId = Utils.getMd5String(Utils.macAddress)
        showUniqueCode("UniqueCode: $uniqueId, Fcm Token $fcmToken")
        Log.d(TAG, "UniqueCode: $uniqueId, Fcm Token $fcmToken")
        Toast.makeText(this, uniqueId, Toast.LENGTH_LONG).show()
        ApiConnection.uploadFCMForAdmin((fcmToken ?: ""), uniqueId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<BaseModel>(this, true) {

                })
    }


    private fun showUniqueCode(code: String) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this, R.style.todoDialogLight)
        alertDialog.setTitle("Copy Code")
        alertDialog.setMessage("Unique Code: $code")
        alertDialog.setNeutralButton(
                "yes"
        ) { _, _ ->

            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("unique code", code)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Copied.", Toast.LENGTH_LONG).show()
        }

        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(true)
        alert.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1010) {
            if (supportFragmentManager.backStackEntryCount > 0)
                if (supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount) != null && supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount).toString().contains("AccountDetailsFragment")) {
                    mContentViewBinding.bottomNavView.menu.findItem(R.id.bottom_profile).isChecked = true
                } else if (supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount) != null && supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount).toString().contains("CategoryPageFragment")) {
                    mContentViewBinding.bottomNavView.menu.findItem(R.id.bottom_category).isChecked = true
                } else if (supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount) != null && (supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount).toString().contains("HomeFragment")
                                || supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount).toString().contains("SupportRequestManagerFragment"))) {
                    mContentViewBinding.bottomNavView.menu.findItem(R.id.bottom_home).isChecked = true
                } else {
                }
        }
    }

}