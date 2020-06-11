package com.th3pl4gu3.locky_offline.repository


object Loading {
    enum class Status { LOADING, DONE, ERROR }

    enum class List { LOADING, EMPTY_VIEW, LIST }
}