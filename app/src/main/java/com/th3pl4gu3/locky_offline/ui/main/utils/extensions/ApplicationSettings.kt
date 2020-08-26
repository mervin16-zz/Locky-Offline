package com.th3pl4gu3.locky_offline.ui.main.utils.extensions

import android.app.Application
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.others.User
import com.th3pl4gu3.locky_offline.ui.main.utils.helpers.LocalStorageManager
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.KEY_USER_ACCOUNT

/*
* Updates the application's theme
*/
fun Application.updateAppTheme() {
    LocalStorageManager.withSettings(
        this
    )
    when (LocalStorageManager.get<String>(
        getString(R.string.settings_key_display_theme)
    )) {
        getString(R.string.settings_value_display_default) -> AppCompatDelegate.setDefaultNightMode(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            } else {
                AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
            }
        )
        getString(R.string.settings_value_display_light) -> AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_NO
        )
        getString(R.string.settings_value_display_dark) -> AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_YES
        )
    }
}

/*
* Returns the current user
*/
val Application.activeUser: User
    get() {
        LocalStorageManager.withLogin(
            this
        )
        return LocalStorageManager.get<User>(
            KEY_USER_ACCOUNT
        )!!
    }