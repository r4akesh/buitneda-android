package com.webkul.mobikul.handlers

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.webkul.mobikul.R

data class ImageUrl(
    val fastLoadUrl: String?,
    val fullImageUrl: String,
    val listener: MyImageRequestListener.Callback
)

@BindingAdapter("android:src")
fun setImageUrl(view: ImageView, imageUrl: ImageUrl?) {
    imageUrl?.let {
        val requestOption = RequestOptions()
            .placeholder(R.drawable.placeholder).centerCrop()

        Glide.with(view.context).load(it.fullImageUrl)
//            .transition(DrawableTransitionOptions.withCrossFade())
//            .thumbnail
//                Glide.with(view.context)
//                    .load(it.fastLoadUrl)
//                    .apply(requestOption)
//            )
            .apply(requestOption)
            .listener(MyImageRequestListener(it.listener))
            .into(view)
    }
}
