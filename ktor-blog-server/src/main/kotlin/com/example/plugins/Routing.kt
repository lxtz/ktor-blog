package com.example.plugins

import com.example.post.Post
import com.example.post.PostService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val postService by inject<PostService>()

    routing {
        staticResources("/", "static")
        get("/hello") {
            call.respondText("Hello Server")
        }
        get("/api/posts") {
            val posts = postService.getPosts()
            call.respond(posts)
        }
        get("/api/posts/{id}") {
            val id = call.parameters["id"]?.toLong()
            if (id != null) {
                val post = postService.getPost(id)
                call.respond(post ?: HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        post("/api/posts") {
            val post = call.receive<Post>()
            postService.createPost(post.title, post.author, post.content)
        }
    }
}
