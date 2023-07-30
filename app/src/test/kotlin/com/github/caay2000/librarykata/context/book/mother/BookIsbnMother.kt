package com.github.caay2000.librarykata.context.book.mother

import com.github.caay2000.librarykata.context.book.domain.BookIsbn
import java.util.UUID

object BookIsbnMother {
    fun random(): BookIsbn = BookIsbn(UUID.randomUUID().toString())
}
