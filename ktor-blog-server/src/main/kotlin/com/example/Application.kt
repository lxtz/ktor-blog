package com.example

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.plugins.configureRouting
import com.example.post.PostService
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

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

    install(Koin) {
        slf4jLogger()
        modules(module {
            single<SqlDriver> { JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY) }
            single { Database(get()) }
            single { PostService(get()) }
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

    configureRouting()
}
