package com.example.post

import com.example.Database
import com.example.db.toModel

class PostService(private val database: Database) {
    fun getPosts(): List<Post> = database.postQueries.getPosts().executeAsList().toModel()

    fun getPost(id: Long): Post? = database.postQueries.getPost(id).executeAsOneOrNull()?.toModel()

    fun createPost(title: String, author: String, content: String): Long {
        database.postQueries.createPost(title, author, content)
        return database.postQueries.lastInsertRowId().executeAsOne()
    }
}