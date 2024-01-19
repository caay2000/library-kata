package com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.transformer

import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.http.shouldProcess
import com.github.caay2000.common.jsonapi.JsonApiDocumentList
import com.github.caay2000.common.jsonapi.JsonApiMeta
import com.github.caay2000.librarykata.eventdriven.context.book.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.Loan
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.primaryadapter.http.serialization.LoanIncludeTransformer
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.primaryadapter.http.serialization.LoanRelationshipTransformer
import com.github.caay2000.librarykata.jsonapi.context.book.BookGroupResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource

class BookGroupDocumentListTransformer() : Transformer<List<Book>, JsonApiDocumentList<BookGroupResource>> {
//    private val loanQueryHandler: QueryHandler<SearchLoanQuery, SearchLoanQueryResponse> = SearchLoanQueryHandler(loanRepository)

    override fun invoke(
        value: List<Book>,
        include: List<String>,
    ): JsonApiDocumentList<BookGroupResource> {
        val groupByIsbn = value.groupBy { it.isbn }
//        val loans = groupByIsbn.keys.flatMap { loanQueryHandler.invoke(SearchLoanQuery.SearchLoanByBookIsbnQuery(it.value)).value }.toSet()
        return value.toJsonApiBookGroupDocumentList(emptyList(), include)
    }
}

fun List<Book>.toJsonApiBookGroupDocumentList(
    loans: Collection<Loan> = emptyList(),
    include: List<String> = emptyList(),
) = JsonApiDocumentList(
    data = groupBy { it.isbn }.map { it.value.toJsonApiBookGroupResource(loans) },
    included = if (include.shouldProcess(LoanResource.TYPE)) LoanIncludeTransformer().invoke(loans) else null,
    meta = JsonApiMeta(total = groupBy { it.isbn }.size),
)

internal fun List<Book>.toJsonApiBookGroupResource(loans: Collection<Loan> = emptyList()) =
    BookGroupResource(
        id = first().isbn.value,
        type = BookGroupResource.TYPE,
        attributes = toJsonApiBookGroupAttributes(),
        relationships = LoanRelationshipTransformer().invoke(loans.filter { loan -> map { it.id }.contains(loan.bookId) }),
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
