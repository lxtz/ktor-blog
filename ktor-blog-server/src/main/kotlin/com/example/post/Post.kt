package com.example.post

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Long,
    val created: String?,
    val title: String,
    val author: String,
    val content: String,
)
