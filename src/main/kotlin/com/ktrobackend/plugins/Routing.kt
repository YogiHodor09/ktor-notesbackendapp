package com.ktrobackend.plugins

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {


    routing {
        get("/") {

            /*println("URI : ${call.request.uri}") // '/'
            println("Headers : ${call.request.headers.names()}") // headers
            println("Query Params : ${call.request.queryParameters.names()}") // query params*/

            call.respondText("Hello Ktor Home Route !")
        }

        get("/iphones/{page}") {
            val pageNumber = call.parameters["page"]
            call.respondText("You are on page number : $pageNumber")
        }

        post("/login") {
            val userInfo = call.receive<UserInfo>()
            print(userInfo)
            call.respondText("Everything is working fine ..")
        }


        // Static plugin. Try to access `/static/index.html`
        static {
            resources("static")
        }
    }
}

@kotlinx.serialization.Serializable
data class UserInfo(
    val email: String,
    val password: String
)
