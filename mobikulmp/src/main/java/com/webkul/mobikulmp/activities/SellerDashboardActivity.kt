package com.webkul.mobikulmp.activities

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikul.helpers.AlertDialogHelper
import com.webkul.mobikul.helpers.ImageHelper
import com.webkul.mobikul.helpers.NetworkHelper
import com.webkul.mobikul.models.BaseModel
import com.webkul.mobikul.network.ApiCustomCallback
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.adapters.SalesPagerAdapter
import com.webkul.mobikulmp.adapters.SellerFeedbackRvAdapter
import com.webkul.mobikulmp.adapters.SellerLatestOrderListRvAdapter
import com.webkul.mobikulmp.adapters.TopSellingProductsRvAdapter
import com.webkul.mobikulmp.databinding.ActivitySellerDashboardBinding
import com.webkul.mobikulmp.databinding.ReportTimeRangeMenuBinding
import com.webkul.mobikulmp.fragments.SellerRecentOrdersListFragment
import com.webkul.mobikulmp.handlers.SellerDashboardActivityHandler
import com.webkul.mobikulmp.handlers.SellerSalesPagerItemHandler
import com.webkul.mobikulmp.helpers.MpAppSharedPref
import com.webkul.mobikulmp.models.seller.SellerDashboardData
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
 * @package com.webkul.mobikulmp.fragments
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 16/6/19
 */

class SellerDashboardActivity : BaseActivity(), SellerRecentOrdersListFragment.OnDetachInterface {

    lateinit var mContentViewBinding: ActivitySellerDashboardBinding
    var selectMapTimeIndex = 3
    var selectGraphTimeIndex = 3
    var mItemTimeRange: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_seller_dashboard)
        startInitialization()
    }

    private fun startInitialization() {
        initSupportActionBar()
        MpAppSharedPref.setIsSeller(this, true)
        MpAppSharedPref.setIsSellerPending(this, false)
        callAPI()
    }

    private fun callAPI() {
        MpApiConnection.getSellerDashboardData(this)
        mContentViewBinding.loading = true
        MpApiConnection.getSellerDashboardData(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : ApiCustomCallback<SellerDashboardData>(this, true) {
                    override fun onNext(sellerDashboardData: SellerDashboardData) {
                        super.onNext(sellerDashboardData)
                        mContentViewBinding.loading = false

                        if (sellerDashboardData.success) {
                            onSuccessResponse(sellerDashboardData)
                        } else {
                            onFailureResponse(sellerDashboardData)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        onErrorResponse(e)
                    }
                })
    }


    fun onSuccessResponse(sellerDashboardData: SellerDashboardData) {

        mContentViewBinding.data = sellerDashboardData
        mContentViewBinding.handler = SellerDashboardActivityHandler(this@SellerDashboardActivity, mContentViewBinding.data!!)

        /*Init Top Selling Product Adapter*/
        if (sellerDashboardData.topSellingProducts.size > 0) {
            mContentViewBinding.topProductsRv.adapter = TopSellingProductsRvAdapter(this@SellerDashboardActivity, sellerDashboardData.topSellingProducts)
            mContentViewBinding.topProductsRv.isNestedScrollingEnabled = false
        }

        /*Init Recent Order List Product Adapter*/
        if (sellerDashboardData.recentOrderList.size > 0) {
            mContentViewBinding.topLatestOrders.adapter = SellerLatestOrderListRvAdapter(this@SellerDashboardActivity, sellerDashboardData.recentOrderList)
            mContentViewBinding.topLatestOrders.isNestedScrollingEnabled = false
        }

        /*Init Recent Review List Adapter*/
        sellerDashboardData.sellerReviewList?.let {
            if (it.isNotEmpty())
                mContentViewBinding.sellerReviewRv.adapter = SellerFeedbackRvAdapter(this@SellerDashboardActivity, it)
            mContentViewBinding.sellerReviewRv.isNestedScrollingEnabled = false
        }

        /*Init Sales View Pager*/
        mContentViewBinding.statsViewPager.adapter = SalesPagerAdapter(this@SellerDashboardActivity, sellerDashboardData.yearlySalesLocationReport, sellerDashboardData.yearlySalesStats, sellerDashboardData.categoryChart!!)
        mContentViewBinding.statsViewPager.offscreenPageLimit = 2
        mContentViewBinding.statsTabs.setupWithViewPager(mContentViewBinding.statsViewPager)

        mContentViewBinding.statsViewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                mItemTimeRange?.isVisible = position != 2
            }
        })
    }

    override fun onFailureResponse(response: Any) {
        AlertDialogHelper.showNewCustomDialog(
                this,
                getString(com.webkul.mobikul.R.string.error),
                (response as BaseModel).message,
                false,
                getString(com.webkul.mobikul.R.string.ok),
            { dialogInterface: DialogInterface, _: Int ->
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
                        callAPI()
                    }
                    , getString(com.webkul.mobikul.R.string.close)
                    , DialogInterface.OnClickListener { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            })
        }
    }

    private fun initSupportActionBar() {
        supportActionBar?.title = getString(R.string.seller_dashboard)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.clear()
        menuInflater.inflate(R.menu.seller_dashboard, menu)
        mItemTimeRange = menu.findItem(R.id.menu_time_range)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        if (item.itemId == R.id.menu_time_range) {
            if (mContentViewBinding.data != null) {
                val layout = layoutInflater.inflate(R.layout.report_time_range_menu, null)
                layout.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

                val mTimeRangePopupWindow = PopupWindow(layout, 290, layout.measuredHeight + 50, true)

                mTimeRangePopupWindow.isOutsideTouchable = true
                mTimeRangePopupWindow.isFocusable = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mTimeRangePopupWindow.elevation = 10f
                }
                mTimeRangePopupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))


                val mBinding: ReportTimeRangeMenuBinding? = DataBindingUtil.bind(layout)
                val count = mContentViewBinding.statsViewPager.currentItem
                val listImages = ArrayList<String>()
                if (count == 0) {
                    mBinding?.selectedDay = resources.getStringArray(R.array.time_range)[selectMapTimeIndex]
                    listImages.add(mContentViewBinding.data!!.dailySalesLocationReport)
                    listImages.add(mContentViewBinding.data!!.weeklySalesLocationReport)
                    listImages.add(mContentViewBinding.data!!.monthlySalesLocationReport)
                    listImages.add(mContentViewBinding.data!!.yearlySalesLocationReport)
                } else if (count == 1) {
                    mBinding?.selectedDay = resources.getStringArray(R.array.time_range)[selectGraphTimeIndex]
                    listImages.add(mContentViewBinding.data!!.dailySalesStats)
                    listImages.add(mContentViewBinding.data!!.weeklySalesStats)
                    listImages.add(mContentViewBinding.data!!.monthlySalesStats)
                    listImages.add(mContentViewBinding.data!!.yearlySalesStats)
                }
                mBinding?.handler = SellerSalesPagerItemHandler(this, mTimeRangePopupWindow, listImages)
                mTimeRangePopupWindow.showAtLocation(mContentViewBinding.mainContainer, Gravity.END, 30, -250)
            }
        }
        return true
    }

    fun setImage(imageUrl: String, selectedTime: Int) {
        val count = mContentViewBinding.statsViewPager.currentItem
        if (count == 0) {
            val view: View = mContentViewBinding.statsViewPager.findViewWithTag(0)
            ImageHelper.load(view.findViewById(R.id.stats_iv), imageUrl, "")
            selectMapTimeIndex = selectedTime
        } else {
            val view: View = mContentViewBinding.statsViewPager.findViewWithTag(1)
            ImageHelper.load(view.findViewById(R.id.stats_iv), imageUrl, "")
            selectGraphTimeIndex = selectedTime
        }
    }

    override fun onFragmentDetached() {
        initSupportActionBar()
        mItemTimeRange?.isVisible = true
    }


}
