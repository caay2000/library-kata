package com.github.caay2000.projectskeleton.common

import com.github.caay2000.common.test.http.HttpDataResponse
import com.github.caay2000.common.test.mock.MockDateProvider
import com.github.caay2000.common.test.mock.MockIdGenerator
import com.github.caay2000.projectskeleton.context.account.domain.Account
import com.github.caay2000.projectskeleton.context.account.domain.AccountId
import com.github.caay2000.projectskeleton.context.account.domain.Email
import com.github.caay2000.projectskeleton.context.account.domain.IdentityNumber
import com.github.caay2000.projectskeleton.context.account.domain.PhoneNumber
import com.github.caay2000.projectskeleton.context.account.domain.PhonePrefix
import com.github.caay2000.projectskeleton.context.account.primaryadapter.http.serialization.AccountDetailsDocument
import com.github.caay2000.projectskeleton.context.book.domain.Book
import com.github.caay2000.projectskeleton.context.book.domain.BookAuthor
import com.github.caay2000.projectskeleton.context.book.domain.BookId
import com.github.caay2000.projectskeleton.context.book.domain.BookIsbn
import com.github.caay2000.projectskeleton.context.book.domain.BookPages
import com.github.caay2000.projectskeleton.context.book.domain.BookPublisher
import com.github.caay2000.projectskeleton.context.book.domain.BookTitle
import com.github.caay2000.projectskeleton.context.book.primaryadapter.http.serialization.BookByIdDocument
import com.github.caay2000.projectskeleton.context.book.primaryadapter.http.serialization.BookDocument
import io.ktor.server.testing.ApplicationTestBuilder
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class TestUseCases(
    private val libraryClient: LibraryClient = LibraryClient(),
    private val mockIdGenerator: MockIdGenerator? = null,
    private val mockDateProvider: MockDateProvider? = null,
) {

    context(ApplicationTestBuilder)
    fun `account is created`(
        account: Account,
        accountId: AccountId? = null,
        identityNumber: IdentityNumber? = null,
        email: Email? = null,
        phonePrefix: PhonePrefix? = null,
        phoneNumber: PhoneNumber? = null,
    ): HttpDataResponse<AccountDetailsDocument> {
        val id = accountId?.value ?: account.id.value
        `id will be mocked`(id)
        `datetime will be mocked`(account.registerDate.value)
        return libraryClient.createAccount(
            identityNumber = identityNumber ?: account.identityNumber,
            name = account.name,
            surname = account.surname,
            birthdate = account.birthdate,
            email = email ?: account.email,
            phonePrefix = phonePrefix ?: account.phonePrefix,
            phoneNumber = phoneNumber ?: account.phoneNumber,
        )
    }

    context(ApplicationTestBuilder)
    fun `find account`(id: AccountId): HttpDataResponse<AccountDetailsDocument> = libraryClient.findAccount(id)

    context(ApplicationTestBuilder)
    fun `book is created`(
        book: Book,
        id: BookId? = null,
        isbn: BookIsbn? = null,
        title: BookTitle? = null,
        author: BookAuthor? = null,
        pages: BookPages? = null,
        publisher: BookPublisher? = null,
    ): HttpDataResponse<BookByIdDocument> {
        `id will be mocked`(id?.value ?: book.id.value)
        return libraryClient.createBook(
            isbn = isbn ?: book.isbn,
            title = title ?: book.title,
            author = author ?: book.author,
            pages = pages ?: book.pages,
            publisher = publisher ?: book.publisher,
        )
    }

    context(ApplicationTestBuilder)
    fun `find book by id`(id: BookId): HttpDataResponse<BookByIdDocument> = libraryClient.findBookById(id)

    context(ApplicationTestBuilder)
    fun `find book by isbn`(isbn: BookIsbn): HttpDataResponse<BookDocument> = libraryClient.findBookByIsbn(isbn)

//
//    context(ApplicationTestBuilder)
//    fun `register a banned user`(
//        user: User,
//        id: UserId? = null,
//        email: Email? = null,
//        name: Name? = null,
//    ): HttpDataResponse<UserDocument> {
//        `user is registered`(user, id, email, name)
//        val book = BookMother.random()
//        `book is created`(book)
//        `book is retrieved`(user = user, book = book, date = LocalDate.now().minusMonths(1))
//        `book is returned`(book)
//        return `find user`(user)
//    }
//

//
//    context(ApplicationTestBuilder)
//    fun `books are browsed`(): HttpDataResponse<AllBooksDocument> = libraryClient.bookBrowse()
//
//    context(ApplicationTestBuilder)
//    fun `book is retrieved`(
//        user: User,
//        book: Book,
//        email: Email? = null,
//        id: LoanId? = null,
//        date: LocalDate? = null,
//    ): HttpDataResponse<LoanDocument> {
//        `id will be mocked`(id?.value ?: UUID.randomUUID())
//        `date will be mocked`(date ?: LocalDate.now())
//
//        return libraryClient.bookRetrieve(
//            email = email ?: user.email,
//            bookId = book.id,
//        )
//    }
//
//    context(ApplicationTestBuilder)
//    fun `book is returned`(
//        book: Book,
//        date: LocalDate? = null,
//    ): HttpDataResponse<Unit> {
//        `date will be mocked`(date ?: LocalDate.now())
//        return libraryClient.bookReturn(bookId = book.id)
//    }

    private fun `id will be mocked`(id: UUID): UUID = mockIdGenerator?.mock(id).let { id }
    private fun `date will be mocked`(date: LocalDate): LocalDate = mockDateProvider?.mock(date).let { date }
    private fun `datetime will be mocked`(datetime: LocalDateTime): LocalDateTime = mockDateProvider?.mock(datetime).let { datetime }
}
