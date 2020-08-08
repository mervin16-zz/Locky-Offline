package com.th3pl4gu3.locky_offline.ui.main.utils.extensions

import com.th3pl4gu3.locky_offline.core.credentials.Card
import com.th3pl4gu3.locky_offline.core.others.User
import java.util.*

/*
* Check if credit card's expiry date is near
*/
fun Card.hasExpired() = this.expiryDate.toFormattedCalendarForCard().before(Calendar.getInstance())

fun Card.expiringWithin30Days(): Boolean {
    val today = Calendar.getInstance()
    val expiringDate = this.expiryDate.toFormattedCalendarForCard()

    // Add 30 days to today's date
    today.add(Calendar.MONTH, 1)
    /*
    * Then we test if expiry date is after today
    * If it is after, then it means card will expire within 30 days
    * If not, then card is safe
    */
    return expiringDate.before(today)
}


/*
* Merge a created user instance into
* a fetched user instance
* This is done to update data of the user that are not
* stored in database.
*/
fun User.merge(fetchedUser: User) = this.apply {
    dateJoined = fetchedUser.dateJoined
}