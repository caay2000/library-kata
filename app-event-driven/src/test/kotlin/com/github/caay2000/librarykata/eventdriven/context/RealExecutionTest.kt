package com.github.caay2000.librarykata.eventdriven.context

import com.github.caay2000.librarykata.eventdriven.common.LibraryClient
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Account
import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.account.mother.AccountMother
import com.github.caay2000.librarykata.eventdriven.context.book.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookIsbn
import com.github.caay2000.librarykata.eventdriven.context.book.mother.BookMother
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.CreatedAt
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.Loan
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.LoanId
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.UserId
import com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.http.serialization.LoanDocument
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import mu.KLogger
import mu.KotlinLogging
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID
import kotlin.random.Random
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.BookId as LoanBookId
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.BookIsbn as LoanBookIsbn

class RealExecutionTest {

    private val libraryClient: LibraryClient = LibraryClient()

    private val books = mutableListOf<Book>()
    private val availableBooks = mutableMapOf<BookIsbn, Int>()
    private val existingAccounts = mutableMapOf<Account, Int>()
    private val existingLoans = mutableMapOf<BookId, Loan>()

    private val logger: KLogger = KotlinLogging.logger {}

    private val map = mutableMapOf<String, Int>()

    @Disabled
    @Test
    fun `real execution test`() = testApplication {
        `multiple accounts are created`(20)
        `multiple books are created`(100)

        repeat(10000) {
            val random = Random.nextInt(1000)
            when (random) {
                in 1..349 -> startLoan()
                in 350..699 -> finishLoan()
                in 700..799 -> `create book copy`()
                in 800..899 -> `create new book`()
                in 900..999 -> `create new account`()
            }
        }

        logger.info { map }
    }

    context(ApplicationTestBuilder)
    private fun startLoan() {
        val book = books.random()
        val account = existingAccounts.keys.random()
        val available = libraryClient.findBookById(book.id).value!!.available
        if (available && existingAccounts[account]!! < 5 && availableBooks[book.isbn]!! > 0) {
            logger.info { "Start new LOAN, book[$book], account[$account]" }
            val loanDocument = libraryClient.createLoan(LoanBookIsbn(book.isbn.value), UserId(account.id.value)).value!!
            availableBooks[book.isbn] = availableBooks[book.isbn]!!.dec()
            existingAccounts[account] = existingAccounts[account]!!.inc()
            existingLoans[book.id] = loanDocument.toLoan()
            map["startLoan"] = map.getOrDefault("startLoan", 0) + 1
        }
    }

    context(ApplicationTestBuilder)
    private fun finishLoan() {
        if (existingLoans.size > 10) {
            val loan = existingLoans.values.random()
            logger.info { "Finishing LOAN, loan[$loan]" }
            libraryClient.finishLoan(loan.bookId)
            val returnedBook = books.first { it.id == BookId(loan.bookId.value) }
            val account = existingAccounts.keys.first { it.id == AccountId(loan.userId.value) }
            availableBooks[returnedBook.isbn] = availableBooks[returnedBook.isbn]!!.inc()
            existingAccounts[account] = existingAccounts[account]!!.dec()
            existingLoans.remove(returnedBook.id)
            map["finishLoan"] = map.getOrDefault("finishLoan", 0) + 1
        }
    }

    context(ApplicationTestBuilder)
    private fun `multiple accounts are created`(size: Int = 100) =
        repeat(size) {
            `create new account`()
        }

    context(ApplicationTestBuilder)
    private fun `multiple books are created`(size: Int = 1000) =
        repeat(size) {
            val random = Random.nextInt(1000)
            if (books.size > 10 && random < 50) {
                `create book copy`()
            } else {
                `create new book`()
            }
        }

    context(ApplicationTestBuilder)
    private fun `create new book`() =
        with(BookMother.random()) {
            logger.info { "Create new BOOK" }
            val id = libraryClient.createBook(
                isbn = isbn,
                title = title,
                author = author,
                pages = pages,
                publisher = publisher,
            ).value!!.id
            books.add(this.copy(id = BookId(id)))
            availableBooks[this.isbn] = availableBooks.getOrDefault(this.isbn, 0) + 1

            map["createBook"] = map.getOrDefault("createBook", 0) + 1
            val size = libraryClient.searchBooks().value!!.books.size
            logger.info { "Current Total books: $size" }
        }

    context(ApplicationTestBuilder)
    private fun `create book copy`() =
        with(books.random()) {
            logger.info { "Create BOOK copy" }
            libraryClient.createBook(
                isbn = isbn,
                title = title,
                author = author,
                pages = pages,
                publisher = publisher,
            )
            val book = books.first { it.isbn == this.isbn }
            availableBooks[book.isbn] = availableBooks[book.isbn]!!.inc()

            map["createBookCopy"] = map.getOrDefault("createBookCopy", 0) + 1
            val size = libraryClient.searchBooks().value!!.books.size
            logger.info { "Current Total books: $size" }
        }

    context(ApplicationTestBuilder)
    private fun `create new account`() =
        with(AccountMother.random()) {
            logger.info { "Create new ACCOUNT" }
            val id = libraryClient.createAccount(
                identityNumber = identityNumber,
                name = name,
                surname = surname,
                birthdate = birthdate,
                email = email,
                phonePrefix = phonePrefix,
                phoneNumber = phoneNumber,
            ).value!!.id
            existingAccounts[this.copy(id = AccountId(UUID.fromString(id)))] = 0

            map["createAccount"] = map.getOrDefault("createAccount", 0) + 1
        }

    private fun LoanDocument.toLoan(): Loan = Loan(
        id = LoanId(id),
        bookId = LoanBookId(bookId),
        userId = UserId(userId),
        createdAt = CreatedAt(LocalDateTime.now()),
        finishedAt = null,
    )
}
