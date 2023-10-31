package com.example.db

fun Account.toModel() = com.example.account.Account(id, created, username, password_hash, admin)

fun List<Account>.toModel() = map { it.toModel() }