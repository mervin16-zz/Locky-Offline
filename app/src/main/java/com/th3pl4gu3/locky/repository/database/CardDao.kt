package com.th3pl4gu3.locky.repository.database

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.th3pl4gu3.locky.core.main.Card

class CardDao : IFirebaseRepository<Card> {

    companion object {
        private const val REFERENCE_CARD = "CARDS"
        private const val FIELD_USER_ID = "userID"
        private val database = Firebase.database
    }

    override fun save(obj: Card): Task<Void> {
        obj.cardID = database.getReference(REFERENCE_CARD).push().key!!
        return database.getReference(REFERENCE_CARD).child(obj.cardID).setValue(obj)
    }

    override fun update(obj: Card): Task<Void> =
        database.getReference(REFERENCE_CARD).child(obj.cardID).setValue(obj)

    override fun remove(key: String): Task<Void> =
        database.getReference(REFERENCE_CARD).child(key).removeValue()

    override fun getAll(key: String): LiveData<DataSnapshot> {
        return FirebaseFetchLiveData(
            query = database.getReference(REFERENCE_CARD)
                .orderByChild(FIELD_USER_ID).equalTo(key)
        )
    }
}
