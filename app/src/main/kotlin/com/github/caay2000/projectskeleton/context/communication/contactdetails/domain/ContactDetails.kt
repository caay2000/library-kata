package com.github.caay2000.projectskeleton.context.communication.contactdetails.domain

import com.github.caay2000.common.ddd.DomainId

data class ContactDetails(
    val accountNumber: AccountNumber,
    val email: Email,
    val phoneNumber: PhoneNumber,
    val phonePrefix: PhonePrefix,
) {

    companion object {
        fun create(accountNumber: AccountNumber, email: Email, phoneNumber: PhoneNumber, phonePrefix: PhonePrefix) =
            ContactDetails(
                accountNumber = accountNumber,
                email = email,
                phoneNumber = phoneNumber,
                phonePrefix = phonePrefix,
            )
    }
}

@JvmInline
value class AccountNumber(val value: String) : DomainId

@JvmInline
value class Email(val value: String)

@JvmInline
value class PhoneNumber(val value: String)

@JvmInline
value class PhonePrefix(val value: String)
