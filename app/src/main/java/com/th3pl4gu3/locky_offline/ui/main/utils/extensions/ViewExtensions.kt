package com.th3pl4gu3.locky_offline.ui.main.utils.extensions

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar


/*
* Load an image from URL
*/
fun ImageView.loadImageUrl(uri: Uri?, loadingResource: Drawable?, errorResource: Drawable?) {
    Glide.with(this.context)
        .load(uri)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .circleCrop()
        .placeholder(loadingResource)
        .error(errorResource)
        .into(this)
}


/*
* SnackBar helpers
*/
inline fun View.snack(
    message: String,
    length: Int = Snackbar.LENGTH_SHORT,
    f: Snackbar.() -> Unit
) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}

inline fun View.snackBar(
    message: String,
    length: Int = Snackbar.LENGTH_INDEFINITE,
    f: Snackbar.() -> Unit
) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}

fun Snackbar.action(action: String, listener: (View) -> Unit) {
    setAction(action, listener)
}