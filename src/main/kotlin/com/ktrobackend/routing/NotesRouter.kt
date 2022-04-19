package com.ktrobackend.routing

import com.ktrobackend.db.DatabaseConnection
import com.ktrobackend.entities.NoteEntity
import com.ktrobackend.model.Note
import com.ktrobackend.model.NoteRequest
import com.ktrobackend.model.NoteResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*

fun Application.notesRoutes() {

    val db = DatabaseConnection.database
    routing {

        // getting all the notes from DB
        get("/notes") {
            val notes = db.from(NoteEntity).select().map {
                val id = it[NoteEntity.id]
                val note = it[NoteEntity.note]

                Note(id ?: -1, note ?: "")
            }
            call.respond(notes)
        }

        // Insert note using body request from the client
        post("/notes/createNote") {
            val noteRequest = call.receive<NoteRequest>()
            val result = db.insert(NoteEntity) {
                set(it.note, noteRequest.note) // set user request note value to DB
            }
            if (result == 1) {
                // Send success response to the client
                call.respond(
                    HttpStatusCode.OK, NoteResponse(
                        success = true,
                        data = "Note successfully inserted !!"
                    )
                )
            } else {
                // send failure response to the client
                call.respond(
                    HttpStatusCode.BadRequest, NoteResponse(
                        success = false,
                        data = "Note insertion failed !!"
                    )
                )
            }
        }

        // get a note by ID
        get("/notes/getNoteById/{id}") {
            val id = call.parameters["id"]?.toInt() ?: -1

            val note = db.from(NoteEntity).select().where { NoteEntity.id eq id }.map {
                val noteId = it[NoteEntity.id]!!
                val note = it[NoteEntity.note]!!
                Note(id = noteId, note = note)
            }.firstOrNull()

            if (note == null) {
                call.respond(
                    HttpStatusCode.NotFound, NoteResponse(
                        success = false,
                        data = "Note not found for this ID : $id"
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK, NoteResponse(
                        success = true,
                        data = note
                    )
                )
            }
        }

        // update a note by ID
        put("/notes/updateNoteById/{id}") {
            val noteId = call.parameters["id"]?.toInt() ?: -1
            val updatedNote = call.receive<NoteRequest>()


            val updatedNoteById = db.update(NoteEntity) {
                set(it.note, updatedNote.note)
                where {
                    it.id eq noteId
                }
            }
            if (updatedNoteById == 1) {
                // Send success response to the client
                call.respond(
                    HttpStatusCode.OK, NoteResponse(
                        success = true,
                        data = "Note successfully updated !!"
                    )
                )
            } else {
                // send failure response to the client
                call.respond(
                    HttpStatusCode.BadRequest, NoteResponse(
                        success = false,
                        data = "Note updation failed !!"
                    )
                )
            }
        }

        // delete a note by ID

        delete("/notes/deleteNoteById/{id}") {
            val deletedNoteId = call.parameters["id"]?.toInt() ?: -1

            val deletedNote = db.delete(NoteEntity) {
                it.id eq deletedNoteId
            }

            if (deletedNote == 1) {
                // Send success response to the client
                call.respond(
                    HttpStatusCode.OK, NoteResponse(
                        success = true,
                        data = "Note deleted !!"
                    )
                )
            } else {
                // send failure response to the client
                call.respond(
                    HttpStatusCode.BadRequest, NoteResponse(
                        success = false,
                        data = "Note deletion failed !!"
                    )
                )
            }
        }
    }

}