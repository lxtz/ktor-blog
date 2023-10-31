package com.example.account

import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val id: Long,
    val created: String?,
    val username: String,
    val passwordHash: String,
    val admin: Boolean,
)