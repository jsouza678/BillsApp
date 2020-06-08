package com.souza.billsapp.sharedextensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.souza.billsapp.R
import com.souza.billsapp.sharedextensions.utils.Constants.Companion.IMAGE_MAX_HEIGHT
import com.souza.billsapp.sharedextensions.utils.Constants.Companion.IMAGE_MAX_WIDTH

private var placeHolderId: Int = R.drawable.place_holder
private var errorImageId: Int = R.drawable.error_image

fun ImageView.loadImageUrl(
    url: String? = null,
    onLoadCompleted: () -> Unit = {},
    onError: () -> Unit = {}
) {
    val requestBuilder = Glide.with(context)
        .`as`(Drawable::class.java)
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                onError()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                onLoadCompleted()
                return false
            }
        })

    placeHolderId.let {
        requestBuilder
            .placeholder(placeHolderId)
            .override(IMAGE_MAX_WIDTH, IMAGE_MAX_HEIGHT)
            .error(errorImageId)
    }

    requestBuilder
        .load(url)
        .into(this)
}
