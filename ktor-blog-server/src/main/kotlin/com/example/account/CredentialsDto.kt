package com.example.account

import kotlinx.serialization.Serializable

@Serializable
data class CredentialsDto(val username: String, val password: String)
