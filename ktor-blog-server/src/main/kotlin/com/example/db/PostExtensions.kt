package com.example.db

fun Post.toModel() = com.example.post.Post(id, created, title, author, content)

fun List<Post>.toModel() = map { it.toModel() }