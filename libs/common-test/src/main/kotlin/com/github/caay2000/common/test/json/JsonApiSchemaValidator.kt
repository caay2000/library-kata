package com.github.caay2000.common.test.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion

class JsonApiSchemaValidator {
    private val factory: JsonSchemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4)

    private val mapper = ObjectMapper()

    fun validate(
        schema: String,
        document: String,
    ): List<String> {
        val inputSource = Thread.currentThread().getContextClassLoader().getResourceAsStream(schema)

        val errors = factory.getSchema(inputSource).validate(mapper.readTree(document))

        return errors.map { it.toString() }
    }
}
