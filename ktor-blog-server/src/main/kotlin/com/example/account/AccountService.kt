package com.example.account

import com.example.Database
import com.example.db.toModel
import org.mindrot.jbcrypt.BCrypt

class AccountService(private val database: Database) {
    fun getAccounts(): List<Account> = database.accountQueries.getAccounts().executeAsList().toModel()

    fun getAccount(id: Long): Account? = database.accountQueries.getAccount(id).executeAsOneOrNull()?.toModel()

    fun verifyCredentials(username: String, password: String): Account? {
        val account =
            database.accountQueries.findAccountByName(username).executeAsOneOrNull()?.toModel()
        return if (account != null && BCrypt.checkpw(password, account.passwordHash)) {
            account
        } else {
            null
        }
    }

    fun createAccount(username: String, password: String, admin: Boolean): Long {
        database.accountQueries.createAccount(username, BCrypt.hashpw(password, BCrypt.gensalt()), admin)
        return database.accountQueries.lastInsertRowId().executeAsOne()
    }
}