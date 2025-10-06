package com.samarth.bookstore.domain.dto

data class AuthorDto(
    val id: Long?,
    val name: String,
    val age: String,
    val description: String,
    val image: String
)