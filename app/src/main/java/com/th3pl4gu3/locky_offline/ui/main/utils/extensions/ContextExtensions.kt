package com.th3pl4gu3.locky_offline.ui.main.utils.extensions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast

/*
* Toast helper functions
*/
fun Context.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    with(Toast.makeText(this, text, duration)) {
        show()
    }
}

/*
* Open an activity
*/
fun <T> Context.openActivity(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
}