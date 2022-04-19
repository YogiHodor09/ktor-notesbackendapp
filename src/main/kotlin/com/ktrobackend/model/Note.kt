package com.ktrobackend.model


@kotlinx.serialization.Serializable
data class Note(
    val id: Int,
    val note: String
)
