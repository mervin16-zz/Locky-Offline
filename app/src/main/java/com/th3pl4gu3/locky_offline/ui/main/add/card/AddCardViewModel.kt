package com.th3pl4gu3.locky_offline.ui.main.add.card

import android.app.Application
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.th3pl4gu3.locky_offline.BR
import com.th3pl4gu3.locky_offline.core.exceptions.FormException
import com.th3pl4gu3.locky_offline.core.main.Card
import com.th3pl4gu3.locky_offline.core.main.Validation
import com.th3pl4gu3.locky_offline.repository.database.CardRepository
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
                /*userID = getUserID()

                val validation = Validation(this)*/
                try {
                    /*validation.validateCardForm()*/
                    insertCardInDatabase(this)
                    _formValidity.value = entryName
                } catch (ex: FormException) {
                    /*assignErrorMessages(validation.errorList)*/
                } catch (ex: Exception) {
                    _toastEvent.value = "Error code 3: ${ex.message}"
                }
            }

        }

        TODO("FIX")
    }

    internal fun doneWithToastEvent() {
        _toastEvent.value = null
    }

    internal fun setCard(card: Card?) {

        /** Check if data comes from edit screen
         *  or data is empty because it comes form add screen
         *  To do that, test if card is null
         **/
        isEmptyCard = card == null || card.cardID.isEmpty()

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

    private suspend fun insertCardInDatabase(card: Card) {
        withContext(Dispatchers.IO) {
            if (isEmptyCard) saveCardToDatabase(card) else updateCardInDatabase(card)
        }
    }

    private suspend fun updateCardInDatabase(card: Card) {
        withContext(Dispatchers.IO) {
            CardRepository(getApplication()).update(card)
        }
    }

    private suspend fun saveCardToDatabase(card: Card) {
        withContext(Dispatchers.IO) {
            CardRepository(getApplication()).insert(card)
        }
    }

    private fun assignErrorMessages(errorList: HashMap<Validation.ErrorField, String>) {
        _nameErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.NAME)) errorList[Validation.ErrorField.NAME] else null
        _numberErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.NUMBER)) errorList[Validation.ErrorField.NUMBER] else null
        _pinErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.PIN)) errorList[Validation.ErrorField.PIN] else null
        _bankErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.BANK)) errorList[Validation.ErrorField.BANK] else null
        _cardHolderErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.CARD_HOLDER)) errorList[Validation.ErrorField.CARD_HOLDER] else null
    }


    private fun getUserID(): String {
        return ""
        TODO("FIX")
    }

}