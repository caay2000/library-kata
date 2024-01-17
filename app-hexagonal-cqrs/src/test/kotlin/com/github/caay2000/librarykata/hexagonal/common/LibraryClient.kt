package com.github.caay2000.librarykata.hexagonal.common

import com.github.caay2000.common.http.ContentType
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiDocumentList
import com.github.caay2000.common.jsonapi.JsonApiErrorDocument
import com.github.caay2000.common.jsonapi.JsonApiRequestDocument
import com.github.caay2000.common.test.http.HttpDataResponse
import com.github.caay2000.librarykata.hexagonal.configuration.jsonMapper
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Birthdate
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Email
import com.github.caay2000.librarykata.hexagonal.context.domain.account.IdentityNumber
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Name
import com.github.caay2000.librarykata.hexagonal.context.domain.account.PhoneNumber
import com.github.caay2000.librarykata.hexagonal.context.domain.account.PhonePrefix
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Surname
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookAuthor
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookId
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookIsbn
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookPages
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookPublisher
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookTitle
import com.github.caay2000.librarykata.jsonapi.context.account.AccountRequestResource
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource
import com.github.caay2000.librarykata.jsonapi.context.book.BookGroupResource
import com.github.caay2000.librarykata.jsonapi.context.book.BookRequestResource
import com.github.caay2000.librarykata.jsonapi.context.book.BookResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanRequestResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.contentType
import io.ktor.server.testing.ApplicationTestBuilder
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import mu.KLogger
import mu.KotlinLogging

class LibraryClient {
    private val logger: KLogger = KotlinLogging.logger {}

    context(ApplicationTestBuilder)
    fun createAccount(
        identityNumber: IdentityNumber,
        name: Name,
        surname: Surname,
        birthdate: Birthdate,
        email: Email,
        phonePrefix: PhonePrefix,
        phoneNumber: PhoneNumber,
    ): HttpDataResponse<JsonApiDocument<AccountResource>> =
        runBlocking {
            val request =
                JsonApiRequestDocument(
                    data =
                        AccountRequestResource(
                            attributes =
                                AccountRequestResource.Attributes(
                                    identityNumber = identityNumber.value,
                                    name = name.value,
                                    surname = surname.value,
                                    birthdate = birthdate.value,
                                    email = email.value,
                                    phonePrefix = phonePrefix.value,
                                    phoneNumber = phoneNumber.value,
                                ),
                        ),
                )
            val jsonRequest = jsonMapper.encodeToString<JsonApiRequestDocument<AccountRequestResource>>(request)
            logger.trace { "CreateAccount Request: $jsonRequest" }
            client.post("/account") {
                setBody(jsonRequest)
                contentType(ContentType.JsonApi)
            }.toHttpDataResponse()
        }

    context(ApplicationTestBuilder)
    fun findAccount(
        id: AccountId,
        include: List<String>,
    ): HttpDataResponse<JsonApiDocument<AccountResource>> =
        runBlocking {
            val includeQuery = include.joinToString { it.lowercase() }.let { if (it.isNotBlank()) "?include=$it" else "" }
            client.get("/account/${id.value}$includeQuery").toHttpDataResponse<JsonApiDocument<AccountResource>>()
        }

    context(ApplicationTestBuilder)
    fun searchAccount(include: List<String>): HttpDataResponse<JsonApiDocumentList<AccountResource>> =
        runBlocking {
            val includeQuery = include.joinToString { it.lowercase() }.let { if (it.isNotBlank()) "?include=$it" else "" }
            client.get("/account$includeQuery").toHttpDataResponse<JsonApiDocumentList<AccountResource>>()
        }

    context(ApplicationTestBuilder)
    fun searchAccountByPhoneNumber(
        phoneNumber: String,
        include: List<String>,
    ): HttpDataResponse<JsonApiDocumentList<AccountResource>> =
        runBlocking {
            val includeQuery = include.joinToString { it.lowercase() }.let { if (it.isNotBlank()) "&include=$it" else "" }
            client.get("/account?filter[phoneNumber]=$phoneNumber$includeQuery").toHttpDataResponse<JsonApiDocumentList<AccountResource>>()
        }

    context(ApplicationTestBuilder)
    fun searchAccountByEmail(
        email: String,
        include: List<String>,
    ): HttpDataResponse<JsonApiDocumentList<AccountResource>> =
        runBlocking {
            val includeQuery = include.joinToString { it.lowercase() }.let { if (it.isNotBlank()) "&include=$it" else "" }
            client.get("/account?filter[email]=$email$includeQuery").toHttpDataResponse<JsonApiDocumentList<AccountResource>>()
        }

    context(ApplicationTestBuilder)
    fun createBook(
        isbn: BookIsbn,
        title: BookTitle,
        author: BookAuthor,
        pages: BookPages,
        publisher: BookPublisher,
    ): HttpDataResponse<JsonApiDocument<BookResource>> =
        runBlocking {
            val request =
                JsonApiRequestDocument(
                    data =
                        BookRequestResource(
                            attributes =
                                BookRequestResource.Attributes(
                                    isbn.value,
                                    title.value,
                                    author.value,
                                    pages.value,
                                    publisher.value,
                                ),
                        ),
                )
            val jsonRequest = jsonMapper.encodeToString(request)
            logger.trace { "CreateBook Request: $jsonRequest" }
            client.post("/book") {
                setBody(jsonRequest)
                contentType(ContentType.JsonApi)
            }.toHttpDataResponse()
        }

    context(ApplicationTestBuilder)
    fun findBook(
        id: BookId,
        include: List<String>,
    ): HttpDataResponse<JsonApiDocument<BookResource>> =
        runBlocking {
            val includeQuery =
                include.joinToString { it.lowercase() }.let {
                    if (it.isNotBlank()) "?include=$it" else ""
                }
            client.get("/book/${id.value}$includeQuery").toHttpDataResponse<JsonApiDocument<BookResource>>()
        }

    context(ApplicationTestBuilder)
    fun findBookByIsbn(isbn: BookIsbn): HttpDataResponse<JsonApiDocumentList<BookGroupResource>> =
        runBlocking { client.get("/book?filter[isbn]=${isbn.value}").toHttpDataResponse() }

    context(ApplicationTestBuilder)
    fun searchBook(): HttpDataResponse<JsonApiDocumentList<BookGroupResource>> = runBlocking { client.get("/book").toHttpDataResponse() }

    context(ApplicationTestBuilder)
    fun createLoan(
        bookIsbn: BookIsbn,
        accountId: AccountId,
    ): HttpDataResponse<JsonApiDocument<LoanResource>> =
        runBlocking {
            client.post("/loan") {
                val request =
                    JsonApiRequestDocument(
                        LoanRequestResource(
                            attributes =
                                LoanRequestResource.Attributes(
                                    bookIsbn = bookIsbn.value,
                                    accountId = accountId.value,
                                ),
                        ),
                    )
                setBody(jsonMapper.encodeToString(request))
                contentType(ContentType.JsonApi)
            }.toHttpDataResponse()
        }

    context(ApplicationTestBuilder)
    fun finishLoan(bookId: BookId): HttpDataResponse<Unit> =
        runBlocking {
            client.post("/loan/${bookId.value}").toHttpDataResponse()
        }

    private suspend inline fun <reified T> HttpResponse.toHttpDataResponse(): HttpDataResponse<T> {
        val body =
            bodyAsText().also {
                logger.trace { "Client Response: $it" }
            }
        return HttpDataResponse(
            value = decodeJsonBody<T?>(body),
            httpResponse = this,
            error = decodeJsonBody<JsonApiErrorDocument?>(body),
        )
    }

    private inline fun <reified T> decodeJsonBody(body: String): T? =
        try {
            jsonMapper.decodeFromString<T>(body)
        } catch (e: Exception) {
            null
        }
}
