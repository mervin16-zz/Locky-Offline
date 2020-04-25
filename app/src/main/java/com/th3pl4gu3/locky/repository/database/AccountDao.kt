package com.th3pl4gu3.locky.repository.database

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.th3pl4gu3.locky.core.Account

class AccountDao : IFirebaseRepository<Account> {

    private val REFERENCE_ACCOUNT = "ACCOUNTS"
    private val database = Firebase.database

    override fun save(credentials: Account): Task<Void> {
        credentials.id = database.getReference(REFERENCE_ACCOUNT).push().key!!
        return database.getReference(REFERENCE_ACCOUNT).child(credentials.id).setValue(credentials)
    }

    override fun update(credentials: Account): Task<Void> =
        database.getReference(REFERENCE_ACCOUNT).child(credentials.id).setValue(credentials)

    override fun remove(key: String): Task<Void> =
        database.getReference(REFERENCE_ACCOUNT).child(key).removeValue()

    override fun getAll(): LiveData<DataSnapshot> =
        FirebaseQueryLiveData(
            query = database.getReference(REFERENCE_ACCOUNT)
        )
}