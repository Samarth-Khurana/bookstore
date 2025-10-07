package com.samarth.bookstore.services

import com.samarth.bookstore.domain.entities.AuthorEntity
import org.springframework.stereotype.Service

@Service
interface AuthorService {
    fun save(authorEntity: AuthorEntity): AuthorEntity
    fun readAllAuthors(): List<AuthorEntity>
    fun readOneAuthor(id: Long): AuthorEntity?
}