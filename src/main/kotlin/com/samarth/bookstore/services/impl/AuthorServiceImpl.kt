package com.samarth.bookstore.services.impl

import com.samarth.bookstore.domain.dto.AuthorDto
import com.samarth.bookstore.domain.entities.AuthorEntity
import com.samarth.bookstore.repositories.AuthorRepository
import com.samarth.bookstore.services.AuthorService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AuthorServiceImpl(
    private val authorRepository: AuthorRepository
) : AuthorService {
    override fun save(authorEntity: AuthorEntity): AuthorEntity {
        return authorRepository.save(authorEntity)
    }

    override fun readAllAuthors(): List<AuthorEntity> {
        return authorRepository.findAll()
    }

    override fun readOneAuthor(id: Long): AuthorEntity? {
        return authorRepository.findByIdOrNull(id)
    }
}