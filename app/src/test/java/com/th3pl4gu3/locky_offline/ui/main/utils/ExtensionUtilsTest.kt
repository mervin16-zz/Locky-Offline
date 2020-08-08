package com.th3pl4gu3.locky_offline.ui.main.utils

import com.th3pl4gu3.locky_offline.core.credentials.Card
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import java.text.SimpleDateFormat
import java.util.*

internal class ExtensionUtilsTest {
    @ParameterizedTest
    @CsvSource(
        "4850986356234582,4850 9863 5623 4582",
        "48509863562345,4850 9863 5623 45",
        "485098635623,4850 9863 5623",
        " 485098635623 ,4850 9863 5623"
    )
    fun creditCardFormatConversion_NormalValues(number: String, expectedResult: String?) {
        //Arrange
        val result: String?

        //Act
        result = number.toCreditCardFormat()

        //Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun creditCardFormatConversion_Empty() {
        //Arrange
        val number = ""
        val expectedResult = ""
        val result: String?

        //Act
        result = number.toCreditCardFormat()

        //Assert
        assertEquals(expectedResult, result)
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "4950256325632147",
            "4850256325632147",
            "4250256325632147",
            "4250256325632147",
            "4850256325632"
        ]
    )
    fun visaCardValidityCheck_ShouldBeValid(number: String) {
        //Arrange
        val expectedResult: Card.CardType = Card.CardType.VISA
        val result: Card.CardType?

        //Act
        result = number.getCardType()

        //Assert
        assertTrue(result == expectedResult)
    }


    @ParameterizedTest
    @ValueSource(
        strings = [
            "1850256325632147",
            "2850256325632147",
            "3850256325632147",
            "5850256325632147",
            "6850256325632147",
            "7850256325632147",
            "8850256325632147",
            "9850256325632147",
            "485025632563214",
            "48502563256321",
            "485025632563",
            "185025632"
        ]
    )
    fun visaCardValidityCheck_ShouldBeInValid(number: String) {
        //Arrange
        val unExpectedResult: Card.CardType = Card.CardType.VISA
        val result: Card.CardType?

        //Act
        result = number.getCardType()

        //Assert
        assertFalse(result == unExpectedResult)
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "5150256325632147",
            "5280256325632147",
            "5360256325632147",
            "5410256325632147",
            "5500256325632147",
            "2221256325632147",
            "2222256325632147",
            "2250256325632147",
            "2271256325632147",
            "2272025632563214"
        ]
    )
    fun masterCardValidityCheck_ShouldBeValid(number: String) {
        //Arrange
        val expectedResult: Card.CardType = Card.CardType.MASTERCARD
        val result: Card.CardType?

        //Act
        result = number.getCardType()

        //Assert
        assertTrue(result == expectedResult)
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "1850256325632147",
            "2850256325632147",
            "3850256325632147",
            "4850256325632147",
            "6850256325632147",
            "7850256325632147",
            "8850256325632147",
            "9850256325632147",
            "515025632563214",
            "51502563256321",
            "5150256325632",
            "515025632563",
            "525025632563214",
            "52502563256321",
            "5250256325632",
            "525025632563",
            "535025632563214",
            "53502563256321",
            "5350256325632",
            "535025632563",
            "545025632563214",
            "54502563256321,",
            "5450256325632",
            "545025632563",
            "555025632563214",
            "55502563256321",
            "5550256325632",
            "555025632563"
        ]
    )
    fun masterCardValidityCheck_ShouldBeInValid(number: String) {
        //Arrange
        val unExpectedResult: Card.CardType = Card.CardType.MASTERCARD
        val result: Card.CardType?

        //Act
        result = number.getCardType()

        //Assert
        assertFalse(result == unExpectedResult)
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "3450256325632147",
            "348025632563214",
            "3760256325632147",
            "371025632563214"
        ]
    )
    fun amexCardValidityCheck_ShouldBeValid(number: String) {
        //Arrange
        val expectedResult: Card.CardType = Card.CardType.AMERICAN_EXPRESS
        val result: Card.CardType?

        //Act
        result = number.getCardType()

        //Assert
        assertTrue(result == expectedResult)
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "3150256325632147",
            "328025632563214",
            "4060256325632147",
            "201025632563214",
            "34502563256321471",
            "34802563256321",
            "37602563256321471",
            "37102563256321"
        ]
    )
    fun amexCardValidityCheck_ShouldBeInValid(number: String) {
        //Arrange
        val unExpectedResult: Card.CardType = Card.CardType.AMERICAN_EXPRESS
        val result: Card.CardType?

        //Act
        result = number.getCardType()

        //Assert
        assertFalse(result == unExpectedResult)
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "3000256325632147",
            "3010256325632147",
            "3020256325632147",
            "3030256325632147",
            "3040256325632147",
            "3050256325632147",
            "3895256325632147",
            "3695256325632147"
        ]
    )
    fun dinnersClubCardValidityCheck_ShouldBeValid(number: String) {
        //Arrange
        val expectedResult: Card.CardType = Card.CardType.DINNERS_CLUB
        val result: Card.CardType?

        //Act
        result = number.getCardType()

        //Assert
        assertTrue(result == expectedResult)
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "2000256325632147",
            "4010256325632147",
            "5020256325632147",
            "6030256325632147",
            "7040256325632147",
            "8050256325632147",
            "9895256325632147",
            "3195256325632147"
        ]
    )
    fun dinnersClubCardValidityCheck_ShouldBeIValid(number: String) {
        //Arrange
        val unExpectedResult: Card.CardType = Card.CardType.DINNERS_CLUB
        val result: Card.CardType?

        //Act
        result = number.getCardType()

        //Assert
        assertFalse(result == unExpectedResult)
    }


    @ParameterizedTest
    @ValueSource(
        strings = [
            "6011256325632147",
            "6221266325632147",
            "6229256325632147",
            "6448993325632147",
            "6495256325632147",
            "6595256325632147"
        ]
    )
    fun discoverCardValidityCheck_ShouldBeValid(number: String) {
        //Arrange
        val expectedResult: Card.CardType = Card.CardType.DISCOVER
        val result: Card.CardType?

        //Act
        result = number.getCardType()

        //Assert
        assertTrue(result == expectedResult)
    }


    @ParameterizedTest
    @CsvSource(
        "0,03/16",
        "1,03/16",
        "40,04/16",
        "-40,02/16",
        "400,04/17",
        "-400,02/15"
    )
    fun calendarToSimpleDateString(number: Int, expectedResult: String?) {
        //Arrange
        val date = Calendar.getInstance()
        val result: String?

        //Act
        date.set(Calendar.DAY_OF_MONTH, 16)
        date.set(Calendar.MONTH, 2)
        date.set(Calendar.YEAR, 2016)
        date.add(Calendar.DAY_OF_MONTH, number)
        result = date.toFormattedStringForCard()

        //Assert
        assertEquals(expectedResult, result)
    }

    @ParameterizedTest
    @CsvSource(
        "03/16,03/16",
        "02/22,02/22",
        "04/96,4/96",
        "09/10,9/10",
        "10/90,10/90",
        "10/20,10/2020",
        "10/50,10/2050",
        "10/96,10/1996"
    )
    fun stringToCalendar(expectedResult: String, date: String) {
        //Arrange
        val result: String?

        //Act
        //Conver to cal
        val calObj = date.toFormattedCalendarForCard()
        //Convert back to string to test
        result = SimpleDateFormat("MM/yy", Locale.ENGLISH).format(calObj.timeInMillis)

        //Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun calendarToString_MonthAndYear() {
        //Arrange
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, 4)
        calendar.set(Calendar.YEAR, 2023)
        val expectedResult = "05/23"
        val result: String?

        //Act
        result = calendar.toFormattedStringForCard()

        //Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun calendarToString_Default() {
        //Arrange
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 20)
        calendar.set(Calendar.MONTH, 4)
        calendar.set(Calendar.YEAR, 2023)
        val expectedResult = "20/05/2023"
        val result: String?

        //Act
        result = calendar.toFormattedStringDefault()

        //Assert
        assertEquals(expectedResult, result)
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "05/18",
            "04/19",
            "03/20",
            "05/20",
            "06/20"
        ]
    )
    fun creditCardHasExpired_WhenItHasExpired(expiryDate: String) {
        //Arrange
        val card = TestUtil.getCard(1, "user@email.com")
        //reset expiry date
        card.expiryDate = expiryDate

        //Act
        val result = card.hasExpired()

        //Assert
        assertTrue(result)
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "07/20",
            "04/21",
            "03/22",
            "05/23",
            "06/40"
        ]
    )
    fun creditCardHasExpired_WhenItHasNotExpired(expiryDate: String) {
        //Arrange
        val card = TestUtil.getCard(1, "user@email.com")
        //reset expiry date
        card.expiryDate = expiryDate

        //Act
        val result = card.hasExpired()

        //Assert
        assertFalse(result)
    }

    @Test
    fun creditCardExpirationWithin30Days_ItWillExpireWithin30days() {
        //Arrange
        val card = TestUtil.getCard(1, "user@email.com")
        //reset expiry date
        card.expiryDate = "07/20"

        //Act
        val result = card.expiringWithin30Days()

        //Assert
        assertTrue(result)
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "08/20",
            "09/20",
            "10/20",
            "05/23",
            "06/40"
        ]
    )
    fun creditCardExpirationWithin30Days_ItWillNotExpireWithin30days(expiryDate: String) {
        //Arrange
        val card = TestUtil.getCard(1, "user@email.com")
        //reset expiry date
        card.expiryDate = expiryDate

        //Act
        val result = card.expiringWithin30Days()

        //Assert
        assertFalse(result)
    }
}