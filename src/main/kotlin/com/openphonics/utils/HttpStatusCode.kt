package com.openphonics.utils

import com.openphonics.application.response.Response
import com.openphonics.application.response.State
import io.ktor.http.*

fun generateHttpCode(response: Response): HttpStatusCode {
    return when (response.status) {
        State.SUCCESS -> HttpStatusCode.OK
        State.NOT_FOUND -> HttpStatusCode.NotFound
        State.FAILED -> HttpStatusCode.BadRequest
        State.UNAUTHORIZED -> HttpStatusCode.Unauthorized
    }
}