package com.openphonics.application.response

import kotlinx.serialization.Serializable

@Serializable
data class StrResponse(
    override val status: State,
    override val message: String,
    val id: String? = null
) : Response {
    companion object {
        fun unauthorized(message: String) = StrResponse(
            State.UNAUTHORIZED,
            message
        )

        fun failed(message: String) = StrResponse(
            State.FAILED,
            message
        )

        fun notFound(message: String) = StrResponse(
            State.NOT_FOUND,
            message
        )

        fun success(id: String) = StrResponse(
            State.SUCCESS,
            "Task successful",
            id
        )
    }
}