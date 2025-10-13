package com.samarth.bookstore.controllers

import com.samarth.bookstore.domain.dto.BookSummaryDto
import com.samarth.bookstore.exceptions.InvalidAuthorException
import com.samarth.bookstore.services.BookService
import com.samarth.bookstore.toBookSummary
import com.samarth.bookstore.toBookSummaryDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/v1/books"])
class BooksController(
    private val bookService: BookService
) {
    @PutMapping(path = ["/{isbn}"])
    fun createFullUpdateBook(
        @PathVariable(name = "isbn") isbn: String,
        @RequestBody bookSummaryDto: BookSummaryDto
    ): ResponseEntity<BookSummaryDto> {
        try {
            val (book, isCreated) = bookService.createUpdateBook(isbn, bookSummaryDto.toBookSummary())


            val responseCode = if (isCreated) HttpStatus.CREATED else HttpStatus.OK

            return ResponseEntity(
                book.toBookSummaryDto(),
                responseCode
            )
        } catch (e: InvalidAuthorException) {
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: IllegalStateException) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping
    fun readManyBooks(@RequestParam("author") authorId: Long?): ResponseEntity<List<BookSummaryDto>> {
        val listOfBookSummaryDto = bookService.readManyBooks(authorId).map {
            it.toBookSummaryDto()
        }

        return ResponseEntity(
            listOfBookSummaryDto,
            HttpStatus.OK
        )
    }

    @GetMapping(path = ["/{isbn}"])
    fun readOneBook(@PathVariable(name = "isbn") isbn: String): ResponseEntity<BookSummaryDto> {
        val book = bookService.readOneBook(isbn) ?: return ResponseEntity(HttpStatus.BAD_REQUEST)

        return ResponseEntity(
            book.toBookSummaryDto(),
            HttpStatus.OK
        )
    }
}