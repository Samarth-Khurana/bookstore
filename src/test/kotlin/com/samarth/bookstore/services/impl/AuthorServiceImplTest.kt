package com.samarth.bookstore.services.impl

import com.samarth.bookstore.repositories.AuthorRepository
import com.samarth.bookstore.testAuthorEntity
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
@Transactional
class AuthorServiceImplTest @Autowired constructor(
    private val underTest: AuthorServiceImpl,
    private val authorRepository: AuthorRepository
) {

    @Test
    fun `test Author persists in database`() {
        val savedAuthor = underTest.save(testAuthorEntity())
        assertThat(savedAuthor.id).isNotNull()

        val recalledAuthor = authorRepository.findByIdOrNull(savedAuthor.id!!)
        assertThat(recalledAuthor).isNotNull()
        assertThat(recalledAuthor!!).isEqualTo(
            testAuthorEntity(id = savedAuthor.id)
        )
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
    fun `test correct author retured if present`() {
        val author1 = authorRepository.save(testAuthorEntity())

        val result = underTest.readOneAuthor(author1.id!!)
        assertEquals(author1, result)
    }
}