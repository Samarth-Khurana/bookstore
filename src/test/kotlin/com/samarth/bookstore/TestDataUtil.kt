package com.samarth.bookstore

import com.samarth.bookstore.domain.AuthorSummary
import com.samarth.bookstore.domain.BookSummary
import com.samarth.bookstore.domain.BookSummaryUpdate
import com.samarth.bookstore.domain.dto.AuthorDto
import com.samarth.bookstore.domain.dto.AuthorSummaryDto
import com.samarth.bookstore.domain.dto.BookSummaryDto
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

fun testBookEntity(isbn: String, authorEntity: AuthorEntity, title: String? = null) = BookEntity(
    isbn = isbn,
    title = title ?: "Test Book",
    description = "Book Desc",
    image = "book-image.jpeg",
    author = authorEntity
)

fun testBookSummaryDto(isbn: String, authorSummaryDto: AuthorSummaryDto) = BookSummaryDto(
    isbn = isbn,
    title = "Test Book",
    description = "Book Desc",
    image = "book-image.jpeg",
    author = authorSummaryDto
)

fun testAuthorSummaryDto(id: Long) = AuthorSummaryDto(
    id = id,
    name = "John Doe",
    image = "image.jpeg"
)

fun testBookSummaryA(isbn: String, authorSummary: AuthorSummary, title: String? = null) = BookSummary(
    isbn = isbn,
    title = title ?: "Test Book",
    description = "Book Desc",
    image = "book-image.jpeg",
    author = authorSummary
)

fun testAuthorSummary(id: Long) = AuthorSummary(
    id = id,
    name = "John Doe",
    image = "image.jpeg"
)