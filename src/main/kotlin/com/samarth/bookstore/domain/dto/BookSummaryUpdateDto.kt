package com.samarth.bookstore.domain.dto

data class BookSummaryUpdateDto(
    val title: String? = null,
    val description: String? = null,
    val image: String? = null,
)
