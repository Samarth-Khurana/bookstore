package com.samarth.bookstore.services.impl

import com.samarth.bookstore.domain.AuthorUpdate
import com.samarth.bookstore.domain.entities.AuthorEntity
import com.samarth.bookstore.repositories.AuthorRepository
import com.samarth.bookstore.testAuthorEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
class AuthorServiceImplTest @Autowired constructor(
    private val underTest: AuthorServiceImpl,
    private val authorRepository: AuthorRepository
) {

    @Test
    fun `test Author persists in database`() {
        val savedAuthor = underTest.create(testAuthorEntity())
        assertThat(savedAuthor.id).isNotNull()

        val recalledAuthor = authorRepository.findByIdOrNull(savedAuthor.id!!)
        assertThat(recalledAuthor).isNotNull()
        assertThat(recalledAuthor!!).isEqualTo(
            testAuthorEntity(id = savedAuthor.id)
        )
    }

    @Test
    fun `author with an id throws IllegalArgumentException`() {
        assertThrows<IllegalArgumentException> {
            val existing = testAuthorEntity(id = 999)
            underTest.create(existing)
        }
    }


    @Test
    fun `test that list returns no authors when database is empty`() {
        val result = underTest.readAllAuthors()

        assertThat(result).isEmpty()
    }


    @Test
    fun `test all the authors are returned from the database if the database isn't empty`() {
        val savedAuthor = authorRepository.save(testAuthorEntity())
        val expected = listOf(savedAuthor)


        val result = underTest.readAllAuthors()
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `test null returned if author is not present`() {
        val result = underTest.readOneAuthor(1)

        assertThat(result).isNull()
    }

    @Test
    fun `test correct author returned if present`() {
        val author1 = authorRepository.save(testAuthorEntity())

        val result = underTest.readOneAuthor(author1.id!!)
        assertEquals(author1, result)
    }

    @Test
    fun `test that fullUpdate successfully updates the author`() {
        val savedAuthor = authorRepository.save(testAuthorEntity())
        val updatedAuthor = AuthorEntity(
            id = savedAuthor.id!!,
            name = "Samarth",
            age = 19,
            description = "description",
            image = "sam.jpeg"
        )
        val result = underTest.fullUpdate(1, updatedAuthor)
        assertEquals(updatedAuthor, result)

        val retrievedAuthor = authorRepository.findByIdOrNull(savedAuthor.id!!)
        assertNotNull(retrievedAuthor!!.id)
        assertEquals(updatedAuthor, retrievedAuthor)
    }

    @Test
    fun `test that fullUpdate returns IllegalStateException when author doesn't exist`() {
        assertThrows<IllegalStateException> {
            underTest.fullUpdate(1, testAuthorEntity())
        }
    }

    @Test
    fun `test that partialUpdate throws IllegalStateException if author doesn't exist`() {
        assertThrows<IllegalStateException> {
            underTest.partialUpdate(1, AuthorUpdate(null, null, null, null, null))
        }
    }

    @Test
    fun `test that partialUpdate doesn't update author is all values are null`() {
        val savedAuthor = authorRepository.save(testAuthorEntity())

        val result = underTest.partialUpdate(
            savedAuthor.id!!,
            AuthorUpdate()
        )

        assertEquals(savedAuthor, result)
    }

    @Test
    fun `test that partialUpdate only changes the fields that need to be updated`() {

        val savedAuthor = authorRepository.save(testAuthorEntity())

        val result = underTest.partialUpdate(
            savedAuthor.id!!,
            AuthorUpdate(
                name = "Samarth",
                image = "samarth.jpeg"
            )
        )


        val retrievedAuthor = authorRepository.findByIdOrNull(savedAuthor.id!!)

        assertNotNull(retrievedAuthor)
        assertNotNull(retrievedAuthor.id)

        assertEquals(retrievedAuthor, result)
    }

    @Test
    fun `test partial Update id`() {
        val savedAuthor = authorRepository.save(testAuthorEntity())

        val result = underTest.partialUpdate(savedAuthor.id!!, AuthorUpdate(id = 1, name = "samarth"))

        val retrievedAuthor = authorRepository.findByIdOrNull(savedAuthor.id!!)
        val retrievedAuthorWithNewId = authorRepository.findByIdOrNull(1)

        assertNotNull(retrievedAuthor)
        assertNotNull(retrievedAuthorWithNewId)

        assertEquals(retrievedAuthor, result)
    }
}