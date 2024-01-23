package com.github.caay2000.librarykata.hexagonal.context.secondaryadapter.database

import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.account.FindAccountCriteria
import com.github.caay2000.librarykata.hexagonal.context.domain.account.SearchAccountCriteria
import com.github.caay2000.memorydb.InMemoryDatasource

class InMemoryAccountRepository(private val datasource: InMemoryDatasource) : AccountRepository {
    override fun save(account: Account) {
        datasource.save(TABLE_NAME, account.id.value, account)
    }

    override fun find(criteria: FindAccountCriteria): Account? =
        when (criteria) {
            is FindAccountCriteria.ById -> datasource.getById<Account>(TABLE_NAME, criteria.id.value)
            is FindAccountCriteria.ByIdentityNumber -> datasource.getAll<Account>(TABLE_NAME).firstOrNull { it.identityNumber == criteria.identityNumber }
            is FindAccountCriteria.ByEmail -> datasource.getAll<Account>(TABLE_NAME).firstOrNull { it.email == criteria.email }
            is FindAccountCriteria.ByPhone -> datasource.getAll<Account>(TABLE_NAME).firstOrNull { it.phone == criteria.phone }
        }

    override fun search(criteria: SearchAccountCriteria): List<Account> =
        when (criteria) {
            SearchAccountCriteria.All -> datasource.getAll(TABLE_NAME)
            is SearchAccountCriteria.ByPhoneNumber -> datasource.getAll<Account>(TABLE_NAME).filter { it.phone.number.value.contains(criteria.phoneNumber.value) }
            is SearchAccountCriteria.ByEmail -> datasource.getAll<Account>(TABLE_NAME).filter { it.email.value.contains(criteria.email.value) }
        }

    companion object {
        private const val TABLE_NAME = "account"
    }
}
