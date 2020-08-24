package com.th3pl4gu3.locky_offline.ui.main

import android.app.Application
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.updateAppTheme

class LockyApplication : Application() {

    companion object {
        @Volatile
        private var INSTANCE: LockyApplication? = null

        fun getInstance() =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: LockyApplication().also { INSTANCE = it }
            }
    }

    override fun onCreate() {
        super.onCreate()

        /* Assigns this to the singleton object */
        INSTANCE = this

        /* Updates the app theme*/
        updateAppTheme()
    }

}