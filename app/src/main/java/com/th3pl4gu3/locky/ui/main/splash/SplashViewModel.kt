package com.th3pl4gu3.locky.ui.main.splash

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import com.th3pl4gu3.locky.core.main.User
import com.th3pl4gu3.locky.repository.LoadingStatus
import com.th3pl4gu3.locky.repository.database.FirebaseUserLiveData
import com.th3pl4gu3.locky.repository.database.UserDao
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

    internal val authenticationState = Transformations.map(FirebaseUserLiveData()) { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

    internal fun finishLoading() {
        _loadingStatus.value = LoadingStatus.DONE
    }

    internal fun resetUsers() {
        _user.value = null
    }

    internal fun login(instance: User) {
        val liveData = UserDao().getAll(instance.email)
        _user.addSource(liveData) {
            _user.removeSource(liveData)
            _user.value = decomposeDataSnapshots(it)
        }
    }

    private fun saveToDatabase() {
        viewModelScope.launch {
            save(User.getInstance())
        }
    }

    private fun decomposeDataSnapshots(snapshot: DataSnapshot?): User? {
        var user: User? = null

        snapshot?.children?.forEach { postSnapshot ->
            postSnapshot.getValue<User>()
                ?.let { user = it }
        }

        return mergeDatabaseUserWithAuthUser(user).also {
            if (user == null) saveToDatabase()
        }
    }

    private fun mergeDatabaseUserWithAuthUser(dbUser: User?) = User.getInstance().apply {
        if (dbUser != null) {
            dateJoined = dbUser.dateJoined
            accountType = dbUser.accountType
            accountStatus = dbUser.accountStatus
        }
    }

    private suspend fun save(user: User) {
        withContext(Dispatchers.IO) {
            UserDao().save(user)
        }
    }
}