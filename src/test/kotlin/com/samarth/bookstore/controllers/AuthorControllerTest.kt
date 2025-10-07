package com.samarth.bookstore.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.samarth.bookstore.domain.dto.AuthorDto
import com.samarth.bookstore.domain.entities.AuthorEntity
import com.samarth.bookstore.services.AuthorService
import com.samarth.bookstore.testAuthorDto
import com.samarth.bookstore.testAuthorEntity
import io.mockk.every
import io.mockk.verify
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import javax.print.attribute.standard.Media

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

    @Test
    fun `test that list returns empty when no authors are present`() {
        every { authorService.readAllAuthors() } answers {
            emptyList()
        }
        mockMvc.get("/v1/authors") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { json("[]") }
        }
    }

    @Test
    fun `test that GET returns all authors present in the database`() {
        every {
            authorService.readAllAuthors()
        } answers {
            listOf(
                testAuthorEntity(1),
            )
        }

        mockMvc.get("/v1/authors") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content {
                jsonPath(
                    "$[0].id", equalTo(1)
                )
            }
            content {
                jsonPath(
                    "$[0].name", equalTo("John Doe")
                )
            }
            content {
                jsonPath(
                    "$[0].age", equalTo(18)
                )
            }
            content {
                jsonPath(
                    "$[0].description", equalTo("Some Desc")
                )
            }
            content {
                jsonPath(
                    "$[0].image", equalTo("image.jpeg")
                )
            }
        }
    }

    @Test
    fun `test that readOneAuthor returns HTTP 404 if author not found in database`() {
        every {
            authorService.readOneAuthor(any())
        } answers {
            null
        }
        mockMvc.get("/v1/authors/1") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNotFound() }
        }
    }


    @Test
    fun `test that readOneAuthor returns HTTP 200 if author present in database`() {
        every {
            authorService.readOneAuthor(any())
        } answers {
            testAuthorEntity(1)
        }

        mockMvc.get("/v1/authors/1") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { jsonPath("$.id", equalTo(1)) }
            content { jsonPath("$.name", equalTo("John Doe")) }
            content { jsonPath("$.description", equalTo("Some Desc")) }
            content { jsonPath("$.image", equalTo("image.jpeg")) }
            content { jsonPath("$.age", equalTo(18)) }
        }
    }
}