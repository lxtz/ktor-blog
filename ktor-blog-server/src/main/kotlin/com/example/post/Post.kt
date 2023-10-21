package com.example.post

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Long? = null,
    val created: String? = null,
    val title: String,
    val author: String,
    val content: String,
)
