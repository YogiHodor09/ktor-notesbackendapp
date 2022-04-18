package com.ktrobackend.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.aboutUsModule() {
    routing {
        get("/contactus") {
            call.respondText("Contact Us")
        }

        get("/aboutUs") {
            call.respondText("About US !!")
        }
    }
}