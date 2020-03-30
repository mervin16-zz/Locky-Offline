package com.th3pl4gu3.locky.ui.main

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.Window
import android.widget.Toast
import com.th3pl4gu3.locky.R


fun Context.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, text, duration).show()

fun Window.activateLightStatusBar(view: View) {
    var flags: Int = view.systemUiVisibility
    flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    view.systemUiVisibility = flags
    this.statusBarColor = Color.WHITE
}

fun Window.activateDarkStatusBar() {
    this.statusBarColor = context.getColor(R.color.colorPrimary)
}