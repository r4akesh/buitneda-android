package com.webkul.mobikulmp.activities

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.Menu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.AppBarLayout
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.helpers.AlertDialogHelper
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikul.helpers.NetworkHelper
import com.webkul.mobikul.helpers.Utils
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.databinding.ActivitySellerProfileBinding
import com.webkul.mobikulmp.fragments.SellerProfileAboutStoreFragment
import com.webkul.mobikulmp.fragments.SellerProfileMoreMenuFragment
import com.webkul.mobikulmp.fragments.SellerRecentlyAddedProductFragment
import com.webkul.mobikulmp.fragments.SellerStoreReviewFragment
import com.webkul.mobikulmp.handlers.SellerProfileActivityHandler
import com.webkul.mobikulmp.helpers.LocationHelper
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_SELLER_ID
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_SELLER_TITLE
import com.webkul.mobikulmp.models.seller.SellerProfileData
import com.webkul.mobikulmp.network.MpApiConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.util.*


class SellerProfileActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var mContentViewBinding: ActivitySellerProfileBinding
    var mSellerId: Int = 0
    private var mSellerProfileData: SellerProfileData? = null
    var shopTitle: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_seller_profile)
        startInitialization()
    }

    private fun startInitialization() {
        mSellerId = intent.getIntExtra(BUNDLE_KEY_SELLER_ID, 0)
        initSupportActionBar()
        setupTitleWithScroll()
        callApi()
        checkAndLoadLocalData()
    }

    private fun initSupportActionBar() {
        shopTitle = intent.getStringExtra(BUNDLE_KEY_SELLER_TITLE)
        setSupportActionBar(mContentViewBinding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupTitleWithScroll() {
        mContentViewBinding.appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    shopTitle?.let {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            mContentViewBinding.collapsingToolbar.title = Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY).toString()
                        } else {
                            mContentViewBinding.collapsingToolbar.title = Html.fromHtml(it).toString()
                        }
                    }

                    isShow = true
                } else if (isShow) {
                    mContentViewBinding.collapsingToolbar.title = " " //carefull there should be a space between double quote otherwise it wont work
                    isShow = false
                }
            }
        })
    }


    fun callApi() {
        mContentViewBinding.loading = true
        mHashIdentifier = Utils.getMd5String("getSellerProfileData" + AppSharedPref.getStoreId(this) + AppSharedPref.getCustomerToken(this) + mSellerId)
        MpApiConnection.getSellerProfileData(
                this,
                mDataBaseHandler.getETagFromDatabase(mHashIdentifier),
                mSellerId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<SellerProfileData>(this, true) {
                    override fun onNext(t: SellerProfileData) {
                        super.onNext(t)
                        mContentViewBinding.loading = false
                        if (t.success) {
                            onSuccessfulResponse(t)
                        } else {
                            onFailureResponse(t)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mContentViewBinding.loading = false
                        onErrorResponse(e)
                    }
                })
    }

    private fun checkAndLoadLocalData() {
        val response = mDataBaseHandler.getResponseFromDatabase(mHashIdentifier)
        if (response.isNotBlank()) {
            onSuccessfulResponse(mGson.fromJson(response, SellerProfileData::class.java))
        }
    }

    private fun onSuccessfulResponse(response: SellerProfileData) {
        shopTitle = mSellerProfileData?.shopTitle
        mSellerProfileData = response
        mContentViewBinding.data = mSellerProfileData
        mContentViewBinding.handler = SellerProfileActivityHandler(this, mSellerId)
        setupViewPager()
    }

    private fun setupViewPager() {
        mContentViewBinding.tabs.setupWithViewPager(mContentViewBinding.viewPager)
        val adapter = SellerProfileVpAdapter(supportFragmentManager)
        adapter.addFragment(SellerProfileAboutStoreFragment.newInstance(mSellerProfileData!!.description), getString(R.string.about))

        val sellerRecentlyAddedProductFragment = SellerRecentlyAddedProductFragment.newInstance(mSellerProfileData!!.recentProductList, mSellerProfileData?.shopTitle!!, mSellerId)
        adapter.addFragment(sellerRecentlyAddedProductFragment, getString(R.string.products))

        val sellerStoreReviewFragment = SellerStoreReviewFragment.newInstance(mSellerProfileData)
        adapter.addFragment(sellerStoreReviewFragment, getString(R.string.reviews))

        val sellerProfileMoreMenuFragment = SellerProfileMoreMenuFragment.newInstance(mSellerProfileData?.returnPolicy, mSellerProfileData?.shippingPolicy, mSellerProfileData?.privacyPolicy)
        adapter.addFragment(sellerProfileMoreMenuFragment, getString(R.string.more))

        mContentViewBinding.viewPager.adapter = adapter
        mContentViewBinding.viewPager.offscreenPageLimit = 2
    }

    override fun onFailureResponse(response: Any) {
        AlertDialogHelper.showNewCustomDialog(
                this,
                getString(R.string.error),
                (response as BaseModel).message,
                false,
                getString(R.string.ok),
            { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                finish()
            }
                , ""
                , null)
    }

    private fun onErrorResponse(error: Throwable) {
        val response = mDataBaseHandler.getResponseFromDatabase(mHashIdentifier)
        if ((!NetworkHelper.isNetworkAvailable(this) || (error is HttpException && error.code() == 304))) {
            onSuccessfulResponse(mGson.fromJson(response, SellerProfileData::class.java))
        } else {
            AlertDialogHelper.showNewCustomDialog(
                    this,
                    getString(R.string.oops),
                    NetworkHelper.getErrorMessage(this, error),
                    false,
                    getString(R.string.try_again),
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

    override fun onResume() {
        super.onResume()
        mContentViewBinding.notifyChange()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    private inner class SellerProfileVpAdapter internal constructor(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }
    }


    val DEFAULT_MAP_ZOOM_VALUE = 10

    override fun onMapReady(googleMap: GoogleMap) {
        if (mSellerProfileData?.location != null) {
            val latLng = LocationHelper.getLatLang(this, mSellerProfileData!!.location!!.trim())
            if (latLng != null) {
                googleMap.addMarker(MarkerOptions().position(latLng))
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_MAP_ZOOM_VALUE.toFloat())
                googleMap.animateCamera(cameraUpdate)
            }
        }
    }
}
