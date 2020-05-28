package com.th3pl4gu3.locky.ui.main.splash

import android.app.Application
import androidx.lifecycle.*
import com.th3pl4gu3.locky.core.main.User
import com.th3pl4gu3.locky.repository.LoadingStatus
import com.th3pl4gu3.locky.ui.main.utils.AuthenticationState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    private var _loadingStatus = MutableLiveData(LoadingStatus.LOADING)
    private var _user = MediatorLiveData<User>()

    val user: LiveData<User>
        get() = _user

    val isLoading = Transformations.map(_loadingStatus) {
        when (it) {
            LoadingStatus.DONE -> false
            else -> true
        }
    }

    internal fun finishLoading() {
        _loadingStatus.value = LoadingStatus.DONE
    }

    internal fun resetUsers() {
        _user.value = null
    }

    internal fun login(instance: User) {
        TODO("FIX")
        /*val liveData = UserDao().getAll(instance.email)
        _user.addSource(liveData) {
            _user.removeSource(liveData)
            _user.value = decomposeDataSnapshots(it)
        }*/
    }

    private fun saveToDatabase() {
        viewModelScope.launch {
            save(User())
        }
    }

    private fun mergeDatabaseUserWithAuthUser(dbUser: User?) = User().apply {
        /*if (dbUser != null) {
            dateJoined = dbUser.dateJoined
            accountType = dbUser.accountType
            accountStatus = dbUser.accountStatus
        }*/

        TODO("FIX")
    }

    private suspend fun save(user: User) {
        withContext(Dispatchers.IO) {
            //UserDao().save(user)
        }
    }
}