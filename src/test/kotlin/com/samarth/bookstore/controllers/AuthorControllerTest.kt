package com.samarth.bookstore.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.samarth.bookstore.domain.entities.AuthorEntity
import com.samarth.bookstore.services.AuthorService
import com.samarth.bookstore.testAuthorDto
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class AuthorControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    @MockkBean private val authorService: AuthorService
) {
    val objectMapper = ObjectMapper()

    @BeforeEach
    fun setUp() {
        every {
            authorService.save(any())
        } answers {
            firstArg()
        }
    }

    @Test
    fun `test that create Author saves the author`() {
        mockMvc.post("/v1/authors") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                testAuthorDto()
            )
        }.andExpect {
            status { isCreated() }
        }


        val expected = AuthorEntity(
            id = null,
            name = "John Doe",
            age = 18,
            description = "Some Desc",
            image = "image.jpeg"
        )

        verify { authorService.save(expected) }
    }

    @Test
    fun `test that Author returns a HTTP 201 status on successful create`() {
        mockMvc.post("/v1/authors") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                testAuthorDto()
            )
        }.andExpect {
            status { isCreated() }
        }
    }
}