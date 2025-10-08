package com.samarth.bookstore.domain.dto

data class AuthorUpdateDto(
    val id: Long?,
    val name: String?,
    val age: Int?,
    val description: String?,
    val image: String?
)
