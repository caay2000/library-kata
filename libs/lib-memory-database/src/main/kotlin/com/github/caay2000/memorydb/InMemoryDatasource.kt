package com.github.caay2000.memorydb

@Suppress("UNCHECKED_CAST")
class InMemoryDatasource : Datasource {
    private val database: MutableMap<String, MutableMap<String, Any>> = mutableMapOf()

    fun <T> save(
        table: String,
        key: String,
        value: T,
    ): T {
        val actualTable: MutableMap<String, *>? = database[table]
        if (actualTable == null) {
            database[table] = mutableMapOf()
        }
        database[table]!![key] = value as Any
        return value
    }

    fun delete(
        table: String,
        key: String,
    ) {
        database[table]?.remove(key)
    }

    fun <T> getById(
        table: String,
        id: String,
    ): T? = database[table]?.get(id) as T?

    fun <T> getAll(table: String): List<T> = (database[table]?.values?.toList() ?: emptyList()) as List<T>

    fun exists(
        table: String,
        key: String,
    ) = database[table]?.containsKey(key) ?: false

    fun clean() = database.clear()
}
