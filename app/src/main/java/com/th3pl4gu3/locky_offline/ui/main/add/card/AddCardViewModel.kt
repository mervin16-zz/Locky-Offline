package com.th3pl4gu3.locky_offline.ui.main.add.card

import android.app.Application
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.th3pl4gu3.locky_offline.BR
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.credentials.Card
import com.th3pl4gu3.locky_offline.core.others.Validation
import com.th3pl4gu3.locky_offline.repository.database.repositories.CardRepository
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.activeUser
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.getString
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.toFormattedCalendarForCard
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.toFormattedStringForCard
import com.th3pl4gu3.locky_offline.ui.main.utils.helpers.ObservableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AddCardViewModel(application: Application) : ObservableViewModel(application) {

    /**
     * Variables
     **/
    private var _toastEvent = MutableLiveData<String>()
    private val _formValidity = MutableLiveData<String>()
    private val _hasErrors = MutableLiveData(false)
    private val _nameErrorMessage = MutableLiveData<String>()
    private val _numberErrorMessage = MutableLiveData<String>()
    private val _pinErrorMessage = MutableLiveData<String>()
    private val _cvcErrorMessage = MutableLiveData<String>()
    private val _bankErrorMessage = MutableLiveData<String>()
    private val _cardHolderErrorMessage = MutableLiveData<String>()
    private val _issuedDateErrorMessage = MutableLiveData<String>()
    private val _expiryDateErrorMessage = MutableLiveData<String>()
    private var _card = Card()
    private var _isNewCard = false

    /**
     * Bindable two-way binding
     **/
    var entryName: String
        @Bindable get() {
            return _card.entryName
        }
        set(value) {
            _card.entryName = value
            notifyPropertyChanged(BR.entryName)
        }

    var cardNumber: String
        @Bindable get() {
            return _card.number
        }
        set(value) {
            _card.number = value
            notifyPropertyChanged(BR.cardNumber)
        }

    var pin: String
        @Bindable get() {
            return _card.pin
        }
        set(value) {
            _card.pin = value
            notifyPropertyChanged(BR.pin)
        }

    var cvc: String
        @Bindable get() {
            return _card.cvc
        }
        set(value) {
            _card.cvc = value
            notifyPropertyChanged(BR.cvc)
        }

    var bank: String
        @Bindable get() {
            return _card.bank
        }
        set(value) {
            _card.bank = value
            notifyPropertyChanged(BR.bank)
        }

    var cardHolderName: String
        @Bindable get() {
            return _card.cardHolderName
        }
        set(value) {
            _card.cardHolderName = value
            notifyPropertyChanged(BR.cardHolderName)
        }

    var issuedDate: String
        @Bindable get() {
            return _card.issuedDate
        }
        set(value) {
            _card.issuedDate = value
            notifyPropertyChanged(BR.issuedDate)
        }

    var expiryDate: String
        @Bindable get() {
            return _card.expiryDate
        }
        set(value) {
            _card.expiryDate = value
            notifyPropertyChanged(BR.expiryDate)
        }

    var moreInfo: String?
        @Bindable get() {
            return _card.additionalInfo
        }
        set(value) {
            _card.additionalInfo = value
            notifyPropertyChanged(BR.moreInfo)
        }

    /**
     * Properties
     **/
    val formValidity: LiveData<String>
        get() = _formValidity

    val hasErrors: LiveData<Boolean>
        get() = _hasErrors

    val nameErrorMessage: LiveData<String>
        get() = _nameErrorMessage

    val numberErrorMessage: LiveData<String>
        get() = _numberErrorMessage

    val pinErrorMessage: LiveData<String>
        get() = _pinErrorMessage

    val cvcErrorMessage: LiveData<String>
        get() = _cvcErrorMessage

    val bankErrorMessage: LiveData<String>
        get() = _bankErrorMessage

    val cardHolderErrorMessage: LiveData<String>
        get() = _cardHolderErrorMessage

    val issuedDateErrorMessage: LiveData<String>
        get() = _issuedDateErrorMessage

    val expiryDateErrorMessage: LiveData<String>
        get() = _expiryDateErrorMessage

    val toastEvent: LiveData<String>
        get() = _toastEvent


    /**
     * Functions
     **/
    fun save() {
        viewModelScope.launch {
            _card.apply {
                val validation =
                    Validation(
                        getApplication()
                    )
                if (validation.isCardFormValid(this)) {

                    /* If validation succeeds, set user ID */
                    this.user = activeUser.email

                    _formValidity.value = insertCardInDatabase(this)

                } else {
                    _hasErrors.value = true
                    assignErrorMessages(validation.errorList)
                }
            }

        }
    }

    internal fun doneWithToastEvent() {
        _toastEvent.value = null
    }

    internal fun resetErrorsFlag() {
        _hasErrors.value = false
    }

    internal fun loadCard(currentKey: Int, previousKey: Int) {
        viewModelScope.launch {

            if (currentKey == -1 && previousKey == -1) {
                /*
                * This means it is an addition
                */
                _isNewCard = true
                return@launch
            }

            val repository = CardRepository.getInstance(getApplication())

            if (currentKey == 0 && previousKey > 0) {
                /*
                * This means it is a duplicate
                */
                _card = repository.get(previousKey)!!.apply {
                    id = 0
                }

                notifyChange()

                /*
                * We need to set new account to true
                * So that it is added instead of updated
                */
                _isNewCard = true

            } else if (currentKey > 0) {
                /* This means it is an edit */
                _card = repository.get(currentKey)!!
                notifyChange()
            }
        }
    }

    internal fun updateIssuedDateText(timeInMillis: Long) {
        val id = Calendar.getInstance()
        id.timeInMillis = timeInMillis
        val ed = expiryDate.toFormattedCalendarForCard()

        cardDatesVerification(id, ed)

        issuedDate = id.toFormattedStringForCard()
    }

    internal fun updateExpiryDateText(timeInMillis: Long) {
        val ed = Calendar.getInstance()
        ed.timeInMillis = timeInMillis
        val id = expiryDate.toFormattedCalendarForCard()

        cardDatesVerification(id, ed)

        expiryDate = ed.toFormattedStringForCard()
    }

    private fun cardDatesVerification(id: Calendar, ed: Calendar) {

        val validation = Validation(
            getApplication()
        )
        validation.isCardDateValid(id, ed)

        if (!validation.isCardDateValid(id, ed)) {
            assignErrorMessages(validation.errorList)
        } else {
            _issuedDateErrorMessage.value = null
            _expiryDateErrorMessage.value = null
        }
    }

    private suspend fun insertCardInDatabase(card: Card): String {

        var message = ""

        withContext(Dispatchers.IO) {
            if (_isNewCard) {
                message = getString(
                    R.string.message_credentials_created,
                    card.entryName
                )
                saveCardToDatabase(card)
            } else {
                message = getString(
                    R.string.message_credentials_modified,
                    card.entryName
                )
                updateCardInDatabase(card)
            }
        }

        return message
    }

    private suspend fun updateCardInDatabase(card: Card) {
        withContext(Dispatchers.IO) {
            CardRepository.getInstance(getApplication()).update(card)
        }
    }

    private suspend fun saveCardToDatabase(card: Card) {
        withContext(Dispatchers.IO) {
            CardRepository.getInstance(getApplication()).insert(card)
        }
    }

    private fun assignErrorMessages(errorList: HashMap<Validation.ErrorField, String>) {
        _nameErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.NAME)) errorList[Validation.ErrorField.NAME] else null
        _numberErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.NUMBER)) errorList[Validation.ErrorField.NUMBER] else null
        _pinErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.PIN)) errorList[Validation.ErrorField.PIN] else null
        _cvcErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.CVC)) errorList[Validation.ErrorField.CVC] else null
        _bankErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.BANK)) errorList[Validation.ErrorField.BANK] else null
        _cardHolderErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.OWNER)) errorList[Validation.ErrorField.OWNER] else null

        _issuedDateErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.ISSUED_DATE)) errorList[Validation.ErrorField.ISSUED_DATE] else null

        _expiryDateErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.EXPIRY_DATE)) errorList[Validation.ErrorField.EXPIRY_DATE] else null
    }
}