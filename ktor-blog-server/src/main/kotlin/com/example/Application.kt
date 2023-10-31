package com.example

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.account.AccountService
import com.example.plugins.configureRouting
import com.example.post.PostService
import io.ktor.http.auth.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

data class AuthSession(val token: String) : Principal
//typealias AuthSession = String

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module).start(wait = true)
}

fun Application.module() {
    install(IgnoreTrailingSlash)

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }

    install(Authentication) {
        jwt("auth-jwt") {
            verifier(JWT.require(Algorithm.HMAC256("jwt-secret")).build())

            authHeader { call ->
                val headerValue = call.request.header("Authorization")
                val cookieValue = call.sessions.get<AuthSession>() ?: return@authHeader null
                try {
                    parseAuthorizationHeader("$headerValue.${cookieValue.token}")
                } catch (cause: IllegalArgumentException) {
                    null
                }
            }

            validate { credential ->
                JWTPrincipal(credential.payload)
            }
        }
    }

    install(Sessions) {
        cookie<AuthSession>("token")
    }

    install(Koin) {
        slf4jLogger()
        modules(module {
            single<SqlDriver> { JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY) }
            single {
                Database(get())
            }
            single { PostService(get()) }
            single { AccountService(get()) }
        })
    }

    val driver by inject<SqlDriver>()
    Database.Schema.create(driver)

    val postService by inject<PostService>()
    postService.createPost(
        "Random Quote 1", "Socrates", "Wonder is the feeling of a philosopher, and philosophy begins in wonder."
    )
    postService.createPost(
        "Random Quote 2",
        "Plato",
        "It is difficult to set forth any of the greater ideas, except by the use of examples; for it would seem that each of us knows everything that he knows as if in a dream and then again, when he is as it were awake, knows nothing of it all."
    )

    val accountService by inject<AccountService>()
    accountService.createAccount(
        "admin",
        "foo",
        true
    )
    accountService.createAccount(
        "user",
        "bar",
        false
    )

    configureRouting()
}
