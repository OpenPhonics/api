package com.openphonics.auth

import com.openphonics.auth.principal.UserPrincipal
import com.openphonics.common.exception.FailureMessages
import com.openphonics.common.plugins.controllers
import com.openphonics.common.utils.generateHttpCode
import dagger.Lazy
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*

@KtorExperimentalAPI
fun Route.AuthAPI(authController: Lazy<AuthController> = controllers.authController()) {
    post("/signup") {
        val authRequest = runCatching { call.receive<AuthRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_CREDENTIALS)
        }

        val response = authController.get().register(authRequest.username)
        call.respond(generateHttpCode(response), response)
    }
    post("/login") {
        val authRequest = runCatching { call.receive<AuthRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_CREDENTIALS)
        }

        val response = authController.get().login(authRequest.username)
        call.respond(generateHttpCode(response), response)
    }
    authenticate {
        get("/auth") {
            val principal = call.principal<UserPrincipal>()
            val response = authController.get().authenticate(principal?.user)
            call.respond(generateHttpCode(response), response)
        }
    }
}