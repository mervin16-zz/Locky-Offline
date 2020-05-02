package com.th3pl4gu3.locky.repository.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.th3pl4gu3.locky.core.User

class UserDao : IFirebaseRepository<User> {

    private val REFERENCE_USER = "USERS"
    private val database = Firebase.database

    override fun save(obj: User): Task<Void> {
        obj.id = database.getReference(REFERENCE_USER).push().key!!
        return database.getReference(REFERENCE_USER).child(obj.id).setValue(obj)
    }

    override fun update(obj: User): Task<Void> =
        database.getReference(REFERENCE_USER).child(obj.id).child(obj.email).setValue(obj)

    override fun remove(key: String): Task<Void> =
        database.getReference(REFERENCE_USER).child(key).removeValue()

    override fun getAll(): LiveData<DataSnapshot> = FirebaseFetchLiveData(
        query = database.getReference(REFERENCE_USER)
    )

    override fun getOne(key: String): MutableLiveData<DataSnapshot> = FirebaseFetchOnceLiveData(
        query = database.getReference(REFERENCE_USER).orderByChild("email").equalTo(key)
    )
}