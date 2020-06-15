package com.th3pl4gu3.locky_offline.repository

/*
* Enum class for all loading types
* Two loading types are currently available:
* - List: Used to demonstrate loading state for recyclerview.
*   1. Loading means that loading animation/view should be shown
*   2. Empty_view means that the empty view should be shown when the recyclerview list returns 0
*   3. List means that the recyclerview's list is not empty and hence should display the recyclerview.
*
* - Status: Used to demonstrate loading state for other types of views
* This is usually used to demonstrate loading for network requests.
*   1. Loading means that we need to should loading animation/view as the network request has not completed
*   2. Done is when the loading has completed
*   3. Error is when an unexpected behavior occurred while performing the request.
*/
object Loading {
    enum class List { LOADING, EMPTY_VIEW, LIST }

    enum class Status { LOADING, DONE, ERROR }
}