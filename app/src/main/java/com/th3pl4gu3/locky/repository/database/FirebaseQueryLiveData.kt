package com.th3pl4gu3.locky.repository.database

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.*


class FirebaseQueryLiveData(val query: Query) : LiveData<DataSnapshot>() {

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