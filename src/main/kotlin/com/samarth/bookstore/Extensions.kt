package com.samarth.bookstore

import com.samarth.bookstore.domain.AuthorUpdate
import com.samarth.bookstore.domain.dto.AuthorDto
import com.samarth.bookstore.domain.dto.AuthorUpdateDto
import com.samarth.bookstore.domain.entities.AuthorEntity


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
