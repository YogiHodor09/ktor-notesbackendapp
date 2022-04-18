package com.ktrobackend

import com.ktrobackend.plugins.aboutUsModule
import com.ktrobackend.plugins.configureRouting
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import org.ktorm.database.Database


fun main() {
    embeddedServer(Netty, port = 8080, host = "localhost") {
        install(ContentNegotiation) {
            json()
        }
        configureRouting()
        aboutUsModule()

        // Create database

        val database = Database.connect(
            url = "jdbc:mysql://127.0.0.1:3306/notes",
            driver = "com.mysql.cj.jdbc.Driver",
            user = "root",
            password = "admin"
        )
    }.start(wait = true)
}


