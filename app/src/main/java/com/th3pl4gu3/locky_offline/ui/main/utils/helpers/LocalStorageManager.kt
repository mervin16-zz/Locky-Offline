package com.th3pl4gu3.locky_offline.ui.main.utils.helpers

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder

object LocalStorageManager {

    //Shared Preference field used to save and retrieve JSON string
    lateinit var preferences: SharedPreferences

    //Name of Shared Preference file
    private const val PREFERENCES_FILE_LOGIN = "com.th3pl4gu3.locky.PREF_LOGIN_DETAILS"
    private const val PREFERENCES_FILE_SETTINGS = "com.th3pl4gu3.locky.PREF_APP_SETTINGS"

    /**
     * Call this first before retrieving or saving object.
     *
     * @param application Instance of application class
     */
    fun withSettings(application: Application) {
        preferences =
            application.getSharedPreferences(PREFERENCES_FILE_SETTINGS, Context.MODE_PRIVATE)
    }

    fun withLogin(application: Application) {
        preferences = application.getSharedPreferences(PREFERENCES_FILE_LOGIN, Context.MODE_PRIVATE)
    }

    /**
     * Removes object into the Preferences.
     **/
    fun remove(key: String) {
        //Remove that entry in SharedPreferences
        preferences.edit().remove(key).apply()
    }

    /**
     * Saves object into the Preferences.
     *
     * @param `object` Object of model class (of type [T]) to save
     * @param key Key with which Shared preferences to
     **/
    fun <T> put(key: String, `object`: T) {
        //Save that String in SharedPreferences
        preferences.edit().putString(key, GsonBuilder().create().toJson(`object`)).apply()
    }

    /**
     * Used to retrieve object from the Preferences.
     *
     * @param key Shared Preference key with which object was saved.
     **/
    inline fun <reified T> get(key: String): T? {
        //We read JSON String which was saved.
        val value = preferences.getString(key, null)
        //JSON String was found which means object can be read.
        //We convert this JSON String to model object. Parameter "c" (of
        //type “T” is used to cast.
        return GsonBuilder().create().fromJson(value, T::class.java)
    }

    /**
     * Used to check if field is present from the Preferences.
     **/
    fun exists(key: String): Boolean = preferences.contains(key)
}