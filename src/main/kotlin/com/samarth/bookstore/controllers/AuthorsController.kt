package com.samarth.bookstore.controllers

import com.samarth.bookstore.domain.dto.AuthorDto
import com.samarth.bookstore.services.AuthorService
import com.samarth.bookstore.toAuthorDto
import com.samarth.bookstore.toAuthorEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(name = "/v1/authors")
class AuthorsController(
    private val authorService: AuthorService
) {
    @PostMapping
    fun createAuthor(@RequestBody authorDto: AuthorDto): AuthorDto {
        return authorService.save(authorDto.toAuthorEntity()).toAuthorDto()
    }
}