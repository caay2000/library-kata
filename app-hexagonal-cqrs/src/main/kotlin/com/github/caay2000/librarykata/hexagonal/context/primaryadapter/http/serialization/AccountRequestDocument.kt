package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization

import com.github.caay2000.common.jsonapi.JsonApiRequestAttributes
import com.github.caay2000.common.jsonapi.JsonApiRequestDocument
import com.github.caay2000.common.jsonapi.JsonApiRequestResource
import java.time.LocalDate

data class AccountRequestDocument(
    override val data: Resource,
) : JsonApiRequestDocument {

    data class Resource(
        override val type: String = "account",
        override val attributes: Attributes,
    ) : JsonApiRequestResource {

        data class Attributes(
            val identityNumber: String,
            val name: String,
            val surname: String,
            val birthdate: LocalDate,
            val email: String,
            val phonePrefix: String,
            val phoneNumber: String,
        ) : JsonApiRequestAttributes
    }
}
