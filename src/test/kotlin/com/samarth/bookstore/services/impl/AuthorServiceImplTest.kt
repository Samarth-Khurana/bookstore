package com.samarth.bookstore.services.impl

import com.samarth.bookstore.repositories.AuthorRepository
import com.samarth.bookstore.testAuthorEntity
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.Test

@SpringBootTest
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
            testAuthorEntity(id=savedAuthor.id)
        )
    }

}