package com.openphonics.application.response

import kotlinx.serialization.Serializable

@Serializable
data class StrIdResponse(
    override val status: State,
    override val message: String,
    val id: String? = null
) : Response {
    companion object {
        fun unauthorized(message: String) = StrIdResponse(
            State.UNAUTHORIZED,
            message
        )

        fun failed(message: String) = StrIdResponse(
            State.FAILED,
            message
        )

        fun notFound(message: String) = StrIdResponse(
            State.NOT_FOUND,
            message
        )

        fun success(id: String) = StrIdResponse(
            State.SUCCESS,
            "Task successful",
            id
        )
    }
}