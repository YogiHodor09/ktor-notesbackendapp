package com.ktrobackend.model


@kotlinx.serialization.Serializable
data class NoteRequest(
    val note: String
)
