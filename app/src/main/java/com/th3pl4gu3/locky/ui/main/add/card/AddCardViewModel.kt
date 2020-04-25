package com.th3pl4gu3.locky.ui.main.add.card

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.th3pl4gu3.locky.core.Card
import com.th3pl4gu3.locky.core.Validation
import com.th3pl4gu3.locky.core.exceptions.FormException
import com.th3pl4gu3.locky.repository.database.CardDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AddCardViewModel : ViewModel() {

    private lateinit var _card: Card
    private var _toastEvent = MutableLiveData<String>()

    //Form Validation variables
    private val _isFormValid = MutableLiveData(false)
    private val _name = MutableLiveData<String>()
    private val _nameErrorMessage = MutableLiveData<String>()
    private val _number = MutableLiveData<Long>()
    private val _numberErrorMessage = MutableLiveData<String>()
    private val _pin = MutableLiveData<Int>()
    private val _pinErrorMessage = MutableLiveData<String>()
    private val _bank = MutableLiveData<String>()
    private val _bankErrorMessage = MutableLiveData<String>()
    private val _cardHolder = MutableLiveData<String>()
    private val _cardHolderErrorMessage = MutableLiveData<String>()
    private val _issuedDate = MutableLiveData<String>()
    private val _expiryDate = MutableLiveData<String>()
    private val _additionalInformation = MutableLiveData<String>()

    private var isEmptyCard = true

    //Validation Properties
    val isFormValid: LiveData<Boolean>
        get() = _isFormValid

    val name: LiveData<String>
        get() = _name

    val number: LiveData<Long>
        get() = _number

    val pin: LiveData<Int>
        get() = _pin

    val bank: LiveData<String>
        get() = _bank

    val cardHolder: LiveData<String>
        get() = _cardHolder

    val issuedDate: LiveData<String>
        get() = _issuedDate

    val expiryDate: LiveData<String>
        get() = _expiryDate

    val additionalInformation: LiveData<String>
        get() = _additionalInformation

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


    //Other Properties
    val toastEvent: LiveData<String>
        get() = _toastEvent

    internal fun doneWithToastEvent() {
        _toastEvent.value = null
    }

    internal fun setCard(card: Card) {

        /** Check if data comes from edit screen
         *  or data is empty because it comes form add screen
         *  To do that, test if the id is empty or not **/
        isEmptyCard = card.id.isEmpty()

        this._card = card.also {
            _name.value = it.name
            _number.value = it.number
            _pin.value = it.pin
            _bank.value = it.bank
            _cardHolder.value = it.cardHolderName
            _issuedDate.value = it.issuedDate
            _expiryDate.value = it.expiryDate
            _additionalInformation.value = it.additionalInfo
        }
    }

    internal fun isFormValid(card: Card) {
        viewModelScope.launch {
            val validation = Validation(card)
            try {
                validation.validateCardForm()
                insertCardInDatabase(card)
                _isFormValid.value = true
            } catch (ex: FormException) {
                assignErrorMessages(validation.errorList)
            } catch (ex: Exception) {
                _toastEvent.value = "Error code 3: ${ex.message}"
            }
        }
    }

    private suspend fun insertCardInDatabase(card: Card) {
        withContext(Dispatchers.IO) {
            if (isEmptyCard) saveCardToDatabase(card) else updateCardInDatabase(card)
        }
    }

    private suspend fun updateCardInDatabase(card: Card) {
        withContext(Dispatchers.IO) {
            CardDao().update(card)
        }
    }

    private suspend fun saveCardToDatabase(card: Card) {
        withContext(Dispatchers.IO) {
            CardDao().save(card)
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
}