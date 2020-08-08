package com.th3pl4gu3.locky_offline.ui.main.view.device

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.credentials.Device
import com.th3pl4gu3.locky_offline.repository.database.repositories.DeviceRepository
import com.th3pl4gu3.locky_offline.ui.main.view.CredentialsField
import kotlinx.coroutines.launch

class ViewDeviceViewModel(application: Application) : AndroidViewModel(application) {

    /*
    * Accessible functions
    */
    internal fun fieldList(device: Device): ArrayList<CredentialsField> =
        ArrayList<CredentialsField>().apply {
            add(
                CredentialsField(
                    subtitle = getString(R.string.field_device_username),
                    data = if (device.username.isEmpty()) getString(R.string.field_placeholder_empty) else device.username,
                    isCopyable = true
                )
            )
            add(
                CredentialsField(
                    subtitle = getString(R.string.field_device_password),
                    data = if (device.password.isEmpty()) getString(R.string.field_placeholder_empty) else device.password,
                    isViewable = true,
                    isShareable = true,
                    isCopyable = true
                )
            )
            add(
                CredentialsField(
                    subtitle = getString(R.string.field_device_ip),
                    data = if (device.ipAddress.isNullOrBlank()) getString(R.string.field_placeholder_empty) else device.ipAddress!!,
                    isCopyable = true
                )
            )
            add(
                CredentialsField(
                    subtitle = getString(R.string.field_device_mac),
                    data = if (device.macAddress.isNullOrEmpty()) getString(R.string.field_placeholder_empty) else device.macAddress!!,
                    isCopyable = true
                )
            )
            add(
                CredentialsField(
                    subtitle = getString(R.string.field_device_additional),
                    data = if (device.additionalInfo.isNullOrEmpty()) getString(R.string.field_placeholder_empty) else device.additionalInfo!!
                )
            )
        }


    /*
    * In-accessible functions
    */
    internal fun delete(key: Int) {
        viewModelScope.launch {
            DeviceRepository.getInstance(getApplication()).delete(key)
        }
    }

    private fun getString(res: Int) = getApplication<Application>().getString(res)
}