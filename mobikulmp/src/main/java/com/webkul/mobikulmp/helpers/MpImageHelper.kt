package com.webkul.mobikulmp.helpers

import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.facebook.shimmer.ShimmerFrameLayout
import com.webkul.mobikul.R
import java.io.File

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
class MpImageHelper {

    companion object {

        @JvmStatic
        fun loadFromFile(view: AppCompatImageView, image: File) {
            val parentView = view.parent
            if (parentView is ShimmerFrameLayout) {
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

            Glide.with(view.context)
                    .load(image)
                    .listener(glideCallBack)
                    .apply(RequestOptions()
                            .placeholder(R.drawable.placeholder)
                            .dontAnimate())
                    .into(view)
        }
    }
}