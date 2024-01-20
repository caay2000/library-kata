package com.github.caay2000.librarykata.eventdriven.context.loan.loan.primaryadapter.http.serialization

import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.http.shouldProcess
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiIncludedResource
import com.github.caay2000.common.jsonapi.JsonApiRelationshipData
import com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.http.transformer.AccountRelationshipTransformer
import com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.transformer.BookRelationshipTransformer
import com.github.caay2000.librarykata.eventdriven.context.loan.account.application.find.FindAccountQuery
import com.github.caay2000.librarykata.eventdriven.context.loan.account.application.find.FindAccountQueryHandler
import com.github.caay2000.librarykata.eventdriven.context.loan.account.application.find.FindAccountQueryResponse
import com.github.caay2000.librarykata.eventdriven.context.loan.account.domain.Account
import com.github.caay2000.librarykata.eventdriven.context.loan.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.loan.account.domain.AccountRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.book.application.find.FindBookQuery
import com.github.caay2000.librarykata.eventdriven.context.loan.book.application.find.FindBookQueryHandler
import com.github.caay2000.librarykata.eventdriven.context.loan.book.application.find.FindBookQueryResponse
import com.github.caay2000.librarykata.eventdriven.context.loan.book.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.loan.book.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.loan.book.domain.BookRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.Loan
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource
import com.github.caay2000.librarykata.jsonapi.context.book.BookResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource

class LoanDocumentTransformer(
    accountRepository: AccountRepository,
    bookRepository: BookRepository,
) : Transformer<Loan, JsonApiDocument<LoanResource>> {
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
        included = manageLoanIncludes(include, null, null),
    )

internal fun Loan.toJsonApiLoanResource(
    account: Account? = null,
    book: Book? = null,
) = LoanResource(
    id = id.value,
    type = LoanResource.TYPE,
    attributes = toJsonApiDocumentLoanAttributes(),
    relationships = manageLoanRelationships(account?.id, book?.id),
)

private fun manageLoanRelationships(
    accountId: AccountId?,
    bookId: BookId?,
): Map<String, JsonApiRelationshipData> {
    val map = mutableMapOf<String, JsonApiRelationshipData>()
    if (accountId != null) {
        val relationship = AccountRelationshipTransformer().invoke(listOf(com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountId(accountId.value)))
        if (relationship != null) {
            map.putAll(relationship)
        }
    }
    if (bookId != null) {
        val relationship = BookRelationshipTransformer().invoke(listOf(com.github.caay2000.librarykata.eventdriven.context.book.domain.BookId(bookId.value)))
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
//        val elements = AccountIncludeTransformer().invoke(listOf(account))
        val elements = emptyList<JsonApiIncludedResource>()
        if (elements != null) {
            included.addAll(elements)
        }
    }
    if (include.shouldProcess(BookResource.TYPE) && book != null) {
//        val elements = BookIncludeTransformer().invoke(listOf(book))
        val elements = emptyList<JsonApiIncludedResource>()
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