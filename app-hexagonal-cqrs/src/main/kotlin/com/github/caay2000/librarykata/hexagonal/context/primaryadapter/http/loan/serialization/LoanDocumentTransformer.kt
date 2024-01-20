package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.loan.serialization

import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.http.shouldProcess
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiIncludedResource
import com.github.caay2000.common.jsonapi.JsonApiRelationshipData
import com.github.caay2000.librarykata.hexagonal.context.application.account.find.FindAccountQuery
import com.github.caay2000.librarykata.hexagonal.context.application.account.find.FindAccountQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.application.account.find.FindAccountQueryResponse
import com.github.caay2000.librarykata.hexagonal.context.application.book.find.FindBookQuery
import com.github.caay2000.librarykata.hexagonal.context.application.book.find.FindBookQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.application.book.find.FindBookQueryResponse
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.transformer.AccountIncludeTransformer
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.transformer.AccountRelationshipTransformer
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.book.transformer.BookIncludeTransformer
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.book.transformer.BookRelationshipTransformer
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource
import com.github.caay2000.librarykata.jsonapi.context.book.BookResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource

class LoanDocumentTransformer(accountRepository: AccountRepository, bookRepository: BookRepository) : Transformer<Loan, JsonApiDocument<LoanResource>> {
    private val accountQueryHandler: QueryHandler<FindAccountQuery, FindAccountQueryResponse> = FindAccountQueryHandler(accountRepository)
    private val bookQueryHandler: QueryHandler<FindBookQuery, FindBookQueryResponse> = FindBookQueryHandler(bookRepository)

    override fun invoke(
        value: Loan,
        include: List<String>,
    ): JsonApiDocument<LoanResource> {
        val account = accountQueryHandler.handle(FindAccountQuery(value.accountId)).account
        val book = bookQueryHandler.handle(FindBookQuery(value.bookId)).book
        return value.toJsonApiDocument(account, book, include)
    }
}

fun Loan.toJsonApiDocument(
    account: Account? = null,
    book: Book? = null,
    include: List<String> = emptyList(),
): JsonApiDocument<LoanResource> =
    JsonApiDocument(
        data = toJsonApiLoanResource(account, book),
        included = manageLoanIncludes(include, account, book),
    )

internal fun Loan.toJsonApiLoanResource(
    account: Account? = null,
    book: Book? = null,
) = LoanResource(
    id = id.value,
    type = LoanResource.TYPE,
    attributes = toJsonApiDocumentLoanAttributes(),
    relationships = manageLoanRelationships(account, book),
)

fun manageLoanRelationships(
    account: Account?,
    book: Book?,
): Map<String, JsonApiRelationshipData> {
    val map = mutableMapOf<String, JsonApiRelationshipData>()
    if (account != null) {
        val relationship = AccountRelationshipTransformer().invoke(listOf(account))
        if (relationship != null) {
            map.putAll(relationship)
        }
    }
    if (book != null) {
        val relationship = BookRelationshipTransformer().invoke(listOf(book))
        if (relationship != null) {
            map.putAll(relationship)
        }
    }
    return map
}

private fun manageLoanIncludes(
    include: List<String>,
    account: Account?,
    book: Book?,
): MutableSet<JsonApiIncludedResource>? {
    if (include.isEmpty()) return null
    val included = mutableSetOf<JsonApiIncludedResource>()
    if (include.shouldProcess(AccountResource.TYPE) && account != null) {
        val elements = AccountIncludeTransformer().invoke(listOf(account))
        if (elements != null) {
            included.addAll(elements)
        }
    }
    if (include.shouldProcess(BookResource.TYPE) && book != null) {
        val elements = BookIncludeTransformer().invoke(listOf(book))
        if (elements != null) {
            included.addAll(elements)
        }
    }
    return included
}

internal fun Loan.toJsonApiDocumentLoanAttributes() =
    LoanResource.Attributes(
        bookId = bookId.value,
        accountId = accountId.value,
        startLoan = createdAt.value,
        finishLoan = finishedAt?.value,
    )
