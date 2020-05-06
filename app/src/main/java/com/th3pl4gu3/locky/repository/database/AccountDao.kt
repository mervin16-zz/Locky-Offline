package com.th3pl4gu3.locky.repository.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.th3pl4gu3.locky.core.main.Account

class AccountDao : IFirebaseRepository<Account> {

    private val REFERENCE_ACCOUNT = "ACCOUNTS"
    private val database = Firebase.database

    override fun save(obj: Account): Task<Void> {
        obj.accountID = database.getReference(REFERENCE_ACCOUNT).push().key!!
        return database.getReference(REFERENCE_ACCOUNT).child(obj.accountID).setValue(obj)
    }

    override fun update(obj: Account): Task<Void> =
        database.getReference(REFERENCE_ACCOUNT).child(obj.accountID).setValue(obj)

    override fun remove(key: String): Task<Void> =
        database.getReference(REFERENCE_ACCOUNT).child(key).removeValue()

    override fun getAll(): LiveData<DataSnapshot> =
        FirebaseFetchLiveData(
            query = database.getReference(REFERENCE_ACCOUNT)
        )

    override fun getOne(key: String): MutableLiveData<DataSnapshot> = FirebaseFetchOnceLiveData(
        query = database.getReference(REFERENCE_ACCOUNT).child(key)
    )
}