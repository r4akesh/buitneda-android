package com.webkul.mobikul.helpers

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.facebook.shimmer.ShimmerFrameLayout
import com.webkul.mobikul.R
import com.webkul.mobikul.helpers.ApplicationConstants.ENABLE_SHIMMER_EFFECT

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
class ImageHelper {

    companion object {

        @JvmStatic
        fun load(view: ImageView, imageUrl: String?, placeholder: String?) {

            val parentView = view.parent
            if (ENABLE_SHIMMER_EFFECT && parentView is ShimmerFrameLayout) {
                parentView.startShimmerAnimation()
            }

            val glideCallBack = object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                    if (parentView is ShimmerFrameLayout) {
                        parentView.stopShimmerAnimation()
                    }
                    return false
                }

                override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                    if (parentView is ShimmerFrameLayout) {
                        parentView.stopShimmerAnimation()
                    }
                    return false
                }
            }

            if (placeholder.isNullOrBlank()) {
                Glide.with(view.context)
                        .load(imageUrl ?: "")
                        .thumbnail(0.1f)
                        .listener(glideCallBack)
                        .apply(RequestOptions()
                                .placeholder(R.drawable.placeholder)
                                .dontAnimate())
                        .into(view)
            } else {
                Glide.with(view.context)
                        .load(imageUrl ?: "")
                        .thumbnail(0.1f)
                        .listener(glideCallBack)
                        .apply(RequestOptions()
                                .placeholder(ColorDrawable(Color.parseColor(placeholder)))
                                .dontAnimate())
                        .into(view)
            }

            /* To remove all colors in offline mode */
//            val matrix = ColorMatrix()
//            if (NetworkHelper.isNetworkAvailable(view.context)) {
//                matrix.setSaturation(1f)
//            } else {
//                matrix.setSaturation(0f)
//            }
//            view.colorFilter = ColorMatrixColorFilter(matrix)
        }
    }
}