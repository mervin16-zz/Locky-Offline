package com.th3pl4gu3.locky.repository.database

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.th3pl4gu3.locky.core.main.Account

class AccountDao : IFirebaseRepository<Account> {

    companion object {
        private const val REFERENCE_ACCOUNT = "ACCOUNTS"
        private const val FIELD_USER_ID = "userID"
        private val database = Firebase.database
    }

    override fun save(obj: Account): Task<Void> {
        obj.accountID = database.getReference(REFERENCE_ACCOUNT).push().key!!
        return database.getReference(REFERENCE_ACCOUNT).child(obj.accountID).setValue(obj)
    }

    override fun update(obj: Account): Task<Void> =
        database.getReference(REFERENCE_ACCOUNT).child(obj.accountID).setValue(obj)

    override fun remove(key: String): Task<Void> =
        database.getReference(REFERENCE_ACCOUNT).child(key).removeValue()

    override fun getAll(key: String): LiveData<DataSnapshot> =
        FirebaseFetchLiveData(
            query = database.getReference(REFERENCE_ACCOUNT)
                .orderByChild(FIELD_USER_ID).equalTo(key)
        )
}