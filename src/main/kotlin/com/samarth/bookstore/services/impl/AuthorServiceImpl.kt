package com.samarth.bookstore.services.impl

import com.samarth.bookstore.domain.AuthorUpdate
import com.samarth.bookstore.domain.entities.AuthorEntity
import com.samarth.bookstore.repositories.AuthorRepository
import com.samarth.bookstore.services.AuthorService
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AuthorServiceImpl(
    private val authorRepository: AuthorRepository
) : AuthorService {
    override fun create(authorEntity: AuthorEntity): AuthorEntity {
        require(authorEntity.id == null)
        return authorRepository.save(authorEntity)
    }

    override fun readAllAuthors(): List<AuthorEntity> {
        return authorRepository.findAll()
    }

    override fun readOneAuthor(id: Long): AuthorEntity? {
        return authorRepository.findByIdOrNull(id)
    }

    @Transactional
    override fun fullUpdate(
        id: Long,
        authorEntity: AuthorEntity
    ): AuthorEntity {
        check(authorRepository.existsById(id))
        val normalizedAuthor = authorEntity.copy(id = id)
        return authorRepository.save(normalizedAuthor)
    }

    @Transactional
    override fun partialUpdate(id: Long, authorUpdate: AuthorUpdate): AuthorEntity {
        val existingAuthor = authorRepository.findByIdOrNull(id)
        checkNotNull(existingAuthor)

        val updatedAuthor = existingAuthor.copy(
            name = authorUpdate.name ?: existingAuthor.name,
            age = authorUpdate.age ?: existingAuthor.age,
            image = authorUpdate.image ?: existingAuthor.image,
            description = authorUpdate.description ?: existingAuthor.description
        )

        return authorRepository.save(updatedAuthor)
    }
}