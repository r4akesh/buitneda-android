package com.webkul.mobikul.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.R
import com.webkul.mobikul.databinding.*
import com.webkul.mobikul.fragments.HomeFragment
import com.webkul.mobikul.handlers.HomePageBannerVpHandler
import com.webkul.mobikul.helpers.ConstantsHelper
import com.webkul.mobikul.models.homepage.BannerImage
import com.webkul.mobikul.models.product.AnalysisModel
import android.webkit.WebViewClient
import com.bumptech.glide.Glide


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
class HomePageVerticalBannerAdapter(
    private val mContext: Context,
    private val mFragment: HomeFragment,
    private val mListData: ArrayList<BannerImage>,
    private val carouselType: String,
    val layoutView: Int
) : RecyclerView.Adapter<HomePageVerticalBannerAdapter.ViewHolder>() {

    /* override fun onCreateViewHolder(
         parent: ViewGroup,
         viewType: Int
     ): ViewHolder {
         val view: View = LayoutInflater.from(mContext).inflate(R.layout.item_home_banner_view_pager, parent, false)
         return ViewHolder(view)
     }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (layoutView == ConstantsHelper.VIEW_TYPE_LIST) {
            ViewHolder(LayoutInflater.from(mFragment.context).inflate(R.layout.item_home_banner_view_pager, parent, false))
        } else {
            ViewHolder(
                LayoutInflater.from(mFragment.context)
                    .inflate(R.layout.row_animated_banner_layout, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /*  holder.itemViewPagerBannerBinding!!.data = mListData[position]
          holder.itemViewPagerBannerBinding.handler = HomePageBannerVpHandler(mFragment)
          carouselType.let {
              holder.itemViewPagerBannerBinding.analysisData = AnalysisModel(it, mListData[position].id)
          }
          holder.itemViewPagerBannerBinding.executePendingBindings()*/

        val eachListData = mListData[position]

       /* if(eachListData.promotion_type=="no"){
            (holder.mBinding as ItemHomeBannerViewPagerBinding).data = eachListData
            holder.mBinding.handler = HomePageBannerVpHandler(mFragment)
            carouselType.let {
                holder.mBinding.analysisData = AnalysisModel(it, mListData[position].id)
            }
            holder.mBinding.executePendingBindings()
        }else{
            (holder.mBinding as RowAnimatedBannerLayoutBinding).data = eachListData
            holder.mBinding.handler = HomePageBannerVpHandler(mFragment)
            carouselType.let {
                holder.mBinding.analysisData = AnalysisModel(it, mListData[position].id)
            }
            setUpGifView(holder.mBinding.gifWebView,eachListData.url!!)
            holder.mBinding.executePendingBindings()
        }*/

        if (layoutView == ConstantsHelper.VIEW_TYPE_LIST) {
            (holder.mBinding as ItemHomeBannerViewPagerBinding).data = eachListData
            holder.mBinding.handler = HomePageBannerVpHandler(mFragment)
            carouselType.let {
                holder.mBinding.analysisData = AnalysisModel(it, mListData[position].id)
            }
            holder.mBinding.executePendingBindings()
        } else {
            (holder.mBinding as RowAnimatedBannerLayoutBinding).data = eachListData
            holder.mBinding.handler = HomePageBannerVpHandler(mFragment)
            carouselType.let {
                holder.mBinding.analysisData = AnalysisModel(it, mListData[position].id)
            }

            holder.mBinding.executePendingBindings()
            Glide.with(mContext).asGif()
                .load(eachListData.url)
                .into(holder.mBinding.gifImageView)
        }

    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: ViewDataBinding? = DataBindingUtil.bind(itemView)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpGifView(webView: WebView, url:String){
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.setBackgroundColor(Color.TRANSPARENT)
        webView.loadUrl(url)

    }
}