package com.ktrobackend.routing

import com.ktrobackend.db.DatabaseConnection
import com.ktrobackend.entities.UserEntity
import com.ktrobackend.model.NoteResponse
import com.ktrobackend.model.UserCredentials
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*
import java.util.*


fun Application.authenticationRoutes() {
    val db = DatabaseConnection.database

    routing {

        // Insert user using body request from the client
        post("/users/register") {
            val userCredentials = call.receive<UserCredentials>()

            // checking valid username and password length

            if (!userCredentials.validCredentials()) {
                call.respond(
                    HttpStatusCode.BadRequest, NoteResponse(
                        success = false,
                        data = "Username length should be greater than or equal to 3 && password length is greater than or equal to 6"
                    )
                )
                return@post
            }

            val username = userCredentials.username.lowercase(Locale.getDefault())
            val password = userCredentials.hashedPassword()


            // check username already exits
            val user = db.from(UserEntity).select().where {
                UserEntity.username eq username
            }.map {
                it[UserEntity.username]
            }.firstOrNull()

            if (user != null) { // user already exists in "user" variable
                call.respond(
                    HttpStatusCode.BadRequest, NoteResponse(
                        success = false, data = "User already exists !! Try with different username"
                    )
                )
                return@post
            } else {
                val result = db.insert(UserEntity) {
                    set(it.username, username)
                    set(it.password, password)
                }

                if (result == 1) {
                    // Send success response to the client
                    call.respond(
                        HttpStatusCode.Created, NoteResponse(
                            success = true,
                            data = "User Created !!"
                        )
                    )
                } else {
                    // send failure response to the client
                    call.respond(
                        HttpStatusCode.BadRequest, NoteResponse(
                            success = false,
                            data = "User Creation failed !!"
                        )
                    )
                }
            }


        }
    }
}