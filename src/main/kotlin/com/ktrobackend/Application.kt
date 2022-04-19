package com.ktrobackend

import com.ktrobackend.entities.NoteEntity
import com.ktrobackend.plugins.aboutUsModule
import com.ktrobackend.plugins.configureRouting
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import org.ktorm.database.Database
import org.ktorm.dsl.*


fun main() {
    embeddedServer(Netty, port = 8080, host = "localhost") {
        install(ContentNegotiation) {
            json()
        }
        configureRouting()
        aboutUsModule()

        // Create database

        val database = Database.connect(
            url = "jdbc:mysql://127.0.0.1:3306/notes",
            driver = "com.mysql.cj.jdbc.Driver",
            user = "root",
            password = "admin"
        )
        
        /* CRUD OPERATIONS IN DB */
        // insert values to DB
        /*database.insert(NoteEntity) {
            set(it.note, "Study Ktor")
        }*/

        // update data in DB

        database.update(NoteEntity) {
            set(it.note, "Become a FullStack Developer")
            where {
                it.id eq 4
            }
        }

        // delete data in DB

        database.delete(NoteEntity) {
            it.id eq 5
        }

        // read the values from DB
        val notesData = database.from(NoteEntity).select()
        for (rowData in notesData) {
            println("${rowData[NoteEntity.id]} : ${rowData[NoteEntity.note]}")
        }

    }.start(wait = true)
}


