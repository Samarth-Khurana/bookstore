package com.samarth.bookstore.services.impl

import com.samarth.bookstore.domain.BookSummaryUpdate
import com.samarth.bookstore.domain.entities.BookEntity
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
import kotlin.test.assertNull

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

    @Test
    fun `test that readManyBooks returns empty list when no book in database`() {
        val books = underTest.readManyBooks()
        assertEquals(emptyList<BookEntity>(), books)
    }

    @Test
    fun `test that readManyBooks returns all the books present in the database`() {
        val savedAuthor = authorRepository.save(testAuthorEntity())
        val savedBook_1 = bookRepository.save(
            testBookEntity(
                isbn = "1234",
                authorEntity = savedAuthor
            )
        )
        val savedBook_2 = bookRepository.save(
            testBookEntity(
                isbn = "2234",
                authorEntity = savedAuthor
            )
        )

        val books = underTest.readManyBooks()

        assertEquals(listOf(savedBook_1, savedBook_2), books)
    }

    @Test
    fun `test that readManyBooks returns only the books by the author when valid author id is provided`() {
        val savedAuthor = authorRepository.save(testAuthorEntity())
        val authorId = savedAuthor.id
        assertNotNull(authorId)

        val book1 = testBookEntity("1234", savedAuthor)

        bookRepository.save(book1)


        val author2 = authorRepository.save(testAuthorEntity())
        bookRepository.save(testBookEntity("123", testAuthorEntity(author2.id)))

        val retrievedBooks = bookRepository.findAll()
        assertEquals(2, retrievedBooks.size)
        val books = underTest.readManyBooks(authorId)

        assertEquals(listOf(book1), books)
    }

    @Test
    fun `test that empty list is returned when invalid author is is provided`() {
        val savedAuthor = authorRepository.save(testAuthorEntity())
        val authorId = savedAuthor.id
        assertNotNull(authorId)


        val book1 = testBookEntity("1234", savedAuthor)
        bookRepository.save(book1)


        val books = underTest.readManyBooks(authorId + 1)


        assertEquals(emptyList(), books)

    }

    @Test
    fun `test that readOneBook returns null when invalid isbn`() {
        val book = underTest.readOneBook("1234")
        assertNull(book)
    }

    @Test
    fun `test that readOneBook returns valid book on valid isbn from db`() {
        val savedBook = bookRepository.save(testBookEntity("1234", testAuthorEntity()))
        assertNotNull(savedBook)


        val answer = underTest.readOneBook("1234")


        assertEquals(savedBook, answer)
    }

    @Test
    fun `test that partialUpdate throws IllegalStateException with invalid isbn`() {
        assertThrows<IllegalStateException> {
            underTest.partialUpdate("1234", BookSummaryUpdate(title = "Another Book"))
        }
    }

    @Test
    fun `test that partialUpdate returns the updated field with valid isbn`() {
        val savedAuthor = authorRepository.save(testAuthorEntity())
        val authorId = savedAuthor.id

        kotlin.test.assertNotNull(savedAuthor)
        assertNotNull(authorId)

        val savedBook = bookRepository.save(testBookEntity("1234", savedAuthor))
        assertNotNull(savedBook)

        val updatedBook = underTest.partialUpdate("1234", BookSummaryUpdate(title = "Another Book"))
        assertNotNull(updatedBook)

        val retrievedBook = bookRepository.findByIdOrNull("1234")
        assertNotNull(retrievedBook)
        assertEquals(updatedBook, retrievedBook)
    }
}