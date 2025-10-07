package com.samarth.bookstore.controllers

import com.samarth.bookstore.domain.dto.AuthorDto
import com.samarth.bookstore.services.AuthorService
import com.samarth.bookstore.toAuthorDto
import com.samarth.bookstore.toAuthorEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/v1/authors"])
class AuthorsController(
    private val authorService: AuthorService
) {
    @PostMapping
    fun createAuthor(@RequestBody authorDto: AuthorDto): ResponseEntity<AuthorDto> {
        val createdAuthor = authorService.save(authorDto.toAuthorEntity()).toAuthorDto()
        return ResponseEntity(createdAuthor, HttpStatus.CREATED)
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
}