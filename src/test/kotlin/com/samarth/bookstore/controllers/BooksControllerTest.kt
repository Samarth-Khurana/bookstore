package com.samarth.bookstore.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.samarth.bookstore.services.BookService
import com.samarth.bookstore.testAuthorEntity
import com.samarth.bookstore.testAuthorSummaryDto
import com.samarth.bookstore.testBookEntity
import com.samarth.bookstore.testBookSummaryDto
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
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

}