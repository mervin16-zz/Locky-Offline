package com.th3pl4gu3.locky_offline.ui.main.main.device

import android.app.Application
import androidx.lifecycle.*
import com.th3pl4gu3.locky_offline.core.main.Device
import com.th3pl4gu3.locky_offline.core.main.DeviceSort
import com.th3pl4gu3.locky_offline.repository.Loading
import com.th3pl4gu3.locky_offline.repository.database.repositories.DeviceRepository
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.KEY_DEVICE_SORT
import com.th3pl4gu3.locky_offline.ui.main.utils.LocalStorageManager
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.activeUser
import kotlinx.coroutines.launch
import java.util.*

class DeviceViewModel(application: Application) : AndroidViewModel(application) {

    /* Private variables */
    private var _loadingStatus = MutableLiveData(Loading.List.LOADING)
    private var _devices = MediatorLiveData<List<Device>>()
    private var _sort = MutableLiveData(loadSortObject())

    /*
    * Properties
    */
    val loadingStatus: LiveData<Loading.List>
        get() = _loadingStatus

    /*
    * Init clause
    */
    init {

        /*
        * We load the accounts on startup
        */
        loadDevices()
    }

    /*
    * Transformations
    */
    private val sortedByName = Transformations.map(_devices) {
        it.sortedBy { device ->
            device.entryName.toLowerCase(
                Locale.ROOT
            )
        }
    }

    private val sortedByUsername = Transformations.map(_devices) {
        it.sortedBy { device ->
            device.username.toLowerCase(
                Locale.ROOT
            )
        }
    }

    private val sortedByIp = Transformations.map(_devices) {
        it.sortedBy { device ->
            device.ipAddress?.toLowerCase(
                Locale.ROOT
            )
        }
    }

    val devices: LiveData<List<Device>> = Transformations.switchMap(_sort) {
        when (true) {
            it.entryName -> sortedByName
            it.username -> sortedByUsername
            it.ipAddress -> sortedByIp
            else -> _devices
        }
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


    /*
    * Non-accessible functions
    */
    private fun loadDevices() {
        viewModelScope.launch {
            _devices.addSource(
                DeviceRepository.getInstance(getApplication()).getAll(activeUser.email)
            ) {
                _devices.value = it
            }
        }
    }

    /*
    * Checks if sorting session exists
    * If sessions exists, we return the sort object
    * Else we return a new sort object
    */
    private fun loadSortObject(): DeviceSort {
        LocalStorageManager.withLogin(getApplication())
        return if (LocalStorageManager.exists(KEY_DEVICE_SORT)) {
            LocalStorageManager.get(KEY_DEVICE_SORT)!!
        } else DeviceSort()
    }

    /* Save sort data to Session for persistent re-usability*/
    private fun saveSortToSession(sort: DeviceSort) {
        LocalStorageManager.withLogin(getApplication())
        LocalStorageManager.put(KEY_DEVICE_SORT, sort)
    }
}