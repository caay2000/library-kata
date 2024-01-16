package com.github.caay2000.common.jsonapi

import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable

@Serializable
data class JsonApiErrorDocument(
    val errors: List<JsonApiErrorAttribute>,
)

@Serializable
data class JsonApiErrorAttribute(
    val status: String? = null,
    val title: String,
    val detail: String,
)

data class ServerResponse(val status: HttpStatusCode, val title: String = "Unknown Error", val detail: String? = title) {
    val jsonApiErrorDocument = jsonApiErrorDocument(status, title, detail)
}

fun jsonApiErrorDocument(
    status: HttpStatusCode,
    title: String = "Unknown Error",
    detail: String? = title,
) = JsonApiErrorDocument(listOf(JsonApiErrorAttribute(status = status.value.toString(), title = title, detail = detail ?: title)))
