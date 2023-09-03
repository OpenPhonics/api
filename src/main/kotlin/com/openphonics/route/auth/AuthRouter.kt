/*
 * Copyright 2020 Shreyas Patil
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.openphonics.route.auth

import com.openphonics.controller.AuthController
import com.openphonics.exception.FailureMessages
import com.openphonics.model.request.LoginRequest
import com.openphonics.model.request.SignUpRequest
import com.openphonics.model.response.generateHttpResponse
import com.openphonics.plugin.controllers
import dagger.Lazy
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.resources.post
import io.ktor.server.routing.*
import io.ktor.util.*



@KtorExperimentalAPI
fun Route.AuthApi(authController: Lazy<AuthController> = controllers.authController()) {
    controllers
    login(authController)
    register(authController)
}
@OptIn(KtorExperimentalAPI::class)
private fun Route.register(controller: Lazy<AuthController>){
    post<Auth.Register> {
        val authRequest = runCatching { call.receive<SignUpRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_CREDENTIALS)
        }

        val authResponse = controller.get().register(authRequest.name, authRequest.classCode, authRequest.native, authRequest.isAdmin, authRequest.language)
        val response = generateHttpResponse(authResponse)
        call.respond(response.code, authResponse)
    }
}
@OptIn(KtorExperimentalAPI::class)
private fun Route.login(controller: Lazy<AuthController>){
    post<Auth.Login> {
        val authRequest = runCatching { call.receive<LoginRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_CREDENTIALS)
        }
        val authResponse = controller.get().login(authRequest.name, authRequest.classCode)
        val response = generateHttpResponse(authResponse)
        call.respond(response.code, authResponse)
    }
}