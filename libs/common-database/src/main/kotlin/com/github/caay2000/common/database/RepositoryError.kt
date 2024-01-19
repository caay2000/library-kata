package com.github.caay2000.common.database

sealed class RepositoryError : RuntimeException() {
    class NotFoundError : RepositoryError()
}
