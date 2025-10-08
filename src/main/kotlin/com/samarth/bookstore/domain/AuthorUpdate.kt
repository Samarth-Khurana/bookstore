package com.samarth.bookstore.domain

data class AuthorUpdate(
    val id: Long? = null,
    val name: String? = null,
    val age: Int? = null,
    val description: String? = null,
    val image: String? = null
)
