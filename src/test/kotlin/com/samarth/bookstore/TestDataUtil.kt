package com.samarth.bookstore

import com.samarth.bookstore.domain.dto.AuthorDto
import com.samarth.bookstore.domain.dto.AuthorSummaryDto
import com.samarth.bookstore.domain.entities.AuthorEntity
import com.samarth.bookstore.domain.entities.BookEntity

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

fun testBookDto(isbn: String?, authorEntity: AuthorEntity) = BookEntity(
    isbn = isbn ?: "1234",
    title = "Test Book",
    description = "Book desc",
    image = "book-image.jpeg",
    author = authorEntity
)