@file:Suppress("NAME_SHADOWING")

package com.ktrobackend.routing

import com.ktrobackend.db.DatabaseConnection
import com.ktrobackend.entities.UserEntity
import com.ktrobackend.model.NoteResponse
import com.ktrobackend.model.User
import com.ktrobackend.model.UserCredentials
import com.ktrobackend.utils.TokenManager
import com.typesafe.config.ConfigFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*
import org.mindrot.jbcrypt.BCrypt
import java.util.*


fun Application.authenticationRoutes() {
    val db = DatabaseConnection.database
    val tokenManager = TokenManager(HoconApplicationConfig(ConfigFactory.load()))
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

        // login user
        post("/users/login") {
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
            val password = userCredentials.password

            // check if user exists
            val user = db.from(UserEntity).select().where {
                UserEntity.username eq username
            }.map {
                val id = it[UserEntity.id]!!
                val username = it[UserEntity.username]!!
                val password = it[UserEntity.password]!!
                User(id, username, password)
            }.firstOrNull()


            if (user == null) { // user not exists
                call.respond(
                    HttpStatusCode.BadRequest, NoteResponse(
                        success = false, data = "Invalid username or password"
                    )
                )
                return@post
            }

            val doesPasswordMatch = BCrypt.checkpw(password, user.password)
            if (!doesPasswordMatch) {
                call.respond(
                    HttpStatusCode.BadRequest, NoteResponse(
                        success = false, data = "Invalid username or password"
                    )
                )
                return@post
            }

            // get Token using token manager

            val token = tokenManager.generateJWTToken(user)

            call.respond(
                HttpStatusCode.OK, NoteResponse(
                    success = true, data = token
                )
            )

        }
    }
}