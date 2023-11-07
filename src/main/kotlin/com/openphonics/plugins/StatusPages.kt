package com.openphonics.plugins

import com.openphonics.application.exception.FailureMessages
import com.openphonics.application.response.FailureResponse
import com.openphonics.application.response.State
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<BadRequestException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, FailureResponse(State.FAILED, cause.message ?: "Bad Request"))
        }
        status(HttpStatusCode.InternalServerError) { call, cause ->
            call.respond(cause, FailureResponse(State.FAILED, FailureMessages.MESSAGE_FAILED))
        }
    }
}