package com.ktrobackend.model


@kotlinx.serialization.Serializable
data class User(
    val id: Int,
    val username: String,
    val password: String
)
