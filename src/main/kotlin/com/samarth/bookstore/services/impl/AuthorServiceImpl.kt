package com.samarth.bookstore.services.impl

import com.samarth.bookstore.domain.entities.AuthorEntity
import com.samarth.bookstore.repositories.AuthorRepository
import com.samarth.bookstore.services.AuthorService

class AuthorServiceImpl(
    private val authorRepository: AuthorRepository
) : AuthorService {
    override fun save(authorEntity: AuthorEntity): AuthorEntity {
        return authorRepository.save<AuthorEntity>(authorEntity)
    }
}