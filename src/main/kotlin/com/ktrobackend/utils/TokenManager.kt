package com.ktrobackend.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.ktrobackend.model.User
import io.ktor.server.config.*

class TokenManager(private val config: HoconApplicationConfig) { // 'HoconApplicationConfig' - to read the app config file
    fun generateJWTToken(user: User): String {
        val audience = config.property("audience").getString()
        val secret = config.property("secret").getString()
        val issuer = config.property("issuer").getString()
        val expirationDate = System.currentTimeMillis() + 60000

        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("username", user.username)
            .withClaim("userId", user.id)
            .sign(Algorithm.HMAC256(secret))
    }
}