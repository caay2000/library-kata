package com.github.caay2000.librarykata.hexagonal.common

import com.github.caay2000.common.http.ContentType
import com.github.caay2000.common.serialization.defaultJacksonConfiguration
import com.github.caay2000.common.serialization.defaultObjectMapper
import com.github.caay2000.common.test.http.HttpDataResponse
import com.github.caay2000.librarykata.hexagonal.context.domain.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.Birthdate
import com.github.caay2000.librarykata.hexagonal.context.domain.BookAuthor
import com.github.caay2000.librarykata.hexagonal.context.domain.BookId
import com.github.caay2000.librarykata.hexagonal.context.domain.BookIsbn
import com.github.caay2000.librarykata.hexagonal.context.domain.BookPages
import com.github.caay2000.librarykata.hexagonal.context.domain.BookPublisher
import com.github.caay2000.librarykata.hexagonal.context.domain.BookTitle
import com.github.caay2000.librarykata.hexagonal.context.domain.Email
import com.github.caay2000.librarykata.hexagonal.context.domain.IdentityNumber
import com.github.caay2000.librarykata.hexagonal.context.domain.Name
import com.github.caay2000.librarykata.hexagonal.context.domain.PhoneNumber
import com.github.caay2000.librarykata.hexagonal.context.domain.PhonePrefix
import com.github.caay2000.librarykata.hexagonal.context.domain.Surname
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.AccountDocument
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.AccountRequestDocument
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.BookByIdDocument
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.BookByIsbnListDocument
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.BookRequestDocument
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.LoanByAccountIdDocument
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.LoanDocument
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.LoanRequestDocument
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.contentType
import io.ktor.serialization.jackson.jackson
import io.ktor.server.testing.ApplicationTestBuilder
import kotlinx.coroutines.runBlocking
import mu.KLogger
import mu.KotlinLogging

class LibraryClient {

    val logger: KLogger = KotlinLogging.logger {}

    context(ApplicationTestBuilder)
    fun createAccount(
        identityNumber: IdentityNumber,
        name: Name,
        surname: Surname,
        birthdate: Birthdate,
        email: Email,
        phonePrefix: PhonePrefix,
        phoneNumber: PhoneNumber,
    ): HttpDataResponse<AccountDocument> =
        runBlocking {
            val client = createClient {
                install(ContentNegotiation) {
                    jackson {
                        defaultJacksonConfiguration()
                    }
                }
            }

            client.post("/account") {
                setBody(
                    AccountRequestDocument(
                        data = AccountRequestDocument.Resource(
                            attributes = AccountRequestDocument.Resource.Attributes(
                                identityNumber = identityNumber.value,
                                name = name.value,
                                surname = surname.value,
                                birthdate = birthdate.value,
                                email = email.value,
                                phonePrefix = phonePrefix.value,
                                phoneNumber = phoneNumber.value,
                            ),
                        ),
                    ),
                )
                contentType(ContentType.JsonApi)
            }.toHttpDataResponse()
        }

    context(ApplicationTestBuilder)
    fun findAccount(id: AccountId, include: List<TestUseCases.AccountInclude>): HttpDataResponse<AccountDocument> =
        runBlocking {
            val includeQuery = include.joinToString { it.name.lowercase() }.let {
                if (it.isNotBlank()) "?include=$it" else ""
            }
            client.get("/account/${id.value}$includeQuery").toHttpDataResponse()
        }

    context(ApplicationTestBuilder)
    fun searchLoanByAccountId(accountId: AccountId): HttpDataResponse<LoanByAccountIdDocument> =
        runBlocking { client.get("/account/${accountId.value}/loan").toHttpDataResponse() }

    context(ApplicationTestBuilder)
    fun createBook(
        isbn: BookIsbn,
        title: BookTitle,
        author: BookAuthor,
        pages: BookPages,
        publisher: BookPublisher,
    ): HttpDataResponse<BookByIdDocument> =
        runBlocking {
            val client = createClient {
                install(ContentNegotiation) {
                    jackson {
                        defaultJacksonConfiguration()
                    }
                }
            }

            client.post("/book") {
                val request = BookRequestDocument(
                    data = BookRequestDocument.Resource(
                        attributes = BookRequestDocument.Resource.Attributes(
                            isbn.value,
                            title.value,
                            author.value,
                            pages.value,
                            publisher.value,
                        ),
                    ),
                )
                setBody(request)
                contentType(ContentType.JsonApi)
            }.toHttpDataResponse()
        }

    context(ApplicationTestBuilder)
    fun findBookById(id: BookId): HttpDataResponse<BookByIdDocument> =
        runBlocking { client.get("/book/${id.value}").toHttpDataResponse() }

    context(ApplicationTestBuilder)
    fun findBookByIsbn(isbn: BookIsbn): HttpDataResponse<BookByIsbnListDocument> =
        runBlocking { client.get("/book?isbn=${isbn.value}").toHttpDataResponse() }

    context(ApplicationTestBuilder)
    fun searchBooks(): HttpDataResponse<BookByIsbnListDocument> =
        runBlocking { client.get("/book").toHttpDataResponse() }

    context(ApplicationTestBuilder)
    fun createLoan(
        bookIsbn: BookIsbn,
        accountId: AccountId,
    ): HttpDataResponse<LoanDocument> =
        runBlocking {
            val client = createClient {
                install(ContentNegotiation) {
                    jackson {
                        defaultJacksonConfiguration()
                    }
                }
            }

            client.post("/loan") {
                val request = LoanRequestDocument(
                    LoanRequestDocument.Resource(
                        attributes = LoanRequestDocument.Resource.Attributes(
                            bookIsbn = bookIsbn.value,
                            accountId = accountId.value,
                        ),
                    ),
                )
                setBody(request)
                contentType(ContentType.JsonApi)
            }.toHttpDataResponse()
        }

    context(ApplicationTestBuilder)
    fun finishLoan(bookId: BookId): HttpDataResponse<Unit> =
        runBlocking {
            client.post("/loan/${bookId.value}").toHttpDataResponse()
        }

    private suspend inline fun <reified T> HttpResponse.toHttpDataResponse(): HttpDataResponse<T> {
        val body = bodyAsText()

        return HttpDataResponse(
            value = decodeJsonBody<T>(body),
            httpResponse = this,
//            error = decodeJsonBody<ErrorResponseDocument>(body),
            error = null,
        )
    }

    private inline fun <reified T> decodeJsonBody(body: String): T? =
        try {
            defaultObjectMapper().readValue(body, T::class.java)
        } catch (e: Exception) {
            logger.error { "Error parsing response: ${e.message}" }
            null
        }
}
