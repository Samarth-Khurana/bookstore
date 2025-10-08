package com.samarth.bookstore.controllers

import com.samarth.bookstore.domain.dto.AuthorDto
import com.samarth.bookstore.domain.dto.AuthorUpdateDto
import com.samarth.bookstore.services.AuthorService
import com.samarth.bookstore.toAuthorDto
import com.samarth.bookstore.toAuthorEntity
import com.samarth.bookstore.toAuthorUpdate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/v1/authors"])
class AuthorsController(
    private val authorService: AuthorService
) {
    @PostMapping
    fun createAuthor(@RequestBody authorDto: AuthorDto): ResponseEntity<AuthorDto> {
        try {
            val createdAuthor = authorService.create(authorDto.toAuthorEntity()).toAuthorDto()
            return ResponseEntity(createdAuthor, HttpStatus.CREATED)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping
    fun readManyAuthors(): ResponseEntity<List<AuthorDto>> {
        return ResponseEntity(
            authorService.readAllAuthors().map {
                it.toAuthorDto()
            },
            HttpStatus.OK
        )
    }

    @GetMapping(path = ["/{id}"])
    fun readOneAuthor(@PathVariable("id") id: Long): ResponseEntity<AuthorDto> {
        val foundAuthor = authorService.readOneAuthor(id = id)?.toAuthorDto()
        return foundAuthor?.let {
            ResponseEntity(
                it,
                HttpStatus.OK
            )
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PutMapping(path = ["/{id}"])
    fun fullUpdateAuthor(@PathVariable("id") id: Long, @RequestBody authorDto: AuthorDto): ResponseEntity<AuthorDto> {
        return try {
            val updatedAuthor = authorService.fullUpdate(id, authorDto.toAuthorEntity()).toAuthorDto()
            ResponseEntity(
                updatedAuthor,
                HttpStatus.OK
            )
        } catch (e: IllegalStateException) {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @PatchMapping(path = ["/{id}"])
    fun partialUpdateAuthor(
        @PathVariable("id") id: Long,
        @RequestBody authorUpdateDto: AuthorUpdateDto
    ): ResponseEntity<AuthorDto> {
        return try {
            val result = authorService.partialUpdate(
                id = id,
                authorUpdate = authorUpdateDto.toAuthorUpdate()
            ).toAuthorDto()

            ResponseEntity(
                result,
                HttpStatus.OK
            )
        } catch (e: IllegalStateException) {
            ResponseEntity(
                HttpStatus.BAD_REQUEST
            )
        }
    }
}

