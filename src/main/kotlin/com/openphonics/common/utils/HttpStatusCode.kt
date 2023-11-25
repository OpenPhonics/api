package com.openphonics.common.utils

import com.openphonics.common.core.Response
import com.openphonics.common.core.State
import io.ktor.http.*


fun generateHttpCode(response: Response): HttpStatusCode {
    return when (response.status) {
        State.SUCCESS -> HttpStatusCode.OK
        State.NOT_FOUND -> HttpStatusCode.NotFound
        State.FAILED -> HttpStatusCode.BadRequest
        State.UNAUTHORIZED -> HttpStatusCode.Unauthorized
    }
}