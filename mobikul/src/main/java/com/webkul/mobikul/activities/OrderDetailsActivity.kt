package com.webkul.mobikul.activities

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Interpolator
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.ActivityOrderDetailsBinding
import com.webkul.mobikul.fragments.*
import com.webkul.mobikul.handlers.OrderDetailsActivityHandler
import com.webkul.mobikul.helpers.*
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.models.user.OrderDetailsModel
import com.webkul.mobikul.network.ApiConnection
import com.webkul.mobikul.network.ApiCustomCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

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

class OrderDetailsActivity : BaseActivity() {

    lateinit var mContentViewBinding: ActivityOrderDetailsBinding
    lateinit var mMenuItemMoreOptions: MenuItem
    lateinit var mNavigationIconClickListener: NavigationIconClickListener
    var mIncrementId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_order_details)
        startInitialization()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_order_details, menu)

        mMenuItemMoreOptions = menu.findItem(R.id.menu_item_more_options)
        mMenuItemMoreOptions.isVisible = false

        return true
    }

    private fun startInitialization() {
        mIncrementId = intent.getStringExtra(BundleKeysHelper.BUNDLE_KEY_INCREMENT_ID)!!
        initSupportActionBar()
        initSwipeRefresh()
        mContentViewBinding.loading=true
        callApi()
    }


    private fun initSwipeRefresh() {
        mContentViewBinding.swipeRefreshLayout.setDistanceToTriggerSync(300)
        mContentViewBinding.swipeRefreshLayout.setOnRefreshListener {
            if (NetworkHelper.isNetworkAvailable(this)) {
              callApi()
            } else {
                mContentViewBinding.swipeRefreshLayout.isRefreshing = false
                ToastHelper.showToast(this@OrderDetailsActivity, getString(R.string.you_are_offline))
            }
        }
    }

    private fun initSupportActionBar() {
        setSupportActionBar(mContentViewBinding.toolbar)
        supportActionBar?.elevation = 0f
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun callApi() {
        mContentViewBinding.swipeRefreshLayout.isRefreshing = true
        mHashIdentifier = Utils.getMd5String("getOrderDetails" + AppSharedPref.getStoreId(this) + AppSharedPref.getCustomerToken(this) + AppSharedPref.getCurrencyCode(this) + mIncrementId)
        ApiConnection.getOrderDetails(this, mDataBaseHandler.getETagFromDatabase(mHashIdentifier), mIncrementId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<OrderDetailsModel>(this, false) {
                    override fun onNext(orderDetailsModel: OrderDetailsModel) {
                        super.onNext(orderDetailsModel)
                        mContentViewBinding.swipeRefreshLayout.isRefreshing = false
                        mContentViewBinding.loading=false

                        if (orderDetailsModel.success) {
                            onSuccessfulResponse(orderDetailsModel)
                        } else {
                            onFailureResponse(orderDetailsModel)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mContentViewBinding.loading=false
                        mContentViewBinding.swipeRefreshLayout.isRefreshing = false
                        onErrorResponse(e)
                    }
                })
        checkAndLoadLocalData()
    }

    private fun checkAndLoadLocalData() {
        val response = mDataBaseHandler.getResponseFromDatabase(mHashIdentifier)
        if (response.isNotBlank()) {
            onSuccessfulResponse(mGson.fromJson(response, OrderDetailsModel::class.java))
        }
    }

    private fun onSuccessfulResponse(orderDetailsModel: OrderDetailsModel) {
        mContentViewBinding.data = orderDetailsModel
        mContentViewBinding.handler = OrderDetailsActivityHandler(this)
        addItemsOrderedFragment()
        setupMoreOptions()
    }

    private fun addItemsOrderedFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_frame, ItemOrderedFragment.newInstance(mIncrementId, mContentViewBinding.data!!), ItemOrderedFragment::class.java.simpleName)
        fragmentTransaction.commitAllowingStateLoss()
    }

    private fun setupMoreOptions() {
        Handler().postDelayed({
            if (this::mMenuItemMoreOptions.isInitialized) {
                mNavigationIconClickListener = NavigationIconClickListener(this,
                        mContentViewBinding.mainContainer
                        , mContentViewBinding.backLayer
                        , null
                        , ContextCompat.getDrawable(this, R.drawable.ic_down_arrow_action_bar)
                        , ContextCompat.getDrawable(this, R.drawable.ic_up_arrow_action_bar))
                mMenuItemMoreOptions.setOnMenuItemClickListener(mNavigationIconClickListener)
                mMenuItemMoreOptions.isVisible = !(mContentViewBinding.data!!.invoiceList.isNullOrEmpty() && mContentViewBinding.data!!.shipmentList.isNullOrEmpty() && mContentViewBinding.data!!.creditMemoList.isNullOrEmpty())
            }
        }, 1000)
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
                        getString(R.string.error),
                        response.message,
                        false,
                        getString(R.string.ok),
                        DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                            dialogInterface.dismiss()
                            if (mContentViewBinding.data == null)
                                finish()
                        }
                        , ""
                        , null)
            }
        }
    }

    private fun onErrorResponse(error: Throwable) {
        if ((!NetworkHelper.isNetworkAvailable(this) || (error is HttpException && error.code() == 304)) && mContentViewBinding.data != null) {
            // Do Nothing as the data is already loaded
        } else {
            AlertDialogHelper.showNewCustomDialog(
                    this,
                    getString(R.string.error),
                    NetworkHelper.getErrorMessage(this, error),
                    false,
                    getString(R.string.try_again),
                    DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                        callApi()
                    }
                    , getString(R.string.dismiss)
                    , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                finish()
            })
        }
    }

    class NavigationIconClickListener @JvmOverloads internal constructor(
            private val context: Context, private val sheet: View, private val backLayer: LinearLayoutCompat, private val interpolator: Interpolator? = null,
            private val openIcon: Drawable? = null, private val closeIcon: Drawable? = null) : MenuItem.OnMenuItemClickListener {

        override fun onMenuItemClick(p0: MenuItem?): Boolean {
            backdropShown = !backdropShown

            // Cancel the existing animations
            animatorSet.removeAllListeners()
            animatorSet.end()
            animatorSet.cancel()

            updateIcon(p0)

            val translateY = backLayer.height

            val animator = ObjectAnimator.ofFloat(sheet, "translationY", (if (backdropShown) translateY else 0).toFloat())
            animator.duration = 500
            if (interpolator != null) {
                animator.interpolator = interpolator
            }
            animatorSet.play(animator)
            animator.start()
            return true
        }

        private val animatorSet = AnimatorSet()
        private var backdropShown = false

        private fun updateIcon(view: MenuItem?) {
            if (openIcon != null && closeIcon != null) {
                if (backdropShown) {
                    view?.icon = closeIcon
                } else {
                    view?.icon = openIcon
                }
            }
        }
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration) {
        val currentFragment = supportFragmentManager.fragments.last()
        if (currentFragment is TrackDeliveryBoyFragment) {
            currentFragment.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        }
    }
}