package com.th3pl4gu3.locky_offline.ui.main.main.device

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.DataSource
import androidx.paging.toLiveData
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.credentials.Device
import com.th3pl4gu3.locky_offline.core.tuning.DeviceSort
import com.th3pl4gu3.locky_offline.repository.Loading
import com.th3pl4gu3.locky_offline.repository.database.repositories.DeviceRepository
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.activeUser
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.resources
import com.th3pl4gu3.locky_offline.ui.main.utils.helpers.LocalStorageManager
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.KEY_DEVICE_SORT
import kotlinx.coroutines.launch
import java.util.*

class DeviceViewModel(application: Application) : AndroidViewModel(application) {

    /* Private variables */
    private var _loadingStatus = MutableLiveData(Loading.List.LOADING)
    private var _devices = DeviceRepository.getInstance(getApplication()).getAll(activeUser.email)
    private var _sort = MutableLiveData(loadSortObject())

    /*
    * Properties
    */
    val loadingStatus: LiveData<Loading.List>
        get() = _loadingStatus

    /*
    * Transformations
    */
    private val DataSource.Factory<Int, Device>.sortedByName: DataSource.Factory<Int, Device>
        get() {
            return this@sortedByName.mapByPage {
                it.sortedBy { device ->
                    device.entryName.toLowerCase(
                        Locale.ROOT
                    )
                }
            }
        }

    private val DataSource.Factory<Int, Device>.sortedByUsername: DataSource.Factory<Int, Device>
        get() {
            return this@sortedByUsername.mapByPage {
                it.sortedBy { device ->
                    device.username.toLowerCase(
                        Locale.ROOT
                    )
                }
            }
        }

    private val DataSource.Factory<Int, Device>.sortedByIp: DataSource.Factory<Int, Device>
        get() {
            return this@sortedByIp.mapByPage {
                it.sortedBy { device ->
                    device.ipAddress?.toLowerCase(
                        Locale.ROOT
                    )
                }
            }
        }

    val devices = Transformations.switchMap(_sort) {
        when (true) {
            it.entryName -> _devices.sortedByName
            it.username -> _devices.sortedByUsername
            it.ipAddress -> _devices.sortedByIp
            else -> _devices
        }.toLiveData(pageSize = resources.getInteger(R.integer.size_paging_list_default))
    }

    /*
    * Accessible functions
    */
    internal fun doneLoading(size: Int) {
        _loadingStatus.value = if (size > 0) Loading.List.LIST else Loading.List.EMPTY_VIEW
    }

    /* Call function whenever there is a change in sorting */
    internal fun sortChange(sort: DeviceSort) {
        /*
        * We first save the sort to session
        * Then we change the value of sort
        */
        if (_sort.value.toString() != sort.toString()) {
            saveSortToSession(sort)
            _sort.value = sort
        }
    }

    internal fun add(device: Device) =
        viewModelScope.launch { DeviceRepository.getInstance(getApplication()).insert(device) }

    /*
    * Non-accessible functions
    */
    /*
    * Checks if sorting session exists
    * If sessions exists, we return the sort object
    * Else we return a new sort object
    */
    private fun loadSortObject(): DeviceSort = with(LocalStorageManager) {
        withLogin(getApplication())
        return if (exists(KEY_DEVICE_SORT)) {
            get(KEY_DEVICE_SORT)!!
        } else DeviceSort()
    }

    /* Save sort data to Session for persistent re-usability*/
    private fun saveSortToSession(sort: DeviceSort) = with(LocalStorageManager) {
        withLogin(getApplication())
        put(KEY_DEVICE_SORT, sort)
    }
}