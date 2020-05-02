package com.th3pl4gu3.locky.ui.main.login.splash

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import com.th3pl4gu3.locky.core.User
import com.th3pl4gu3.locky.repository.database.FirebaseUserLiveData
import com.th3pl4gu3.locky.repository.database.UserDao
import com.th3pl4gu3.locky.ui.main.utils.AuthenticationState

class SplashViewModel : ViewModel() {

    private var _usersDataSnapShots = UserDao().getAll()

    internal val users = Transformations.map(_usersDataSnapShots) {
        decomposeDataSnapshots(it)
    }

    internal val authenticationState = Transformations.map(FirebaseUserLiveData()) { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

    private fun decomposeDataSnapshots(snapshot: DataSnapshot?): List<User> =
        /**
         * Function to transform data snapshots into a list of users
         **/
        if (snapshot != null) {
            val accountList = ArrayList<User>()
            snapshot.children.forEach { postSnapshot ->
                postSnapshot.getValue<User>()?.let { accountList.add(it) }
            }
            accountList
        } else {
            ArrayList()
        }
}