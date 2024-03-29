package com.github.caay2000.librarykata.hexagonal.context.book.primaryadapter.http.transformer

import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.http.shouldProcess
import com.github.caay2000.common.jsonapi.JsonApiDocumentList
import com.github.caay2000.common.jsonapi.JsonApiMeta
import com.github.caay2000.librarykata.hexagonal.context.book.domain.Book
import com.github.caay2000.librarykata.hexagonal.context.loan.application.search.SearchLoanQuery
import com.github.caay2000.librarykata.hexagonal.context.loan.application.search.SearchLoanQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.loan.application.search.SearchLoanQueryResponse
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.Loan
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.loan.primaryadapter.http.transformer.toJsonApiLoanResource
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource
import com.github.caay2000.librarykata.jsonapi.context.book.BookGroupResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource
import com.github.caay2000.librarykata.jsonapi.transformer.IncludeTransformer
import com.github.caay2000.librarykata.jsonapi.transformer.RelationshipIdentifier
import com.github.caay2000.librarykata.jsonapi.transformer.RelationshipTransformer

class BookGroupDocumentListTransformer(loanRepository: LoanRepository) : Transformer<List<Book>, JsonApiDocumentList<BookGroupResource>> {
    private val loanQueryHandler: QueryHandler<SearchLoanQuery, SearchLoanQueryResponse> = SearchLoanQueryHandler(loanRepository)

    override fun invoke(
        value: List<Book>,
        include: List<String>,
    ): JsonApiDocumentList<BookGroupResource> {
        val groupByIsbn = value.groupBy { it.isbn }
        val loans = groupByIsbn.keys.flatMap { loanQueryHandler.invoke(SearchLoanQuery.SearchLoanByBookIsbnQuery(it.value)).value }.toSet()
        return value.toJsonApiBookGroupDocumentList(loans, include)
    }
}

fun List<Book>.toJsonApiBookGroupDocumentList(
    loans: Collection<Loan> = emptyList(),
    include: List<String> = emptyList(),
) = JsonApiDocumentList(
    data = groupBy { it.isbn }.map { it.value.toJsonApiBookGroupResource(loans) },
    included = if (include.shouldProcess(LoanResource.TYPE)) IncludeTransformer.invoke(loans.map { it.toJsonApiLoanResource() }) else null,
    meta = JsonApiMeta(total = groupBy { it.isbn }.size),
)

internal fun List<Book>.toJsonApiBookGroupResource(loans: Collection<Loan> = emptyList()) =
    BookGroupResource(
        id = first().isbn.value,
        type = BookGroupResource.TYPE,
        attributes = toJsonApiBookGroupAttributes(),
        relationships =
            RelationshipTransformer.invoke(
                loans.filter { loan -> map { it.id }.contains(loan.bookId) }
                    .map { RelationshipIdentifier(it.id.value, AccountResource.TYPE) },
            ),
    )

internal fun List<Book>.toJsonApiBookGroupAttributes() =
    BookGroupResource.Attributes(
        isbn = first().isbn.value,
        title = first().title.value,
        author = first().author.value,
        pages = first().pages.value,
        publisher = first().publisher.value,
        copies = size,
        availableCopies = count { it.isAvailable },
    )
