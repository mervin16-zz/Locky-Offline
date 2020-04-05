package com.th3pl4gu3.locky.ui.main.utils

import com.th3pl4gu3.locky.core.Card
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

internal class ExtensionUtilsTest{
    @ParameterizedTest
    @CsvSource(
        "4850986356234582,4850 9863 5623 4582",
        "48509863562345,4850 9863 5623 45",
        "485098635623,4850 9863 5623",
        " 485098635623 ,4850 9863 5623"
    )
    fun creditCardFormatConversion(number: Long, expectedResult: String?) {
        //Arrange
        val result: String?

        //Act
        result = number.toCreditCardFormat()

        //Assert
        assertEquals(expectedResult, result)
    }

    @ParameterizedTest
    @ValueSource(longs = [
        4950256325632147,
        4850256325632147,
        4250256325632147,
        4250256325632147,
        4850256325632
    ]
    )
    fun visaCardValidityCheck_ShouldBeValid(number: Long) {
        //Arrange
        val expectedResult: Card.CardType = Card.CardType.VISA
        val result: Card.CardType?

        //Act
        result = number.getCardType()

        //Assert
        assertTrue(result == expectedResult)
    }



    @ParameterizedTest
    @ValueSource(longs = [
        1850256325632147,
        2850256325632147,
        3850256325632147,
        5850256325632147,
        6850256325632147,
        7850256325632147,
        8850256325632147,
        9850256325632147,
        485025632563214,
        48502563256321,
        485025632563,
        185025632
    ]
    )
    fun visaCardValidityCheck_ShouldBeInValid(number: Long) {
        //Arrange
        val unExpectedResult: Card.CardType = Card.CardType.VISA
        val result: Card.CardType?

        //Act
        result = number.getCardType()

        //Assert
        assertFalse(result == unExpectedResult)
    }

    @ParameterizedTest
    @ValueSource(longs = [
        5150256325632147,
        5280256325632147,
        5360256325632147,
        5410256325632147,
        5500256325632147,
        2221256325632147,
        2222256325632147,
        2250256325632147,
        2271256325632147,
        2272025632563214
    ]
    )
    fun masterCardValidityCheck_ShouldBeValid(number: Long) {
        //Arrange
        val expectedResult: Card.CardType = Card.CardType.MASTERCARD
        val result: Card.CardType?

        //Act
        result = number.getCardType()

        //Assert
        assertTrue(result == expectedResult)
    }

    @ParameterizedTest
    @ValueSource(longs = [
        1850256325632147,
        2850256325632147,
        3850256325632147,
        4850256325632147,
        6850256325632147,
        7850256325632147,
        8850256325632147,
        9850256325632147,
        515025632563214,
        51502563256321,
        5150256325632,
        515025632563,
        525025632563214,
        52502563256321,
        5250256325632,
        525025632563,
        535025632563214,
        53502563256321,
        5350256325632,
        535025632563,
        545025632563214,
        54502563256321,
        5450256325632,
        545025632563,
        555025632563214,
        55502563256321,
        5550256325632,
        555025632563
    ]
    )
    fun masterCardValidityCheck_ShouldBeInValid(number: Long) {
        //Arrange
        val unExpectedResult: Card.CardType = Card.CardType.MASTERCARD
        val result: Card.CardType?

        //Act
        result = number.getCardType()

        //Assert
        assertFalse(result == unExpectedResult)
    }

    @ParameterizedTest
    @ValueSource(longs = [
        3450256325632147,
        348025632563214,
        3760256325632147,
        371025632563214
    ]
    )
    fun amexCardValidityCheck_ShouldBeValid(number: Long) {
        //Arrange
        val expectedResult: Card.CardType = Card.CardType.AMERICAN_EXPRESS
        val result: Card.CardType?

        //Act
        result = number.getCardType()

        //Assert
        assertTrue(result == expectedResult)
    }

    @ParameterizedTest
    @ValueSource(longs = [
        3150256325632147,
        328025632563214,
        4060256325632147,
        201025632563214,
        34502563256321471,
        34802563256321,
        37602563256321471,
        37102563256321
    ]
    )
    fun amexCardValidityCheck_ShouldBeInValid(number: Long) {
        //Arrange
        val unExpectedResult: Card.CardType = Card.CardType.AMERICAN_EXPRESS
        val result: Card.CardType?

        //Act
        result = number.getCardType()

        //Assert
        assertFalse(result == unExpectedResult)
    }

    @ParameterizedTest
    @ValueSource(longs = [
        3000256325632147,
        3010256325632147,
        3020256325632147,
        3030256325632147,
        3040256325632147,
        3050256325632147,
        3895256325632147,
        3695256325632147
    ]
    )
    fun dinnersClubCardValidityCheck_ShouldBeValid(number: Long) {
        //Arrange
        val expectedResult: Card.CardType = Card.CardType.DINNERS_CLUB
        val result: Card.CardType?

        //Act
        result = number.getCardType()

        //Assert
        assertTrue(result == expectedResult)
    }

    @ParameterizedTest
    @ValueSource(longs = [
        2000256325632147,
        4010256325632147,
        5020256325632147,
        6030256325632147,
        7040256325632147,
        8050256325632147,
        9895256325632147,
        3195256325632147
    ]
    )
    fun dinnersClubCardValidityCheck_ShouldBeIValid(number: Long) {
        //Arrange
        val unExpectedResult: Card.CardType = Card.CardType.DINNERS_CLUB
        val result: Card.CardType?

        //Act
        result = number.getCardType()

        //Assert
        assertFalse(result == unExpectedResult)
    }


    @ParameterizedTest
    @ValueSource(longs = [
        6011256325632147,
        6221266325632147,
        6229256325632147,
        6240006325632147,
        6269996325632147,
        6282006325632147,
        6448993325632147,
        6495256325632147,
        6595256325632147
    ]
    )
    fun discoverCardValidityCheck_ShouldBeValid(number: Long) {
        //Arrange
        val expectedResult: Card.CardType = Card.CardType.DISCOVER
        val result: Card.CardType?

        //Act
        result = number.getCardType()

        //Assert
        assertTrue(result == expectedResult)
    }
}