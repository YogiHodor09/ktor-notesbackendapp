package com.ktrobackend

import com.ktrobackend.plugins.configureRouting
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*


fun main() {
    embeddedServer(Netty, port = 8080, host = "localhost") {
        configureRouting()
        //aboutUsModule()
        install(ContentNegotiation) {// negotiate content with JSON
            json()
        }

        // install Authentication with JWT for SECURITY ACCESS

        install(Authentication) {
            jwt {

            }
        }


    }.start(wait = true)
}


