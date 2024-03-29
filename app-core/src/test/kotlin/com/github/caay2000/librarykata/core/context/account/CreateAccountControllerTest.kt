package com.github.caay2000.librarykata.core.context.account

import com.github.caay2000.common.jsonapi.jsonApiErrorDocument
import com.github.caay2000.common.test.http.assertJsonApiErrorDocument
import com.github.caay2000.common.test.http.assertJsonApiResponse
import com.github.caay2000.common.test.http.assertStatus
import com.github.caay2000.common.test.mock.MockDateProvider
import com.github.caay2000.common.test.mock.MockIdGenerator
import com.github.caay2000.dikt.DiKt
import com.github.caay2000.librarykata.core.common.TestUseCases
import com.github.caay2000.librarykata.core.context.account.mother.AccountDocumentMother
import com.github.caay2000.librarykata.core.context.account.mother.AccountMother
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CreateAccountControllerTest {
    private val mockIdGenerator = MockIdGenerator()
    private val mockDateProvider = MockDateProvider()
    private val testUseCases = TestUseCases(mockIdGenerator = mockIdGenerator, mockDateProvider = mockDateProvider)

    @BeforeEach
    fun setUp() {
        DiKt.clear()
        DiKt.register(override = true) { mockIdGenerator }
        DiKt.register(override = true) { mockDateProvider }
    }

    @Test
    fun `an account can be created`() =
        testApplication {
            val expected = AccountDocumentMother.random(account)
            testUseCases.`account is created`(account)
                .assertStatus(HttpStatusCode.Created)
                .assertJsonApiResponse(expected)
        }

    @Test
    fun `fails when creating an account with repeated identityNumber`() =
        testApplication {
            testUseCases.`account is created`(account)
                .assertStatus(HttpStatusCode.Created)

            testUseCases.`account is created`(sameIdentityNumberAccount)
                .assertStatus(HttpStatusCode.BadRequest)
                .assertJsonApiErrorDocument(
                    jsonApiErrorDocument(
                        status = HttpStatusCode.BadRequest,
                        title = "IdentityNumberAlreadyExists",
                        detail = "an account with identity number ${account.identityNumber.value} already exists",
                    ),
                )
        }

    @Test
    fun `fails when creating an account with repeated email`() =
        testApplication {
            testUseCases.`account is created`(account)
                .assertStatus(HttpStatusCode.Created)

            testUseCases.`account is created`(sameEmailAccount)
                .assertStatus(HttpStatusCode.BadRequest)
                .assertJsonApiErrorDocument(
                    jsonApiErrorDocument(
                        status = HttpStatusCode.BadRequest,
                        title = "EmailAlreadyExists",
                        detail = "an account with email ${account.email.value} already exists",
                    ),
                )
        }

    @Test
    fun `fails when creating an account with repeated phone`() =
        testApplication {
            testUseCases.`account is created`(account)
                .assertStatus(HttpStatusCode.Created)

            testUseCases.`account is created`(samePhoneAccount)
                .assertStatus(HttpStatusCode.BadRequest)
                .assertJsonApiErrorDocument(
                    jsonApiErrorDocument(
                        status = HttpStatusCode.BadRequest,
                        title = "PhoneAlreadyExists",
                        detail = "an account with phone ${account.phone} already exists",
                    ),
                )
        }

    private val account = AccountMother.random()
    private val sameIdentityNumberAccount = AccountMother.random().copy(identityNumber = account.identityNumber)
    private val sameEmailAccount = AccountMother.random().copy(email = account.email)
    private val samePhoneAccount = AccountMother.random().copy(phone = account.phone)
}
