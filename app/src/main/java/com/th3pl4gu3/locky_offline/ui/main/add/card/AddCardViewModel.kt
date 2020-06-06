package com.th3pl4gu3.locky_offline.ui.main.add.card

import android.app.Application
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.th3pl4gu3.locky_offline.BR
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.main.Card
import com.th3pl4gu3.locky_offline.core.main.User
import com.th3pl4gu3.locky_offline.core.main.Validation
import com.th3pl4gu3.locky_offline.repository.database.CardRepository
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants
import com.th3pl4gu3.locky_offline.ui.main.utils.LocalStorageManager
import com.th3pl4gu3.locky_offline.ui.main.utils.ObservableViewModel
import com.th3pl4gu3.locky_offline.ui.main.utils.toFormattedStringForCard
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
    private val _bankErrorMessage = MutableLiveData<String>()
    private val _cardHolderErrorMessage = MutableLiveData<String>()
    private var isEmptyCard = true
    private var _card = Card()

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
            return _card.cardMoreInfo
        }
        set(value) {
            _card.cardMoreInfo = value
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

    val bankErrorMessage: LiveData<String>
        get() = _bankErrorMessage

    val cardHolderErrorMessage: LiveData<String>
        get() = _cardHolderErrorMessage

    val toastEvent: LiveData<String>
        get() = _toastEvent


    /**
     * Functions
     **/
    fun save() {
        viewModelScope.launch {
            _card.apply {
                val validation = Validation()
                if (validation.isCardFormValid(this)) {

                    /* If validation succeeds, set user ID */
                    this.user = getUser().email

                    _formValidity.value = insertCardInDatabase(this)

                } else {
                    _hasErrors.value = true
                    assignErrorMessages(validation.errorList)
                }
            }

        }
    }

    internal fun resetChanges(unEditedCard: Card) {
        entryName = unEditedCard.entryName
        cardNumber = unEditedCard.number
        pin = unEditedCard.pin
        bank = unEditedCard.bank
        cardHolderName = unEditedCard.cardHolderName
        issuedDate = unEditedCard.issuedDate
        expiryDate = unEditedCard.expiryDate
        moreInfo = unEditedCard.cardMoreInfo
    }

    internal fun doneWithToastEvent() {
        _toastEvent.value = null
    }

    internal fun resetErrorsFlag() {
        _hasErrors.value = false
    }

    internal fun setCard(card: Card?) {

        /** Check if data comes from edit screen
         *  or data is empty because it comes form add screen
         *  To do that, test if card is null
         **/
        isEmptyCard = card == null

        this._card = card ?: Card()
    }

    internal fun updateIssuedDateText(timeInMillis: Long) {
        val cal = Calendar.getInstance()
        cal.timeInMillis = timeInMillis
        issuedDate = cal.toFormattedStringForCard()
    }

    internal fun updateExpiryDateText(timeInMillis: Long) {
        val cal = Calendar.getInstance()
        cal.timeInMillis = timeInMillis
        expiryDate = cal.toFormattedStringForCard()
    }

    private suspend fun insertCardInDatabase(card: Card): String {

        var message = ""

        withContext(Dispatchers.IO) {
            /*
            * Firs we try to fetch card to see if it is already present
            * If already present, it means it is an edit
            * Else, it means it is an addition
            */
            val repository = CardRepository.getInstance(getApplication())
            val fetchedCard = repository.get(card.cardID)
            if (fetchedCard == null) {
                message = getApplication<Application>().getString(
                    R.string.message_credentials_modified,
                    card.entryName
                )
                saveCardToDatabase(card)
            } else {
                message = getApplication<Application>().getString(
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

    private fun assignErrorMessages(errorList: HashMap<Validation.ErrorField, Validation.ErrorType>) {
        _nameErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.NAME)) getApplication<Application>().getString(
                R.string.error_field_validation_blank
            ) else null
        _numberErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.NUMBER)) getApplication<Application>().getString(
                R.string.error_field_validation_blank
            ) else null
        _pinErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.PIN)) getApplication<Application>().getString(
                R.string.error_field_validation_blank
            ) else null
        _bankErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.BANK)) getApplication<Application>().getString(
                R.string.error_field_validation_blank
            ) else null
        _cardHolderErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.CARD_HOLDER)) getApplication<Application>().getString(
                R.string.error_field_validation_blank
            ) else null
    }

    private fun getUser(): User {
        LocalStorageManager.withLogin(getApplication())
        return LocalStorageManager.get<User>(Constants.KEY_USER_ACCOUNT)!!
    }
}