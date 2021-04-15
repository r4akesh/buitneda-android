package com.webkul.mobikulmp.handlers

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.View
import com.google.android.gms.maps.SupportMapFragment
import com.webkul.mobikul.activities.BaseActivity
import com.webkul.mobikulmp.activities.SellerProfileActivity
import com.webkul.mobikulmp.fragments.ReportSellerBottomSheetFragment
import com.webkul.mobikulmp.models.sellerinfo.ReportData

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
class SellerProfileActivityHandler(private val mContext: SellerProfileActivity, private val mSellerId: Int) {

    fun onClickViewLargeMap() {
        val supportMapFragment = SupportMapFragment.newInstance()
        val fragmentTransaction = mContext.supportFragmentManager.beginTransaction()
        fragmentTransaction.add(android.R.id.content, supportMapFragment, SupportMapFragment::class.java.simpleName)
        fragmentTransaction.addToBackStack(SupportMapFragment::class.java.simpleName)
        fragmentTransaction.commit()
        supportMapFragment.getMapAsync(mContext)
    }

    private val TYPE_FACEBOOK = 1
    private val TYPE_TWITTER = 2
    private val TYPE_INSTAGRAM = 3
    private val TYPE_PINTEREST = 4
    private val TYPE_YOUTUBE = 5
    private val TYPE_GOOGLE = 6
    private val TYPE_VIMEO = 7
    private var url: String? = null

    fun onClickSocialMedia(view: View, socialmediaId: String?, socialMediaType: Int) {
        var socialMediaId = socialmediaId ?: ""
        try {
            when (socialMediaType) {
                TYPE_TWITTER -> {
                    url = "https://twitter.com/$socialMediaId"
                    val sendIntent = Intent()
                    sendIntent.setPackage("com.twitter.android")
                    sendIntent.action = Intent.ACTION_VIEW

                    if (sendIntent.resolveActivity(mContext.packageManager) != null) {
                        sendIntent.data = Uri.parse("twitter://user?user_id=$socialMediaId")
                        mContext.startActivity(sendIntent)
                    } else {
                        openBrowser(mContext, url!!)
                    }

                }
                TYPE_YOUTUBE -> {
                    url = "https://www.youtube.com/channel/$socialMediaId"
                    val sendIntent = Intent(Intent.ACTION_VIEW)
                    sendIntent.setPackage("com.google.android.youtube")
                    sendIntent.data = Uri.parse(url)
                    if (sendIntent.resolveActivity(mContext.packageManager) != null) {
                        mContext.startActivity(sendIntent)
                    } else {
                        openBrowser(mContext, url!!)
                    }
                }
                TYPE_INSTAGRAM -> {
                    url = "https://www.instagram.com/$socialMediaId"
                    val sendIntent = Intent(Intent.ACTION_VIEW)
                    sendIntent.data = Uri.parse(url)
                    mContext.startActivity(sendIntent)

                }
                TYPE_FACEBOOK -> {
                    url = "https://www.facebook.com/$socialMediaId"
                    val intent = Intent(Intent.ACTION_VIEW)
                    val facebookUrl = getFacebookUrl(mContext, socialMediaId)
                    if (facebookUrl == null || facebookUrl.isEmpty()) {
                        Log.d("facebook Url", " is coming as " + facebookUrl!!)
                        return
                    }
                    intent.data = Uri.parse(facebookUrl)
                    mContext.startActivity(intent)

                }
                TYPE_PINTEREST -> {
                    url = "https://www.pinterest.com/$socialMediaId"
                    mContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("pinterest://www.pinterest.com/$socialMediaId")))

                }
                TYPE_GOOGLE -> {
                    url = "https://plus.google.com/$socialMediaId"
                    val intent = Intent(Intent.ACTION_VIEW)

                    intent.setPackage("com.google.android.apps.plus")
                    intent.data = Uri.parse(url)
                    if (intent.resolveActivity(mContext.packageManager) != null) {
                        mContext.startActivity(intent)
                    } else {
                        openBrowser(mContext, url!!)
                    }
                }
                TYPE_VIMEO -> {
                    try {
                        url = "http://player.vimeo.com/video/$socialMediaId"
                        var browserIntent: Intent? = null
                        val pmi = mContext.packageManager
                        browserIntent = pmi.getLaunchIntentForPackage("com.vimeo.android.videoapp")
                        browserIntent!!.action = Intent.ACTION_VIEW
                        browserIntent.data = Uri.parse(url)

                        mContext.startActivity(browserIntent)
                    } catch (e: Exception) {
                        openBrowser(mContext, url.toString())
                    }

                }

            }
        } catch (ex: Exception) {
            openBrowser(mContext, url!!)
        }

    }

    private fun getFacebookUrl(context: Context, socialMediaId: String): String? {

        val packageManager = context.packageManager
        try {
            val versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode
            if (versionCode >= 3002850) { //newer versions of fb app
                Log.d("facebook api", "new")
                return "fb://facewebmodal/f?href=" + url!!
            } else { //older versions of fb app
                Log.d("facebook api", "old")
                return "fb://page/$socialMediaId"
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.d("facebook api", "exception")
            return url //normal web url
        }

    }


    private fun openBrowser(context: Context, url: String) {
        val sendIntent = Intent(Intent.ACTION_VIEW)
        sendIntent.data = Uri.parse(url.trim { it <= ' ' })
        context.startActivity(sendIntent)
    }


    fun onClickReportProduct(reportData: ReportData, id:Int,sellerName:String){
        val mReportSellerBottomSheetFragment= ReportSellerBottomSheetFragment.newInstance(1,reportData, id.toString(),sellerName)
        mReportSellerBottomSheetFragment.show((mContext as BaseActivity).supportFragmentManager, ReportSellerBottomSheetFragment::class.java.simpleName)
    }

}
