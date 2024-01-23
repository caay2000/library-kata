package com.github.caay2000.librarykata.hexagonal.context.account.domain

import com.github.caay2000.memorydb.Repository

interface AccountRepository : Repository {
    fun save(account: Account)

    fun find(criteria: FindAccountCriteria): Account?

    fun search(criteria: SearchAccountCriteria): List<Account>
}

sealed class FindAccountCriteria {
    class ById(val id: AccountId) : FindAccountCriteria()

    class ByIdentityNumber(val identityNumber: IdentityNumber) : FindAccountCriteria()

    class ByEmail(val email: Email) : FindAccountCriteria()

    class ByPhone(val phone: Phone) : FindAccountCriteria()
}

sealed class SearchAccountCriteria {
    data object All : SearchAccountCriteria()

    data class ByPhoneNumber(val phoneNumber: PhoneNumber) : SearchAccountCriteria()

    data class ByEmail(val email: Email) : SearchAccountCriteria()
}
