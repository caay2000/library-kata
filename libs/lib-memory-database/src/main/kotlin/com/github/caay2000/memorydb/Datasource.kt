package com.github.caay2000.memorydb

interface Datasource {

    fun <T> save(table: String, key: String, value: T): T
    fun <T> getById(table: String, id: String): T?
    fun <T> getAll(table: String): List<T>
    fun exists(table: String, key: String): Boolean
    fun clean()
}
