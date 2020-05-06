package com.th3pl4gu3.locky.repository.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.th3pl4gu3.locky.core.main.Card

class CardDao : IFirebaseRepository<Card> {

    private val REFERENCE_CARD = "CARDS"
    private val database = Firebase.database

    override fun save(obj: Card): Task<Void> {
        obj.cardID = database.getReference(REFERENCE_CARD).push().key!!
        return database.getReference(REFERENCE_CARD).child(obj.cardID).setValue(obj)
    }

    override fun update(obj: Card): Task<Void> =
        database.getReference(REFERENCE_CARD).child(obj.cardID).setValue(obj)

    override fun remove(key: String): Task<Void> =
        database.getReference(REFERENCE_CARD).child(key).removeValue()

    override fun getAll(): LiveData<DataSnapshot> =
        FirebaseFetchLiveData(
            query = database.getReference(REFERENCE_CARD)
        )

    override fun getOne(key: String): MutableLiveData<DataSnapshot> = FirebaseFetchOnceLiveData(
        query = database.getReference(REFERENCE_CARD).child(key)
    )
}
