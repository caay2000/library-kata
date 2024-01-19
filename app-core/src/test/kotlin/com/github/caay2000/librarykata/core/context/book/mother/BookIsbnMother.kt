package com.github.caay2000.librarykata.core.context.book.mother

import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookIsbn
import java.util.UUID

object BookIsbnMother {
    fun random(): BookIsbn = BookIsbn(UUID.randomUUID().toString())
}
