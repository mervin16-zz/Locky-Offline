package com.th3pl4gu3.locky.repository.database

import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener


class FirebaseFetchLiveData(val query: Query) : LiveData<DataSnapshot>() {

    private val _valueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            value = dataSnapshot
        }

        override fun onCancelled(databaseError: DatabaseError) {
        }
    }

    override fun onActive() {
        query.addValueEventListener(_valueEventListener)
    }

    override fun onInactive() {
        query.removeEventListener(_valueEventListener)
    }

}