package com.ktrobackend.model

@kotlinx.serialization.Serializable
data class NoteResponse<T>(
    val data: T,
    val success: Boolean
)
