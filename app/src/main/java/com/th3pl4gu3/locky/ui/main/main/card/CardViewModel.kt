package com.th3pl4gu3.locky.ui.main.main.card

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.th3pl4gu3.locky.repository.LoadingStatus
import com.th3pl4gu3.locky.repository.database.CardDao

class CardViewModel : ViewModel(){

    private var _showSnackbarEvent = MutableLiveData<String>()
    private var _loadingStatus = MutableLiveData<LoadingStatus>()
    private val _cards = CardDao().getAll()

    val cards: LiveData<DataSnapshot>
        get() = _cards

    val showSnackBarEvent: LiveData<String>
        get() = _showSnackbarEvent

    val loadingStatus: LiveData<LoadingStatus>
        get() = _loadingStatus

    fun setSnackBarMessage(message: String) {
        _showSnackbarEvent.value = message
    }

    fun doneShowingSnackBar() {
        _showSnackbarEvent.value = null
    }

    fun setLoading(status: LoadingStatus) {
        _loadingStatus.value = status
    }

}