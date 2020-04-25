package com.th3pl4gu3.locky.repository.database

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.th3pl4gu3.locky.core.Card

class CardDao : IFirebaseRepository<Card> {

    private val REFERENCE_CARD = "CARDS"
    private val database = Firebase.database

    override fun save(credentials: Card): Task<Void> {
        credentials.id = database.getReference(REFERENCE_CARD).push().key!!
        return database.getReference(REFERENCE_CARD).child(credentials.id).setValue(credentials)
    }

    override fun update(credentials: Card): Task<Void> =
        database.getReference(REFERENCE_CARD).child(credentials.id).setValue(credentials)

    override fun remove(key: String): Task<Void> =
        database.getReference(REFERENCE_CARD).child(key).removeValue()

    override fun getAll(): LiveData<DataSnapshot> =
        FirebaseQueryLiveData(
            query = database.getReference(REFERENCE_CARD)
        )

    /*fun getBySortedBank(): LiveData<DataSnapshot> =
        FirebaseQueryLiveData(
            query = database.getReference(REFERENCE_CARD).orderByChild("bank").orderByValue()
        )*/
}
