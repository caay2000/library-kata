package com.github.caay2000.projectskeleton.context.loan.application

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.projectskeleton.context.loan.domain.Book
import com.github.caay2000.projectskeleton.context.loan.domain.BookId
import com.github.caay2000.projectskeleton.context.loan.domain.BookIsbn

interface BookRepository {

    fun save(book: Book): Either<RepositoryError, Unit>

    fun findById(id: BookId): Either<RepositoryError, Book>

    fun searchByIsbn(isbn: BookIsbn): Either<RepositoryError, List<Book>>
}
