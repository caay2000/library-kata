package com.github.caay2000.librarykata.hexagonal.context.loan.primaryadapter.http.transformer

import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.http.shouldProcess
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiIncludedResource
import com.github.caay2000.common.jsonapi.JsonApiRelationshipData
import com.github.caay2000.librarykata.hexagonal.context.account.application.find.FindAccountQuery
import com.github.caay2000.librarykata.hexagonal.context.account.application.find.FindAccountQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.account.application.find.FindAccountQueryResponse
import com.github.caay2000.librarykata.hexagonal.context.account.domain.Account
import com.github.caay2000.librarykata.hexagonal.context.account.domain.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.account.primaryadapter.http.transformer.toJsonApiAccountResource
import com.github.caay2000.librarykata.hexagonal.context.book.application.find.FindBookQuery
import com.github.caay2000.librarykata.hexagonal.context.book.application.find.FindBookQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.book.application.find.FindBookQueryResponse
import com.github.caay2000.librarykata.hexagonal.context.book.domain.Book
import com.github.caay2000.librarykata.hexagonal.context.book.domain.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.book.primaryadapter.http.transformer.toJsonApiBookResource
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.Loan
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource
import com.github.caay2000.librarykata.jsonapi.context.book.BookResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource
import com.github.caay2000.librarykata.jsonapi.transformer.IncludeTransformer
import com.github.caay2000.librarykata.jsonapi.transformer.RelationshipIdentifier
import com.github.caay2000.librarykata.jsonapi.transformer.RelationshipTransformer

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
        included = this.manageLoanIncludes(include, account, book),
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
): Map<String, JsonApiRelationshipData>? {
    val map = mutableMapOf<String, JsonApiRelationshipData>()
    if (account != null) {
        val relationship = RelationshipTransformer.invoke(RelationshipIdentifier(account.id.value, AccountResource.TYPE))
        if (relationship != null) {
            map.putAll(relationship)
        }
    }
    if (book != null) {
        val relationship = RelationshipTransformer.invoke(RelationshipIdentifier(book.id.value, BookResource.TYPE))
        if (relationship != null) {
            map.putAll(relationship)
        }
    }
    return map.ifEmpty { null }
}

internal fun Loan.manageLoanIncludes(
    include: Collection<String>,
    account: Account?,
    book: Book?,
): MutableSet<JsonApiIncludedResource>? {
    val included = mutableSetOf<JsonApiIncludedResource>()
    if (include.shouldProcess(AccountResource.TYPE) && account != null) {
        included.add(IncludeTransformer.invoke(account.toJsonApiAccountResource(loans = listOf(this))))
    }
    if (include.shouldProcess(BookResource.TYPE) && book != null) {
        included.add(IncludeTransformer.invoke(book.toJsonApiBookResource(loans = listOf(this))))
    }
    return included.ifEmpty { null }
}

internal fun Loan.toJsonApiDocumentLoanAttributes() =
    LoanResource.Attributes(
        bookId = bookId.value,
        accountId = accountId.value,
        startLoan = createdAt.value,
        finishLoan = finishedAt?.value,
    )
