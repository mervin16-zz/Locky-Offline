package com.th3pl4gu3.locky_offline.ui.main

import android.app.Application
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.updateAppTheme

/*
* This is the Application class for Locky
* The class is a singleton so that we can access it anywhere
* E.g access it from Binding Adapters
*/
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