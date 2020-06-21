package com.th3pl4gu3.locky_offline.ui.main.add.bank_account

import android.app.Application
import androidx.lifecycle.*
import com.th3pl4gu3.locky_offline.ui.main.utils.resources
import com.th3pl4gu3.locky_offline.R

class LogoAccentBottomSheetViewModel(application: Application) : AndroidViewModel(application) {

    /*
    * Accessible Functions
    */
    internal fun getAvailableHexColors() =  (resources.getStringArray(R.array.hex_accent_logo)).asList()
}