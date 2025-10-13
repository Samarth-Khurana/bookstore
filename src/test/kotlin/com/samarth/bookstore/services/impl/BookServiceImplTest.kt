package com.samarth.bookstore.services.impl

import com.samarth.bookstore.repositories.AuthorRepository
import com.samarth.bookstore.repositories.BookRepository
import com.samarth.bookstore.testAuthorEntity
import com.samarth.bookstore.testAuthorSummary
import com.samarth.bookstore.testBookEntity
import com.samarth.bookstore.testBookSummaryA
import jakarta.transaction.Transactional
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
@Transactional
class BookServiceImplTest @Autowired constructor(
    private val underTest: BookServiceImpl,
    private val authorRepository: AuthorRepository,
    private val bookRepository: BookRepository
) {
    @Test
    fun `test that createUpdate throws IllegalStateException when author doesn't exist`() {
        val isbn = "1234"
        val authorSummary = testAuthorSummary(1)
        val bookSummary = testBookSummaryA(
            isbn = isbn,
            authorSummary = authorSummary
        )
        assertThrows<IllegalStateException> {
            underTest.createUpdateBook(isbn = isbn, bookSummary = bookSummary)
        }
    }

    @Test
    fun `test that createUpdate successfully creates a book in the database`() {
        val savedAuthorEntity = authorRepository.save(testAuthorEntity())
        val authorId = savedAuthorEntity.id
        assertNotNull(authorId)

        val isbn = "1234"

        val authorSummary = testAuthorSummary(authorId)

        val bookSummary = testBookSummaryA(
            isbn = isbn,
            authorSummary = authorSummary
        )

        val (savedBook, isCreated) = underTest.createUpdateBook(
            isbn = isbn,
            bookSummary = bookSummary
        )

        val retrievedBook = bookRepository.findByIdOrNull(savedBook.isbn)
        assertNotNull(retrievedBook)

        assertEquals(true, isCreated)

        assertEquals(retrievedBook, savedBook)

    }

    @Test
    fun `test that createUpdate successfully updates a book in the database`() {
        val savedAuthorEntity = authorRepository.save(testAuthorEntity())
        val authorId = savedAuthorEntity.id
        assertNotNull(authorId)


        val isbn = "1234"

        val savedBook = bookRepository.save(
            testBookEntity(
                isbn = isbn,
                authorEntity = savedAuthorEntity
            )
        )
        assertNotNull(savedBook)

        val authorSummary = testAuthorSummary(authorId)

        val bookSummary = testBookSummaryA(
            isbn = isbn,
            authorSummary = authorSummary,
            title = "Another Test Book"
        )

        val (updatedBook, isCreated) = underTest.createUpdateBook(
            isbn = isbn,
            bookSummary = bookSummary
        )

        val retrievedBook = bookRepository.findByIdOrNull(updatedBook.isbn)
        assertNotNull(retrievedBook)

        assertEquals(false, isCreated)

        assertEquals(retrievedBook, updatedBook)

    }
}