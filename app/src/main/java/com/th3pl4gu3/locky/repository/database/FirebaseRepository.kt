package com.th3pl4gu3.locky.repository.database

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.th3pl4gu3.locky.core.Card

class FirebaseRepository{

    private val REFERENCE_CARD = "CARDS"
    private val REFERENCE_ACCOUNT = "ACCOUNTS"
    private val database = Firebase.database

    fun save(card: Card): Task<Void> = database.getReference(REFERENCE_CARD).push().setValue(card)

    //fun save(account: Account): Task<Void> = database.getReference(REFERENCE_ACCOUNT).push().setValue(account)

    fun fetchCards(): LiveData<DataSnapshot> =
        FirebaseQueryLiveData(
            query = database.getReference(REFERENCE_CARD)
        )

}
