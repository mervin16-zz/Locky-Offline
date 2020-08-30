package com.th3pl4gu3.locky_offline.ui.main.main.password_generator

import android.app.Application
import androidx.databinding.Bindable
import com.th3pl4gu3.locky_offline.BR
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.getString
import com.th3pl4gu3.locky_offline.ui.main.utils.helpers.LocalStorageManager
import com.th3pl4gu3.locky_offline.ui.main.utils.helpers.ObservableViewModel

class PasswordGeneratorViewModel(application: Application) : ObservableViewModel(application) {

    private var _password = ""
    private var _hasLowercase = false
    private var _hasUppercase = false
    private var _hasNumbers = false
    private var _hasDash = false
    private var _hasSpecials = false

    var password: String
        @Bindable get() {
            return _password
        }
        set(value) {
            _password = value
            notifyPropertyChanged(BR.password)
        }

    var hasLowercase: Boolean
        @Bindable get() {
            return _hasLowercase
        }
        set(value) {
            _hasLowercase = value
            notifyPropertyChanged(BR.hasLowercase)

            /* Saves the criteria value */
            save(
                getString(R.string.settings_key_passwordgen_haslower),
                value
            )
            /* Automatically regenerates password */
            regeneratePassword()
        }

    var hasUppercase: Boolean
        @Bindable get() {
            return _hasUppercase
        }
        set(value) {
            _hasUppercase = value
            notifyPropertyChanged(BR.hasUppercase)

            /* Saves the criteria value */
            save(
                getString(R.string.settings_key_passwordgen_hasupper),
                value
            )
            /* Automatically regenerates password */
            regeneratePassword()
        }

    var hasNumbers: Boolean
        @Bindable get() {
            return _hasNumbers
        }
        set(value) {
            _hasNumbers = value
            notifyPropertyChanged(BR.hasNumbers)

            /* Saves the criteria value */
            save(
                getString(R.string.settings_key_passwordgen_hasnumbers),
                value
            )
            /* Automatically regenerates password */
            regeneratePassword()
        }

    var hasDash: Boolean
        @Bindable get() {
            return _hasDash
        }
        set(value) {
            _hasDash = value
            notifyPropertyChanged(BR.hasDash)

            /* Saves the criteria value */
            save(
                getString(R.string.settings_key_passwordgen_hasdash),
                value
            )
            /* Automatically regenerates password */
            regeneratePassword()
        }

    var hasSpecials: Boolean
        @Bindable get() {
            return _hasSpecials
        }
        set(value) {
            _hasSpecials = value
            notifyPropertyChanged(BR.hasSpecials)

            /* Saves the criteria value */
            save(
                getString(R.string.settings_key_passwordgen_hasspecials),
                value
            )
            /* Automatically regenerates password */
            regeneratePassword()
        }

    /*
    * Init
    */
    init {
        /* Pre load the criterias */
        preloadCriterias()
    }

    /*
    * Accessible Functions
    */
    fun regeneratePassword() = with(PasswordGenerator) {
        withOptions(hasLowercase, hasUppercase, hasNumbers, hasDash, hasSpecials)
        password = generate()
    }

    /*
    * Inaccessible Functions
    */
    private fun preloadCriterias() {
        hasLowercase =
            get(getString(R.string.settings_key_passwordgen_haslower))
        hasUppercase =
            get(getString(R.string.settings_key_passwordgen_hasupper))
        hasNumbers =
            get(getString(R.string.settings_key_passwordgen_hasnumbers))
        hasDash =
            get(getString(R.string.settings_key_passwordgen_hasdash))
        hasSpecials =
            get(getString(R.string.settings_key_passwordgen_hasspecials))
    }

    private fun save(key: String, value: Boolean) = with(LocalStorageManager) {
        withSettings(getApplication())
        put(key, value)
    }

    private fun get(key: String) = with(LocalStorageManager) {
        withSettings(getApplication())
        get<Boolean>(key) ?: false
    }
}