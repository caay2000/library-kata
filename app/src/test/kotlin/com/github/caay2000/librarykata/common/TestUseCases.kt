package com.github.caay2000.librarykata.common

import com.github.caay2000.common.test.http.HttpDataResponse
import com.github.caay2000.common.test.mock.MockDateProvider
import com.github.caay2000.common.test.mock.MockIdGenerator
import com.github.caay2000.librarykata.context.account.domain.Account
import com.github.caay2000.librarykata.context.account.domain.AccountId
import com.github.caay2000.librarykata.context.account.domain.Email
import com.github.caay2000.librarykata.context.account.domain.IdentityNumber
import com.github.caay2000.librarykata.context.account.domain.PhoneNumber
import com.github.caay2000.librarykata.context.account.domain.PhonePrefix
import com.github.caay2000.librarykata.context.account.mother.AccountMother
import com.github.caay2000.librarykata.context.account.primaryadapter.http.serialization.AccountDetailsDocument
import com.github.caay2000.librarykata.context.book.domain.Book
import com.github.caay2000.librarykata.context.book.domain.BookAuthor
import com.github.caay2000.librarykata.context.book.domain.BookId
import com.github.caay2000.librarykata.context.book.domain.BookIsbn
import com.github.caay2000.librarykata.context.book.domain.BookPages
import com.github.caay2000.librarykata.context.book.domain.BookPublisher
import com.github.caay2000.librarykata.context.book.domain.BookTitle
import com.github.caay2000.librarykata.context.book.mother.BookIdMother
import com.github.caay2000.librarykata.context.book.mother.BookIsbnMother
import com.github.caay2000.librarykata.context.book.primaryadapter.http.serialization.AllBooksDocument
import com.github.caay2000.librarykata.context.book.primaryadapter.http.serialization.BookByIdDocument
import com.github.caay2000.librarykata.context.book.primaryadapter.http.serialization.BookDocument
import com.github.caay2000.librarykata.context.book.primaryadapter.http.serialization.LoanByUserIdDocument
import com.github.caay2000.librarykata.context.loan.domain.CreatedAt
import com.github.caay2000.librarykata.context.loan.domain.FinishedAt
import com.github.caay2000.librarykata.context.loan.domain.Loan
import com.github.caay2000.librarykata.context.loan.domain.LoanId
import com.github.caay2000.librarykata.context.loan.domain.UserId
import com.github.caay2000.librarykata.context.loan.mother.LoanMother
import com.github.caay2000.librarykata.context.loan.primaryadapter.http.serialization.LoanDocument
import io.ktor.server.testing.ApplicationTestBuilder
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import com.github.caay2000.librarykata.context.loan.domain.BookId as LoanBookId
import com.github.caay2000.librarykata.context.loan.domain.BookIsbn as LoanBookIsbn

class TestUseCases(
    private val libraryClient: LibraryClient = LibraryClient(),
    private val mockIdGenerator: MockIdGenerator? = null,
    private val mockDateProvider: MockDateProvider? = null,
) {

    context(ApplicationTestBuilder)
    fun `account is created`(
        account: Account = AccountMother.random(),
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
    fun `multiple copies of the same book are created`(book: Book, copies: Int) {
        repeat(copies) { index ->
            if (index == 1) {
                `book is created`(book)
            } else {
                `book is created`(book, id = BookIdMother.random())
            }
        }
    }

    context(ApplicationTestBuilder)
    fun `find book by id`(id: BookId): HttpDataResponse<BookByIdDocument> = libraryClient.findBookById(id)

    context(ApplicationTestBuilder)
    fun `find book by isbn`(isbn: BookIsbn): HttpDataResponse<BookDocument> = libraryClient.findBookByIsbn(isbn)

    context(ApplicationTestBuilder)
    fun `search all books`(): HttpDataResponse<AllBooksDocument> = libraryClient.searchBooks()

    context(ApplicationTestBuilder)
    fun `search all loans by UserId`(userId: UserId): HttpDataResponse<LoanByUserIdDocument> = libraryClient.searchLoanByUserId(userId)

    context(ApplicationTestBuilder)
    fun `loan is created`(
        loan: Loan = LoanMother.random(),
        bookIsbn: BookIsbn? = null,
        id: LoanId? = null,
        userId: UserId? = null,
        createdAt: CreatedAt? = null,
    ): HttpDataResponse<LoanDocument> {
        `id will be mocked`(id?.value ?: loan.id.value)
        `datetime will be mocked`(createdAt?.value ?: loan.createdAt.value)
        return libraryClient.createLoan(
            bookIsbn = LoanBookIsbn(bookIsbn?.value ?: BookIsbnMother.random().value),
            userId = userId ?: loan.userId,
        )
    }

    context(ApplicationTestBuilder)
    fun `loan is finished`(
        loan: Loan = LoanMother.random(),
        bookId: LoanBookId? = null,
        finishedAt: FinishedAt? = null,
    ): HttpDataResponse<Unit> {
        val finishDateTime = finishedAt?.value ?: loan.finishedAt?.value ?: LocalDateTime.now()
        `datetime will be mocked`(finishDateTime)
        return libraryClient.finishLoan(bookId = bookId ?: loan.bookId)
    }

    private fun `id will be mocked`(id: UUID): UUID = mockIdGenerator?.mock(id).let { id }
    private fun `date will be mocked`(date: LocalDate): LocalDate = mockDateProvider?.mock(date).let { date }
    private fun `datetime will be mocked`(datetime: LocalDateTime): LocalDateTime = mockDateProvider?.mock(datetime).let { datetime }
}
