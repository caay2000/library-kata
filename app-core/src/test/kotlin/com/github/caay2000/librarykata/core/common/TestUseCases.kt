package com.github.caay2000.librarykata.core.common

import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiDocumentList
import com.github.caay2000.common.test.http.HttpDataResponse
import com.github.caay2000.common.test.mock.MockDateProvider
import com.github.caay2000.common.test.mock.MockIdGenerator
import com.github.caay2000.librarykata.core.context.account.mother.AccountMother
import com.github.caay2000.librarykata.core.context.book.mother.BookIdMother
import com.github.caay2000.librarykata.core.context.book.mother.BookMother
import com.github.caay2000.librarykata.core.context.loan.mother.LoanMother
import com.github.caay2000.librarykata.hexagonal.context.account.domain.Account
import com.github.caay2000.librarykata.hexagonal.context.account.domain.AccountId
import com.github.caay2000.librarykata.hexagonal.context.account.domain.Email
import com.github.caay2000.librarykata.hexagonal.context.account.domain.IdentityNumber
import com.github.caay2000.librarykata.hexagonal.context.account.domain.PhoneNumber
import com.github.caay2000.librarykata.hexagonal.context.account.domain.PhonePrefix
import com.github.caay2000.librarykata.hexagonal.context.book.domain.Book
import com.github.caay2000.librarykata.hexagonal.context.book.domain.BookAuthor
import com.github.caay2000.librarykata.hexagonal.context.book.domain.BookId
import com.github.caay2000.librarykata.hexagonal.context.book.domain.BookIsbn
import com.github.caay2000.librarykata.hexagonal.context.book.domain.BookPages
import com.github.caay2000.librarykata.hexagonal.context.book.domain.BookPublisher
import com.github.caay2000.librarykata.hexagonal.context.book.domain.BookTitle
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.CreatedAt
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.FinishedAt
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.Loan
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.LoanId
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource
import com.github.caay2000.librarykata.jsonapi.context.book.BookGroupResource
import com.github.caay2000.librarykata.jsonapi.context.book.BookResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource
import io.ktor.server.testing.ApplicationTestBuilder
import java.time.LocalDateTime
import java.util.UUID

class TestUseCases(
    private val libraryClient: LibraryClient = LibraryClient(),
    private val mockIdGenerator: MockIdGenerator? = null,
    private val mockDateProvider: MockDateProvider? = null,
) {
    context(ApplicationTestBuilder)
    @TestCase
    fun `account is created`(
        account: Account = AccountMother.random(),
        accountId: AccountId? = null,
        identityNumber: IdentityNumber? = null,
        email: Email? = null,
        phonePrefix: PhonePrefix? = null,
        phoneNumber: PhoneNumber? = null,
    ): HttpDataResponse<JsonApiDocument<AccountResource>> {
        val id = accountId?.value ?: account.id.value
        `id will be mocked`(UUID.fromString(id))
        `datetime will be mocked`(account.registerDate.value)
        return libraryClient.createAccount(
            identityNumber = identityNumber ?: account.identityNumber,
            name = account.name,
            surname = account.surname,
            birthdate = account.birthdate,
            email = email ?: account.email,
            phonePrefix = phonePrefix ?: account.phone.prefix,
            phoneNumber = phoneNumber ?: account.phone.number,
        )
    }

    context(ApplicationTestBuilder)
    @TestCase
    fun `account is created with a loan`(
        account: Account = AccountMother.random(),
        book: Book = BookMother.random(),
        loan: Loan = LoanMother.random(accountId = account.id, bookId = book.id),
    ) {
        `account is created`(account)
        `book is created`(book)
        `loan is created`(loan)
    }

    context(ApplicationTestBuilder)
    @TestCase
    fun `find account`(
        id: AccountId,
        include: List<String> = emptyList(),
    ): HttpDataResponse<JsonApiDocument<AccountResource>> = libraryClient.findAccount(id, include)

    context(ApplicationTestBuilder)
    @TestCase
    fun `search account`(include: List<String> = emptyList()): HttpDataResponse<JsonApiDocumentList<AccountResource>> = libraryClient.searchAccount(include)

    context(ApplicationTestBuilder)
    @TestCase
    fun `search account by phoneNumber`(
        phoneNumber: String,
        include: List<String> = emptyList(),
    ): HttpDataResponse<JsonApiDocumentList<AccountResource>> = libraryClient.searchAccountByPhoneNumber(phoneNumber, include)

    context(ApplicationTestBuilder)
    @TestCase
    fun `search account by email`(
        email: String,
        include: List<String> = emptyList(),
    ): HttpDataResponse<JsonApiDocumentList<AccountResource>> = libraryClient.searchAccountByEmail(email, include)

    context(ApplicationTestBuilder)
    @TestCase
    fun `book is created`(
        book: Book,
        id: BookId? = null,
        isbn: BookIsbn? = null,
        title: BookTitle? = null,
        author: BookAuthor? = null,
        pages: BookPages? = null,
        publisher: BookPublisher? = null,
    ): HttpDataResponse<JsonApiDocument<BookResource>> {
        `id will be mocked`(UUID.fromString(id?.value ?: book.id.value))
        return libraryClient.createBook(
            isbn = isbn ?: book.isbn,
            title = title ?: book.title,
            author = author ?: book.author,
            pages = pages ?: book.pages,
            publisher = publisher ?: book.publisher,
        )
    }

    context(ApplicationTestBuilder)
    @TestCase
    fun `multiple copies of the same book are created`(
        book: Book,
        copies: Int,
    ) {
        repeat(copies) { index ->
            if (index == 0) {
                `book is created`(book)
            } else {
                `book is created`(book, id = BookIdMother.random())
            }
        }
    }

    context(ApplicationTestBuilder)
    @TestCase
    fun `find book by id`(
        id: BookId,
        include: List<String> = emptyList(),
    ): HttpDataResponse<JsonApiDocument<BookResource>> = libraryClient.findBook(id, include)

    context(ApplicationTestBuilder)
    @TestCase
    fun `find book by isbn`(isbn: BookIsbn): HttpDataResponse<JsonApiDocumentList<BookGroupResource>> = libraryClient.findBookByIsbn(isbn)

    context(ApplicationTestBuilder)
    @TestCase
    fun `search book`(): HttpDataResponse<JsonApiDocumentList<BookGroupResource>> = libraryClient.searchBook()

    context(ApplicationTestBuilder)
    @TestCase
    fun `loan is created`(
        loan: Loan = LoanMother.random(),
        bookIsbn: BookIsbn? = null,
        id: LoanId? = null,
        accountId: AccountId? = null,
        createdAt: CreatedAt? = null,
    ): HttpDataResponse<JsonApiDocument<LoanResource>> {
        `id will be mocked`(UUID.fromString(id?.value ?: loan.id.value))
        `datetime will be mocked`(createdAt?.value ?: loan.createdAt.value)
        return libraryClient.createLoan(
            bookIsbn = bookIsbn ?: BookIsbn(libraryClient.findBook(loan.bookId, emptyList()).value!!.data.attributes.isbn),
            accountId = accountId ?: loan.accountId,
        )
    }

    context(ApplicationTestBuilder)
    @TestCase
    fun `find loan`(
        id: LoanId,
        include: List<String> = emptyList(),
    ): HttpDataResponse<JsonApiDocument<LoanResource>> = libraryClient.findLoan(id, include)

    context(ApplicationTestBuilder)
    @TestCase
    fun `loan is finished`(
        loan: Loan = LoanMother.random(),
        bookId: BookId? = null,
        finishedAt: FinishedAt? = null,
    ): HttpDataResponse<Unit> {
        val finishDateTime = finishedAt?.value ?: loan.finishedAt?.value ?: LocalDateTime.now()
        `datetime will be mocked`(finishDateTime)
        return libraryClient.finishLoan(bookId = bookId ?: loan.bookId)
    }

    @TestCase
    private fun `id will be mocked`(id: UUID): UUID = mockIdGenerator?.mock(id).let { id }

    @TestCase
    private fun `datetime will be mocked`(datetime: LocalDateTime): LocalDateTime = mockDateProvider?.mock(datetime).let { datetime }
}
