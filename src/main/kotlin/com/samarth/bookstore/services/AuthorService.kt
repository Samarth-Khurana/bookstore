package com.samarth.bookstore.services

import com.samarth.bookstore.domain.AuthorUpdate
import com.samarth.bookstore.domain.entities.AuthorEntity
import org.springframework.stereotype.Service

@Service
interface AuthorService {
    fun create(authorEntity: AuthorEntity): AuthorEntity
    fun readAllAuthors(): List<AuthorEntity>
    fun readOneAuthor(id: Long): AuthorEntity?
    fun fullUpdate(id: Long, authorEntity: AuthorEntity): AuthorEntity
    fun partialUpdate(id: Long, authorUpdate: AuthorUpdate): AuthorEntity
}