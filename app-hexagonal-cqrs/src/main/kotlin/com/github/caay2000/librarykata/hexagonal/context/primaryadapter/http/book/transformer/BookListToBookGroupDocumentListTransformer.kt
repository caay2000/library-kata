package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.book.transformer

import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.http.shouldProcess
import com.github.caay2000.common.jsonapi.JsonApiDocumentList
import com.github.caay2000.common.jsonapi.JsonApiMeta
import com.github.caay2000.librarykata.hexagonal.context.application.loan.search.SearchLoanQuery
import com.github.caay2000.librarykata.hexagonal.context.application.loan.search.SearchLoanQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.application.loan.search.SearchLoanQueryResponse
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.loan.serialization.LoanRelationshipTransformer
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.toJsonApiDocumentIncludedResource
import com.github.caay2000.librarykata.jsonapi.context.book.BookGroupResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource

class BookListToBookGroupDocumentListTransformer(loanRepository: LoanRepository) : Transformer<List<Book>, JsonApiDocumentList<BookGroupResource>> {
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
    data = groupBy { it.isbn }.map { it.value.toJsonApiDocumentBookGroupResource(loans) },
    included = if (include.shouldProcess(LoanResource.TYPE)) loans.toJsonApiDocumentIncludedResource() else null,
    meta = JsonApiMeta(total = groupBy { it.isbn }.size),
)

internal fun List<Book>.toJsonApiDocumentBookGroupResource(loans: Collection<Loan> = emptyList()) =
    BookGroupResource(
        id = first().isbn.value,
        type = "book-group",
        attributes = toJsonApiDocumentBookGroupAttributes(),
        relationships = LoanRelationshipTransformer().invoke(loans.filter { loan -> map { it.id }.contains(loan.bookId) }),
    )

internal fun List<Book>.toJsonApiDocumentBookGroupAttributes() =
    BookGroupResource.Attributes(
        isbn = first().isbn.value,
        title = first().title.value,
        author = first().author.value,
        pages = first().pages.value,
        publisher = first().publisher.value,
        copies = size,
        availableCopies = count { it.isAvailable },
    )
