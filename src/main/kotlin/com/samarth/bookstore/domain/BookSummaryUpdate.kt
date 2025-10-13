package com.samarth.bookstore.domain

data class BookSummaryUpdate(
    val title: String? = null,
    val description: String? = null,
    val image: String? = null,
)
