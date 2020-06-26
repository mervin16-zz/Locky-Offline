package com.th3pl4gu3.locky_offline.ui.main.utils.extensions

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.main.User
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants
import com.th3pl4gu3.locky_offline.ui.main.utils.LocalStorageManager

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
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
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
            Constants.KEY_USER_ACCOUNT
        )!!
    }