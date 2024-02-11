package com.github.caay2000.common.resourcebus

import com.github.caay2000.common.jsonapi.JsonApiRelationshipData
import com.github.caay2000.common.jsonapi.JsonApiResource
import com.github.caay2000.common.jsonapi.JsonApiResourceAttributes
import com.github.caay2000.common.test.RandomStringGenerator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class JsonApiResourceBusTest {
    private val sut = JsonApiResourceBus()

    @Test
    fun `simple resource handler works`() {
        sut.register(TestJsonApiResourceQueryHandler())

        val identifier = "identifier"

        val result = sut.retrieve(TestJsonApiResource::class, identifier)

        val expected = TestJsonApiResourceMother.testJsonApiResourceMother(identifier)
        assertThat(result).isEqualTo(expected)
    }

    class TestJsonApiResourceQueryHandler : ResourceBusQueryHandler<TestJsonApiResource> {
        override fun handle(identifier: String): TestJsonApiResource {
            return TestJsonApiResourceMother.testJsonApiResourceMother(identifier)
        }
    }

    data class TestJsonApiResource(
        override val id: String,
        override val type: String = "test",
        override val attributes: TestJsonApiResourceAttributes,
        override val relationships: Map<String, JsonApiRelationshipData>? = emptyMap(),
    ) : JsonApiResource

    data class TestJsonApiResourceAttributes(
        val value: String,
    ) : JsonApiResourceAttributes
}

object TestJsonApiResourceMother {
    fun testJsonApiResourceMother(identifier: String = RandomStringGenerator.randomString()) =
        JsonApiResourceBusTest.TestJsonApiResource(
            id = identifier,
            attributes = JsonApiResourceBusTest.TestJsonApiResourceAttributes(identifier),
        )
}
