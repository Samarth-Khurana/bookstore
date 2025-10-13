package com.samarth.bookstore.services.impl

import com.samarth.bookstore.domain.BookSummary
import com.samarth.bookstore.domain.entities.BookEntity
import com.samarth.bookstore.repositories.AuthorRepository
import com.samarth.bookstore.repositories.BookRepository
import com.samarth.bookstore.services.BookService
import com.samarth.bookstore.toBookEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class BookServiceImpl(
    private val bookRepository: BookRepository,
    private val authorRepository: AuthorRepository
) : BookService {
    override fun createUpdateBook(
        isbn: String,
        bookSummary: BookSummary
    ): Pair<BookEntity, Boolean> {
        val normalizedBook = bookSummary.copy(isbn = isbn)
        val bookExists = bookRepository.existsById(bookSummary.isbn)

        val author = authorRepository.findByIdOrNull(bookSummary.author.id)
        checkNotNull(author)

        val savedBook = bookRepository.save(normalizedBook.toBookEntity(author))
        return Pair(savedBook, !bookExists)
    }

    override fun readManyBooks(authorId: Long?): List<BookEntity> {
        return authorId?.let {
            bookRepository.findByAuthorId(authorId)
        } ?: bookRepository.findAll()
    }
}