/*
 * Copyright 2021 Shreyas Patil
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

package com.openphonics.plugin

import com.openphonics.exception.FailureMessages
import com.openphonics.model.response.FailureResponse
import com.openphonics.model.response.State
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

        status(HttpStatusCode.Unauthorized) {call, cause ->
            call.respond(cause, FailureResponse(State.UNAUTHORIZED, FailureMessages.MESSAGE_ACCESS_DENIED))
        }
    }
}