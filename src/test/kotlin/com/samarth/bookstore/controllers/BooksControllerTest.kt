package com.samarth.bookstore.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.samarth.bookstore.domain.BookSummaryUpdate
import com.samarth.bookstore.services.BookService
import com.samarth.bookstore.testAuthorEntity
import com.samarth.bookstore.testAuthorSummaryDto
import com.samarth.bookstore.testBookEntity
import com.samarth.bookstore.testBookSummaryDto
import io.mockk.every
import org.hamcrest.CoreMatchers.equalTo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.put
import kotlin.test.Test

@SpringBootTest
@AutoConfigureMockMvc
class BooksControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    @MockkBean private val bookService: BookService
) {
    val objectMapper = ObjectMapper()

    @Test
    fun `test that createFullUpdate return HTTP 201 on successful creation of a book`() {
        val isbn = "1234"
        val author = testAuthorEntity(1)

        val book = testBookEntity(isbn = isbn, authorEntity = author)

        every { bookService.createUpdateBook(any(), any()) } answers {
            Pair(book, true)
        }
        mockMvc.put("/v1/books/1234") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                testBookSummaryDto(isbn = isbn, authorSummaryDto = testAuthorSummaryDto(1))
            )
        }.andExpect {
            status { isCreated() }
        }
    }

    @Test
    fun `test that createFullUpdate return HTTP 500 on null author id of a book`() {
        val isbn = "1234"
        val author = testAuthorEntity()

        val book = testBookEntity(isbn = isbn, authorEntity = author)

        every { bookService.createUpdateBook(any(), any()) } answers {
            Pair(book, true)
        }
        mockMvc.put("/v1/books/1234") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                testBookSummaryDto(isbn = isbn, authorSummaryDto = testAuthorSummaryDto(1))
            )
        }.andExpect {
            status { isInternalServerError() }
        }
    }

    @Test
    fun `test that createFullUpdate return HTTP 400 on invalid author`() {
        val isbn = "1234"
        val author = testAuthorEntity()

        val book = testBookEntity(isbn = isbn, authorEntity = author)

        every { bookService.createUpdateBook(any(), any()) } throws IllegalStateException()
        mockMvc.put("/v1/books/1234") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                testBookSummaryDto(isbn = isbn, authorSummaryDto = testAuthorSummaryDto(1))
            )
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `test readManyBooks returns HTTP 200 and a list of books`() {
        every { bookService.readManyBooks() } answers {
            listOf(
                testBookEntity("1234", testAuthorEntity(1)),
            )
        }
        mockMvc.get("/v1/books") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { jsonPath("$[0].isbn", equalTo("1234")) }
            content { jsonPath("$[0].title", equalTo("Test Book")) }
            content { jsonPath("$[0].description", equalTo("Book Desc")) }
            content { jsonPath("$[0].image", equalTo("book-image.jpeg")) }
            content { jsonPath("$[0].author.id", equalTo(1)) }
            content { jsonPath("$[0].author.name", equalTo("John Doe")) }
            content { jsonPath("$[0].author.name", equalTo("John Doe")) }
        }
    }

    @Test
    fun `test that list returns no books when they do not match the author id`() {
        every {
            bookService.readManyBooks(any())
        } answers {
            emptyList()
        }

        mockMvc.get("/v1/books?author=999") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { json("[]") }
        }
    }

    @Test
    fun `test that list returns when matches the author id`() {
        val isbn = "1234"
        val authorEntity = testAuthorEntity(1)
        every {
            bookService.readManyBooks(authorId = 1)
        } answers {
            listOf(
                testBookEntity(
                    isbn = isbn,
                    authorEntity = authorEntity
                )
            )
        }

        mockMvc.get("/v1/books?author=1") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { jsonPath("$[0].isbn", equalTo(isbn)) }
            content { jsonPath("$[0].title", equalTo("Test Book")) }
            content { jsonPath("$[0].image", equalTo("book-image.jpeg")) }
            content { jsonPath("$[0].description", equalTo("Book Desc")) }
            content { jsonPath("$[0].author.id", equalTo(1)) }
            content { jsonPath("$[0].author.name", equalTo("John Doe")) }
            content { jsonPath("$[0].author.image", equalTo("image.jpeg")) }
        }
    }

    @Test
    fun `test that readOneBook returns HTTP 400 when invalid book id`() {
        every {
            bookService.readOneBook(any())
        } answers {
            null
        }

        mockMvc.get("/v1/books/123") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `test that readOneBook returns HTTP with valid book when valid isbn`() {
        every {
            bookService.readOneBook(any())
        } answers {
            testBookEntity("1234", testAuthorEntity(1))
        }

        mockMvc.get("/v1/books/1234") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { jsonPath("$.isbn", equalTo("1234")) }
            content { jsonPath("$.title", equalTo("Test Book")) }
            content { jsonPath("$.image", equalTo("book-image.jpeg")) }
            content { jsonPath("$.description", equalTo("Book Desc")) }
            content { jsonPath("$.author.id", equalTo(1)) }
            content { jsonPath("$.author.name", equalTo("John Doe")) }
            content { jsonPath("$.author.image", equalTo("image.jpeg")) }
        }
    }

    @Test
    fun `test that partialUpdate returns HTTP 400 on invalid isbn`() {
        val bookSummaryUpdate = BookSummaryUpdate(
            title = "Another Book",
        )
        every {
            bookService.partialUpdate(
                any(), bookSummaryUpdate
            )
        } throws IllegalStateException()

        mockMvc.patch("/v1/books/999") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                bookSummaryUpdate
            )
        }.andExpect {
            status { isBadRequest() }
        }
    }


    @Test
    fun `test that partialUpdate returns HTTP 200 on valid isbn`() {
        val bookSummaryUpdate = BookSummaryUpdate(
            title = "Another Book",
        )
        every {
            bookService.partialUpdate(
                any(), bookSummaryUpdate
            )
        } answers {
            testBookEntity(
                isbn = "1234",
                authorEntity = testAuthorEntity(1),
                title = "Another Book"
            )
        }

        mockMvc.patch("/v1/books/1234") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                bookSummaryUpdate
            )
        }.andExpect {
            status { isOk() }
            content { jsonPath("$.isbn", equalTo("1234")) }
            content { jsonPath("$.title", equalTo("Another Book")) }
            content { jsonPath("$.image", equalTo("book-image.jpeg")) }
            content { jsonPath("$.description", equalTo("Book Desc")) }
            content { jsonPath("$.author.id", equalTo(1)) }
            content { jsonPath("$.author.name", equalTo("John Doe")) }
            content { jsonPath("$.author.image", equalTo("image.jpeg")) }
        }
    }
}