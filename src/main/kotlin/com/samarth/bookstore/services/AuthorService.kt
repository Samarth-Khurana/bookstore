package com.samarth.bookstore.services

import com.samarth.bookstore.domain.dto.AuthorDto
import com.samarth.bookstore.domain.entities.AuthorEntity
import org.springframework.stereotype.Service

@Service
interface AuthorService {
    fun save(authorEntity: AuthorEntity): AuthorEntity
    fun readAllAuthors(): List<AuthorEntity>
}