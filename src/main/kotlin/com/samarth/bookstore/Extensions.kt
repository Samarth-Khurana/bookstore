package com.samarth.bookstore

import com.samarth.bookstore.domain.AuthorSummary
import com.samarth.bookstore.domain.AuthorUpdate
import com.samarth.bookstore.domain.BookSummary
import com.samarth.bookstore.domain.dto.AuthorDto
import com.samarth.bookstore.domain.dto.AuthorSummaryDto
import com.samarth.bookstore.domain.dto.AuthorUpdateDto
import com.samarth.bookstore.domain.dto.BookSummaryDto
import com.samarth.bookstore.domain.entities.AuthorEntity
import com.samarth.bookstore.domain.entities.BookEntity
import com.samarth.bookstore.exceptions.InvalidAuthorException


fun AuthorEntity.toAuthorDto() = AuthorDto(
    id = this.id,
    name = this.name,
    age = this.age,
    description = this.description,
    image = this.image
)

fun AuthorDto.toAuthorEntity() = AuthorEntity(
    id = this.id,
    name = this.name,
    age = this.age,
    description = this.description,
    image = this.image,
)

fun AuthorUpdateDto.toAuthorUpdate(): AuthorUpdate {
    return AuthorUpdate(
        id = this.id,
        name = this.name,
        age = this.age,
        description = this.description,
        image = this.image
    )
}

fun BookSummary.toBookEntity(author: AuthorEntity): BookEntity {
    return BookEntity(
        isbn = this.isbn,
        title = this.title,
        description = this.description,
        image = this.image,
        author = author
    )
}

fun BookSummaryDto.toBookSummary(): BookSummary {
    return BookSummary(
        isbn = this.isbn,
        title = this.title,
        description = this.description,
        image = this.image,
        author = this.author.toAuthorSummary()
    )
}

fun AuthorSummaryDto.toAuthorSummary() = AuthorSummary(
    id = this.id,
    name = this.name,
    image = this.image
)

fun BookEntity.toBookSummaryDto() = BookSummaryDto(
    isbn = this.isbn,
    title = this.title,
    description = this.description,
    image = this.image,
    author = this.author.toAuthorSummaryDto()
)

fun AuthorEntity.toAuthorSummaryDto(): AuthorSummaryDto {
    val authorId = this.id ?: throw InvalidAuthorException()
    return AuthorSummaryDto(
        id = authorId,
        name = this.name,
        image = this.image
    )
}