package com.ktrobackend

import com.ktrobackend.plugins.configureRouting
import com.ktrobackend.utils.TokenManager
import com.typesafe.config.ConfigFactory
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*


fun main() {
    embeddedServer(Netty, port = 8083, host = "192.168.1.176") {
        val config = HoconApplicationConfig(ConfigFactory.load())
        val tokenManager = TokenManager(config)

        // install Authentication with JWT for SECURITY ACCESS

        install(Authentication) {
            jwt {
                verifier(tokenManager.verifyJWTToken())
                realm = config.property("realm").getString()
                validate { jwtCredential ->
                    if (jwtCredential.payload.getClaim("username").toString().isNotEmpty()) {
                        JWTPrincipal(jwtCredential.payload)

                    } else {
                        null

                    }
                }
            }
        }

        install(ContentNegotiation) {// negotiate content with JSON
            json()
        }



        configureRouting()


    }.start(wait = true)
}


