package com.github.caay2000.librarykata.hexagonal.context.book.primaryadapter.http.transformer

import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.http.shouldProcess
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.librarykata.hexagonal.context.book.domain.Book
import com.github.caay2000.librarykata.hexagonal.context.loan.application.search.SearchLoanQuery
import com.github.caay2000.librarykata.hexagonal.context.loan.application.search.SearchLoanQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.loan.application.search.SearchLoanQueryResponse
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.Loan
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.loan.primaryadapter.http.serialization.toJsonApiLoanResource
import com.github.caay2000.librarykata.jsonapi.context.book.BookResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource
import com.github.caay2000.librarykata.jsonapi.transformer.IncludeTransformer
import com.github.caay2000.librarykata.jsonapi.transformer.RelationshipIdentifier
import com.github.caay2000.librarykata.jsonapi.transformer.RelationshipTransformer

class BookDocumentTransformer(loanRepository: LoanRepository) : Transformer<Book, JsonApiDocument<BookResource>> {
    private val loanQueryHandler: QueryHandler<SearchLoanQuery, SearchLoanQueryResponse> = SearchLoanQueryHandler(loanRepository)

    override fun invoke(
        value: Book,
        include: List<String>,
    ): JsonApiDocument<BookResource> {
        // TODO When the Book is brand new, this query is not needed, as it won't have any relationship
        val loans = loanQueryHandler.invoke(SearchLoanQuery.SearchLoanByBookIdQuery(value.id.value)).value
        return value.toJsonApiBookDocument(loans, include)
    }
}

fun Book.toJsonApiBookDocument(
    loans: List<Loan> = emptyList(),
    include: List<String> = emptyList(),
) = JsonApiDocument(
    data = toJsonApiBookResource(loans),
    included = if (include.shouldProcess(LoanResource.TYPE)) IncludeTransformer.invoke(loans.map { it.toJsonApiLoanResource() }) else null,
)

internal fun Book.toJsonApiBookResource(loans: List<Loan> = emptyList()) =
    BookResource(
        id = id.value,
        type = BookResource.TYPE,
        attributes = toJsonApiBookAttributes(),
        relationships = RelationshipTransformer.invoke(loans.map { RelationshipIdentifier(it.id.value, LoanResource.TYPE) }),
    )

internal fun Book.toJsonApiBookAttributes() =
    BookResource.Attributes(
        isbn = isbn.value,
        title = title.value,
        author = author.value,
        pages = pages.value,
        publisher = publisher.value,
        available = available.value,
    )
