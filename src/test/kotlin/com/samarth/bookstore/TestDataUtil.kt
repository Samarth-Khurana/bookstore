package com.samarth.bookstore

import com.samarth.bookstore.domain.dto.AuthorDto
import com.samarth.bookstore.domain.entities.AuthorEntity

fun testAuthorDto(id: Long? = null): AuthorDto {
    return AuthorDto(
        id = id,
        name = "John Doe",
        age = 18,
        description = "Some Desc",
        image = "image.jpeg"
    )
}

fun testAuthorEntity(id: Long? = null) = AuthorEntity(
    id = id,
    name = "John Doe",
    age = 18,
    description = "Some Desc",
    image = "image.jpeg"
)