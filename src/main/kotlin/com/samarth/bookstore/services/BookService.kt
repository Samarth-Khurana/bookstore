package com.samarth.bookstore.services

import com.samarth.bookstore.domain.BookSummary
import com.samarth.bookstore.domain.entities.BookEntity
import org.springframework.stereotype.Service

@Service
interface BookService {
    fun createUpdateBook(isbn: String, bookSummary: BookSummary): Pair<BookEntity, Boolean>
    fun readManyBooks(): List<BookEntity>
}