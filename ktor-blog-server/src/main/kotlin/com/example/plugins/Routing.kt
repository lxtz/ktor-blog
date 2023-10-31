package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.AuthSession
import com.example.account.AccountService
import com.example.account.CredentialsDto
import com.example.post.Post
import com.example.post.PostService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val postService by inject<PostService>()
    val accountService by inject<AccountService>()

    routing {
        singlePageApplication {
            useResources = true
            filesPath = "static"
            defaultPage = "index.html"
        }
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
        post("/login") {
            val credentials = call.receive<CredentialsDto>()
            val account = accountService.verifyCredentials(credentials.username, credentials.password)
            if (account != null) {
                val token = JWT.create()
                    .withClaim("username", account.username)
                    .sign(Algorithm.HMAC256("jwt-secret"))
                val signature = token.substringAfterLast('.')
                val payload = token.substringBeforeLast('.')
                call.sessions.set(AuthSession(signature))
                call.respond(hashMapOf("token" to payload))
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
        post("/logout") {
            call.sessions.clear("token")
            call.respond(HttpStatusCode.OK)
        }
        authenticate("auth-jwt") {
            post("/api/posts") {
                val post = call.receive<Post>()
                call.respond(postService.createPost(post.title, post.author, post.content))
            }
        }
    }
}
