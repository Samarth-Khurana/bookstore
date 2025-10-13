package com.samarth.bookstore.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.samarth.bookstore.domain.dto.BookSummaryDto
import com.samarth.bookstore.services.BookService
import com.samarth.bookstore.testAuthorEntity
import com.samarth.bookstore.testBookDto
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
        val author = testAuthorEntity()
        mockMvc.put("/v1/authors/1234") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                testBookDto(
                    authorEntity = author,
                    isbn = "1234"
                )
            )
        }.andExpect {
            status { isCreated() }
            content {  }
        }
    }
}