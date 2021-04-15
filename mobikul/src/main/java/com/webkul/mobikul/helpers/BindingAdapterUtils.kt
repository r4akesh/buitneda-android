/*
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

package com.webkul.mobikul.helpers

import android.annotation.TargetApi
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.webkit.WebSettingsCompat
import com.airbnb.lottie.LottieAnimationView
import com.amulyakhare.textdrawable.TextDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.textfield.TextInputLayout
import com.webkul.mobikul.R
import com.webkul.mobikul.helpers.ApplicationConstants.BASE_URL
import com.webkul.mobikul.helpers.ConstantsHelper.ORDER_STATUS_CANCELLED
import com.webkul.mobikul.helpers.ConstantsHelper.ORDER_STATUS_CLOSED
import com.webkul.mobikul.helpers.ConstantsHelper.ORDER_STATUS_COMPLETE
import com.webkul.mobikul.helpers.ConstantsHelper.ORDER_STATUS_HOLD
import com.webkul.mobikul.helpers.ConstantsHelper.ORDER_STATUS_NEW
import com.webkul.mobikul.helpers.ConstantsHelper.ORDER_STATUS_PENDING
import com.webkul.mobikul.helpers.ConstantsHelper.ORDER_STATUS_PROCESSING
import com.webkul.mobikul.models.product.SwatchData
import java.util.*

class BindingAdapterUtils {

    companion object {

        @JvmStatic
        @BindingAdapter("layout_width")
        fun setLayoutWidth(view: View, width: Int) {
            val layoutParams = view.layoutParams
            layoutParams.width = width
            view.layoutParams = layoutParams
        }

        @JvmStatic
        @BindingAdapter("layout_height")
        fun setLayoutHeight(view: View, height: Int) {
            val layoutParams = view.layoutParams
            layoutParams.height = height
            view.layoutParams = layoutParams
        }

        @JvmStatic
        @BindingAdapter(value = ["imageUrl", "placeholder"], requireAll = false)
        fun setImageUrl(view: ImageView, imageUrl: String?, placeholder: String?) {
            ImageHelper.load(view, imageUrl, placeholder)
        }

        @JvmStatic
        @BindingAdapter("tiledBackgroundImageUrl")
        fun setTiledBackgroundImageUrl(view: ImageView, imageUrl: String?) {
            if (!imageUrl.isNullOrBlank()) {
                Glide.with(view.context).asBitmap().load(imageUrl).into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        val bitmapDrawable = BitmapDrawable(view.resources, resource)
                        bitmapDrawable.tileModeX = Shader.TileMode.REPEAT
                        bitmapDrawable.tileModeY = Shader.TileMode.REPEAT
                        view.background = bitmapDrawable
                    }
                })
            }
        }

        @JvmStatic
        @BindingAdapter("backgroundColor")
        fun setBackgroundColor(view: View, colorHexCode: String?) {
            try {
                if (colorHexCode != null && colorHexCode.isNotBlank())
                    view.setBackgroundColor(Color.parseColor(colorHexCode))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        @JvmStatic
        @BindingAdapter("srcCompat")
        fun setSrcCompat(view: ImageView, drawable: Drawable) {
            view.setImageDrawable(drawable)
        }

        @JvmStatic
        @BindingAdapter("isInWishList")
        fun setIsInWishList(view: LottieAnimationView, isInWishList: Boolean) {
            if (isInWishList) view.progress = 1f else view.progress = 0f
        }

        @JvmStatic
        @BindingAdapter("drawableId")
        fun setImageByDrawableId(view: ImageView, drawableId: Int) {
            if (drawableId != 0)
                view.setImageDrawable(ContextCompat.getDrawable(view.context, drawableId))
        }

        @JvmStatic
        @BindingAdapter("orderStatusBackground")
        fun setOrderStatusBackground(view: TextView, status: String?) {
            var drawable = ColorDrawable(Color.parseColor("#7986CB"))
            if (status != null) {
                when (status.toLowerCase()) {
                    ORDER_STATUS_COMPLETE -> drawable = ColorDrawable(ContextCompat.getColor(view.context, R.color.orderStatusCompleteColor))

                    ORDER_STATUS_PENDING -> drawable = ColorDrawable(ContextCompat.getColor(view.context, R.color.orderStatusPendingColor))

                    ORDER_STATUS_PROCESSING -> drawable = ColorDrawable(ContextCompat.getColor(view.context, R.color.orderStatusProcessingColor))

                    ORDER_STATUS_HOLD -> drawable = ColorDrawable(ContextCompat.getColor(view.context, R.color.orderStatusHoldColor))

                    ORDER_STATUS_CANCELLED -> drawable = ColorDrawable(ContextCompat.getColor(view.context, R.color.orderStatusCancelColor))

                    ORDER_STATUS_NEW -> drawable = ColorDrawable(ContextCompat.getColor(view.context, R.color.orderStatusNewColor))

                    ORDER_STATUS_CLOSED -> drawable = ColorDrawable(ContextCompat.getColor(view.context, R.color.orderStatusClosedColor))
                }
            }

            view.background = drawable
        }

        @JvmStatic
        @BindingAdapter("loadHtmlText")
        fun setLoadHtmlText(textView: TextView, htmlText: String?) {
            if (htmlText != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    textView.text = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    textView.text = Html.fromHtml(htmlText)
                }
            }
        }

        @JvmStatic
        @BindingAdapter("loadWebViewData")
        fun setLoadData(webView: WebView, content: String?) {
            if (content == null) {
                return
            }

            val htmlContent = "<!DOCTYPE html>" +
                    "<html lang=\"en\">" +
                    "<head>" +
                    "<style>" +
                    "img{" +
                    "    height: auto;" +
                    "    max-width: auto;" +
                    "}" +
                    "</style>" +
                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">" +
                    "<meta charset=\"utf-8\">" +
                    "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + BASE_URL + "/pub/media/styles.css\" media=\"all\">" +
                    "<link rel=\"stylesheet\" type=\"text/css\" media=\"all\" href=\"" + BASE_URL + "/pub/static/version1552020129/_cache/merged/9e5d309101b494f03ad662f3809381e2.min.css\">" +
                    "<link rel=\"stylesheet\" type=\"text/css\" media=\"screen and (min-width: 768px)\" href=\"" + BASE_URL + "/pub/static/version1552020129/frontend/Magento/luma/en_US/css/styles-l.min.css\">" +
                    "<script type=\"text/javascript\" src=\"" + BASE_URL + "/pub/static/version1552020129/_cache/merged/46c637c347e4d4a95871eb42fdf6692b.min.js\"></script>" +
                    "</head>" +
                    "<body data-container=\"body\" class=\"cms-privacy-policy-cookie-restriction-mode cms-page-view\">" +
                    "<div class=\"page-wrapper\">" +
                    "<main id=\"maincontent\" class=\"page-main\">" +
                    "<div class=\"columns\">" +
                    "<div class=\"column main\">" +
                    content +
                    "</div>" +
                    "</div>" +
                    "</main>" +
                    "</div>" +
                    "</body>" +
                    "</html>"

//            webView.loadData(htmlContent, "text/html; charset=utf-8", "UTF-8")
            webView.loadDataWithBaseURL(BASE_URL, htmlContent, "text/html; charset=utf-8", "UTF-8", "")

            val webSettings = webView.settings
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);
            webSettings.minimumFontSize = 34
            try {
                val nightModeFlags = webView.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                    WebSettingsCompat.setForceDark(webSettings, WebSettingsCompat.FORCE_DARK_ON)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            webSettings.defaultFontSize = 14
            webSettings.defaultTextEncodingName = "utf-8"

            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    if (url.contains("goo.gl")) {
                        val gmmIntentUri = Uri.parse(url)
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage("com.google.android.apps.maps")
                        if (mapIntent.resolveActivity(webView.context.packageManager) != null) {
                            webView.context. startActivity(mapIntent)
                        }
                        return true
                    } else {
                        CustomTabsHelper.openTab(webView.context, url)
                        return true
                    }

                }

                @TargetApi(Build.VERSION_CODES.N)
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    if (request.url.toString().contains("goo.gl")) {
                        val gmmIntentUri = Uri.parse(request.url.toString())
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage("com.google.android.apps.maps")
                        if (mapIntent.resolveActivity(webView.context.packageManager) != null) {
                            webView.context.startActivity(mapIntent)
                        }
                        return true
                    } else {
                        CustomTabsHelper.openTab(webView.context, request.url.toString())
                        return true
                    }
                }
            }

            LocaleUtils.updateConfig(webView.context)
        }

        @JvmStatic
        @BindingAdapter("loadCmsData")
        fun setCmsData(webView: WebView, content: String?) {
            if (content == null) {
                return
            }


            val htmlContent = "<!DOCTYPE html>" +
                    "<html lang=\"en\">" +
                    "<head>" +
                    "<style>" +
                    "img{" +
                    "    height: auto;" +
                    "    max-width: 100%;" +
                    "}" +
                    "</style>" +
                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">" +
                    "<meta charset=\"utf-8\">" +
                    "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + BASE_URL + "/pub/media/styles.css\" media=\"all\">" +
                    "<link rel=\"stylesheet\" type=\"text/css\" media=\"all\" href=\"" + BASE_URL + "/pub/static/version1552020129/_cache/merged/9e5d309101b494f03ad662f3809381e2.min.css\">" +
                    "<link rel=\"stylesheet\" type=\"text/css\" media=\"screen and (min-width: 768px)\" href=\"" + BASE_URL + "/pub/static/version1552020129/frontend/Magento/luma/en_US/css/styles-l.min.css\">" +
                    "<script type=\"text/javascript\" src=\"" + BASE_URL + "/pub/static/version1552020129/_cache/merged/46c637c347e4d4a95871eb42fdf6692b.min.js\"></script>" +
                    "</head>" +
                    "<body data-container=\"body\" class=\"cms-privacy-policy-cookie-restriction-mode cms-page-view\" style=\"padding:20px 5px 5px 5px;\">" +
                    "<div class=\"page-wrapper\">" +
                    "<main id=\"maincontent\" class=\"page-main\">" +
                    "<div class=\"columns\">" +
                    "<div class=\"column main\">" +
                    content +
                    "</div>" +
                    "</div>" +
                    "</main>" +
                    "</div>" +
                    "</body>" +
                    "</html>"

//            webView.loadData(htmlContent, "text/html", "UTF-8")
            webView.loadDataWithBaseURL("", htmlContent, "text/html; charset=utf-8", "UTF-8", "")

            val webSettings = webView.settings

            try {
                val nightModeFlags = webView.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                    WebSettingsCompat.setForceDark(webSettings, WebSettingsCompat.FORCE_DARK_ON)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            webSettings.defaultFontSize = 14

            LocaleUtils.updateConfig(webView.context)
        }

        @JvmStatic
        @BindingAdapter("swatchData")
        fun loadSwatchData(imageView: ImageView, swatchData: SwatchData) {
            if (swatchData.type == "1") {
                imageView.setBackgroundColor(Color.parseColor(swatchData.value))
            } else if (swatchData.type == "2") {
                ImageHelper.load(imageView, swatchData.value, null)
            }
        }

        @JvmStatic
        @BindingAdapter("ratingColor")
        fun setRatingColor(textView: AppCompatTextView, ratingValue: String?) {

            if (ratingValue != null && ratingValue.isNotBlank()) {
                when {
                    ratingValue.toDouble() < 2 -> textView.setBackgroundColor(ContextCompat.getColor(textView.context, R.color.single_star_color))
                    ratingValue.toDouble() < 3 -> textView.setBackgroundColor(ContextCompat.getColor(textView.context, R.color.two_star_color))
                    ratingValue.toDouble() < 4 -> textView.setBackgroundColor(ContextCompat.getColor(textView.context, R.color.three_star_color))
                    ratingValue.toDouble() < 5 -> textView.setBackgroundColor(ContextCompat.getColor(textView.context, R.color.four_star_color))
                    ratingValue.toDouble() == 5.toDouble() -> textView.setBackgroundColor(ContextCompat.getColor(textView.context, R.color.five_star_color))
                }
            }
        }

        @JvmStatic
        @BindingAdapter("hintWithAsterisk")
        fun setHintWithAsterisk(view: TextInputLayout, hint: String?) {
            val textSpannable = SpannableString("$hint*")
            textSpannable.setSpan(ForegroundColorSpan(ContextCompat.getColor(view.context!!, R.color.text_color_secondary)), 0, textSpannable.length - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            textSpannable.setSpan(ForegroundColorSpan(Color.RED), textSpannable.length - 1, textSpannable.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            view.hint = textSpannable
        }

        @JvmStatic
        @BindingAdapter("textWithAsterisk")
        fun setTextWithAsterisk(view: AppCompatTextView, hint: String?) {
            val textSpannable = SpannableString("$hint*")
            textSpannable.setSpan(ForegroundColorSpan(ContextCompat.getColor(view.context!!, R.color.text_color_secondary)), 0, textSpannable.length - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            textSpannable.setSpan(ForegroundColorSpan(Color.RED), textSpannable.length - 1, textSpannable.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            view.hint = textSpannable
        }


        @JvmStatic
        @BindingAdapter("circleTextDrawable")
        fun setCircleTextDrawable(view: ImageView, text: String) {
            val textDrawable = TextDrawable.builder()
                    .beginConfig()
                    .textColor(Color.WHITE)
                    .toUpperCase()
                    .endConfig()
                    .buildRoundRect(text[0].toString().toUpperCase(Locale.getDefault()),
                            ContextCompat.getColor(view.context, R.color.orderStatusNewColor),
                            60)
            view.setImageDrawable(textDrawable)
        }
    }
}