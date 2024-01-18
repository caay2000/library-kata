package com.github.caay2000.common.jsonapi.documentation

import com.github.caay2000.common.http.ContentType
import com.github.caay2000.common.jsonapi.JsonApiErrorDocument
import com.github.caay2000.common.jsonapi.jsonApiErrorDocument
import io.github.smiley4.ktorswaggerui.dsl.OpenApiResponses
import io.ktor.http.HttpStatusCode

fun OpenApiResponses.errorResponses(
    httpStatusCode: HttpStatusCode,
    summary: String,
    vararg examples: JsonApiDocumentationErrorResponse,
) {
    httpStatusCode to {
        description = summary
        body<JsonApiErrorDocument> {
            mediaType(ContentType.JsonApi)
            examples.forEach {
                example(
                    name = it.name,
                    value =
                        jsonApiErrorDocument(
                            status = httpStatusCode,
                            title = it.name,
                            detail = it.detail,
                        ),
                )
            }
        }
    }
}

data class JsonApiDocumentationErrorResponse(val name: String, val detail: String)

fun responseExample(
    name: String,
    detail: String,
) = JsonApiDocumentationErrorResponse(name = name, detail = detail)
