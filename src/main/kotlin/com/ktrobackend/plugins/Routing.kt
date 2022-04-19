package com.ktrobackend.plugins

import com.ktrobackend.routing.authenticationRoutes
import com.ktrobackend.routing.notesRoutes
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Application.configureRouting() {

    notesRoutes() // calling routes for notes
    authenticationRoutes() // calling routes for users

    // basic routes for all endpoints in basic
    routing {
        get("/") {

            /*println("URI : ${call.request.uri}") // '/'
            println("Headers : ${call.request.headers.names()}") // headers
            println("Query Params : ${call.request.queryParameters.names()}") // query params*/

//            call.respondText("Hello Ktor Home Route !")
            val userResponse = UserInfoResponse("Yogeshwar", email = "yogesh@gmail.com")
            print(userResponse)
            call.respond(userResponse) // getting object as response
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

        get("/headers") {
            call.response.headers.append("server-name", "ktor-Server") // to append headers in response
            call.respondText("Headers Attached")
        }

        // downloading file in local system
        get("/fileDownload") {
            val file = File("files/rabbit1.jpg")
            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Attachment.withParameter( // download file into local system uses 'Attachment'
                    ContentDisposition.Parameters.FileName, "downloadableImage.jpg"
                ).toString()
            )

            call.respondFile(file)
        }


        // opening file in browser
        get("/fileOpen") {
            val file = File("files/rabbit4.jpg")
            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Inline.withParameter( // open the image in browser using 'Inline'
                    ContentDisposition.Parameters.FileName, "openImage.jpg"
                ).toString()
            )

            call.respondFile(file)
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


@kotlinx.serialization.Serializable
data class UserInfoResponse(
    val name: String,
    val email: String
)
