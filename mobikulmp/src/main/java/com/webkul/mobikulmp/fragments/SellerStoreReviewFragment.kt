/**
 * Webkul Software.
 *
 * @author Webkul
 * @category Mobikul
 * @package com.webkul.mobikulmp.models.seller
 * @copyright Copyright (c) 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html
 * @date 15/7/19
 */

package com.webkul.mobikulmp.fragments


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.webkul.mobikul.fragments.BaseFragment
import com.webkul.mobikul.helpers.AppSharedPref
import com.webkul.mobikul.helpers.ConstantsHelper.RC_LOGIN
import com.webkul.mobikulmp.R
import com.webkul.mobikulmp.adapters.SellerFeedbackRvAdapter
import com.webkul.mobikulmp.databinding.FragmentSellerStoreReviewBinding
import com.webkul.mobikulmp.handlers.SellerStoreReviewHandler
import com.webkul.mobikulmp.helpers.MpBundleKeysHelper.BUNDLE_KEY_STORE_RATING_DATA
import com.webkul.mobikulmp.models.seller.SellerProfileData

class SellerStoreReviewFragment : BaseFragment() {
    lateinit var mContentViewBinding: FragmentSellerStoreReviewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContentViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_seller_store_review, container, false)
        mContentViewBinding.handler = SellerStoreReviewHandler(this)
        return mContentViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val sellerProfileData: SellerProfileData? = arguments?.getParcelable(BUNDLE_KEY_STORE_RATING_DATA)
        mContentViewBinding.data = sellerProfileData
        //display recent seller feedback
        mContentViewBinding.recentSellerFeedbackRv.layoutManager = LinearLayoutManager(context)
        mContentViewBinding.recentSellerFeedbackRv.adapter = mContentViewBinding.data!!.reviewList?.let { SellerFeedbackRvAdapter(context!!, it) }


        if (mContentViewBinding.data!!.averagePriceRating.equals(0.0) && mContentViewBinding.data!!.averageQualityRating.equals(0.0) && mContentViewBinding.data!!.averageValueRating.equals(0.0)) {
            setupReviewsPieCartLayout(0F, 0F, 0F, 0F, 0F, mContentViewBinding.emptyPieChart)
        } else {
            setupReviewsPieCartLayout(sellerProfileData!!.price1Star, sellerProfileData.price2Star, sellerProfileData.price3Star, sellerProfileData.price4Star, sellerProfileData.price5Star, mContentViewBinding.reviewPieChart)
            setupReviewsPieCartLayout(sellerProfileData.value1Star, sellerProfileData.value2Star, sellerProfileData.value3Star, sellerProfileData.value4Star, sellerProfileData.value5Star, mContentViewBinding.valueReviewPieChart)
            setupReviewsPieCartLayout(sellerProfileData.quality1Star, sellerProfileData.quality2Star, sellerProfileData.quality3Star, sellerProfileData.quality4Star, sellerProfileData.quality5Star, mContentViewBinding.qualityReviewPieChart)
        }

    }

    companion object {
        fun newInstance(data: SellerProfileData?): SellerStoreReviewFragment {
            val sellerStoreReviewFragment = SellerStoreReviewFragment()
            val args = Bundle()
            args.putParcelable(BUNDLE_KEY_STORE_RATING_DATA, data)
            sellerStoreReviewFragment.arguments = args
            return sellerStoreReviewFragment
        }
    }


    private fun setupReviewsPieCartLayout(starOne: Float, starTwo: Float, starThree: Float, starFour: Float, starFive: Float, reviewPieChart: PieChart) {
        reviewPieChart.isClickable = false
        reviewPieChart.legend.isEnabled = false
        reviewPieChart.isRotationEnabled = true
        reviewPieChart.holeRadius = 85f
        reviewPieChart.setTransparentCircleAlpha(0)
        reviewPieChart.setDrawEntryLabels(false)
        reviewPieChart.description.isEnabled = false

        val yEntrys = ArrayList<PieEntry>()

        if (mContentViewBinding.data!!.averagePriceRating.equals(0.0) && mContentViewBinding.data!!.averageQualityRating.equals(0.0) && mContentViewBinding.data!!.averageValueRating.equals(0.0)) {
            yEntrys.add(PieEntry(1f))
        } else {
            yEntrys.add(PieEntry(starOne))
            yEntrys.add(PieEntry(starTwo))
            yEntrys.add(PieEntry(starThree))
            yEntrys.add(PieEntry(starFour))
            yEntrys.add(PieEntry(starFive))
        }

        //create the data set
        val pieDataSet = PieDataSet(yEntrys, "Ratings")
        pieDataSet.setDrawValues(false)
        pieDataSet.sliceSpace = 0f

        //add colors to data set
        val colors = ArrayList<Int>()


        if (mContentViewBinding.data!!.averagePriceRating.equals(0.0) && mContentViewBinding.data!!.averageQualityRating.equals(0.0) && mContentViewBinding.data!!.averageValueRating.equals(0.0)) {
            colors.add(ContextCompat.getColor(context!!, R.color.grey_500))
        } else {
            colors.add(ContextCompat.getColor(context!!, R.color.single_star_color))
            colors.add(ContextCompat.getColor(context!!, R.color.two_star_color))
            colors.add(ContextCompat.getColor(context!!, R.color.three_star_color))
            colors.add(ContextCompat.getColor(context!!, R.color.four_star_color))
            colors.add(ContextCompat.getColor(context!!, R.color.five_star_color))
        }
        pieDataSet.colors = colors

        //create pie data object
        val pieData = PieData(pieDataSet)
        reviewPieChart.data = pieData
        reviewPieChart.invalidate()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_LOGIN && resultCode == Activity.RESULT_OK) {
            if (AppSharedPref.isLoggedIn(context!!)) {
                mContentViewBinding.loginTv.visibility = View.GONE
                mContentViewBinding.writeReviewBtn.visibility = View.VISIBLE
            }

        }
    }
}
