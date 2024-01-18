package com.github.caay2000.common.database

sealed class RepositoryError : RuntimeException {
    constructor() : super()
    constructor(throwable: Throwable) : super(throwable)

    class NotFoundError : RepositoryError()
}
