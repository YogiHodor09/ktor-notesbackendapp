package com.ktrobackend.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.ktrobackend.model.User
import io.ktor.server.config.*
import java.util.*

class TokenManager(private val config: HoconApplicationConfig) { // 'HoconApplicationConfig' - to read the app config file
    val audience = config.property("audience").getString()
    val secret = config.property("secret").getString()
    val issuer = config.property("issuer").getString()
    val expirationDate = System.currentTimeMillis() + 60000
    fun generateJWTToken(user: User): String {


        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("username", user.username)
            .withClaim("userId", user.id)
            .withExpiresAt(Date(expirationDate))
            .sign(Algorithm.HMAC256(secret))
    }

    // verify JWT Token and return JWT Verifier
    fun verifyJWTToken(): JWTVerifier {
        return JWT.require(Algorithm.HMAC256(secret))
            .withAudience(audience)
            .withIssuer(issuer)
            .build()
    }
}