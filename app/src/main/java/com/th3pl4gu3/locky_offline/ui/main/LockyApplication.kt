package com.th3pl4gu3.locky_offline.ui.main

import android.app.Application
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.updateAppTheme

class LockyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        /* Updates the app theme*/
        updateAppTheme()
    }

}