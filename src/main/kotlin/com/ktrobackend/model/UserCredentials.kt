package com.ktrobackend.model

import org.mindrot.jbcrypt.BCrypt


@kotlinx.serialization.Serializable
data class UserCredentials(
    val username: String,
    val password: String
) {
    fun hashedPassword(): String { // Returning hashed password using bcrypt package
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun validCredentials(): Boolean {
        return username.length >= 3 && password.length >= 6
    }
}
