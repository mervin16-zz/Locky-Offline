package com.th3pl4gu3.locky_offline.ui.main.add.device

import android.app.Application
import androidx.databinding.Bindable
import com.th3pl4gu3.locky_offline.BR
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.resources
import com.th3pl4gu3.locky_offline.ui.main.utils.helpers.ObservableViewModel

class LogoCustomizationBottomSheetViewModel(application: Application) :
    ObservableViewModel(application) {

    private var _accent: String = ""
    private var _icon: String = ""
    private val _deviceIcons = setOf(
        R.drawable.ic_device,
        R.drawable.ic_device_iot,
        R.drawable.ic_device_linux,
        R.drawable.ic_device_mac,
        R.drawable.ic_device_modem,
        R.drawable.ic_device_windows
    )

    /* Observations */
    var accent: String
        @Bindable get() {
            return _accent
        }
        set(value) {
            _accent = value
            notifyPropertyChanged(BR.accent)
        }

    var icon: String
        @Bindable get() {
            return _icon
        }
        set(value) {
            _icon = value
            notifyPropertyChanged(BR.icon)
        }

    /*
    * Accessible Functions
    */
    internal fun getAvailableHexColors() =
        (resources.getStringArray(R.array.hex_accent_logo)).asList()

    internal fun getAvailableIcons() = ArrayList<String>().apply {
        _deviceIcons.forEach { resId ->
            add(resources.getResourceEntryName(resId))
            sortBy { it }
        }
    }
}